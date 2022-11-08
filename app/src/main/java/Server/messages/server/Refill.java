package Server.messages.server;

import java.util.List;

import Game.Models.Tile;
import Server.messages.Message;
 
public class Refill  extends Message{
    private static final long serialVersionUID = 107L;

    public List<Tile> tiles;

    public Refill(List<Tile> tiles) {
        this.tiles = tiles;
    }
}
