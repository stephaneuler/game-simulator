package basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import jserver.XSendDE;
import plotter.Sleep;

public class Position {
	private static boolean animateCheck = false;

	int N = 7;
	int[][] board = new int[N + 2][N + 2];
	int nextPlayer = 1;
	int winner = 0;
	int ply = 0;
	boolean win = false;
	private XSendDE xsend;
	private Stack<Move> history = new Stack<>();

	public Position(Position position) {
		nextPlayer = position.nextPlayer;
		win = position.win;
		xsend = position.xsend;
		history = (Stack<Move>) position.history.clone();
		for (int x = 1; x <= N; x++) {
			for (int y = 1; y <= N; y++) {
				board[x][y] = position.board[x][y];
			}
		}
	}

	public Position() {
	}

	public int getWinner() {
		return winner;
	}

	public void setXsend(XSendDE xsend) {
		this.xsend = xsend;
	}

	public XSendDE getXsend() {
		return xsend;
	}

	public static void toogleAnimateCheck() {
		animateCheck = !animateCheck;
	}

	public Move getLastMove() {
		if (history.isEmpty()) {
			return null;
		}
		return history.peek();
	}

	public String showHistory() {
		String h = "";
		for (int i = 0; i < history.size(); i++) {
			h += "P" + (i % 2 + 1) + " " + history.get(i).s + "; ";
		}
		return h;
	}

	public boolean isWin() {
		return win;
	}

	public List<Move> getMoves() {
		List<Move> moves = new ArrayList<>();

		if (!win) {
			for (int s = 1; s <= N; s++) {
				if (board[s][N] == 0) {
					moves.add(new Move(s));
				}
			}
		}

		return moves;
	}

	public void move(Move move) {
		history.push(move);

		// find empty field
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
		Move move = history.pop();
		win = false;

		for (int y = N-1; y >=0; y--) {
			if (board[move.s][y] != 0) {
				board[move.s][y] = 0;
				return;
			}
		}
	}

	private void checkWin(int x, int y) {
		if (animateCheck & xsend != null) {
			xsend.text2(x, y, "" + nextPlayer);
			xsend.farbe2(x, y, GUI.lightColor(nextPlayer));
		}

		check(x, y, 1, 0);
		check(x, y, 0, 1);
		check(x, y, 1, 1);
		check(x, y, -1, 1);

	}

	/**
	 * check for a group of 4 pieces around a given position.
	 * 
	 * @param x the x-coordinate of the position
	 * @param y the y-coordinate of the position
	 * @param dx
	 * @param dy
	 */
	private void check(int x, int y, int dx, int dy) {
		int q = 1;
		for (int i = 1; i < 4; i++) {
			if (animateCheck & xsend != null) {
				xsend.text2(x + i * dx, y + i * dy, "" + q);
				Sleep.sleep(200);
			}
			if (board[x + i * dx][y + i * dy] == nextPlayer) {
				++q;
			} else {
				break;
			}
		}
		for (int i = -1; i > -4; i--) {
			if (animateCheck & xsend != null) {
				xsend.text2(x + i * dx, y + i * dy, "" + q);
				Sleep.sleep(200);
			}
			if (board[x + i * dx][y + i * dy] == nextPlayer) {
				++q;
			} else {
				break;
			}
		}
		if (q >= 4) {
			win = true;
			winner  = nextPlayer;
		}
		if (animateCheck & xsend != null) {
			Sleep.sleep(400);
			xsend.getBoard().receiveMessage("clearAllText");
		}

	}

	public void nextPlayer() {
		nextPlayer = -nextPlayer;
	}

}
