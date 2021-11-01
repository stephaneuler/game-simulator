package basic;

public class Field {
	private int x;
	private int y;
	private int val;

	public Field(int x, int y, int val) {
		this(x, y);
		this.val = val;
	}

	public Field(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + getX() + ", " + getY() + ")";
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getVal() {
		return val;
	}

	@Override
	public boolean equals(Object obj) {
		Field f2 = (Field) obj;
		return f2.x == x & f2.y == y & f2.val == val;
	}

	public String asChess() {
		char c = (char) ('A' + x -1);
		return "" + c + y;
	}

}
