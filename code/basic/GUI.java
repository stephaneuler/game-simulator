package basic;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jserver.Board;
import jserver.InfoBox;
import jserver.Utils;
import jserver.XSendAdapter;
import jserver.XSendDE;
import plotter.Graphic;

//Version wer  wann     was
//.1      se   17-04    erste stabile Version
//.15     se   17-05-02 verbessertes GUI

public class GUI implements ActionListener, ChangeListener {
	static final int SLEEP_MIN = 30;
	static final int SLEEP_MAX = 1000;
	static final int SLEEP_INIT = SLEEP_MAX / 2;
	static final String version = "0.15 Mai 17";

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
	private Position position;

	public GUI(GameSimulator gameSimulator) {
		this.gameSimulator = gameSimulator;
		board = new Board();
		
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
		String[] players = gameSimulator.getPlayerNames();

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
		Utils.addMenuItem(this, menuOptions, "history");
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

		setStatusText( null );

	}

	public void show(Position p) {
		setStatusText( p );
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
			xsend.text2(x,0, "" + x);
		}

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
					
					if( position.getWinner() == 1 ) {
						setText("<html>Winner: <span style='color:red'>" + p1Name + "</span>");
					} else {
						setText("<html>Winner: <span style='color:blue'>" + p2Name + "</span>");
					}
				} 
			}.start();

		} else if (cmd.equals("Match")) {
			new Thread() {
				public void run() {
					String result = gameSimulator.competion();
					
					InfoBox info = new InfoBox(board.getGraphic(), "", 1200, 400);
					info.setTitle( "Ergebnis");
					info.getTextArea().setFont( new Font("Consolas", Font.PLAIN, 16) );
					info.getTextArea().setText( result );
					info.setVisible(true);
				}
			}.start();

		} else if (cmd.equals("animate")) {
			Position.toogleAnimateCheck();
			xsend.getBoard().receiveMessage("clearAllText");
			
		} else if (cmd.equals("Pause")) {
			gameSimulator.getGame().togglePause();
			
		} else if (cmd.equals("history")) {
			InfoBox info = new InfoBox(board.getGraphic(), "", 800, 400);
			info.setTitle( "Spielverlauf");
			info.getTextArea().setFont( new Font("Consolas", Font.PLAIN, 16) );
			info.getTextArea().setText(  "" + position.showHistory()   );
			info.setVisible(true);
			
		}
	}

	public static int lightColor(int nextPlayer) {
		if (nextPlayer == 1) {
			return XSendAdapter.LIGHTSALMON;
		} else if (nextPlayer == -1) {
			return XSendAdapter.LIGHTBLUE;
		} 
		return 0;
	}

	public void setText(String text) {
		xsend.statusText(text);	
	}

	private void setStatusText() {
		setStatusText( null );		
	}

	private void setStatusText(Position p) {
		String text = "";
		text += "<html><span style='color:red'>" + p1Name + "</span> - ";
		text += "<html><span style='color:blue'>" + p2Name + "</span>";
		if( p != null ) {
			text += "  Ply " + p.ply;
		}
		text += "</html>";
		xsend.statusText( text );	
	}


}
