package basic;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Stack;

import jserver.ColorNames;

public abstract class Position {

	protected static boolean animateCheck = false;
	protected static int N = 8;

	protected GUI gui;
	protected boolean win = false;
	protected Stack<Move> history = new Stack<>();
	protected int ply = 0;

	public static int getN() {
		return N;
	}

	public static void setN(int n) {
		N = n;
	}

	public static void toogleAnimateCheck() {
		animateCheck = !animateCheck;
	}

	public void setGUI(GUI gui) {
		this.gui = gui;
	}

	public GUI getGUI() {
		return gui;
	}

	public int getPly() {
		return ply;
	}

	public String showHistory() {
		String h = "";
		for (int i = 0; i < history.size(); i++) {
			h += ColorNames.getName(GUI.color(i % 2 * -2 + 1)) + " " + history.get(i) + "; ";
		}
		return h;
	}

	public String showHistoryTable() {
		String h = getName() + System.lineSeparator();
		for (int i = 0; i < history.size(); i += 2) {
			h += (i / 2 + 1) + ".) ";
			h += history.get(i) + " - ";
			if (i + 1 < history.size()) {
				h += history.get(i + 1);
			}
			h += System.lineSeparator();
		}
		return h;
	}

	public boolean isWin() {
		return win;
	}

	public void save(String filename) throws IOException {
		String t = "";
		t += "#" + this.getClass().getName() + System.lineSeparator();
		for (Move m : history) {
			t += m + System.lineSeparator();
		}
		Files.write(Paths.get(filename), t.getBytes());

	}

	public static Position load(String filename) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		List<String> lines;
		lines = Files.readAllLines(Paths.get(filename));
		String head = lines.remove(0);
		String gameType = head.substring(1);

		File root = new File(".");
		URLClassLoader classLoader;
			classLoader = URLClassLoader.newInstance(new URL[] { root.toURI().toURL() });
			Class<?> c = Class.forName(gameType, true, classLoader);
			
		Position position = (Position) c.getDeclaredConstructor().newInstance();
		position.getMovesFromLines( lines );
		return position;
	}

	protected abstract void getMovesFromLines(List<String> lines);

	public Move getLastMove() {
		if (history.isEmpty()) {
			return null;
		}
		return history.peek();
	}

	protected abstract String getName();
	protected abstract BoardTypes getBoardType();

	protected abstract void nextPlayer();

	protected abstract void move(Move move);

	protected abstract int[][] getBoard();

	protected abstract int getWinner();

	protected abstract void undo();

	protected abstract List<Field> getWinFields();

	protected abstract List<Move> getMoves();

	protected abstract Position copy();

}
