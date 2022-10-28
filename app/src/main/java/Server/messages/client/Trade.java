package Server.messages.client;

import java.util.List;

import Game.Models.Tile;
import Server.messages.Message;
 
public class Trade extends Message{
    private static final long serialVersionUID = 3L;

    public List<Tile> tiles;
}
