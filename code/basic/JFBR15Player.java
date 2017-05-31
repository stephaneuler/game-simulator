package basic;


import java.util.ArrayList;
import java.util.List;

public class JFBR15Player extends Player {
	Move bestMove = null;
	int searchDepth = 1;
	@Override
	Move nextMove(Position p, List<Move> moves) {
		bestMove = moves.get(0);
		miniMax(p, searchDepth, true);
		return bestMove;
	}

	private int miniMax(Position node, int depth, boolean maximizingPlayer){
		//base case
		if(depth == 0 || node.isWin()) {
			return bewerten(node);
		}

		if(maximizingPlayer){
			//MAX
			List<Integer> rootMoves = new ArrayList<>();
			int bestValue = Integer.MIN_VALUE;
			for (Move child: node.getMoves()) {
				node.move(child);
				node.nextPlayer();
				int value = miniMax(node, depth - 1, false);
				node.undo();
				if(searchDepth == depth){
					rootMoves.add(value);
				}
				if(value != 0){
					if (value > bestValue) {
						bestValue = value;
						if(searchDepth == depth)
							bestMove = child;
					}
				}
			}
			if(searchDepth == depth){
				System.out.println(rootMoves);
			}
			return bestValue;
		} else {
			//MIN
			int bestValue = Integer.MAX_VALUE;
			for (Move child: node.getMoves()) {
				node.move(child);
				node.nextPlayer();
				int value = miniMax(node, depth - 1, true);
				node.undo();
				if(value != 0){
					bestValue = Math.min(bestValue, value);
				}
			}
			return bestValue;
		}
	}

	private int bewerten(Position node){
		if(node.isWin()){
			return 1;
		}else {
			return 0;
		}
		//Maybe refactor to return node.getWinner() * player
	}
}
 