import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class pa3_client {

    public static void main(String args[]) throws IOException {
        // hard coded port - last 4 digits of UFID
        int port = 1664;

        // address is passed in as an argument from command line
        String address;
        try {
            address = args[0];
        } catch (Exception e) {
            System.out.println("Error: no hostname provided!");
            throw new RuntimeException(e);
        }

        // resolve an IP address from the passed hostname
        InetAddress IP;
        try {
            IP = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            System.out.println("Error in resolving IP address!");
            throw new RuntimeException(e);
        }

        // use the address and port to create a client socket
        BufferedReader input;
        DataOutputStream output;
        DataInputStream serverInput;
        Socket socket;
        try {
            socket = new Socket(IP, port);
            System.out.println("Client-side socket established.");
            System.out.println("Hostname: " + address);
            System.out.println("Address: " + IP.getHostAddress());
            System.out.println("Port: " + port);

            // input is taken in from the console
            input = new BufferedReader(new InputStreamReader(System.in));
            serverInput = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            // output from the socket is sent to the server socket for reading
            output = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println("Error in establishing a socket connection!");
            throw new RuntimeException(e);
        }

        // Welcome Message
        System.out.println("---------------------------------------------------------------");
        System.out.println("Welcome to Aidan's Remote Directory");
        System.out.println("Operated via the Remote Directory Protocol (RDP).");
        System.out.println("Enter \"Help\" to view commands.");
        System.out.println("---------------------------------------------------------------");

        // Read in a list of the server's files allowed for download
        String in = serverInput.readUTF();
        Set<String> set = new HashSet<>(Arrays.asList(in));
        while (!in.equals("End")){
            set.add(in);
            in = serverInput.readUTF();
        }

        String consoleInput = "";
        while (!consoleInput.equals("Bye")) {
            try {
                consoleInput = input.readLine();
                output.writeUTF(consoleInput);

                if (consoleInput.equals("Bye"))
                    break;
                if (consoleInput.equals("Random")){
                    System.out.println("---------------------------------------------------------------");
                    System.out.println("Requesting a random file from the server.");
                    in = serverInput.readUTF();
                    consoleInput = "Download " + in;
                }
                if (consoleInput.equals("Help")){
                    System.out.println("---------------------------------------------------------------");
                    System.out.println("\"Help\" = List all commands");
                    System.out.println("\"List\" = List the files available for download");
                    System.out.println("\"Random\" = Download a random file from the server");
                    System.out.println("\"Upload FILENAME\" = Uploads FILENAME to the server");
                    System.out.println("\"Download FILENAME\" = Downloads FILENAME from the server");
                    System.out.println("\"Bye\" = Terminate the connection with the server");
                    System.out.println("---------------------------------------------------------------");
                }
                else if (consoleInput.equals("List")){
                    System.out.println("---------------------------------------------------------------");
                    for (String i : set){
                        System.out.println(i);
                    }
                    System.out.println("---------------------------------------------------------------");
                }
                else { // upload or download command
                    // parse input for command (upload, or download?) - and filename
                    StringBuilder parse = new StringBuilder();
                    StringBuilder parse2 = new StringBuilder();
                    int i = 0;
                    while (i < consoleInput.length() && consoleInput.charAt(i) != ' ') {
                        parse.append(consoleInput.charAt(i));
                        i++;
                    }
                    String command = String.valueOf(parse);
                    i++;
                    while (i < consoleInput.length()) {
                        parse2.append(consoleInput.charAt(i));
                        i++;
                    }
                    String filename = String.valueOf(parse2);

                    if (command.equals("Upload")) {
                        System.out.println("Attempting to upload " + filename + " to the server.");
                        try {
                            sendFile(filename, output);
                        } catch (Exception e) {
                            System.out.println("ERROR! " + filename + " could not be uploaded. Please enter a different command.");
                            continue;
                        }
                        set.add(filename);
                        System.out.println(filename + " successfully uploaded!");
                        System.out.println("---------------------------------------------------------------");
                    }
                    else if (command.equals("Download")) {
                        System.out.println("Attempting to download " + filename + "...");
                        if (!set.contains(filename + "\n")){
                            System.out.println("Sorry, the Remote Directory does not contain " + filename);
                            System.out.println("---------------------------------------------------------------");
                            continue;
                        }
                        try {
                            FileOutputStream writeFile;
                            writeFile = new FileOutputStream(filename);
                            DataInputStream fileInput = new DataInputStream(socket.getInputStream());
                            long fileSize = fileInput.readLong();
                            byte[] buffer = new byte[8192];
                            int bytesRead = fileInput.read(buffer, 0, buffer.length);
                            while (bytesRead != -1) {
                                writeFile.write(buffer, 0, bytesRead);
                                fileSize -= bytesRead;
                                if (fileSize <= 0) {
                                    break;
                                }
                                bytesRead = fileInput.read(buffer, 0, buffer.length);
                            }
                            writeFile.close();
                        } catch (Exception e) {
                            System.out.println("ERROR! " + filename + " could not be downloaded. Please enter a different command.");
                            continue;
                        }
                        System.out.println(filename + " has been received");
                        System.out.println("---------------------------------------------------------------");
                    }
                    else{
                        System.out.println("Error - that is not a command. To see all commands, type \"Help\"");
                    }
                }
            } catch (IOException e) {
                System.out.println("Error in reading from the console!");
                throw new RuntimeException(e);
            }
        }

        // terminate the connection
        System.out.println("Goodbye server! Closing connection.");
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Error in disconnecting the client-server interface!");
            throw new RuntimeException(e);
        }
    }

    // method which takes a file name and sends it from the server to the client requesting that file
    public static void sendFile(String filepath, DataOutputStream writeFile) throws IOException {
        File file = new File(filepath);
        FileInputStream readFile = new FileInputStream(file);
        writeFile.writeLong(file.length());
        byte[] buffer = new byte[8192];
        int bytesLeft = readFile.read(buffer);
        while (bytesLeft != -1) {
            writeFile.write(buffer, 0, bytesLeft);
            writeFile.flush();
            bytesLeft = readFile.read(buffer);
        }
        readFile.close();
    }
}