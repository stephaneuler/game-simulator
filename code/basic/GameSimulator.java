package basic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameSimulator {
	Random random = new Random();
	String[] playerNames = { "RandomPlayer", "FirstPlayer", "MiddlePlayer", "SeqPlayer", "SucherSE" };
	GUI gui;
	Game game;
	int numGames = 500;

	public GameSimulator() {
		super();
		gui = new GUI(this);
	}

	public Game getGame() {
		return game;
	}

	public String[] getPlayerNames() {
		return playerNames;
	}

	public void setPlayerNames(String[] playerNames) {
		this.playerNames = playerNames;
	}

	public static void main(String[] args) {
		GameSimulator simu = new GameSimulator();
		// simu.singleGame();
		// simu.competion();

	}

	public Position singleGameGUI( Player[] players ) {

		game = new Game();
		game.setGui(gui);
		Player winner = game.play(players);
		System.out.println("Winner:" + winner);
		//gui.setText("Winner:" + winner);
		
		return game.getPosition();
	}

	private void singleGame() {
		GUI gui = new GUI(this);
		Map<Player, Integer> scores = new HashMap<>();

		Player[] players = new Player[2];
		players[0] = new RandomPlayer();
		players[1] = new RandomPlayer();

		scores.put(players[0], 0);
		scores.put(players[1], 0);

		for (int n = 0; n < 1; n++) {
			Game game = new Game();
			game.setGui(gui);
			Player winner = game.play(players);
			System.out.println("Winner:" + winner);
			if (winner != null) {
				scores.put(winner, scores.get(winner) + 1);
			}
			System.out.println("Scores:" + scores);
		}

	}

	Player playerFromName(String name) {
		File root = new File(".");
		URLClassLoader classLoader;
		try {
			classLoader = URLClassLoader.newInstance(new URL[] { root.toURI().toURL() });
			Class<?> c = Class.forName("basic." + name, true, classLoader);
			return (Player) c.newInstance();
		} catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public String competion() {
		int NP = 2;
		Player[] players = new Player[playerNames.length * NP];

		File root = new File(".");
		URLClassLoader classLoader;
		try {
			classLoader = URLClassLoader.newInstance(new URL[] { root.toURI().toURL() });
			int j = 0;
			for (int i = 0; i < playerNames.length; i++) {
				Class<?> c = Class.forName("basic." + playerNames[i], true, classLoader);
				for (int n = 0; n < NP; n++) {
					players[j] = (Player) c.newInstance();
					players[j].setName( "P" +j);
					++j;
				}
			}
		} catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		int N = players.length;

		for (int i = 0; i < N - 1; i++) {
			for (int j = i + 1; j < N; j++) {
				Match match = new Match(players[i], players[j], numGames);
				Map<Player, Integer> scores = match.play();

				System.out.println(scores);
				players[i].addScore(players[j], scores.get(players[i]));
				players[j].addScore(players[i], scores.get(players[j]));
			}

		}

		Arrays.sort(players);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(os);
		showTable(players, ps);
		String output = os.toString();
		System.out.print( output );
		return output;
	}

	private void showTable(Player[] players, PrintStream out) {
		out.println("***********************************************************");
		for (Player p : players) {
			out.printf("%25s | ", p.toString());
			for (Player q : players) {
				if (p == q) {
					out.printf("   x  ");
				} else {
					out.printf("%5d ", p.getScore(q));
				}

			}
			out.printf(" | %5d ", p.getTotal());
			out.println();
		}

	}

}
