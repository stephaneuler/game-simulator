package basic;

import java.util.List;

public class SeqPlayer extends Player {
	int x = 0;

	@Override
	Move nextMove(Position p, List<Move> moves) {
		x = (x+1)% moves.size();
		return moves.get( x );
	}


}
