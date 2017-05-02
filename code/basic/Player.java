package basic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Player implements Comparable<Object> {
	String name = "?";
	Map<Player, Integer> scores = new HashMap<>();
	int total = 0;
	double elo = 1400;
	
	abstract Move nextMove( Position p, List<Move> moves );

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " " + name + " (" + (int) elo + ")";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addScore(Player player, Integer score) {
		scores.put( player, score);
		total += score;
	}

	public Object getScore(Player q) {
		return scores.get( q );
	}
	
	public int getTotal() {
		return total;
	}

	public void reset() {
		// children overwrite this method to clean up before next game
	}

	@Override
	public int compareTo(Object o) {
		Player p2 = (Player) o;
		return p2.total - total;
	}


}
