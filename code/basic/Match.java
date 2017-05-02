package basic;

import java.util.HashMap;
import java.util.Map;

public class Match {
	Player p1, p2;
	int N;
	
	public Match(Player p1, Player p2, int n) {
		super();
		this.p1 = p1;
		this.p2 = p2;
		N = n;
	}

	public Map<Player, Integer> play() {
		Map<Player, Integer> scores = new HashMap<>();

		scores.put(p1, 0);
		scores.put(p2, 0);

		Player[] players = new Player[2];
		for (int n = 0; n < N; n++) {
			if( n % 2 == 0 ) {
				players[0] = p1;
				players[1] = p2;
			} else {
				players[0] = p2;
				players[1] = p1;				
			}
			p1.reset();
			p2.reset();
			
			Game game = new Game();
			Player winner = game.play(players);
			System.out.println(p1 + " - " + p2 + " Winner:" + winner);
			System.out.println( game.getPosition().showHistory() );
			if (winner == null) {
				scores.put(p1, scores.get(p1) + 1);
				scores.put(p2, scores.get(p2) + 1);
			} else {
				scores.put(winner, scores.get(winner) + 2);
			}
			//System.out.println(scores);
		}		
		return scores;
	}
	
	

}
