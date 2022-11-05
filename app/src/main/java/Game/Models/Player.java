package Game.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player implements Serializable {
	private static final long serialVersionUID = 206L;

	private final String name;
	private final List<Tile> hand;
	private int avatar;
	private int points;
	public static final int MAXHANDSIZE = 6;

	public Player(String name) {
		this.name = name;
		hand = new ArrayList<Tile>();
		points = 0;
	}

	public Player(String name, int avatar) {
		this.name = name;
		this.avatar = avatar;
		hand = new ArrayList<Tile>();
		points = 0;
	}

	// add a tile to the players hand
	public void receiveTile(Tile tile) {
		if (hand.size() < MAXHANDSIZE)
			hand.add(tile);
	}

	// This will return the tile taken from the players hand
	public Tile removeTile(Tile tile) {
		for (Tile t : hand) {
			if (t.equals(tile)) {
				return hand.remove(hand.indexOf(t));
			}
		}
		return null;
	}

	public void emptyHand(){
		hand.clear();
	}

	// add points to the players current points
	public void addPoints(int points) {
		this.points = this.points + points;
	}

	public List<Tile> getHand() {
		return this.hand;
	}

	public int getPoints() {
		return this.points;
	}

	public String getName() {
		return this.name;
	}

	public int getAvatar() {
		return avatar;
	}

	public int similarAttribute() {
		List<Tile> tiles = this.getHand();
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

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Player))
			return false;

		Player player = (Player) obj;
		return avatar == player.avatar && player.name.equals(name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, avatar);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
}
