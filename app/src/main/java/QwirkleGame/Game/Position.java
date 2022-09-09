package QwirkleGame.Game;

public class Position {
	private final int x;
	private final int y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public static Position getPosition(String position){
		String[] pos = position.split(",");
		return new Position(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]));
	}

	@Override
	public String toString() {
		return x + "," + y;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Position))
			return false;
			
		Position position = (Position) obj;
		return (this.getX() == position.getX() && this.getY() == position.getY());
	}

	public boolean equals(String position){
		return (x + "," + y).equals(position);
	}

}