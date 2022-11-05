package Server.messages.server;

import java.util.List;

import Game.Models.Tile;
import Server.messages.Message;

public class TileRefill extends Message {
    private static final long serialVersionUID = 108L;

    public String name;
    public List<Tile> tiles;

    public TileRefill(String name, List<Tile> tiles){
        this.name = name;
        this.tiles = tiles;
    }
}
