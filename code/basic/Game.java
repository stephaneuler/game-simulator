package basic;

import java.util.List;

import plotter.Sleep;

public class Game {
	static int sleepTime = 500;
	boolean pause = false;

	Position position = new Position();
	GUI gui;

	public void setGui(GUI gui) {
		this.gui = gui;
	}

	public Position getPosition() {
		return position;
	}

	public static int getSleepTime() {
		return sleepTime;
	}

	public static void setSleepTime(int sleepTime) {
		Game.sleepTime = sleepTime;
	}

	public Player play(Player[] players) {
		if (gui != null) {
			position.setXsend(gui.getXsend());
			gui.show(position);
		}

		for (;;) {
			for (Player player : players) {
				while( pause ) {
					Sleep.sleep(100);
				}
				List<Move> moves = position.getMoves();
				// System.out.println(moves);
				if (moves.isEmpty()) {
					//System.out.println("No more moves");
					// No winner
					return null;
				}
				Move move = player.nextMove(new Position( position ), moves);
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

	public void togglePause() {
		pause  = ! pause;
	}

}
