package basic;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.StyledDocument;

import jserver.Board;
import jserver.ColorNames;
import jserver.InfoBox;
import jserver.Utils;
import jserver.XSendAdapter;
import jserver.XSendDE;
import plotter.Graphic;

//Version wer  wann     was
//.1      se   17-04    erste stabile Version
//.15     se   17-05-02 verbessertes GUI
//.2      se   17-05-23 pause, save & load, ...

public class GUI implements ActionListener, ChangeListener {
	static final int SLEEP_MIN = 30;
	static final int SLEEP_MAX = 1000;
	static final int SLEEP_INIT = SLEEP_MAX / 2;
	static final String version = "0.2 Mai 17";

	private int N = 7;
	private XSendDE xsend;
	private GameSimulator gameSimulator;
	private Board board;
	private boolean verbose = true;
	private String p1Name;
	private String p2Name;
	private JButton startButton = new JButton("Spiel");
	private JButton pauseButton = new JButton("Pause");
	private JButton matchButton = new JButton("Match");
	private JTextPane  moveDisplay = new JTextPane ();
	StyledDocument moveDoc = moveDisplay.getStyledDocument();
	private Position position;
	private CompetionSetup competionSetup = new CompetionSetup();
	private String fileOpenDirectory;
	private Properties properties;;

	public GUI(GameSimulator gameSimulator) {
		this.gameSimulator = gameSimulator;
		board = new Board();
		properties = board.getProperties();
		fileOpenDirectory = properties.getProperty("saveDir");

		board.setSize(600, 500);
		board.receiveMessage("statusfontsize 16");
		xsend = new XSendAdapter(board);
		xsend.groesse(N + 2, N + 2);
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
		}
		buildComponents();
	}

	public XSendDE getXsend() {
		return xsend;
	}

	private void buildComponents() {
		Graphic graphic = board.getGraphic();
		String[] players = competionSetup.getAllPlayerNames();

		graphic.setTitle("Game Simulator V" + version);
		p1Name = players[0];
		p2Name = players[0];

		graphic.getJMenuBar().removeAll();
		JMenu menuS1 = new JMenu("Spieler 1");
		JMenu menuS2 = new JMenu("Spieler 2");
		JMenu menuOptions = new JMenu("Optionen");

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

		Utils.addMenuItem(this, menuOptions, "animate");
		Utils.addMenuItem(this, menuOptions, "showGame");
		Utils.addMenuItem(this, menuOptions, "saveGame");
		Utils.addMenuItem(this, menuOptions, "loadGame");
		graphic.addExternMenu(menuS1);
		graphic.addExternMenu(menuS2);
		graphic.addExternMenu(menuOptions);

		startButton.addActionListener(this);
		graphic.addSouthComponent(startButton);

		pauseButton.addActionListener(this);
		pauseButton.setEnabled(false);
		graphic.addSouthComponent(pauseButton);

		matchButton.addActionListener(this);
		graphic.addSouthComponent(matchButton);

		JSlider sleepTimeSlider = new JSlider(JSlider.HORIZONTAL, SLEEP_MIN, SLEEP_MAX, SLEEP_INIT);
		sleepTimeSlider.setMajorTickSpacing(250);
		sleepTimeSlider.setMinorTickSpacing(50);
		sleepTimeSlider.setPaintTicks(true);
		sleepTimeSlider.setPaintLabels(true);
		sleepTimeSlider.addChangeListener(this);

		graphic.addSouthComponent(sleepTimeSlider);
		
		moveDisplay.setEditable(false);
		moveDisplay.setText( moveDisplayHeader() );
		graphic.addEastComponent( moveDisplay );

		setStatusText(null);

	}

	private String moveDisplayHeader() {
		String text = "Züge" + System.lineSeparator() ;
		text += ColorNames.getName( color(1)) + " - " + ColorNames.getName( color(-1)) + System.lineSeparator();
		return text;
	}

	public void show(Position p) {
		position = p;
		setStatusText(p);
		for (int x = 1; x <= N; x++) {
			for (int y = 1; y <= N; y++) {
				int here = p.board[x][y];
				if (here == 1) {
					xsend.farbe2(x, y, XSendAdapter.RED);
				} else if (here == -1) {
					xsend.farbe2(x, y, XSendAdapter.BLUE);
				} else {
					xsend.farbe2(x, y, XSendAdapter.WHITE);
				}
			}
			xsend.text2(x, 0, "" + x);
		}
		moveDisplay.setText( moveDisplayHeader() + p.showHistoryTable() );

	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();
		if (!source.getValueIsAdjusting()) {
			int sleepTime = (int) source.getValue();
			System.out.println("SleepTime_: " + sleepTime);
			Game.setSleepTime(sleepTime);
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (verbose) {
			System.out.println("GUI cmd: " + cmd);
		}

		if (cmd.startsWith("s1:")) {
			p1Name = cmd.substring(3);
			setStatusText();
		} else if (cmd.startsWith("s2:")) {
			p2Name = cmd.substring(3);
			setStatusText();
		} else if (cmd.equals("Spiel")) {
			startButton.setEnabled(false);
			pauseButton.setEnabled(true);
			board.receiveMessage("clearCommands");
			new Thread() {
				public void run() {
					Player[] players = new Player[2];

					players[0] = gameSimulator.playerFromName(p1Name);
					players[1] = gameSimulator.playerFromName(p2Name);

					position = gameSimulator.singleGameGUI(players);
					startButton.setEnabled(true);
					pauseButton.setEnabled(false);

					if (position.getWinner() == 1) {
						setText("<html>Sieger: <span style='color:red'>" + p1Name + "</span>");
					} else if (position.getWinner() == -1) {
						setText("<html>Sieger: <span style='color:blue'>" + p2Name + "</span>");
					} else {
						setText("Remis");
					}
				}
			}.start();

		} else if (cmd.equals("Match")) {
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

		} else if (cmd.equals("animate")) {
			Position.toogleAnimateCheck();
			xsend.getBoard().receiveMessage("clearAllText");

		} else if (cmd.equals("Pause")) {
			gameSimulator.getGame().togglePause();

		} else if (cmd.equals("showGame")) {
			InfoBox info = new InfoBox(board.getGraphic(), "", 800, 400);
			info.setTitle("Spielverlauf");
			info.getTextArea().setFont(new Font("Consolas", Font.PLAIN, 16));
			String text;
			if (position == null) {
				text = "noch kein fertiges Spiel";
			} else {
				text = position.showHistory();
			}
			info.getTextArea().setText(text);
			info.setVisible(true);

		} else if (cmd.equals("saveGame")) {
			position.save("save.sav");

		} else if (cmd.equals("loadGame")) {
			String filename = askCodeFileName();
			if( filename != null ) {
				position = new Position();
				position.load( filename );
				show(position);
			}
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
			b.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						System.out.println("add " + p);
						competionSetup.add(p);
					} else {
						System.out.println("remove " + p);
						competionSetup.remove(p);
					}
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

	public static int lightColor(int nextPlayer) {
		if (nextPlayer == 1) {
			return XSendAdapter.LIGHTSALMON;
		} else if (nextPlayer == -1) {
			return XSendAdapter.LIGHTBLUE;
		}
		return 0;
	}

	public static int  color(int nextPlayer) {
		if (nextPlayer == 1) {
			return XSendAdapter.RED;
		} else if (nextPlayer == -1) {
			return XSendAdapter.BLUE;
		}
		return 0;
	}

	public void setText(String text) {
		xsend.statusText(text);
	}

	private void setStatusText() {
		setStatusText(null);
	}

	private void setStatusText(Position p) {
		String text = "";
		text += "<html><span style='color:red'>" + p1Name + "</span> - ";
		text += "<html><span style='color:blue'>" + p2Name + "</span>";
		if (p != null) {
			text += "  Ply " + p.ply;
		}
		text += "</html>";
		xsend.statusText(text);
	}

	private String askCodeFileName() {
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
			// System.out.println(filename);
			return filename;
		} else {
			return null;
		}

	}

}
