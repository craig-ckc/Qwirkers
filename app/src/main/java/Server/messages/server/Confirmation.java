package Server.messages.server;

import Server.messages.Message;

public class Confirmation extends Message {
    private static final long serialVersionUID = 100L;

    public String game;

    public Confirmation(String game){
        this.game = game;
    }
}
