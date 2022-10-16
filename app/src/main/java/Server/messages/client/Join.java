package Server.messages.client;

import Server.messages.Message;

public class Join extends Message{
    private static final long serialVersionUID = 1L;
    public String name;
    public int avatar;

    public Join(String name, int avatar) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("%s joined", name);
    }
}
