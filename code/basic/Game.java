package basic;

import java.util.List;

import plotter.Sleep;

public class Game {
	static int sleepTime = 500;
	static boolean moveMode = false;
	static GameTypes gameType = GameTypes.KuT;
	Position position;
	GUI gui;
	boolean pause = false;

	public static void toogleMoveMode() {
		moveMode = !moveMode;
	}

	public void togglePause() {
		pause = !pause;
	}

	public static GameTypes getGameType() {
		return gameType;
	}

	public static void setGameType(GameTypes gameType) {
		Game.gameType = gameType;
	}

	public Game() {
		switch (gameType) {
		case GOBANG:
			position = new PositionGoB();
			break;
		case VIERGEWINNT:
			position = new PositionVG();
			break;
		case KuT:
			position = new PositionKuT();
			break;
		}
	}

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
			position.setGUI(gui);
			gui.show(position);
		}

		for (;;) {
			for (Player player : players) {
				while (pause) {
					Sleep.sleep(100);
				}
				while (gui != null && !gui.isNextMove()) {
					Sleep.sleep(10);
				}
				List<Move> moves = position.getMoves();
				System.out.println(moves + " " + position.isWin());
				if (moves.isEmpty()) {
					// System.out.println("No more moves");
					if (position.isWin()) {
						return player;
					}
					// No winner
					return null;
				}
				Move move = player.nextMove(position.copy(), moves);
				if (move == null) {
					return null;
				}
				position.move(move);
				if (gui != null) {
					if (!(player instanceof Human)) {
						Sleep.sleep(sleepTime);
					}
					gui.show(position);
				}
				if (position.isWin()) {
					// System.out.println("Win: " + move);
					return player;
				}
				position.nextPlayer();
				if (gui != null) {
					gui.setNextMove(!moveMode);
				}
			}

		}

	}

}
