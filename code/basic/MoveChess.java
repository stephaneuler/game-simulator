package basic;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoveChess extends Move {
	static String regex = "\\D*(\\d+)\\D*(\\d+)\\D*";
	static Pattern pattern = Pattern.compile(regex);
	Field from;
	Field to;
	int taken = 0;

	public void setTaken(int taken) {
		this.taken = taken;
	}

	public int getTaken() {
		return taken;
	}

	public MoveChess(int x, int y, int x2, int y2) {
		super();
		from = new Field(x, y);
		to = new Field(x2, y2);
	}

	public MoveChess(String line) {
		Matcher matcher = pattern.matcher(line);
		if (matcher.matches()) {
			int x = Integer.parseInt(matcher.group(1));
			int y = Integer.parseInt(matcher.group(2));
			to = new Field(x, y);
		}
	}

	@Override
	public String toString() {
		String s = from.asChess();
		if( taken == 0 )          {
			s += "-";
		} else {
			s += "x";
		}
		s += to.asChess();
		return s;
	}

	@Override
	public boolean equals(Object obj) {
		MoveChess m2 = (MoveChess) obj;
		return from.equals(m2.from) && to.equals(m2.to);
	}


}
