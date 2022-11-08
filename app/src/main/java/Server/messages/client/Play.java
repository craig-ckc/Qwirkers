package Server.messages.client;

import Game.Models.Position;
import Game.Models.Tile;
import Server.messages.Message;
 
public class Play extends Message{
    private static final long serialVersionUID = 7L;
    
    public Position position;
    public Tile tile;

    public Play(Position position, Tile tile){
        this.position = position;
        this.tile = tile;
    }
}