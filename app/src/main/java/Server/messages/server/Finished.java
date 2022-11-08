package Server.messages.server;

import java.util.List;

import Game.Models.Player;
import Server.messages.Message;
 
public class Finished  extends Message{
    private static final long serialVersionUID = 101L;

    public List<Player> players;
    public Player player;
    public int bag;

    public Finished(List<Player> players, Player player, int bag) {
        this.players = players;
        this.player = player;
        this.bag = bag;
    }
}
