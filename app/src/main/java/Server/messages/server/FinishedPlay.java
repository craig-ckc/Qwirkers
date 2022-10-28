package Server.messages.server;

import java.util.List;

import Game.Models.Player;
import Server.messages.Message;
 
public class FinishedPlay extends Message{
    private static final long serialVersionUID = 105L;

    public List<Player> players;

    public FinishedPlay(List<Player> players) {
        this.players = players;
    }
    
}
