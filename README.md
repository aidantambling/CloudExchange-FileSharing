# CloudExchange: Remote Directory Access

## Overview
CloudExchange is a client-server application designed for the sharing and management of files through a remote (non-local) directory. This is accomplished through TCP interaction with a directory hosted on a remote server. Clients can list available files, request random files, and both upload and download files to and from the server. It provides a convenient medium through which users can store and access data non-locally.

## Features
- **File Listing:** Clients can list the files available for download from the server.
- **Random File:** Clients can request a random file from the server for download.
- **File Upload:** Clients can upload files to the server for storage.
- **File Download:** Clients can download files from the server.
- **Help Command:** Clients can request a list of available commands and their descriptions.
- **Graceful Termination:** Clients can gracefully terminate the connection with the server using the "Bye" command.

![image](https://github.com/aidantambling/CloudExchange-FileSharing/assets/101668617/b8c47388-da46-4b06-b628-e42e9a762c10)

## Installation
This application requires the use of two terminal windows. This can be accomplished on one or two machines.

Before running the client-server application, make sure you have Java installed on your system. Next, clone the project repository to your local machine(s):

```console
git clone https://github.com/aidantambling/CloudExchange-FileSharing.git
cd CloudExchange-FileSharing
```

## Usage
### Starting the Server

Compile the server Java file:

```console
javac pa3_server.java
```

To start the server, run the following command:

```console
java pa3_server
```

The server will be launched in the terminal and will display its ip to the screen
### Starting the Client

Compile the client Java file:

```console
javac pa3_client.java
```

To start the client, run the following command, replacing <server_ip> with the IP address or hostname of the server (as displayed during the server launch):

```console
java pa3_client <server_ip>
```

Once the client is running, you can use the available commands to interact with the server. The server necessarily must be launched first for any connection to be made.

## Configuration
You can modify the code to change the default port number used for the server-client communication.
Additional configuration settings can be added as needed.

## Acknowledgements
This project was developed by Aidan Tambling as part of Internet Network Technologies at the University of Florida.
Further documentation can be examined in this RFC document: [Remote Directory Protocol - RFC](tambling_majumder_report.pdf)


## Contact
For any questions or issues related to this project, please contact:

[Aidan Tambling] - atambling@ufl.edu
Project Link: https://github.com/aidantambling/CloudExchange-FileSharing
