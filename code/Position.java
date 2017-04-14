package basic;

import java.util.ArrayList;
import java.util.List;

import jserver.XSendDE;

public class Position {
	int N = 7;
	int[][] board = new int[N + 2][N + 2];
	int nextPlayer = 1;
	int ply = 0;
	boolean win = false;
	private XSendDE xsend;
	private boolean animate = false;

	public void setXsend(XSendDE xsend) {
		this.xsend = xsend;
	}

	public boolean isWin() {
		return win;
	}

	public List<Move> getMoves() {
		List<Move> moves = new ArrayList<>();

		for (int s = 1; s <= N; s++) {
			if (board[s][N] == 0) {
				moves.add(new Move(s));
			}
		}
		return moves;
	}

	public void move(Move move) {
		for (int z = 1; z <= N; z++) {
			if (board[move.s][z] == 0) {
				board[move.s][z] = nextPlayer;
				checkWin(move.s, z);
				++ply;
				return;
			}
		}

	}

	private void checkWin(int s, int z) {
		if (animate) {
			xsend.text2(s, z, "" + nextPlayer);
			xsend.farbe2(s, z, 0xff00);
		}

		check(s, z, 1, 0);
		check(s, z, 0, 1);
		check(s, z, 1, 1);
		check(s, z, -1, 1);

	}

	private void check(int s, int z, int dx, int dy) {
		int q = 1;
		for (int i = 1; i < 4; i++) {
			if (animate) {
				xsend.text2(s + i * dx, z + i * dy, "" + q);
			}
			if (board[s + i * dx][z + i * dy] == nextPlayer) {
				++q;
			} else {
				break;
			}
		}
		for (int i = -1; i > -4; i--) {
			if (animate) {
				xsend.text2(s + i * dx, z + i * dy, "" + q);
			}
			if (board[s + i * dx][z + i * dy] == nextPlayer) {
				++q;
			} else {
				break;
			}
		}
		if (q >= 4) {
			win = true;
		}
		// System.out.println(q);

	}

	public void nextPlayer() {
		nextPlayer = -nextPlayer;
	}

}
