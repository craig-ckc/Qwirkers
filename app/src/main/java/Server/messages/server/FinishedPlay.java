package Server.messages.server;

import java.util.List;

import Game.Models.Player;
import Server.messages.Message;

public class FinishedPlay extends Message{
    private static final long serialVersionUID = 101L;
    
    public List<Player> players;
    public Player player;
    public String bagSize;

    public FinishedPlay(List<Player> players, Player player, String bagSize){
        this.players = players;
        this.player = player;
        this.bagSize = bagSize;
    }
}
