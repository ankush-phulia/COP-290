package network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/** Created by nitin on 25/4/16. */
public class NewClient implements Runnable {
    private LocalServer localServer;

    public NewClient(LocalServer server) {
        localServer = server;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket waitingForClient;
                try {
                    waitingForClient = localServer.myServer.accept();
                } catch (IOException e) {
                    continue;
                }

                ReadData readFromClient = new ReadData(waitingForClient);
                Thread readThread = new Thread(readFromClient);
                readThread.start();

                localServer.addElements(
                        waitingForClient,
                        new DataOutputStream(waitingForClient.getOutputStream()),
                        waitingForClient.getInetAddress(),
                        readFromClient);

                threadMessage("Just connected to " + waitingForClient.getRemoteSocketAddress());
            }
        } catch (IOException e) {
            threadMessage("Problem occurred while accepting new client. :(");
        }
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
