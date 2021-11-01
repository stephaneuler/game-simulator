package basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PositionGoB extends PositionWithBoard {

	public PositionGoB() {
	}

	public PositionGoB(PositionGoB position) {
		super(position);
	}

	@Override
	protected Position copy() {
		return new PositionGoB(this);
	}

	public List<Move> getMoves() {
		List<Move> moves = new ArrayList<>();

		if (!win) {
			for (int x = 1; x <= N; x++) {
				for (int y = 1; y <= N; y++) {
					if (board[x][y] == 0) {
						moves.add(new MoveGoB(x, y));
					}
				}
			}
		}

		return moves;
	}

	public void move(Move moveA) {
		history.push(moveA);

		MoveGoB move = (MoveGoB) moveA;
		// set piece
		int x = move.field.getX();
		int y = move.field.getY();

		board[x][y] = nextPlayer;
		checkWin(x, y);
		checkRemove(x, y, nextPlayer, move);
		++ply;
		return;

	}

	private void checkRemove(int x, int y, int nextPlayer, MoveGoB move) {
		int opp = -nextPlayer;
		checkPair(x, y, 1, 0, nextPlayer, opp, move);
		checkPair(x, y, -1, 0, nextPlayer, opp, move);
		checkPair(x, y, 0, 1, nextPlayer, opp, move);
		checkPair(x, y, 0, -1, nextPlayer, opp, move);

		checkPair(x, y, 1, 1, nextPlayer, opp, move);
		checkPair(x, y, 1, -1, nextPlayer, opp, move);
		checkPair(x, y, -1, 1, nextPlayer, opp, move);
		checkPair(x, y, -1, -1, nextPlayer, opp, move);

	}

	private void checkPair(int x, int y, int i, int j, int nextPlayer, int opp, MoveGoB move) {
		if (board[x + i][y + j] == opp && board[x + 2 * i][y + 2 * j] == opp
				&& board[x + 3 * i][y + 3 * j] == nextPlayer) {
			board[x + i][y + j] = 0;
			board[x + 2 * i][y + 2 * j] = 0;

			move.addTaken(new Field(x, y, opp));
			move.addTaken(new Field(x + 2 * i, y + 2 * j, opp));
		}

	}

	/**
	 * undo the last move: remove the top piece in the column of the last move
	 */
	public void undo() {
		if (history.isEmpty()) {
			return;
		}
		MoveGoB move = (MoveGoB) history.pop();
		win = false;
		int x = move.field.getX();
		int y = move.field.getY();
		board[x][y] = 0;
		
		for( Field taken : move.getTaken() ) {
			board[taken.getX()][taken.getY()] = taken.getVal();
		}
	}

	@Override
	protected String getName() {
		return "Go Bang";
	}

	@Override
	protected void getMovesFromLines(List<String> lines) {
		for (String line : lines) {
			System.out.println("line read: " + line);
			move(new MoveGoB(line));
			nextPlayer();
		}
		
	}

	@Override
	protected BoardTypes getBoardType() {
		return BoardTypes.VIERGEWINNT;
	}

}
