package basic;

import java.util.List;

import plotter.Sleep;

public class Game {
	Position position = new Position();
	GUI gui;
	int sleepTime = 500;

	public void setGui(GUI gui) {
		this.gui = gui;
	}

	public Player play(Player[] players) {
		if (gui != null) {
			position.setXsend(gui.getXsend());
			gui.show(position);
		}

		for (;;) {
			for (Player player : players) {
				List<Move> moves = position.getMoves();
				// System.out.println(moves);
				if (moves.isEmpty()) {
					System.out.println("No more moves");
					return null;
				}
				Move move = player.nextMove(position, moves);
				position.move(move);
				if (gui != null) {
					Sleep.sleep(sleepTime);
					gui.show(position);
				}
				if (position.isWin()) {
					//System.out.println("Win: " + move);
					return player;
				}
				position.nextPlayer();
			}

		}

	}

}
