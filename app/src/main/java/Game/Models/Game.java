package Game.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private Player player;
    private Bag bag;
    private Rules rules;
    private Board board;
    
    private List<Player> players;
    private List<Move> moves;
    private List<Tile> trade;
    private Map<Position, Integer> points;

    public Game(List<Player> players) {
        this.players = players;
    }

    public Game() {
        this.players = new ArrayList<>();
    }

    public void start() {
        this.bag = new Bag();
        this.rules = new Rules();
        this.board = new Board();

        this.moves = new ArrayList<>();
        this.trade = new ArrayList<>();
        this.points = new HashMap<>();

        dealHands();

        players = rules.playerOrder(players);
        player = players.get(0);
    }

    private void dealHands() {
        for (Player player : players) {
            player.hand().clear();
            for (int i = 0; i < Player.MAXHANDSIZE; i++) {
                player.receiveTile(bag.drawTile());
            }
        }
    }

    public List<Position> validMoves(Tile tile) {
        return rules.validMoves(tile, board);
    }

    public boolean play(Tile tile, Position position) {
        if (!rules.isValid(position, tile))
            return false;
        
        for (Tile t : trade) {
            if(t.UID() == tile.UID()) return false;
        }

        Move move = new Move(player.removeTile(tile), position);
        board.setBlock(position, tile);
        moves.add(move);

        Map<Position, Integer> entries = rules.scores(move, moves);
        for (Position pos : entries.keySet()) {
            if (points.containsKey(pos)) {
                if (entries.get(pos) > points.get(pos))
                    points.replace(pos, entries.get(pos));
            } else
                points.put(pos, entries.get(pos));
        }

        return true;
    }

    public Player nextPlayer() {
        this.player = players.stream()
                .filter(player -> player.turn() == (this.player.turn() + 1) % players.size())
                .findAny()
                .orElse(this.player);

        return this.player;
    }

    public Map<Position, Tile> board() {
        return board.board();
    }

    public boolean addPlayer(Player player) {
        if (players.size() < 4) {
            players.add(player);
            return true;
        }

        return false;
    }



    private void score() {
        int score = 0;

        for (Integer point : points.values())
            score += point;

        player.addPoints((score > 0) ? score : 1);
    }

    private void score(int score) {
        player.addPoints(score);
    }

    public boolean done() {
        // score the moves made by the current player
        score();

        // clear the move list for the next player
        moves.clear();
        trade.clear();
        points.clear();

        // check win condition
        if (bag.getSize() < 1 && player.hand().size() < 1) {
            score(6);
            return true;
        }

        // refill currentPlayers hand
        while (player.hand().size() < Player.MAXHANDSIZE && bag.getSize() > 0) {
            Tile tile = bag.drawTile();

            if (tile == null)
                break;

            player.receiveTile(tile);
        }

        // change player
        nextPlayer();

        return false;
    }

    public Move undo() {
        // return if there is no moves to undo
        if (moves.size() < 1)
            return null;

        Move move = moves.remove(moves.size() - 1);
        points.remove(move.getPosition());

        // remove tile board
        Position position = move.getPosition();
        board.removeTile(position);

        // return tile to players hand
        player.receiveTile(move.getTile());

        return move;
    }

    public List<Move> clear() {
        List<Move> moves = new ArrayList<>();

        while (this.moves.size() > 0) {
            moves.add(undo());
        }

        return moves;
    }

    public List<Tile> trade(List<Tile> tiles) {
        if (tiles.size() < bag.getSize()) {
            for (Tile tile : tiles) {
                player.removeTile(tile);
            }

            List<Tile> temp = bag.tradeTiles(tiles);

            for (Tile tile : temp) {
                player.receiveTile(tile);
            }

            this.trade.addAll(temp);

            return temp;
        }

        return new ArrayList<>();
    }

    public void passPlay() {
        clear();
        nextPlayer();
    }



    public int bagCount() {
        return bag.getSize();
    }

    public Player currentPlayer() {
        return player;
    }

    public List<Player> players() {
        return players;
    }



    public List<Move> removePlayer(Player player){
        for(Tile tile : player.hand()){
            bag.returnTile(tile);
        }

        List<Move> clear = new ArrayList<>();

        if(this.player.equals(player) && this.moves.size() > 1){
            while(moves.size() > 1){
                clear.add(undo());
            }
        }

        this.players.remove(player);
        this.points.clear();

        return clear;
    }



    public int size(){
        return players.size();
    }
}
