package Game.Models;

import java.io.Serializable;

import Game.Enums.Color;
import Game.Enums.Shape;

public class Tile implements Serializable {
    private static final long serialVersionUID = 210L;

    private final Shape shape;
    private final Color color;
    private final int UID;

    public Tile(int UID, Shape shape, Color color) {
        this.UID = UID;
        this.shape = shape;
        this.color = color;
    }

    public Shape shape() {
        return this.shape;
    }

    public Color color() {
        return this.color;
    }

    public int UID() {
        return UID;
    }

    public Object getSimilar(Tile tile) {
        if (this.shape == tile.shape()) {
            return this.shape;
        } else if (this.color == tile.color()) {
            return this.color;
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Tile))
            return false;

        Tile tile = (Tile) obj;
        return shape == tile.shape && color == tile.color;
    }

    @Override
    public String toString() {
        return color + "-" + shape;
    }

}
