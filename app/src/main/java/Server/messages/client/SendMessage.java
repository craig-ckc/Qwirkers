package Server.messages.client;

import Game.Models.Move;
import Server.messages.Message;

public class SendMessage extends Message {
    private static final long serialVersionUID = 5L;

    // The text message to be sent to all clients in the same group.
    public Move move;

    public SendMessage(Move move) {
        this.move = move;
    }

    @Override
    public String toString() {
        return String.format("SendChatMessage('%s')", move);
    }
}
