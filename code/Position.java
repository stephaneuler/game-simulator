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
	int ply = 0;
	boolean win = false;
	private XSendDE xsend;
	private Stack<Move> history = new Stack<>(); 

	public void setXsend(XSendDE xsend) {
		this.xsend = xsend;
	}

	public XSendDE getXsend() {
		return xsend;
	}

	public static void toogleAnimateCheck() {
		 animateCheck = ! animateCheck;
	}

	public Move getLastMove() {
		if( history.isEmpty() ) {
			return null;
		}
		return history.peek();
	}
	
	
	public String showHistory() {
		String h = "";
		for( int i=0; i<history.size(); i++ ) {
			h  += "P" + (i % 2+1) + " " + history.get(i).s + "; ";
		}
		return h;
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
		history.push( move );
		
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

	public void undo() {
		if( history.isEmpty() ) {
			return;
		}
		Move move = history.pop();
		for (int z = 1; z <= N; z++) {
			if (board[move.s][z] != 0) {
				board[move.s][z] = 0;
				return;
			}
		}
	}

	private void checkWin(int s, int z) {
		if (animateCheck & xsend != null ) {
			xsend.text2(s, z, "" + nextPlayer);
			xsend.farbe2(s, z, GUI.lightColor( nextPlayer));
		}

		check(s, z, 1, 0);
		check(s, z, 0, 1);
		check(s, z, 1, 1);
		check(s, z, -1, 1);

	}

	private void check(int s, int z, int dx, int dy) {
		int q = 1;
		for (int i = 1; i < 4; i++) {
			if (animateCheck & xsend != null) {
				xsend.text2(s + i * dx, z + i * dy, "" + q);
				Sleep.sleep(200);
			}
			if (board[s + i * dx][z + i * dy] == nextPlayer) {
				++q;
			} else {
				break;
			}
		}
		for (int i = -1; i > -4; i--) {
			if (animateCheck & xsend != null) {
				xsend.text2(s + i * dx, z + i * dy, "" + q);
				Sleep.sleep(200);
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
		if (animateCheck & xsend != null) {
			Sleep.sleep(400);
			xsend.getBoard().receiveMessage("clearAllText");
		}

	}

	public void nextPlayer() {
		nextPlayer = -nextPlayer;
	}
	

}
