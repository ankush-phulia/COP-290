package network;

import game.BoardMulti;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/** Created by nitin on 24/4/16. */
public class ReadData implements Runnable {
    Socket connection;
    DataInputStream in;
    List<String> buffer;
    boolean isAlive = false;
    public Boolean isStateBoardMulti = false;

    public ReadData(Socket connection) {
        this.connection = connection;
        buffer = new ArrayList<>();
        try {
            in = new DataInputStream(connection.getInputStream());
        } catch (IOException e) {
            threadMessage("Cannot get DataInputStream of client...");
        }
        isAlive = true;
    }

    @Override
    public void run() {
        try {
            String lineRead;
            while (connection.isConnected()) {
                lineRead = in.readUTF();
                if (isStateBoardMulti) BoardMulti.parse_packet(lineRead);
                else buffer.add(lineRead);
            }
        } catch (IOException e) {
            threadMessage("Unable to read! Disconnected probably?");
            isAlive = false;
        }
    }

    public String readFromBuffer() {
        if (buffer.isEmpty()) return null;
        String token = buffer.get(0);
        buffer.remove(0);
        return token;
    }

    /* For debugging purpose only.
     * Display a message, preceded by
     * the name of the current thread
     */
    private void threadMessage(String message) {
        String threadName = Thread.currentThread().getName();
        System.out.format("%s: %s%n", threadName, message);
    }
}
