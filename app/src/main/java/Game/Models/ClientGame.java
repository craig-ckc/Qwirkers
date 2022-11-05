package Game.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientGame {
    private final Board board;
    private Rules rules;
    private final List<Player> players;
    private final List<Move> moves;

    public Player currentPlayer;

    public ClientGame() {
        this.players = new ArrayList<>();
        this.moves = new ArrayList<>();
        this.currentPlayer = new Player("Player", -1);

        this.board = new Board();
    }

    public List<Position> validMoves(Tile tile) {
        return rules.validMoves(tile, board);
    }

    public Map<Position, Tile> getBoard() {
        return board.board();
    }

    public boolean placeTile(Tile tile, Position position) {
        if (!rules.isValid(position, tile)) return false;

        Move move = new Move(currentPlayer.removeTile(tile), position);
        board.setBlock(position, tile);
        moves.add(move);

        return true;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players.clear();
        this.players.addAll(players);
    }

    public Move undoLastMove() {
        // return if there is no moves to undo
        if(moves.size() < 1) return null;

        Move move = moves.remove(moves.size() - 1);

        // remove tile board
        Position position = move.getPosition();
        board.removeTile(position);

        // return tile to players hand
        currentPlayer.receiveTile(move.getTile());

        return move;
    }

    public int moves(){
        return moves.size();
    }
}
