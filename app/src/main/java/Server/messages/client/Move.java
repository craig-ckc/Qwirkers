package Server.messages.client;

import Game.Models.Position;
import Game.Models.Tile;
import Server.messages.Message;
 
public class Move extends Message{
    private static final long serialVersionUID = 2L;

    public String game;
    public Tile tile;
    public Position position;

    public Move(String game, Tile tile, Position position){
        this.game = game;
        this.tile = tile;
        this.position = position;
    }
}
