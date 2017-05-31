package basic;

import java.util.ArrayList;
import java.util.List;

public class CompetionSetup {
	String[] allPlayerNames = { "RandomPlayer", "FirstPlayer", "MiddlePlayer", "SeqPlayer", "SucherSE",
			"KaylaWhitePlayer", "JFBR15Player", "BrainlessPlayer", "BlockerPlayer", "TobiasKohlPlayer" };
	List<String> playerNames = new ArrayList<>();
	int numGames = 5;
	int numCopies = 2;

	public int getNumCopies() {
		return numCopies;
	}

	public void setNumCopies(int numCopies) {
		this.numCopies = numCopies;
	}

	public int getNumGames() {
		return numGames;
	}

	public void setNumGames(int numGames) {
		this.numGames = numGames;
	}

	public List<String> getPlayerNames() {
		return playerNames;
	}

	public int getNumberPlayers() {
		return playerNames.size();
	}

	public String[] getAllPlayerNames() {
		return allPlayerNames;
	}

	public boolean hasPlayer(String p) {
		return playerNames.contains(p);
	}

	public void setPlayerNames(List<String> selectedPlayers) {
		playerNames = selectedPlayers;

	}

	public void add(String p) {
		if (!playerNames.contains(p)) {
			playerNames.add(p);
		}

	}

	public void remove(String p) {
		playerNames.remove(p);

	}

}
