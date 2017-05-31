package basic;

import java.util.List;
import java.util.Random;


public class SucherSE extends Player {
	Random random = new Random();

	@Override
	Move nextMove(Position p, List<Move> moves) {
		// win with next move?
		for( Move m : moves ) {
			p.move( m );
			if( p.isWin() ) {
				return m;
			}
			p.undo();
		}
		
		// opponent would with next move?
		p.nextPlayer();
		for( Move m : moves ) {
			p.move( m );
			if( p.isWin() ) {
				return m;
			}
			p.undo();
		}
		
		return moves.get(moves.size()/2);
	}

}
