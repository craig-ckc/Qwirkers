package Server.messages.server;

import java.util.List;

import Game.Models.Player;
import Server.messages.Message;
 
public class Left extends Message{
    private static final long serialVersionUID = 106L;
    
    public List<Player> players;
    public String player;

    public Left(List<Player> players, String player) {
        this.players = players;
        this.player = player;
    }
}
