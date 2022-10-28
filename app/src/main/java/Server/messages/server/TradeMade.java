package Server.messages.server;

import java.util.List;

import Game.Models.Tile;
import Server.messages.Message;

public class TradeMade extends Message {
    private static final long serialVersionUID = 104L;

    public List<Tile> tiles;

    public TradeMade(List<Tile> tiles) {
        this.tiles = tiles;
    }
}
