package Server.messages.client;

import Game.Models.Player;
import Server.messages.Message;
 
public class Join extends Message{
    private static final long serialVersionUID = 1L; 

    public Player player;
    public String game;

    public Join(Player player, String game) {
        this.player = player;
        this.game = game;
    }
}