package basic;

import java.util.List;
import java.util.Random;

public class RandomPlayer extends Player {

	@Override
	Move nextMove(Position  p, List<Move> moves) {
		return moves.get(random.nextInt(moves.size()));
	}

}
