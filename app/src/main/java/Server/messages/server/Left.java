package Server.messages.server;

import Server.messages.Message;

public class Left extends Message {
    private static final long serialVersionUID = 102L;

    public String groupName;
    public String handle;

    public Left(String groupName, String handle) {
        this.groupName = groupName;
        this.handle = handle;
    }

    @Override
    public String toString() {
        return String.format("Left('%s', '%s')", groupName, handle);
    }
}
