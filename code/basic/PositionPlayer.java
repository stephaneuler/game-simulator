package basic;

import java.util.List;

public class PositionPlayer extends Player {

	@Override
	Move nextMove(Position position, List<Move> moves) {
		Move bestMove = null;
		double bestScore = Double.NEGATIVE_INFINITY;
		for (Move move : moves) {
			position.move(move);
			double evaluationOfMove = eval(position);
			if (evaluationOfMove > bestScore) {
				bestMove = move;
				bestScore = evaluationOfMove;
			}
			position.undo();
		}
		return bestMove;
	}

	private double eval(Position p) {
		return Math.random();
	}
}
