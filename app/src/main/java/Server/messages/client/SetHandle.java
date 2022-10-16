package Server.messages.client;

import Server.messages.Message;
import Server.messages.server.HandleSet;

public class SetHandle extends Message {
    private static final long serialVersionUID = 6L;

    public String handle;

    public SetHandle(String handle) {
        this.handle = handle;
    }

    @Override
    public String toString() {
        return String.format("SetHandle('%s')", handle);
    }
}