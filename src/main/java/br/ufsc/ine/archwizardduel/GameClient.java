package br.ufsc.ine.archwizardduel;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;

/**
 * Graphical user interface.
 */
public class GameClient extends JFrame implements Client { // @FIXME

	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;

	private JButton hostButton;
	private JButton connectButton;
	private JButton disconnectButton;
	private JButton startGame;
	private JButton exitGame;
	private JButton playButton;
	private JScrollPane textArea;
	private JTextArea typeHere;

	private final Player player;
	private final Server network;
	private Session connection;

	/*
		0 = Blank Tile
		1 = You
		2 = Second player
		3 = P1 throws fire
		4 = P2 throws fire
	*/
	private int LEVEL_WIDTH = 15;
	private int LEVEL_HEIGHT = 3;
	private int level[][] = new int[LEVEL_HEIGHT][LEVEL_WIDTH];

	/*
		TILES:

		wizard:
		[X] [] [] [] []
		[X] [] [] [] []
		[X] [] [] [] []
		ou
		[] [] [] [] [X]
		[] [] [] [] [X]
		[] [] [] [] [X]

		blank:
		[X] [X] [X] [X] [X]
		[X] [X] [X] [X] [X]
		[X] [X] [X] [X] [X]

		weapon:
		[] [X] [X] [X] []
		[] [X] [X] [X] []
		[] [X] [X] [X] []

	*/
	private JLabel wizardOne[] = new JLabel[LEVEL_HEIGHT];
	private JLabel wizardTwo[] = new JLabel[LEVEL_HEIGHT];
	private JLabel ground[][] = new JLabel[LEVEL_HEIGHT][LEVEL_WIDTH];
	private JLabel fireOne[][] = new JLabel[LEVEL_HEIGHT][LEVEL_WIDTH - 2];
	private JLabel fireTwo[][] = new JLabel[LEVEL_HEIGHT][LEVEL_WIDTH - 2];

	private JLabel status;


	/*************************** PUBLIC INTERFACE *****************************/

	/**
	 * Builds a Client and sets its reference to the game server. This
	 * constructor may create user interactions in order to configure itself.
	 *
	 * @param server injected server dependency
	 */
	public GameClient(Server server) {
		super("Archwizard Duel");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setSize(WIDTH, HEIGHT);
		setVisible(true);

		String username = null;
		do {
			username = JOptionPane.showInputDialog(
				this, "Please enter a valid username:"
			);
		} while (username == null || username.length() == 0);

		player = new Player(username);
		network = server;

		final int width = 200;

		final int height = 30;
		hostButton = new JButton("Create Session");
		hostButton.addActionListener(e -> makeSession());
		hostButton.setBounds(WIDTH - width, HEIGHT - 2*height, width, height);

		this.add(hostButton);
		connectButton = new JButton("Join Session");
		connectButton.addActionListener(e -> joinSession());
		connectButton.setBounds(WIDTH*0, HEIGHT - 2*height, width, height);

		this.add(connectButton);
		disconnectButton = new JButton("Quit Session");
		disconnectButton.addActionListener(e -> leaveSession());
		disconnectButton.setBounds(WIDTH*0, HEIGHT - 2*height, width, height);
		this.add(disconnectButton);

		startGame = new JButton("Start Match");
		startGame.addActionListener(e -> startMatch());
		startGame.setBounds(WIDTH/3, HEIGHT - 2*height, width, height);
		this.add(startGame);

		exitGame = new JButton("Quit Match");
		exitGame.addActionListener(e -> quitMatch());
		exitGame.setBounds(WIDTH/3, HEIGHT - 2*height, width, height);
		this.add(exitGame);

		playButton = new JButton("Send Play");
		playButton.addActionListener(e -> play());
		playButton.setBounds(WIDTH - width, HEIGHT - 2*height, width, height);
		this.add(playButton);

		typeHere = new JTextArea();
		textArea = new JScrollPane(typeHere);
		textArea.setBounds(0*WIDTH, HEIGHT/2, WIDTH, HEIGHT/3);
		this.add(textArea);

		level[1][0] = 1; // PLAYER 1 INITIAL POSITION
		level[1][LEVEL_WIDTH - 1] = 2; // PLAYER 2 INITIAL POSITION

		// TEST FIRES
		level[1][10] = 3;
		level[0][3] = 4;

		int space = 40;
		int th = 66;
		int tw = 48;

		// I NEED TO KNOW IF I'M P1 OR P2

		for (int i = 0; i < LEVEL_HEIGHT; ++i) {
			for (int j = 0; j < LEVEL_WIDTH; ++j) {
				ground[i][j] = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("ground.jpg")));
				ground[i][j].setBounds(space + j * tw, i * th, tw, th);
				this.add(ground[i][j]);
				ground[i][j].setVisible(false);
			}
		}

		for (int i = 0; i < LEVEL_HEIGHT; ++i) {
			wizardOne[i] = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("player1.jpg")));
			wizardTwo[i] = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("player2.jpg")));
			wizardOne[i].setBounds(space, i * th, tw, th);
			wizardTwo[i].setBounds(space + (LEVEL_WIDTH - 1) * tw, i * th, tw, th);
			this.add(wizardOne[i]);
			this.add(wizardTwo[i]);
			wizardOne[i].setVisible(false);
			wizardTwo[i].setVisible(false);
		}

		for (int i = 0; i < LEVEL_HEIGHT; ++i) {
			for (int j = 0; j < LEVEL_WIDTH - 2; ++j) {
				fireOne[i][j] = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("weapon1.jpg")));
				fireTwo[i][j] = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("weapon2.jpg")));
				fireOne[i][j].setBounds(space + tw * (1 + j), th * i, tw, th);
				fireTwo[i][j].setBounds(space + tw * (1 + j), th * i, tw, th);
				this.add(fireOne[i][j]);
				this.add(fireTwo[i][j]);
				fireOne[i][j].setVisible(false);
				fireTwo[i][j].setVisible(false);
			}
		}

		status = new JLabel("Mana = 10000");
		status.setBounds(300, 210, 100, 50);
		this.add(status);
		status.setVisible(false);
		showBegin();
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	@Override
	public void notify(String msg) {
		JOptionPane.showMessageDialog(
			this, msg, "Notification", JOptionPane.PLAIN_MESSAGE
		);
	}

	@Override
	public void showBegin() {
		connection = null;
		deselectScreen();
		hostButton.setVisible(true);
		connectButton.setVisible(true);
		disconnectButton.setVisible(false);
		startGame.setVisible(false);
		exitGame.setVisible(false);
		playButton.setVisible(false);
		textArea.setVisible(false);
		typeHere.setVisible(false);
		this.repaint();
	}

	@Override
	public void showSession() {
		deselectScreen();
		hostButton.setVisible(false);
		connectButton.setVisible(false);
		disconnectButton.setVisible(true);
		startGame.setVisible(true);
		exitGame.setVisible(false);
		playButton.setVisible(false);
		textArea.setVisible(false);
		typeHere.setVisible(false);
		this.repaint();
	}

	@Override
	public void showMatch() {
		hostButton.setVisible(false);
		connectButton.setVisible(false);
		disconnectButton.setVisible(false);
		startGame.setVisible(false);
		exitGame.setVisible(true);
		playButton.setVisible(true);
		textArea.setVisible(true);
		typeHere.setVisible(true);
		updateScreen();
		this.repaint();
	}


	/**************************** PRIVATE METHODS *****************************/

	private void deselectScreen() {
		status.setVisible(false);
		for (int i = 0; i < LEVEL_HEIGHT; ++i) {
			for (int j = 0; j < LEVEL_WIDTH; ++j) {
				ground[i][j].setVisible(false);
			}
		}

		for (int i = 0; i < LEVEL_HEIGHT; ++i) {
			for (int j = 0; j < LEVEL_WIDTH - 2; ++j) {
				fireOne[i][j].setVisible(false);
				fireTwo[i][j].setVisible(false);
			}
		}

		for (int i = 0; i < LEVEL_HEIGHT; ++i) {
			wizardOne[i].setVisible(false);
			wizardTwo[i].setVisible(false);
		}
	}

	private void updateScreen() {
		status.setVisible(true);
		for (int i = 0; i < LEVEL_HEIGHT; ++i) {
			for (int j = 0; j < LEVEL_WIDTH; ++j) {
				if (level[i][j] == 0) {
					ground[i][j].setVisible(true);
				}
			}
		}

		for (int i = 0; i < LEVEL_HEIGHT; ++i) {
			for (int j = 0; j < LEVEL_WIDTH - 2; ++j) {
				if (level[i][j + 1] == 3) {
					fireOne[i][j].setVisible(true);
				} if (level[i][j + 1] == 4) {
					fireTwo[i][j].setVisible(true);
				}
			}
		}

		for (int i = 0; i < LEVEL_HEIGHT; ++i) {
			if (level[i][0] == 1) {
				wizardOne[i].setVisible(true);
			} if (level[i][LEVEL_WIDTH - 1] == 2) {
				wizardTwo[i].setVisible(true);
			}
		}
	}

	private void makeSession() {
		final String ip = JOptionPane.showInputDialog(
			this,
			"Enter IP address where session will be hosted:",
			"localhost"
		);
		if ((connection = network.makeSession(this, ip)) != null)
			showSession();
	}

	private void joinSession() {
		final String ip = JOptionPane.showInputDialog(
			this,
			"Enter IP address of session to be joined:",
			"127.0.0.1"
		);
		if ((connection = network.joinSession(this, ip)) != null)
			showSession();
	}

	private void leaveSession() {
		network.leaveSession();
		showBegin();
	}

	private void startMatch() {
		if (connection.startMatch(this))
			showMatch();
		else
			notify(
				"Could not start match:\n" +
				"Not enough players or you are not hosting the session."
			);
	}

	private void quitMatch() {
		connection.quitMatch();
		showSession();
	}

	private void play() {
		connection.push(typeHere.getText());
	}

}
