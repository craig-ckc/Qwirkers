package Server.messages.server;

import java.util.List;

import Game.Models.Player;
import Server.messages.Message;

public class StartGame extends Message{
    private static final long serialVersionUID = 106L;
    
    public List<Player> players;
    public Player activePlayer;
    public String bagSize;

    public StartGame(List<Player> players, Player activePlayer, String bagSize){
        this.players = players;
        this.activePlayer = activePlayer;
        this.bagSize = bagSize;
    }
}
