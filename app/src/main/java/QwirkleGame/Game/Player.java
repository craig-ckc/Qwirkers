package QwirkleGame.Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
	private String name;
	private List<Tile> hand;
	private int profileImg;
	private int points;
	public static final int MAXHANDSIZE = 6;

	public Player(String name) {
		this.name = name;
		hand = new ArrayList<Tile>();
		points = 0;
	}

	public Player(String name, int profileImg) {
		this.name = name;
		this.profileImg = profileImg;
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

	public int getProfileImg() {
		return profileImg;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
}
