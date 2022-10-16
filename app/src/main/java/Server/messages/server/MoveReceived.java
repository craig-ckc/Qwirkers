package Server.messages.server;

import Game.Models.Move;
import Server.messages.Message;

public class MoveReceived extends Message{
    private static final long serialVersionUID = 103L;

    public String gameSession;
    public String handle;
    public Move move;

    public MoveReceived(String gameSession, String handle, Move move) {
        this.gameSession = gameSession;
        this.handle = handle;
        this.move = move;
    }

    public MoveReceived(String gameSession2, String handle2, Server.messages.client.Move move2) {
    }

    @Override
    public String toString() {
        return String.format("MoveReceived(%s, '%s', '%s', '%s')", gameSession, handle, move);
    }
}
