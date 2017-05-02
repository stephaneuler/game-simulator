package basic;

import java.util.List;
import java.util.Random;

public class SeqPlayer extends Player {
	int x = 0;

	@Override
	Move nextMove(Position p, List<Move> moves) {
		x = (x+1)% moves.size();
		return moves.get( x );
	}

	@Override
	public void reset() {
		x = 0;
		super.reset();
	}


}
