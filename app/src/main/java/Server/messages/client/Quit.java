package Server.messages.client;

import Server.messages.Message;
 
public class Quit extends Message{
    private static final long serialVersionUID = 5L;

    public String game;
    public String name;

    public Quit(String game, String name){
        this.game = game;
        this.name = name;
    }
}
