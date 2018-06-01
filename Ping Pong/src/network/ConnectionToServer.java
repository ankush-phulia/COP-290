package network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/** Created by nitin on 24/4/16. */
public class ConnectionToServer {
    Socket clientSocket;
    DataOutputStream writingStream;
    public ReadData readingStream;

    /* Connect to a client with IP Address/Name 'inetAddr' & port no. 'port'
     *  Throws IOException if unsuccessful
     *  Timeout of 10 seconds is set
     */
    public ConnectionToServer(String inetAddr, int port) {
        try {
            clientSocket = new Socket();
            try {
                clientSocket.connect(new InetSocketAddress(inetAddr, port), 10000);
            } catch (IOException e) {
                e.printStackTrace();
                threadMessage("Unable to connect to a given IP address.");
                clientSocket = null;
                return;
            }

            threadMessage(
                    "Connection established with "
                            + clientSocket.getRemoteSocketAddress()
                            + " on port "
                            + clientSocket.getLocalPort());

            writingStream = new DataOutputStream(clientSocket.getOutputStream());
            readingStream = new ReadData(clientSocket);
            Thread readThread = new Thread(readingStream);
            readThread.start();
        } catch (IOException e) {
            threadMessage("Error occurred while obtaining output stream of server.");
            clientSocket = null;
        }
    }

    // checks whether connection is alive or not
    public boolean connectionEstablished() {
        return clientSocket != null;
    }

    // closes the socket
    public boolean disconnect() {
        try {
            clientSocket.close();
            return true;
        } catch (IOException ioe) {
            return false;
        }
    }

    // returns IP address of client
    public InetAddress getIPAddress() {
        return clientSocket.getLocalAddress();
    }

    // writes 'writeData' to server
    // returns false if client is disconnected else true
    public boolean writeToServer(String writeData) {

        try {
            writingStream.writeUTF(writeData);
        } catch (IOException e) {
            e.printStackTrace();
            if (!clientSocket.isConnected()) {
                threadMessage("Server is disconnected...");
                // Server is disconnected. Take necessary steps here
                clientSocket = null;
                return false;
            }
        }
        return true;
    }

    // reads data from server
    // returns null if no data is available
    public String readFromServer() {
        return readingStream.readFromBuffer();
    }

    /* For debugging purpose only.
     * Display a message, preceded by
     * the name of the current thread
     */
    private static void threadMessage(String message) {
        String threadName = Thread.currentThread().getName();
        System.out.format("%s: %s%n", threadName, message);
    }
}
