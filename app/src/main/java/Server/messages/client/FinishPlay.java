package Server.messages.client;

import Server.messages.Message;
 
public class FinishPlay extends Message{
    private static final long serialVersionUID = 4L;

    public String game;

    public FinishPlay(String game){
        this.game = game;
    }
}
