package basic;

import java.util.List;

public class MiddlePlayer extends Player {

	@Override
	Move nextMove(Position  p, List<Move> moves) {
		int pos = moves.size()/2;
		if(moves.size() > 3 ) {
			pos += -1 + random.nextInt(3);
		}
		return moves.get( pos );
	}

}
