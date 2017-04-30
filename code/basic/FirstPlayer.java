package basic;

import java.util.List;

public class FirstPlayer extends basic.Player {

	@Override
	Move nextMove(Position p, List<Move> moves) {
		return moves.get(0);
	}


}
