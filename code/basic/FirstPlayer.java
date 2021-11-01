package basic;

import java.util.List;

// take randomly one of the first 3 moves 
public class FirstPlayer extends Player {

	@Override
	Move nextMove(Position p, List<Move> moves) {
		return moves.get( random.nextInt( Integer.min(3, moves.size())));
	}

}
