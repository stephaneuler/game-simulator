package basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PositionVG extends PositionWithBoard {

	int winner = 0;

	@Override
	protected Position copy() {
		return new PositionVG(this);
	}

	public PositionVG(PositionVG position) {
		super(position);
	}

	public PositionVG() {
	}

	public List<Move> getMoves() {
		List<Move> moves = new ArrayList<>();

		if (!win) {
			for (int s = 1; s <= N; s++) {
				if (board[s][N] == 0) {
					moves.add(new MoveVG(s));
				}
			}
		}

		return moves;
	}

	public void move(Move moveA) {
		history.push(moveA);

		MoveVG move = (MoveVG) moveA;
		// find empty field in column
		for (int z = 1; z <= N; z++) {
			if (board[move.s][z] == 0) {
				// set piece
				board[move.s][z] = nextPlayer;
				checkWin(move.s, z);
				++ply;
				return;
			}
		}

	}

	/**
	 * undo the last move: remove the top piece in the column of the last move
	 */
	public void undo() {
		if (history.isEmpty()) {
			return;
		}
		MoveVG move = (MoveVG) history.pop();
		win = false;

		for (int y = N - 1; y >= 0; y--) {
			if (board[move.s][y] != 0) {
				board[move.s][y] = 0;
				return;
			}
		}
	}

	@Override
	protected String getName() {
		return "Vier Gewinnt";
	}

	@Override
	protected BoardTypes getBoardType() {
		return BoardTypes.VIERGEWINNT;
	}
	
	@Override
	protected void getMovesFromLines(List<String> lines) {
		for (String line : lines) {
			move(new MoveVG(line));
			nextPlayer();
		}
	}

}
