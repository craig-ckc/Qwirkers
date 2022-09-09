package QwirkleGame.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Game {
    private Board board;
    private Rules rules;
    private Player currentPlayer;
    private List<Player> players;
    private Bag bag;
    private List<Move> moves;
    private int playerCount; // helper number to change the players

    public Game(Player[] players) {
        this.players = new ArrayList<>();

        this.players.addAll(Arrays.asList(players));

        this.moves = new ArrayList<>();

        start();
    }

    // region SETUP

    private void start() {
        board = new Board();

        rules = new Rules();

        bag = new Bag();

        dealHands();

        players = rules.playerOrder(players);

        currentPlayer = players.get(0);

        playerCount = 0;

        System.out.println("Working AOK");
    }

    // endregion

    private void dealHands() {
        for (Player player : players) {
            for (int i = 0; i < Player.MAXHANDSIZE; i++) {
                player.receiveTile(bag.takeTile());
            }
        }
    }


    // #region GAME PLAY

    public ArrayList<Tile> getBoard() {
        return board.getBlocks();
    }

    public Map<String, Tile> getBoard(int i) {
        return board.getBoard();
    }

    public void changePlayer() {
        playerCount = (playerCount + 1) % players.size();
        currentPlayer = players.get(playerCount);
    }

    public List<Position> validMoves(Tile tile) {
        return rules.validMoves(tile, board);
    }

    public boolean placeTile(Tile tile, Position position) {
        if(!rules.isValid(position.getX(), position.getY(), tile)) return false;

        Move move = new Move(currentPlayer.removeTile(tile), position);
        board.setBlock(position.getX(), position.getY(), tile);
        moves.add(move);

        // TODO: check if the players hand is empty and the bag is empty as well (Win condition)

        return true;
    }

    public void presetMoves(Tile tile, Position position) {
        board.setBlock(position.getX(), position.getY(), tile);
    }


    public void scorePlay() {
        for (Move move : moves) {
            Position position = move.getPosition();
            currentPlayer.addPoints(rules.scorePlayer(position.getX(), position.getY()));
        }
    }

    public void donePlay() {
        // score the moves made by the current player
        scorePlay();

        // clear the move list for the next player
        moves.clear();

        // refill currentPlayers hand
        while (currentPlayer.getHand().size() < Player.MAXHANDSIZE) {
            currentPlayer.receiveTile(bag.takeTile());
        }

        // change player
        changePlayer();
    }

    public void undoLastMove() {
        // return if there is no moves to undo
        if(moves.size() < 1) return;

        Move move = moves.remove(moves.size() - 1);

        // remove tile board
        Position position = move.getPosition();
        board.removeTile(position.getX(), position.getY());

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
            for(Tile tile : tiles){
                currentPlayer.removeTile(tile);
            }

            List<Tile> temp = bag.tradeTile(tiles);

            for (Tile tile : temp) {
                currentPlayer.receiveTile(tile);
            }
        }
    }

    // #endregion

    public void passPlay() {
        clearMoves();
        changePlayer();
    }

    private void seePlayersHands() {
        for (Player player : players) {
            System.out.println(player.getName() + "'s hand:");
            for (Tile tile : player.getHand()) {
                System.out.println(tile);
            }
            System.out.println();
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
