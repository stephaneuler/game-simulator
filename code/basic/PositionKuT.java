package basic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PositionKuT extends PositionWithBoard {
	private static final int BORDER = 1000;
	Field[] rookDirections = { new Field(0, 1), new Field(-1, 0), new Field(1, 0), new Field(0, -1) };
	Field[] bishopDirections = { new Field(1, 1), new Field(-1, 1), new Field(1, -1), new Field(-1, -1) };
	Field[] kingDirections = Stream.concat(Stream.of(rookDirections), Stream.of(bishopDirections))
			.toArray(Field[]::new);
	int player = 1;

	public PositionKuT() {
		setBorder();
		board[4][4] = 1;
		board[1][2] = 3;
		board[6][6] = -1;
//		board[1][3] = 3;
//		board[1][4] = 4;
//		board[1][5] = 5;
//		board[1][6] = 6;
//		board[4][1] = -1;
//		board[4][2] = -2;
//		board[4][3] = -3;
//		board[4][4] = -4;
//		board[4][5] = -5;
//		board[4][6] = -6;
	}

	private void setBorder() {
		for (int n = 0; n <= N + 1; n++) {
			board[0][n] = BORDER;
			board[N + 1][n] = BORDER;
			board[n][0] = BORDER;
			board[n][N + 1] = BORDER;
		}

	}

	public PositionKuT(PositionKuT position) {
		super(position);
	}

	@Override
	protected Position copy() {
		return new PositionKuT(this);
	}

	public List<Move> getMoves() {
		if (ply >= 100 | onlyKings()) {
			return new ArrayList<>();
		}
		List<Move> moves = getAllMoves();
		List<Move> legalMoves = moves.stream().filter(m -> isLegal((MoveChess) m)).collect(Collectors.toList());

		if (legalMoves.isEmpty() && isInCheckOtherPlayer()) {
			System.out.println("legalMoves.isEmpty() && isInCheck() " + player);
			win = true;
			if (player == 1) {
				winner = 0;
			} else {
				winner = 1;
			}
		}
		return legalMoves;
	}

	private boolean isInCheckOtherPlayer() {
		player = -player;
		boolean isInCheck = isInCheck();
		player = -player;
		return isInCheck;
	}

	private boolean onlyKings() {
		for (int x = 1; x <= N; x++) {
			for (int y = 1; y <= N; y++) {
				if (Math.abs(board[x][y]) > 1) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean isInCheck() {
		List<Move> moves = getAllMoves();
		for (Move m : moves) {
			if (((MoveChess) m).getTaken() == -player) {
				return true;
			}
		}
		return false;
	}

	public List<Move> getAllMoves() {
		List<Move> movesP1 = new ArrayList<>();
		List<Move> movesP2 = new ArrayList<>();

		for (int x = 1; x <= N; x++) {
			for (int y = 1; y <= N; y++) {
				List<Move> moves = movesP1;
				int piece = board[x][y];
				if( piece < 0 ) {
					moves = movesP2;
					piece = -piece;
				}
				if (piece == 1) {
					getMovesForPiece(moves, x, y, kingDirections, 1);
				} else if (piece == 2) {
					getMovesForPiece(moves, x, y, kingDirections, 10);
				} else if (piece == 3) {
					getMovesForPiece(moves, x, y, rookDirections, 10);
				} else if (piece == 4) {
					getMovesForPiece(moves, x, y, bishopDirections, 10);
				} 
			}
		}

		if (player == 1) {
			return movesP1;
		} else {
			return movesP2;
		}
	}

	private void getMovesForPiece(List<Move> moves, int x, int y, Field[] directions, int n) {
		for (Field direction : directions) {
			int x2 = x + direction.getX();
			int y2 = y + direction.getY();
			for (int w = 0; w < n; w++) {
				if (board[x2][y2] == 0) {
					MoveChess move = new MoveChess(x, y, x2, y2);
					moves.add(move);
				} else if (board[x2][y2] == BORDER) {
					break;
				} else {
					if (board[x2][y2] * board[x][y] < 0) {
						MoveChess move = new MoveChess(x, y, x2, y2);
						move.setTaken(board[x2][y2]);
						moves.add(move);
					}
					break;
				}
				x2 += direction.getX();
				y2 += direction.getY();
			}
		}
	}

	// check if opponent can take king
	private boolean isLegal(MoveChess move) {
		move(move);
		boolean leagal = !isInCheck();
		undo();
		return leagal;
	}

	public void move(Move moveA) {
		history.push(moveA);

		MoveChess move = (MoveChess) moveA;
		// set piece
		int x1 = move.from.getX();
		int y1 = move.from.getY();
		int x2 = move.to.getX();
		int y2 = move.to.getY();

		board[x2][y2] = board[x1][y1];
		board[x1][y1] = 0;
		++ply;
		player = -player;
		return;

	}

	/**
	 * undo the last move: remove the top piece in the column of the last move
	 */
	public void undo() {
		if (history.isEmpty()) {
			return;
		}
		MoveChess move = (MoveChess) history.pop();
		win = false;
		int x1 = move.from.getX();
		int y1 = move.from.getY();
		int x2 = move.to.getX();
		int y2 = move.to.getY();
		board[x1][y1] = board[x2][y2];
		board[x2][y2] = move.getTaken();
		--ply;
		player = -player;
	}

	@Override
	protected String getName() {
		return "Koenig und Turm";
	}

	@Override
	protected BoardTypes getBoardType() {
		return BoardTypes.SCHACH;
	}

	@Override
	protected void getMovesFromLines(List<String> lines) {
		for (String line : lines) {
			System.out.println("line read: " + line);
			move(new MoveGoB(line));
			nextPlayer();
		}

	}

	public boolean isPlayerOnField(int x, int y) {
		int piece = board[x][y];
		return piece * player > 0;
	}

}
