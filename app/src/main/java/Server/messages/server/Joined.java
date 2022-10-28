package Server.messages.server;

import java.util.List;

import Game.Models.Player;
import Server.messages.Message;

public class Joined extends Message {
    private static final long serialVersionUID = 101L;

    public List<Player> players;

    public Joined(List<Player> players) {
        this.players = players;
    }
}
