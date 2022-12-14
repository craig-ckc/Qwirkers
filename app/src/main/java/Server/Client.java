package Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import Server.messages.Message;
import Server.messages.Receiver;
import Server.messages.client.Leave;

public class Client {
    private static Client instance;
    private final BlockingQueue<Message> outgoingMessages;
    private String serverAddress;
    private ObjectInputStream input = null;
    private ObjectOutputStream output = null;
    private Receiver receiver;
    private Thread readThread;
    private Thread writeThread;
    private Socket connection;

    public String session;
    public String name;

    public int turn;

    private Client() {
        super();
        outgoingMessages = new LinkedBlockingQueue<>();
    }

    public static Client getInstance() {
        if (instance == null)
            instance = new Client();

        return instance;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public boolean isConnected() {
        if (instance.connection == null)
            return false;
        else
            return instance.connection.isConnected();
    }

    public void send(Message message) {
        try {
            outgoingMessages.put(message);
        } catch (InterruptedException e) {
            
        }
    }

    public void connect(String serverAddress) {
        this.serverAddress = serverAddress;

        readThread = new ReadThread();
        readThread.start();

    }

    public void disconnect() {
        send(new Leave());
    }

    private class ReadThread extends Thread {
        @Override
        public void run() {

            readThread = this;

            try {
                connection = new Socket(serverAddress, 5050);

                input = new ObjectInputStream(connection.getInputStream());
                output = new ObjectOutputStream(connection.getOutputStream());
                output.flush();

                writeThread = new WriteThread();
                writeThread.start();

                Message msg;
                do {
                    msg = (Message) input.readObject();

                    if (receiver != null) receiver.received(msg);
                } while (msg.getClass() != Leave.class);

                connection.close();

            } catch (Exception e) {

            } finally {
                readThread = null;
                if (writeThread != null) writeThread.interrupt();
            }
        }
    }

    private class WriteThread extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    Message msg = outgoingMessages.take();

                    output.writeObject(msg);
                    output.flush();

                }
            } catch (Exception e) {
                writeThread = null;
            }
        }
    }
}
