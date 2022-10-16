package Server.messages.server;

import Server.messages.Message;

public class Joined extends Message {
    private static final long serialVersionUID = 101L;

    public String gameSession;
    public String handle;

    public Joined(String gameSession, String handle) {
        this.gameSession = gameSession;
        this.handle = handle;
    }

    @Override
    public String toString() {
        return String.format("Joined(%s, %s)", gameSession, handle);
    }
}
