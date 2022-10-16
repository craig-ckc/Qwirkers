package Server;

import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import Server.messages.Message;
import Server.messages.MessageReceiver;
import Server.messages.client.Join;
import Server.messages.client.Quit;
import Server.messages.client.SetHandle;

public class GameClient {
    private String serverAddress;

    private Socket connection;
    private ObjectInputStream input = null;
    private ObjectOutputStream output = null;

    private final BlockingQueue<Message> outgoingMessages;
    private final MessageReceiver messageReceiver;

    private Thread readThread;
    private Thread writeThread;

    public GameClient(MessageReceiver messageReceiver) {
        super();
        outgoingMessages = new LinkedBlockingQueue<>();
        this.messageReceiver = messageReceiver;
    }

    private void send(Message message) {
        try {
            outgoingMessages.put(message);
        } catch (InterruptedException e) {
            Log.e("ChatClient", e.getMessage());
        }
    }

    public void connect(String serverAddress, String handle) {
        Log.i("ChatClient", "Connecting to " + serverAddress + "...");

        // Cache info.
        this.serverAddress = serverAddress;
        // Details about the client.

        // Start the read thread (which establishes a connection.
        Log.i("ChatClient", "Starting Read Loop thread...");
        readThread = new ReadThread();
        readThread.start();

        // Send the setHandle message.
        Log.i("ChatClient", "Queuing SetHandle(" + handle + ")");
        send(new SetHandle(handle));
    }

    public void disconnect() {
        send(new Quit());
    }


    public void ready(String name, int avatar){
        send(new Join(name, avatar));
    }


    public boolean isConnected(){
        return connection != null;
    }

    private class ReadThread extends Thread {
        @Override
        public void run() {
            Log.i("ChatClient", "Started Read Loop thread...");

            readThread = this;

            try {
                // Connect to server (if can).
                connection = new Socket(serverAddress, 5050);
                Log.i("ChatClient", "Connected to " + serverAddress + "...");

                // Obtain I/O streams.
                // Possible GOTCHA: the order of obtaining the I/O streams in the server and
                // client is flipped. Needed due to synchronising the OBJECT streams on both ends.
                input = new ObjectInputStream(connection.getInputStream());
                output = new ObjectOutputStream(connection.getOutputStream());
                output.flush();
                Log.i("ChatClient", "Obtained I/O stream...");

                // Create and start the write thread.
                Log.i("ChatClient", "Starting Write Loop thread...");
                writeThread = new WriteThread();
                writeThread.start();

                // Go into read message loop.
                Log.i("ChatClient", "Starting Read Loop...");
                Message msg;
                do {
                    // Read message from server.
                    msg = (Message) input.readObject();
                    Log.i("ChatClient", ">> " + msg);

                    // If Message Receiver given, pass it the message.
                    if (messageReceiver != null) messageReceiver.messageReceived(msg);
                } while (msg.getClass() != Quit.class);

                // Done, so close connection.
                connection.close();
                Log.i("ChatClient", "Closed connection...");


            } catch (Exception e) {
                Log.e("ChatClient", "Exception: " + e.getMessage());

            } finally {
                readThread = null;
                if (writeThread != null) writeThread.interrupt();
            }
        }
    }

    private class WriteThread extends Thread {
        @Override
        public void run() {
            Log.i("ChatClient", "Started Write Loop thread...");

            try {
                // Check outgoing messages and send.
                while (true) {
                    // Dequeue message to send. Take blocks until something to send.
                    Message msg = outgoingMessages.take();

                    output.writeObject(msg);
                    output.flush();

                    Log.i("ChatClient", "<< " + msg);
                }
            } catch (Exception e) {
                writeThread = null;
            }
        }
    }
}
