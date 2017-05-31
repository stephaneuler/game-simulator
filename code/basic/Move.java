package basic;

public class Move {
	int s;

	public Move(int s) {
		super();
		this.s = s;
	}

	public Move(String line) {
		this( Integer.parseInt(line));
	}

	@Override
	public String toString() {
		return "Move [s=" + s + "]";
	}
	
}
