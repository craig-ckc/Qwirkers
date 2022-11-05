package Server.messages.server;

import java.util.List;

import Game.Models.Player;
import Server.messages.Message;

public class Joined extends Message{
    private static final long serialVersionUID = 103L;
    
    public String name;
    public List<Player> players;

    public Joined(String name, List<Player> players){
        this.name = name;
        this.players = players;
    }
}
