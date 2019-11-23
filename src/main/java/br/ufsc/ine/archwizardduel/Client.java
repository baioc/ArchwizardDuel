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
public class Client extends JFrame {

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
	private JLabel placeHolder;

	private final Server network;
	private Player player;
	private Session connection;


	/*************************** PUBLIC INTERFACE *****************************/

	/**
	 * Builds a Client and sets its reference to the game server. This
	 * constructor may create user interactions in order to configure itself.
	 *
	 * @param server injected server dependency
	 */
	public Client(Server server) {
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
		} while (username == null);

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

		placeHolder = new JLabel(
			new ImageIcon(getClass().getClassLoader().getResource("wol.jpeg"))
		);
		placeHolder.setBounds(0*WIDTH, 0*HEIGHT + 30, WIDTH, HEIGHT/3 + height);
		this.add(placeHolder);

		showBegin();
	}

	public Player getPlayer() {
		return player;
	}

	/**
	 * Notifies the user of some event.
	 */
	public void showMessage(String msg) {
		JOptionPane.showMessageDialog(
			this, msg, "Notification", JOptionPane.PLAIN_MESSAGE
		);
	}

	/**
	 * Shows the initial GUI configuration.
	 */
	public void showBegin() {
		connection = null;
		hostButton.setVisible(true);
		connectButton.setVisible(true);
		disconnectButton.setVisible(false);
		startGame.setVisible(false);
		exitGame.setVisible(false);
		playButton.setVisible(false);
		textArea.setVisible(false);
		typeHere.setVisible(false);
		placeHolder.setVisible(false);
		this.repaint();
	}

	/**
	 * Shows the currently active session.
	 */
	public void showSession() {
		hostButton.setVisible(false);
		connectButton.setVisible(false);
		disconnectButton.setVisible(true);
		startGame.setVisible(true);
		exitGame.setVisible(false);
		playButton.setVisible(false);
		textArea.setVisible(false);
		typeHere.setVisible(false);
		placeHolder.setVisible(false);
		this.repaint();
	}

	/**
	 * Shows the current running match.
	 */
	public void showMatch() {
		hostButton.setVisible(false);
		connectButton.setVisible(false);
		disconnectButton.setVisible(false);
		startGame.setVisible(false);
		exitGame.setVisible(true);
		playButton.setVisible(true);
		textArea.setVisible(true);
		typeHere.setVisible(true);
		placeHolder.setVisible(true);
		this.repaint();
	}


	/**************************** PRIVATE METHODS *****************************/

	private void makeSession() {
		if ((connection = network.makeSession(this)) != null)
			showSession();
	}

	private void joinSession() {
		final String ip = JOptionPane.showInputDialog(
			this,
			"Enter IP address of session to be joined:"
		);
		if (ip != null && (connection = network.joinSession(this, ip)) != null)
			showSession();
	}

	private void leaveSession() {
		network.leaveSession();
		showBegin();
	}

	private void startMatch() {
		if (connection.startMatch())
			showMatch();
	}

	private void quitMatch() {
		connection.quitMatch();
		showSession();
	}

	private void play() {
		connection.push(typeHere.getText());
	}

}
