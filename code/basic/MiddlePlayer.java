package basic;

import java.util.List;

public class MiddlePlayer extends Player {

	@Override
	Move nextMove(Position p, List<Move> moves) {
		return moves.get(moves.size()/2);
	}

}
