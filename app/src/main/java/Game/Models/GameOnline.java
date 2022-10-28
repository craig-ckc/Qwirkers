package Game.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameOnline {
    private Board board;
    private Rules rules;
    private Player currentPlayer;
    private List<Player> players;
    private Bag bag;
    private List<Move> moves;
    private int playerCount; // helper number to change the players

    public GameOnline() {
    }

    public List<Position> validMoves(Tile tile) {
        return rules.validMoves(tile, board);
    }

    public Map<Position, Tile> getBoard() {
        return board.getBoard();
    }

    public boolean placeTile(Tile tile, Position position) {
        if(!rules.isValid(position, tile)) return false;

        Move move = new Move(currentPlayer.removeTile(tile), position);
        board.setBlock(position, tile);
        moves.add(move);

        return true;
    }
}
