package Server.messages.client;

import java.util.List;

import Game.Models.Tile;
import Server.messages.Message;

public class Trade extends Message{
    private static final long serialVersionUID = 5L;

    public String session;
    public List<Tile> tiles;

    public Trade(String session, List<Tile> tiles){
        this.session = session;
        this.tiles = tiles;
    }
}
