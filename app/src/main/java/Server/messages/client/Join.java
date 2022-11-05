package Server.messages.client;

import Game.Models.Player;
import Server.messages.Message;

public class Join extends Message{
    private static final long serialVersionUID = 2L;

    public String session;
    public Player player;

    public Join(String session, Player player){
        this.session = session;
        this.player = player;
    }
}
