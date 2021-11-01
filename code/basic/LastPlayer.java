package basic;

import java.util.List;

// take randomly one of the last 3 moves 
public class LastPlayer extends Player {

	@Override
	Move nextMove(Position  p, List<Move> moves) {
		int pos = moves.size() - 1 - random.nextInt(3);
		return moves.get(   Integer.max( 0, pos)   );
	}

}
