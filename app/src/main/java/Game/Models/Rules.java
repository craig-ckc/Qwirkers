package Game.Models;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import Game.Enums.Dimension;
import Game.Enums.Direction;

public class Rules {
    private static final int MAXLINELENGHT = 6;
    private Board board;
    private List<Move> moves;

    public Rules() {
    }

    public List<Player> playerOrder(List<Player> players) {
        return players.stream()
                .sorted(Comparator.comparingInt(Player::similarAttribute).reversed())
                .collect(Collectors.toList());
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    private int similarAttributeCount(Player player) {
        List<Tile> tiles = player.getHand();
        int colorCount = 0;
        int shapeCount = 0;

        int index = 0;

        while (index < Player.MAXHANDSIZE) {
            int temp = 0;
            for (int i = index + 1; i < Player.MAXHANDSIZE; i++) {
                temp += tiles.get(index).color() == tiles.get(i).color() ? 1 : 0;
            }
            colorCount = Math.max(colorCount, temp);
            index++;
        }

        index = 0;

        while (index < Player.MAXHANDSIZE) {
            int temp = 0;
            for (int i = index + 1; i < Player.MAXHANDSIZE; i++) {
                temp += tiles.get(index).shape() == tiles.get(i).shape() ? 1 : 0;
            }
            shapeCount = Math.max(shapeCount, temp);
            index++;
        }

        return Math.max(shapeCount, colorCount);
    }

    public boolean gameOver(Player player, Bag bag) {
        return bag.getSize() < 1 && player.getHand().size() < 1;
    }

    public List<Position> validMoves(Tile tile, Board board) {
        List<Position> locations = new ArrayList<>();
        Dimension[] dim = new Dimension[]{Dimension.DIMX, Dimension.DIMY};

        this.board = board;

        if (board.isEmpty()) {
            locations.add(new Position((Dimension.DIMX.getDim() / 2), (Dimension.DIMY.getDim() / 2)));
            return locations;
        }

        List<Position> potential = getPotentialBlocks();

        for (Position position : potential) {
            if (isValid(position, tile)) {
                locations.add(position);
            }
        }

        return locations;
    }

    public boolean isValid(Position pos, Tile tile) {
        int x = pos.getX();
        int y = pos.getY();

        // CASE 00: when the board is empty
        if (board.isEmpty() && x == Dimension.DIMX.getDim() / 2 && y == Dimension.DIMY.getDim() / 2)
            return true;

        // CASE 01: Check if the block is empty
        if (board.getBlock(new Position(x, y)) != null)
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

        if (top.size() + down.size() - 1 > MAXLINELENGHT)
            return false;

        if (right.size() + left.size() - 1 > MAXLINELENGHT)
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
            if (prev.equals(line.get(i)))
                return false;

        // CASE 04: Check if all the Tiles in the line have a similar attribute
        Object obj = prev.getSimilar(line.get(1));

        if (obj == null)
            return false;

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
            if ((board.getBlock(new Position(x + dir.getX(), y + dir.getY())) != null))
                tiles.add(board.getBlock(new Position(x + dir.getX(), y + dir.getY())));
        }

        return tiles;
    }

    private List<Tile> getLine(int x, int y, Direction dir, Tile tile) {
        List<Tile> tiles = new ArrayList<>();

        tiles.add(tile);

        x += dir.getX();
        y += dir.getY();

        while (board.getBlock(new Position(x, y)) != null) {
            tiles.add(board.getBlock(new Position(x, y)));

            x += dir.getX();
            y += dir.getY();
        }

        return tiles;
    }

    public List<Player> playerRanking(List<Player> players) {
        return players.stream()
                .sorted(Comparator.comparingInt(Player::getPoints).reversed())
                .collect(Collectors.toList());
    }

    private List<Position> getPotentialBlocks() {
        List<Position> potential = new ArrayList<>();
        List<Position> filledBlocks = board.filledBlocks();

        for (Position position : filledBlocks) {
            for (Direction dir : Direction.values()) {
                Position pos = new Position(position.getX() + dir.getX(), position.getY() + dir.getY());
                if (board.getBlock(pos) == null) {
                    potential.add(pos);
                }
            }
        }

        return potential;
    }

    private List<Move> getLineMove(int x, int y, Direction dir, Move move) {
        List<Move> moves = new ArrayList<>();

        x += dir.getX();
        y += dir.getY();

        while (board.getBlock(new Position(x, y)) != null) {
            Tile tile = board.getBlock(new Position(x, y));
            Position pos = new Position(x, y);

            moves.add(new Move(tile, pos));

            x += dir.getX();
            y += dir.getY();
        }

        return moves;
    }

    public Map<Position, Integer> scores(Move move, List<Move> moves) {
        Map<Position, Integer> entries = new HashMap<>();
        Position pos = move.getPosition();

        // point on the vertical line
        int points = 0;
        List<Move> vertical = Stream
                .concat(
                        getLineMove(pos.getX(), pos.getY(), Direction.UP, move).stream(),
                        getLineMove(pos.getX(), pos.getY(), Direction.DOWN, move).stream()
                ).collect(Collectors.toList());

        points += (vertical.size() < 1) ? vertical.size() : vertical.size() + 1;
        entries.put(new Position(0, pos.getY()), points);

        // point on the horizontal line
        points = 0;
        List<Move> horizontal = Stream
                .concat(
                        getLineMove(pos.getX(), pos.getY(), Direction.RIGHT, move).stream(),
                        getLineMove(pos.getX(), pos.getY(), Direction.LEFT, move).stream()
                ).collect(Collectors.toList());

        points += (horizontal.size() < 1) ? horizontal.size() : horizontal.size() + 1;
        entries.put(new Position(pos.getX(), 0), points);

        return entries;
    }
}
