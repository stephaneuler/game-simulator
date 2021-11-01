package basic;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoveGoB extends Move {
	static String regex = "\\D*(\\d+)\\D*(\\d+)\\D*";
	static Pattern pattern = Pattern.compile(regex);
	Field field;
	List<Field> taken = new ArrayList<>();

	public MoveGoB(int x, int y) {
		super();
		field = new Field(x, y);
	}

	public MoveGoB(String line) {
		Matcher matcher = pattern.matcher(line);
		if (matcher.matches()) {
			int x = Integer.parseInt(matcher.group(1));
			int y = Integer.parseInt(matcher.group(2));
			field = new Field(x, y);
		}
	}

	public List<Field> getTaken() {
		return taken;
	}

	@Override
	public String toString() {
		return field.toString();
	}

	@Override
	public boolean equals(Object obj) {
		MoveGoB m2 = (MoveGoB) obj;
		return field.equals(m2.field);
	}

	public void addTaken(Field f) {
		taken.add(f);
	}

}
