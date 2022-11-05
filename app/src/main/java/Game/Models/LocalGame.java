package Game.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalGame implements Serializable {
    private final List<Move> moves;
    private final Map<Position, Integer> points;
    private Board board;
    private Rules rules;
    private Player currentPlayer;
    private List<Player> players;
    private Bag bag;
    private int playerCount; // helper number to change the players


    public LocalGame(List<Player> players) {
        this.players = players;
        this.moves = new ArrayList<>();
        this.points = new HashMap<>();
    }

    public void start() {
        board = new Board();

        rules = new Rules();

        bag = new Bag();

        dealHands();

        players = rules.playerOrder(players);

        currentPlayer = players.get(0);

        playerCount = 0;
    }

    private void dealHands() {
        for (Player player : players) {
            for (int i = 0; i < Player.MAXHANDSIZE; i++) {
                player.receiveTile(bag.takeTile());
            }
        }
    }

    public ArrayList<Tile> getBoard() {
        return board.blocks();
    }

    public Map<Position, Tile> getBoard(int i) {
        return board.board();
    }

    public void changePlayer() {
        playerCount = (playerCount + 1) % players.size();
        currentPlayer = players.get(playerCount);
    }

    public List<Position> validMoves(Tile tile) {
        return rules.validMoves(tile, board);
    }

    public int getBoardWidth() {
        return board.getWidth();
    }

    public boolean placeTile(Tile tile, Position position) {
        if (!rules.isValid(position, tile)) return false;

        Move move = new Move(currentPlayer.removeTile(tile), position);
        board.setBlock(position, tile);
        moves.add(move);

        Map<Position, Integer> entries = rules.scores(move, moves);
        for (Position pos : entries.keySet()){
            if (points.containsKey(pos)){
                if(entries.get(pos) > points.get(pos))
                    points.replace(pos, entries.get(pos));
            }
            else
                points.put(pos, entries.get(pos));
        }

        return true;
    }

    public void presetMoves(Tile tile, Position position) {
        board.setBlock(position, tile);
    }

    public void scorePlay() {
        int score = 0;

        for (Integer point : points.values())
            score += point;

        currentPlayer.addPoints((score > 0) ? score : 1);
    }

    public void scorePlay(int score) {
        currentPlayer.addPoints(score);
    }

    public boolean done() {
        // score the moves made by the current player
        scorePlay();

        // clear the move list for the next player
        moves.clear();
        points.clear();

        // check win condition
        if (bag.getSize() < 1 && currentPlayer.getHand().size() < 1) {
            scorePlay(6);
            return true;
        }

        // refill currentPlayers hand
        while (currentPlayer.getHand().size() < Player.MAXHANDSIZE && bag.getSize() > 0) {
            Tile tile = bag.takeTile();

            if (tile == null)
                break;

            currentPlayer.receiveTile(tile);
        }

        // change player
        changePlayer();

        return false;
    }

    public void undoLastMove() {
        // return if there is no moves to undo
        if (moves.size() < 1) return;

        Move move = moves.remove(moves.size() - 1);

        // remove tile board
        Position position = move.getPosition();
        board.removeTile(position);

        // return tile to players hand
        currentPlayer.receiveTile(move.getTile());
    }

    public void clearMoves() {
        while (moves.size() > 0) {
            undoLastMove();
        }
    }

    public void trade(List<Tile> tiles) {
        if (tiles.size() < bag.getSize()) {
            for (Tile tile : tiles) {
                currentPlayer.removeTile(tile);
            }

            List<Tile> temp = bag.tradeTile(tiles);

            for (Tile tile : temp) {
                currentPlayer.receiveTile(tile);
            }
        }
    }

    public void passPlay() {
        clearMoves();
        changePlayer();
    }

    public int getBagSize() {
        return bag.getSize();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
