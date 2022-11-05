package Server.messages.server;

import java.util.List;

import Game.Models.Tile;
import Server.messages.Message;

public class Traded extends Message{
    private static final long serialVersionUID = 108L;
    
    public String name;
    public List<Tile> tiles;

    public Traded(String name, List<Tile> tiles){
        this.name = name;
        this.tiles = tiles;
    }
}