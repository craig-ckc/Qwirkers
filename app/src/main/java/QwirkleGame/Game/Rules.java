package QwirkleGame.Game;

import java.util.ArrayList;
import java.util.List;

import QwirkleGame.Enums.Dimension;
import QwirkleGame.Enums.Direction;

public class Rules {
    private static final int MAXLINELENGHT = 6;
    private Board board;

    public Rules() {
    }

    // region SETUP

    public List<Player> playerOrder(List<Player> players) {
        int n = players.size();
        for (int i = 0; i < n - 1; i++)
            for (int j = 0; j < n - i - 1; j++)
                if (similarAttriteCount(players.get(j)) < similarAttriteCount(players.get(j + 1))) {
                    Player temp = players.get(j);
                    players.set(j, players.get(j + 1));
                    players.set(j + 1, temp);
                }

        return players;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    private int similarAttriteCount(Player player) {
        List<Tile> tiles = player.getHand();
        int colorCount = 0;
        int shapeCount = 0;

        int index = 0;

        while (index < Player.MAXHANDSIZE) {
            int temp = 0;
            for (int i = index + 1; i < Player.MAXHANDSIZE; i++) {
                temp += tiles.get(index).getColor() == tiles.get(i).getColor() ? 1 : 0;
            }
            colorCount = Math.max(colorCount, temp);
            index++;
        }

        index = 0;

        while (index < Player.MAXHANDSIZE) {
            int temp = 0;
            for (int i = index + 1; i < Player.MAXHANDSIZE; i++) {
                temp += tiles.get(index).getShape() == tiles.get(i).getShape() ? 1 : 0;
            }
            shapeCount = Math.max(shapeCount, temp);
            index++;
        }

        return Math.max(shapeCount, colorCount);
    }

    // endregion


    // region GAMEPLAY

    public boolean checkWinCondition(Player currentPlayer, Bag bag){
        return currentPlayer.getHand().size() == 0 && bag.getSize() == 0;
    }

    public List<Position> validMoves(Tile tile, Board board) {
        List<Position> locations = new ArrayList<>();
        Dimension[] dim = new Dimension[]{Dimension.DIMX, Dimension.DIMY};

        this.board = board;

        if(board.isEmpty()){
            locations.add( new Position((Dimension.DIMX.getDim() / 2) , (Dimension.DIMY.getDim() / 2)));
            return locations;
        }

        for (int x = 0; x < dim[0].getDim(); x++)
            for (int y = 0; y < dim[1].getDim(); y++)
                if (isValid(x, y, tile)) {
                    locations.add(new Position( x , y));
                }

        return locations;
    }

    public boolean isValid(int x, int y, Tile tile) {
        // CASE 00: when the board is empty
        if (board.isEmpty() && x == Dimension.DIMX.getDim() / 2 && y == Dimension.DIMY.getDim() / 2)
            return true;

        // CASE 01: Check if the block is empty
        if (board.getBlock(x, y) != null)
            return false;

        // CASE 02: Check if the block is connected to a tile
        if (getNeighbours(x, y).size() < 1)
            return false;

        // CASE 03: Check if block is connected to a completed line
        List<Tile> top = getLine(x, y, Direction.UP, tile);
        List<Tile> right = getLine(x, y, Direction.RIGHT, tile);
        List<Tile> down = getLine(x, y, Direction.DOWN, tile);
        List<Tile> left = getLine(x, y, Direction.LEFT, tile);

        if (!(isLineValid(top) && isLineValid(right) && isLineValid(down) && isLineValid(left)))
            return false;

        // CASE 04: check if tiles in the same orientation are valid
        return isNeighbourhoodValid(top, right, down, left, tile);
    }

    private boolean isNeighbourhoodValid(List<Tile> top, List<Tile> right, List<Tile> down, List<Tile> left, Tile tile) {

        if (top.size() + down.size() - 1> MAXLINELENGHT)
            return false;

        if (right.size() + left.size() - 1> MAXLINELENGHT)
            return false;

        try {
            Tile t = top.get(1);
            Tile d = down.get(1);

            if (t.getSimilar(tile) != d.getSimilar(tile))
                return false;
        } catch (Exception e) {
            // TODO: handle exception
        }

        try {
            Tile r = right.get(1);
            Tile l = left.get(1);

            if (r.getSimilar(tile) != l.getSimilar(tile))
                return false;
        } catch (Exception e) {
            // TODO: handle exception
        }

        return true;
    }

    private boolean isLineValid(List<Tile> line) {
        if (line.size() < 2)
            return true;

        // CASE 01: Check if tile is being added to a block that is connected to a
        // completed line
        if (line.size() > MAXLINELENGHT)
            return false;

        // CASE 02: Check if the line has a duplicate of the tile
        Tile prev = line.get(0);
        for (int i = 1; i < line.size(); i++)
            if (prev.equal(line.get(i)))
                return false;

        // CASE 04: Check if all the Tiles in the line have a similar attribute
        Object obj = prev.getSimilar(line.get(1));

        if (obj == null) return false;

        for (int i = 1; i < line.size(); i++) {
            if (obj != prev.getSimilar(line.get(i)))
                return false;

            prev = line.get(i);
        }

        return true;
    }

    private List<Tile> getNeighbours(int x, int y) {
        List<Tile> tiles = new ArrayList<Tile>();

        Direction[] dirs = new Direction[]{Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT};

        for (Direction dir : dirs) {
            if ((board.getBlock(x + dir.getX(), y + dir.getY()) != null))
                tiles.add(board.getBlock(x + dir.getX(), y + dir.getY()));
        }

        return tiles;
    }

    private List<Tile> getLine(int x, int y, Direction dir, Tile tile) {
        List<Tile> tiles = new ArrayList<>();

        tiles.add(tile);

        x += dir.getX();
        y += dir.getY();

        while (board.getBlock(x, y) != null) {
            tiles.add(board.getBlock(x, y));

            x += dir.getX();
            y += dir.getY();
        }

        return tiles;
    }

    // endregion


    // region SCORING

    public int scorePlayer(int x, int y) {
        int points = 0;

        Direction[] dirs = new Direction[]{Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT};

        for (Direction dir : dirs) {
            List<Tile> line = getLine(x, y, dir, board.getBlock(x, y));
            points += (line.size() > 1) ? line.size() : 0;
        }

        return points;
    }

    public List<Player> playerRanking(List<Player> players) {
        // Bubble Sort Algorithm
        int n = players.size();
        for (int i = 0; i < n - 1; i++)
            for (int j = 0; j < n - i - 1; j++)
                if (players.get(j).getPoints() < players.get(j + 1).getPoints()) {
                    Player temp = players.get(j);
                    players.set(j, players.get(j + 1));
                    players.set(j + 1, temp);
                }

        return players;
    }

    // endregion
}
