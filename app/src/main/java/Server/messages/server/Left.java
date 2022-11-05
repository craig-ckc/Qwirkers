package Server.messages.server;

import java.util.List;

import Game.Models.Player;
import Server.messages.Message;

public class Left extends Message{
    private static final long serialVersionUID = 104L;
    
    public String name;
    public Player player;
    public List<Player> players;
    public String bagSize;

    public Left(String name, Player player, List<Player> players, String bagSize){
        this.name = name;
        this.player = player;
        this.players = players;
        this.bagSize = bagSize;
    }
}
