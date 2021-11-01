package basic;

import java.util.List;


// start from a random position
// move with random step size thru moves list
public class SeqPlayer extends Player {
	private int x = random.nextInt( 8 );
	private int step = 1 + random.nextInt(3);

	@Override
	Move nextMove(Position  p, List<Move> moves) {
		x = (x+step)% moves.size();
		return moves.get( x );
	}

	@Override
	public void reset() {
		x = 0;
		super.reset();
	}


}
