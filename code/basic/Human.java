package basic;

import java.util.List;

import jserver.Dialogs;
import plotter.Sleep;

public class Human extends Player {
	Field from = null;

	@Override
	Move nextMove(Position p, List<Move> moves) {
		while (true) {
			String input = p.getGUI().ask();
			if (input.startsWith("#")) {
				Move move = parseLine(input, p);
				if (move != null && moves.contains(move)) {
					return move;
				}
			} else if (input.startsWith("moves")) {
				Dialogs.showList(moves, "Zuege", null);
			} else if (input.startsWith("resign")) {
				return null;
			}
			Sleep.sleep(20);
		}
	}

	private Move parseLine(String fieldString, Position position) {
		String[] parts = fieldString.split(" ");
		int x = Integer.parseInt(parts[2]);
		int y = Integer.parseInt(parts[3]);
		if (position instanceof PositionVG) {
			if (1 <= x & x <= Position.getN()) {
				return new MoveVG(x);
			}
		} else if (position instanceof PositionGoB) {
			return new MoveGoB(x, y);
		} else if (position instanceof PositionKuT) {
			if (from == null) {
				if( ((PositionKuT)position).isPlayerOnField( x, y) ) {
					from = new Field(x, y);
				} 
			} else {
				Move m = new MoveChess(from.getX(), from.getY(), x, y);
				from = null;
				return m;
			}
		}
		return null;
	}

}
