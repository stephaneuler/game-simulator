package basic;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import jserver.Board;
import jserver.ColorNames;
import jserver.Utils;
import jserver.XSendAdapter;
import jserver.XSendDE;
import plotter.Graphic;
import plotter.InfoBox;

//Version wer  wann     was
//.1      se   17-04    erste stabile Version
//.15     se   17-05-02 verbessertes GUI
//.2      se   17-05-23 pause, save & load, ...

public class GUI implements ActionListener, ChangeListener {
	static final int SLEEP_MIN = 30;
	static final int SLEEP_MAX = 1000;
	static final int SLEEP_INIT = SLEEP_MAX / 2;
	static final int PLAYER1 = 1;
	static final int PLAYER2 = -1;
	static final String version = "0.3 August 21";
	private static final int BLACKSQUARE = 0xFFF7A4;
	private static final int WHITESQUARE = 0xafafaf;

	private XSendDE xsend;
	private GameSimulator gameSimulator;
	private Board board;
	private String p1Name;
	private String p2Name;
	private JButton startButton = new JButton("Spiel");
	private JButton zugButton = new JButton("Zug");
	private JButton pauseButton = new JButton("Pause");
	private JButton matchButton = new JButton("Match");
	private JTextPane moveDisplay = new JTextPane();
	private Position position;
	private CompetionSetup competionSetup = new CompetionSetup();
	private String fileOpenDirectory;
	private Properties properties;
	private String[] pieceNames = { "", "K", "T" };
	private boolean nextMove = true;

	public GUI(GameSimulator gameSimulator) {
		this.gameSimulator = gameSimulator;
		board = new Board();
		properties = board.getProperties();
		fileOpenDirectory = properties.getProperty("saveDir");

		setUpBoard();
		buildComponents();
	}

	public boolean isNextMove() {
		return nextMove;
	}

	public void setNextMove(boolean nextMove) {
		this.nextMove = nextMove;
	}

	public XSendDE getXsend() {
		return xsend;
	}

	private void setUpBoard() {
		board.setSize(600, 500);
		board.receiveMessage("statusfontsize 16");
		board.receiveMessage("borderWidth  3");

		xsend = new XSendAdapter(board);
		xsend.rahmen( XSendAdapter.BLACK );
		drawBoard();
	}

	private void drawBoard() {
		drawBoard(BoardTypes.VIERGEWINNT);
	}

	void drawBoard(BoardTypes boardType) {
		System.out.println( "drawBoard: " + boardType );
		int N = Position.getN();
		xsend.groesse(N + 2, N + 2);
		xsend.formen("c");
		board.receiveMessage("clearAllText");
		switch (boardType) {
		case VIERGEWINNT:
			for (int i = 0; i < N + 2; i++) {
				xsend.form2(i, 0, "s");
				xsend.form2(i, N + 1, "s");
				xsend.form2(0, i, "s");
				xsend.form2(N + 1, i, "s");
			}
			for (int x = 1; x <= N; x++) {
				for (int y = 1; y <= N; y++) {
					xsend.symbolGroesse2(x, y, 0.45);
				}
				xsend.text2(x, 0, "" + x);
			}
			break;
		case SCHACH:
			board.receiveMessage( ">>fontsize 24" );
			board.receiveMessage( ">>fonttype Dialog" );
			
			xsend.formen("s");
			xsend.farben(XSendAdapter.WHITE);
			for (int s = 1; s <= N; s++) {
				xsend.text2(s, 0, linienName(s));
				xsend.text2(s, N + 1, linienName(s));
				xsend.text2(0, s, "" + s);
				xsend.text2(N + 1, s, "" + s);
				for (int z = 1; z <= N; z++) {
					xsend.form2(s, z, "none");
					setBackground(s, z);
				}
			}
			break;
		}
	}

	private String linienName(int s) {
		return "" + " ABCDEFGH  ".charAt(s);
	}

	private void setBackground(int s, int z) {
		if ((s + z) % 2 == 0) {
			xsend.hintergrund2(s, z, WHITESQUARE);
		} else {
			xsend.hintergrund2(s, z, BLACKSQUARE);
		}

	}

	private void buildComponents() {
		Graphic graphic = board.getGraphic();
		String[] players = competionSetup.getAllPlayerNames();

		p1Name = players[0];
		p2Name = players[0];

		buildMenus(players);
		buildButtons();
		buildSlider();
		buildMoveDisplay();

		graphic.setTitle("Game Simulator V" + version);
		graphic.revalidate();
		setStatusText(null);

	}

	private void buildMoveDisplay() {
		moveDisplay.setEditable(false);
		moveDisplay.setText(moveDisplayHeader());
		JScrollPane scrollPane = new JScrollPane( moveDisplay );
		//scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		board.getGraphic().addEastComponent(scrollPane);
	}

	private void buildMenus(String[] players) {
		board.getGraphic().getJMenuBar().removeAll();
		JMenu menuS1 = new JMenu("Spieler 1");
		JMenu menuS2 = new JMenu("Spieler 2");
		JMenu menuOptions = new JMenu("Optionen");
		JMenu gameOptions = new JMenu("Spiele");

		for (String player : players) {
			JMenuItem mi = Utils.addMenuItem(this, menuS1, player);
			mi.setActionCommand("s1:" + player);
			mi = Utils.addMenuItem(this, menuS2, player);
			mi.setActionCommand("s2:" + player);
		}
		JMenuItem mi = Utils.addMenuItem(this, menuS1, "Human");
		mi.setActionCommand("s1:Human");
		mi = Utils.addMenuItem(this, menuS2, "Human");
		mi.setActionCommand("s2:Human");

		for (GameTypes gameType : GameTypes.values()) {
			JMenuItem jmi = new JMenuItem(gameType.name());
			jmi.addActionListener(e -> {
				Game.setGameType(gameType);
				setStatusText();
			});
			gameOptions.add(jmi);
		}

		Utils.addMenuItem((ActionListener) (e -> toggleAnimate()), menuOptions, "animate");
		Utils.addMenuItem((ActionListener) (e -> getSize()), menuOptions, "groesse");
		Utils.addMenuItem((ActionListener) (e -> Game.toogleMoveMode()), menuOptions, "Zug fuer Zug");
		Utils.addMenuItem((ActionListener) (e -> board.receiveMessage( ">>toggleInput" )), menuOptions, "Input-Feld");
		Utils.addMenuItem((ActionListener) (e -> saveGame()), menuOptions, "saveGame");
		Utils.addMenuItem((ActionListener) (e -> loadGame()), menuOptions, "loadGame");


		board.getGraphic().addExternMenu(menuS1);
		board.getGraphic().addExternMenu(menuS2);
		board.getGraphic().addExternMenu(gameOptions);
		board.getGraphic().addExternMenu(menuOptions);
	}


	private void buildButtons() {
		startButton.addActionListener(e -> runGame());
		zugButton.addActionListener(e -> nextMove());
		matchButton.addActionListener(e -> runMatch());
		pauseButton.addActionListener(e -> gameSimulator.getGame().togglePause());
		pauseButton.setEnabled(false);

		board.getGraphic().addSouthComponent(startButton);
		board.getGraphic().addSouthComponent(zugButton);
		board.getGraphic().addSouthComponent(pauseButton);
		board.getGraphic().addSouthComponent(matchButton);
	}

	private void nextMove() {
		nextMove  = true;
	}

	private void buildSlider() {
		JSlider sleepTimeSlider = new JSlider(JSlider.HORIZONTAL, SLEEP_MIN, SLEEP_MAX, SLEEP_INIT);
		sleepTimeSlider.setMajorTickSpacing(250);
		sleepTimeSlider.setMinorTickSpacing(50);
		sleepTimeSlider.setPaintTicks(true);
		sleepTimeSlider.setPaintLabels(true);
		sleepTimeSlider.addChangeListener(this);

		board.getGraphic().addSouthComponent(sleepTimeSlider);
	}

	private String moveDisplayHeader() {
		String text = "Züge" + System.lineSeparator();
		text += ColorNames.getName(color(PLAYER1)) + " - " + ColorNames.getName(color(PLAYER2))
				+ System.lineSeparator();
		return text;
	}

	public void show(Position p) {
		position = p;
		setStatusText(p);
		switch (p.getBoardType()) {
		case VIERGEWINNT:
			for (int x = 1; x <= Position.getN(); x++) {
				for (int y = 1; y <= Position.getN(); y++) {
					xsend.farbe2(x, y, color(p.getBoard()[x][y]));
				}
			}
			break;
		case SCHACH:
			for (int x = 1; x <= Position.getN(); x++) {
				for (int y = 1; y <= Position.getN(); y++) {
					showPiece(x, y, p.getBoard()[x][y]);
				}
			}

		}
		moveDisplay.setText(moveDisplayHeader() + p.showHistoryTable());

	}

	private void showPiece(int x, int y, int piece) {
		xsend.text2(x, y, GuiHelper.getChessUnicode( piece ));

	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();
		if (!source.getValueIsAdjusting()) {
			int sleepTime = (int) source.getValue();
			Game.setSleepTime(sleepTime);
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		if (cmd.startsWith("s1:")) {
			p1Name = cmd.substring(3);
			setStatusText();
		} else if (cmd.startsWith("s2:")) {
			p2Name = cmd.substring(3);
			setStatusText();
		}
	}

	private void toggleAnimate() {
		PositionVG.toogleAnimateCheck();
		xsend.getBoard().receiveMessage("clearAllText");
	}

	private void getSize() {
		String a = JOptionPane.showInputDialog(null, "Neuer Wert N", Position.getN());
		if (a != null) {
			Position.setN(Integer.parseInt(a));
			drawBoard();
		}

	}

	private void runGame() {
		startButton.setEnabled(false);
		pauseButton.setEnabled(true);
		board.receiveMessage("clearCommands");

		// All code inside SwingWorker runs on a seperate thread
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			public Void doInBackground() {
				Player[] players = new Player[2];

				players[0] = gameSimulator.playerFromName(p1Name);
				players[1] = gameSimulator.playerFromName(p2Name);

				position = gameSimulator.singleGameWithGUI(players);
				System.out.println("SwingWorker::Winner:" + position.getWinner());
				startButton.setEnabled(true);
				pauseButton.setEnabled(false);

				if (position.getWinner() == PLAYER1) {
					setText("<html>Sieger: <span style='color:red'>" + p1Name + "</span>");
				} else if (position.getWinner() == PLAYER2) {
					setText("<html>Sieger: <span style='color:blue'>" + p2Name + "</span>");
				} else {
					setText("Remis");
				}
				return null;
			}
			protected void done() {
		        try {
		            System.out.println("Done");
		            get();
		        } catch (ExecutionException e) {
		            e.getCause().printStackTrace();
		            String msg = String.format("Unexpected problem: %s", 
		                           e.getCause().toString());
		            JOptionPane.showMessageDialog(null,
		                msg, "Error", JOptionPane.ERROR_MESSAGE );
		        } catch (InterruptedException e) {
		            // Process e here
		        }
		    }
		};
		worker.execute();

	}

	private void runMatch() {
		int ret = setupCompetitionDialog();
		if (ret != JOptionPane.OK_OPTION) {
			return;
		}
		new Thread() {
			public void run() {
				String result = gameSimulator.competion(competionSetup);

				InfoBox info = new InfoBox(board.getGraphic(), "", 1200, 400);
				info.setTitle("Ergebnis");
				info.getTextArea().setFont(new Font("Consolas", Font.PLAIN, 16));
				info.getTextArea().setText(result);
				info.setVisible(true);
			}
		}.start();

	}

	private void loadGame() {
		String filename = askSaveFileName();
		if (filename != null) {
			try {
				position = Position.load(filename);
				resetWin();
				show(position);
			} catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				JOptionPane.showMessageDialog(null, e.toString(), "fileload", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void saveGame() {
		if (position == null) {
			JOptionPane.showMessageDialog(null, "noch nichts zu speichern", "filesave", JOptionPane.WARNING_MESSAGE);
			return;
		}
		try {
			String filename = "save.sav";
			position.save(filename);
			Path p = Paths.get(filename);
			JOptionPane.showMessageDialog(null, "saved to " + p.toAbsolutePath(), "filesave",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.toString(), "filesave", JOptionPane.ERROR_MESSAGE);
		}
	}

	public int setupCompetitionDialog() {
		JTextField numGamesField = new JTextField("" + competionSetup.getNumGames());
		JTextField numCopiesField = new JTextField("" + competionSetup.getNumCopies());
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JLabel("Spiele pro Match"));
		panel.add(numGamesField);
		panel.add(new JLabel("Spieler pro Typ"));
		panel.add(numCopiesField);
		List<String> selectedPlayers = new ArrayList<>();
		for (String p : competionSetup.getAllPlayerNames()) {
			JCheckBox b = new JCheckBox(p);
			if (competionSetup.hasPlayer(p)) {
				b.setSelected(true);
			}
			b.addItemListener(e -> {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					competionSetup.add(p);
				} else {
					competionSetup.remove(p);
				}
			});
			panel.add(b);
		}
		int ret = JOptionPane.showConfirmDialog(null, panel, "Replace", JOptionPane.OK_CANCEL_OPTION);
		competionSetup.setNumGames(Integer.parseInt(numGamesField.getText()));
		competionSetup.setNumCopies(Integer.parseInt(numCopiesField.getText()));
		System.out.println(competionSetup.getNumGames());
		return ret;
	}

	public static int lightColor(int field) {
		if (field == PLAYER1) {
			return XSendAdapter.LIGHTSALMON;
		} else if (field == PLAYER2) {
			return XSendAdapter.LIGHTBLUE;
		} else {
			return XSendAdapter.WHITE;
		}
	}

	public static int color(int field) {
		if (field == PLAYER1) {
			return XSendAdapter.RED;
		} else if (field == PLAYER2) {
			return XSendAdapter.BLUE;
		} else {
			return XSendAdapter.WHITE;
		}
	}

	public void setText(String text) {
		xsend.statusText(text);
	}

	private void setStatusText() {
		setStatusText(null);
	}

	private void setStatusText(Position p) {
		String text = "<html>";
		text += Game.getGameType() + ": ";
		text += "<span style='color:red'>" + p1Name + "</span> - ";
		text += "<span style='color:blue'>" + p2Name + "</span>";
		if (p != null) {
			text += "  Ply " + p.getPly();
		}
		text += "</html>";
		xsend.statusText(text);
	}

	private String askSaveFileName() {
		JFileChooser chooser = new JFileChooser();
		if (fileOpenDirectory != null) {
			chooser.setCurrentDirectory(new File(fileOpenDirectory));
		}
		chooser.setDialogTitle("saveFile");
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		String fileSuffixes = "sav";
		FileNameExtensionFilter filter = new FileNameExtensionFilter(" SAV", fileSuffixes);
		chooser.setFileFilter(filter);

		int retval = chooser.showDialog(null, null);
		if (retval == JFileChooser.APPROVE_OPTION) {
			fileOpenDirectory = chooser.getCurrentDirectory().getAbsolutePath();
			properties.setProperty("saveDir", fileOpenDirectory);
			board.saveProperties();
			String filename = chooser.getSelectedFile().getAbsolutePath();
			return filename;
		} else {
			return null;
		}

	}

	public void showWin() {
		for (Field field : position.getWinFields()) {
			xsend.hintergrund2(field.getX(), field.getY(), XSendAdapter.GOLD);
		}
	}

	public void resetWin() {
		board.receiveMessage("backgrounds 0xeeeeee \n");

	}

	public void markPlayer(int x, int y, int nextPlayer) {
		xsend.text2(x, y, "" + nextPlayer);
		xsend.farbe2(x, y, lightColor(nextPlayer));
	}

	public void markField(int x, int y, String text) {
		xsend.text2(x, y, text);
	}

	public void clearAllText() {
		xsend.getBoard().receiveMessage("clearAllText");
	}

	public String ask() {
		return xsend.abfragen();
	}

}
