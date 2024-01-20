import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.DirectoryStream;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class pa3_server {
    public static void main(String args[])
    {
        // hard coded port - last 4 digits of UFID
        int port = 1664;

        // launches the server on the given platform with the hard coded port
        try
        {
            ServerSocket server = new ServerSocket(port);
            InetAddress local = InetAddress.getLocalHost();
            System.out.println("Remote Directory has been launched with port " + port);
            System.out.println("and IP " + local.getHostAddress());

            // when a client connects with the appropriate address + port, the program continues
            Socket socket = server.accept();
            System.out.println("Incoming connection detected from client");

            // takes input from the client socket
            DataInputStream input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            String clientInput = "";

            // display the directory's files; send this list to the client
            String path = System.getProperty("user.dir");
            path += "/files";
            Set<String> set = listLocalFiles(path);
            System.out.println("---------------------------------------------------------------");
            System.out.println("Files available in the directory: ");
            for (String i : set){
                System.out.println(i);
                output.writeUTF((i + "\n"));
            }
            output.writeUTF("End");
            System.out.println("---------------------------------------------------------------");

            // reads commands from clients; provides corresponding meme
            while (!clientInput.equals("bye"))
            {
                try
                {
                    clientInput = input.readUTF();

                    if (clientInput.equals("Bye")){
                        break;
                    }
                    if (clientInput.equals("List")){
                        System.out.println("Listing Files....");
                    }
                    else if (clientInput.equals("Help")){
                        System.out.println("Printing help for the client....");
                    }
                    else if (clientInput.equals("Random")){
                        // grab random file
                        System.out.println("Sending the client a random file....");
                        Random rand = new Random();
                        int randInt = rand.nextInt(set.size());
                        int i = 0;
                        for (String file : set){
                            if (i == randInt){
                                System.out.println("Sending random file: " + file);
                                output.writeUTF(file);
                                sendFile("files//" + file, output);
                            }
                            i++;
                        }
                    }
                    else { // upload or download command
                        // parse the server's input for command and filename
                        StringBuilder parse = new StringBuilder();
                        StringBuilder parse2 = new StringBuilder();
                        int i = 0;
                        while (i < clientInput.length() && clientInput.charAt(i) != ' ') {
                            parse.append(clientInput.charAt(i));
                            i++;
                        }
                        String command = String.valueOf(parse);
                        i++;
                        while (i < clientInput.length()) {
                            parse2.append(clientInput.charAt(i));
                            i++;
                        }
                        String filename = String.valueOf(parse2);

                        if (command.equals("Upload")) {
                            System.out.println("Attempting to receive " + filename + "...");
                            try {
                                FileOutputStream writeFile;
                                writeFile = new FileOutputStream("files//" + filename);
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
                                System.out.println("ERROR! " + filename + " could not be read. Please enter a different command.");
                                continue;
                            }
                            set.add(filename + "\n");
                            System.out.println(filename + " has been received");
                        } else if (command.equals("Download")) {
                            if (!set.contains(filename)) {
                                System.out.println("Remote Directory does not contain " + filename);
                                continue;
                            }
                            try {
                                sendFile("files//" + filename, output);
                                System.out.println("Attempting to send " + filename + "...");
                            } catch (Exception e) {
                                System.out.println("ERROR! " + filename + " could not be read. Please enter a different command.");
                            }
                        }
                        else {
                            System.out.println("Error: " + clientInput + " is not a command!");
                        }
                    }
                }
                catch(Exception e)
                {
                    System.out.println("Error in reading!");
                    throw new RuntimeException(e);
                }
            }

            // terminate the connection
            System.out.println("See you later client! Closing connection");
            socket.close();
            input.close();
        }

        catch(IOException i)
        {
            System.out.println("Error in connection with client or input detection");
            throw new RuntimeException(i);
        }
    }

    // method which takes a file name and sends it from the server to the client requesting that file
    public static void sendFile(String filepath, DataOutputStream writeFile) throws IOException {
        File file = new File(filepath);
        FileInputStream readFile = new FileInputStream(file);
        writeFile.writeLong(file.length());
        byte[] buffer = new byte[8192];
        int bytesLeft = readFile.read(buffer);
        while (bytesLeft != -1){
            writeFile.write(buffer, 0, bytesLeft);
            writeFile.flush();
            bytesLeft = readFile.read(buffer);
        }
        readFile.close();
    }

    public static Set<String> listLocalFiles(String path) throws IOException {
        Set<String> files = new HashSet<>();
        try {
            DirectoryStream<Path> localDirectory = Files.newDirectoryStream(Paths.get(path));
                for (Path p : localDirectory) {
                    if (!Files.isDirectory(p)) {
                        files.add(p.getFileName().toString());
                    }
                }
            } catch (IOException e){
            System.out.println("Error parsing local directory.");
        }
        return files;
    }
}

