package network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/** Created by nitin on 24/4/16. */
public class LocalServer {
    ServerSocket myServer;
    List<Socket> clientSocket = new ArrayList<>();
    List<DataOutputStream> writingStreamClient = new ArrayList<>();
    List<InetAddress> ip_address = new ArrayList<>();
    List<ReadData> inputStreamClient = new ArrayList<>();

    public InetAddress getMyIP() {
        return myServer.getInetAddress();
    }

    // Returns a list of all available IPs (over different network interfaces)
    public static List<InetAddress> getAllAvailableIP() {
        List<InetAddress> IPs = new ArrayList<>();
        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) e.nextElement();

                Enumeration ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    try {
                        InetAddress i = (Inet4Address) ee.nextElement();
                        IPs.add(i);
                        System.out.println(i.getHostAddress());
                    } catch (ClassCastException c) {
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return IPs;
    }

    // Creates a new server with port no. 'port'
    // Throws IOException if unsuccessful
    public LocalServer(int port) throws IOException {
        myServer = new ServerSocket(port, 3);
        myServer.setSoTimeout(2000);
        threadMessage("Server created on port " + myServer.getLocalPort());
    }

    // returns list of IP Address of all clients
    // (useful for keeping track of all clients)
    public List<InetAddress> getAllClients() {
        ArrayList<InetAddress> connections = new ArrayList<>();
        for (int count = 0; count < clientSocket.size(); count++) {
            if (inputStreamClient.get(count).isAlive) {
                connections.add(ip_address.get(count));
            }
        }
        return connections;
    }

    // Tries to establish connection with a new client
    // returns the corresponding thread
    public Thread acceptClient() {
        NewClient cl = new NewClient(this);
        Thread clientThread = new Thread(cl);
        clientThread.start();
        return clientThread;
    }

    // closes the server socket
    // returns true if closed successfully
    public boolean disconnect() {
        try {
            myServer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // returns true if myServer is alive
    public boolean alive() {
        return myServer != null && !myServer.isClosed();
    }

    // writes 'writeData' to client with IP Address 'ip_addr'
    // returns false if client is disconnected else true
    public boolean writeToClient(InetAddress ip_addr, String writeData) {
        int position = ip_address.indexOf(ip_addr);
        DataOutputStream out = writingStreamClient.get(position);
        Socket client = clientSocket.get(position);

        try {
            out.writeUTF(writeData);
        } catch (IOException e) {
            e.printStackTrace();
            if (!client.isConnected()) {
                threadMessage("Client is disconnected...");
                // Client is disconnected. Take necessary steps here
                ip_address.remove(position);
                writingStreamClient.remove(position);
                clientSocket.remove(position);
                inputStreamClient.remove(position);
                return false;
            }
        }
        return true;
    }

    // writes 'writeData' to all clients
    // returns IP address of lost client
    public InetAddress writeToAllClients(String writeData) {
        InetAddress lost = null;
        for (int position = 0; position < clientSocket.size(); position++) {
            DataOutputStream out = writingStreamClient.get(position);
            Socket client = clientSocket.get(position);

            try {
                out.writeUTF(writeData);
            } catch (IOException e) {
                e.printStackTrace();

                threadMessage("Client is disconnected...");
                // Client is disconnected. Take necessary steps here
                lost = ip_address.remove(position);
                writingStreamClient.remove(position);
                clientSocket.remove(position);
                inputStreamClient.remove(position);

                threadMessage("Disconnected: " + lost + client.toString());

                position--;
            }
        }
        return lost;
    }

    // reads data from client with IP Address 'ip_addr'
    // returns null if no data is available
    public String readFromClient(InetAddress ip_addr) {
        int position = ip_address.indexOf(ip_addr);
        ReadData rd = inputStreamClient.get(position);
        return rd.readFromBuffer();
    }

    // helper function 'NewClient' class
    // NOT for outside use
    synchronized void addElements(Socket s, DataOutputStream dos, InetAddress sa, ReadData rd) {
        clientSocket.add(s);
        writingStreamClient.add(dos);
        ip_address.add(sa);
        inputStreamClient.add(rd);
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
