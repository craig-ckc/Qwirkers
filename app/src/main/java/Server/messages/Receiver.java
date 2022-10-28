package Server.messages;

@FunctionalInterface
public interface Receiver {
    void received(Message message);
}
