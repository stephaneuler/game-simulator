package basic;

public class Crash {

	public static void main(String[] args) {
		Position p1 = new Position();
		Position p2 = new Position(p1);

		int N = 8;
		for (int x = 0; x <= 7; x++) {
			for (int y = 7 - 1; y >= 0; y--) {
				if (p2.board[x][y] == 0) {
					System.out.println( "+");;
				}

			}
		}
		p1.print();
	}
}
