package basic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class Player implements Comparable<Object> {
	static Random random = new Random();
	private String name = "?";
	private Map<Player, Integer> scores = new HashMap<>();
	private int totalScore = 0;
	private double elo = 1400;
	
	abstract Move nextMove( Position  p, List<Move> moves );

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
		totalScore += score;
	}

	public Object getScore(Player q) {
		return scores.get( q );
	}
	
	public int getTotal() {
		return totalScore;
	}

	public void reset() {
		// children overwrite this method to clean up before next game
	}

	@Override
	public int compareTo(Object o) {
		Player p2 = (Player) o;
		return p2.totalScore - totalScore;
	}


}
