package basic;

public class MoveVG extends Move {
	int s;

	public MoveVG(int s) {
		super();
		this.s = s;
	}

	public MoveVG(String line) {
		this( Integer.parseInt(line) );
	}

	@Override
	public String toString() {
		return ""+s;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
	        return true;
	    if (o == null || getClass() != o.getClass()) 
	        return false;
		return ((MoveVG) o).s == s;
	}
	
}
