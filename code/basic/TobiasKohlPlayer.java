package basic;

import java.util.List;
import java.util.Random;

public class TobiasKohlPlayer extends Player {
	int currentMove;
	Random random = new Random();
	int player;
	Move m;
	Position p;
	List<Move> moves;

	@Override
	Move nextMove(Position p, List<Move> moves) {
		this.p = p;
		this.moves = moves;
		player = p.nextPlayer;
		work();
		m = new Move(currentMove);
		return m;
	}

	public void work() {

		// checking if i can win anywhere
		for (int x = 1; x <= 7; x++) {
			m = new Move(x);
			p.move(m);
			if (p.win) {
				currentMove = x;
				return;
			} else {
				p.undo();
			}
		}
		// if i cant win then checking if theres somewhere the enemy would win
		// and blocking it
		p.nextPlayer();
		for (int y = 1; y <= 7; y++) {
			m = new Move(y);
			p.move(m);
			if (p.win) {
				currentMove = y;
				return;
			} else {
				p.undo();
			}
		}
		p.nextPlayer();
		randomMatches();
	}

	public void randomMatches() {
		Tester t = new Tester();
		int chance = 0;
		Position test = new Position(p);
		Move testM = new Move(1);
		for (int f = 1; f <= 7; f++) {

			int o = 0;
			int mo = 0;
			for (int g = 1; g < moves.size(); g++) {
				if (f == moves.get(g).s) {
					mo = moves.get(g).s;
				}
			}
			if (f == mo) {
				for (int n = 1; n <= 1000; n++) {
					test = new Position(p);
					testM = new Move(f);
					test.move(testM);

					while (test.isWin() == false) {

						test.nextPlayer();
						testM = t.nextMove(test, test.getMoves());
						test.move(testM);
					}

					if (test.getWinner() == player) {
						o = o + 1;
						test.win = false;
					}
					test = null;

				}

				if (o > chance) {
					chance = o;
					currentMove = f;
					System.out.println("Chance:" + chance + "und o" + o + "bei move" + currentMove + "und f" + f);
				}
			}

		}

	}

	private class Tester extends Player {
		Random random = new Random();
		int testMove;
		Move m;
		Position p;
		List<Move> moves;

		@Override
		Move nextMove(Position p, List<Move> moves) {
			this.p = p;
			work();
			this.moves = moves;
			m = new Move(testMove);
			return m;
		}

		public void work() {

			// checking if i can win anywhere
			for (int x = 1; x <= 7; x++) {
				m = new Move(x);
				p.move(m);
				if (p.isWin() == true) {
					testMove = x;
					return;
				} else {
					p.undo();
				}
			}
			// if i cant win then checking if theres somewhere the enemy would
			// win and blocking it
			p.nextPlayer();
			for (int y = 1; y <= 7; y++) {
				m = new Move(y);
				p.move(m);
				if (p.isWin() == true) {
					testMove = y;
					return;
				} else {
					p.undo();
				}
			}
			p.nextPlayer();
			testMove = random.nextInt(7) + 1;

		}

	}
}
