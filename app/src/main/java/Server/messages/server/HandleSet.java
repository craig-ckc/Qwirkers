package Server.messages.server;

import Server.messages.Message;

public class HandleSet extends Message {
    private static final long serialVersionUID = 100L;

    public String handle;

    public HandleSet(String handle) {
        this.handle = handle;
    }

    @Override
    public String toString() {
        return String.format("HandleSet('%s')", handle);
    }
}
