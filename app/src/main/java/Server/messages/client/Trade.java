package Server.messages.client;

import java.util.List;

import Game.Models.Tile;
import Server.messages.Message;
 
public class Trade extends Message{
    private static final long serialVersionUID = 3L;

    public String game;
    public List<Tile> tiles;

    public Trade(String game, List<Tile> tiles){
        this.game = game;
        this.tiles = tiles;
    }
}
