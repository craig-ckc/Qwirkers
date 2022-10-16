package Server.messages;

@FunctionalInterface
public interface MessageReceiver {
    void messageReceived(Message message);
}
