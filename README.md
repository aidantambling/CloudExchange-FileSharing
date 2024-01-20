# Remote File Sharing with Java

## Overview
Remote File Sharing with Java is a client-server application designed for sharing and managing files remotely. This project allows users to interact with a remote directory hosted on a server. Clients can list available files, request random files, upload files to the server, and download files from the server. It provides a convenient way to transfer files between the client and server over a network connection.

## Features
- **File Listing:** Clients can list the files available for download from the server.
- **Random File:** Clients can request a random file from the server for download.
- **File Upload:** Clients can upload files to the server for storage.
- **File Download:** Clients can download files from the server.
- **Help Command:** Clients can request a list of available commands and their descriptions.
- **Graceful Termination:** Clients can gracefully terminate the connection with the server using the "Bye" command.

## Installation
Before running the client-server application, make sure you have Java installed on your system. Clone the project repository to your local machine:

```console
git clone https://github.com/your-username/Remote-File-Sharing-Java.git
cd Remote-File-Sharing-Java
```

Compile the client and server Java files:

```console
javac pa3_client.java
javac pa3_server.java
```

## Usage
### Starting the Server
To start the server, run the following command:

```console
java pa3_server
```
### Starting the Client
To start a client, run the following command, replacing <server_ip> with the IP address or hostname of the server:

```console
java pa3_client <server_ip>
```

Once the client is running, you can use the available commands to interact with the server.

## Configuration
You can modify the code to change the default port number used for the server-client communication.
Ensure that the client and server have access to a shared directory for file uploads and downloads.
Additional configuration settings can be added as needed.

## Acknowledgements
This project was developed by Aidan Tambling as part of Internet Network Technologies at the University of Florida.

## Contact
For any questions or issues related to this project, please contact:

[Aidan Tambling] - atambling@ufl.edu
Project Link: https://github.com/your-username/Remote-File-Sharing-Java
