package Server.messages.server;

import java.util.List;

import Game.Models.Player;
import Server.messages.Message;

public class Update extends Message{
    private static final long serialVersionUID = 111L;

    public List<Player> players;
    public Player player;
    public int bag;

    public Update(List<Player> players, Player player, int bag) {
        this.players = players;
        this.player = player;
        this.bag = bag;
    }
}
