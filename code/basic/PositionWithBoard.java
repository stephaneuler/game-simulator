package basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import plotter.Sleep;

public abstract class PositionWithBoard extends Position {

	// make the board larger, add borders
	protected int[][] board = new int[N + 2][N + 2];
	private List<Field> testedFields = new ArrayList<>();
	private List<Field> winFields = new ArrayList<>();
	protected int nextPlayer = 1;
	protected int winner = 0;

	public PositionWithBoard( ) {
	}

	public PositionWithBoard(PositionWithBoard position) {
		nextPlayer = position.nextPlayer;
		win = position.win;
		gui = position.gui;
		history = (Stack<Move>) position.history.clone();
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[x].length; y++) {
				board[x][y] = position.board[x][y];
			}
		}
	}

	public int[][] getBoard() {
		return board;
	}

	public List<Field> getWinFields() {
		return winFields;
	}

	protected void checkWin(int x, int y) {
		if (animateCheck ) {
			gui.markPlayer( x, y, nextPlayer );
		}
	
		check(x, y, 1, 0);
		check(x, y, 0, 1);
		check(x, y, 1, 1);
		check(x, y, -1, 1);
	
	}

	/**
	 * check for a group of 4 pieces around a given position.
	 * 
	 * @param x  the x-coordinate of the position
	 * @param y  the y-coordinate of the position
	 * @param dx
	 * @param dy
	 */
	private void check(int x, int y, int dx, int dy) {
		int q = 1;
		testedFields.clear();
		testedFields.add(new Field(x, y));
	
		for (int i = 1; i < 4; i++) {
			if (animateCheck  ) {
				gui.markField(x + i * dx, y + i * dy, "" + q);
				Sleep.sleep(200);
			}
			if (board[x + i * dx][y + i * dy] == nextPlayer) {
				++q;
				testedFields.add(new Field(x + i * dx, y + i * dy));
			} else {
				break;
			}
		}
		for (int i = -1; i > -4; i--) {
			if (animateCheck  ) {
				gui.markField(x + i * dx, y + i * dy, "" + q);
				Sleep.sleep(200);
			}
			if (board[x + i * dx][y + i * dy] == nextPlayer) {
				++q;
				testedFields.add(new Field(x + i * dx, y + i * dy));
			} else {
				break;
			}
		}
		if (q >= 4) {
			win = true;
			winner = nextPlayer;
			winFields = new ArrayList<>(testedFields);
		}
		if (animateCheck ) {
			Sleep.sleep(400);
			gui.clearAllText();
		}
	
	}

	public void nextPlayer() {
		nextPlayer = -nextPlayer;
	}

	public void print() {
		for (int y = N - 1; y > 0; y--) {
			for (int x = 1; x <= N; x++) {
				System.out.print(board[x][y] + " ");
			}
			System.out.println();
		}
	
	}

	public int getWinner() {
		return winner;
	}


}
