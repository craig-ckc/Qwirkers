package Server.messages.server;

import java.util.List;

import Game.Models.Player;
import Server.messages.Message;
 
public class Left  extends Message{
    private static final long serialVersionUID = 105L;

    public String name;
    public List<Player> players;
    public Player player;
    public int bag;

    public Left(String name, List<Player> players, Player player, int bag) {
        this.name = name;
        this.players = players;
        this.player = player;
        this.bag = bag;
    }
}
