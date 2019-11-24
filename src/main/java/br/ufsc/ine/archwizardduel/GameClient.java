package br.ufsc.ine.archwizardduel;

import br.ufsc.ine.archwizardduel.GameObject;
import br.ufsc.ine.archwizardduel.Wizard;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import java.awt.Image;

/**
 * Graphical user interface.
 */
public class GameClient extends JFrame implements Client {

	private final Player player;
	private final Server network;
	private Session connection;

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
	private JLabel[][] map;
	private ImageIcon ground;
	private ImageIcon rock;
	private ImageIcon fireball;
	private ImageIcon wizardUp;
	private ImageIcon wizardLeft;
	private ImageIcon wizardDown;
	private ImageIcon wizardRight;


	/*************************** PUBLIC INTERFACE *****************************/

	/**
	 * Builds a Client and sets its reference to the game server. This
	 * constructor may make user interactions in order to configure itself.
	 *
	 * @param server injected server dependency
	 */
	public GameClient(Server server) {
		super("Archwizard Duel");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setResizable(false);
		setSize(WIDTH, HEIGHT);

		String username = null;
		do {
			username = JOptionPane.showInputDialog(
				this, "Please enter a valid username:"
			);
		} while (username == null || username.length() == 0);

		player = new Player(username);
		network = server;

		final int buttonWidth = 200;
		final int buttonHeight = 30;
		final int spacing = 40;
		final int rightAligned = WIDTH - buttonWidth - spacing;
		final int leftAligned = spacing;
		final int bottom = HEIGHT - 2*spacing;

		hostButton = new JButton("Create Session");
		hostButton.addActionListener(e -> makeSession());
		hostButton.setBounds(rightAligned, bottom, buttonWidth, buttonHeight);
		this.add(hostButton);

		connectButton = new JButton("Join Session");
		connectButton.addActionListener(e -> joinSession());
		connectButton.setBounds(leftAligned, bottom, buttonWidth, buttonHeight);
		this.add(connectButton);

		disconnectButton = new JButton("Quit Session");
		disconnectButton.addActionListener(e -> leaveSession());
		disconnectButton.setBounds(rightAligned, bottom, buttonWidth, buttonHeight);
		this.add(disconnectButton);

		startGame = new JButton("Start Match");
		startGame.addActionListener(e -> startMatch());
		startGame.setBounds(leftAligned, bottom, buttonWidth, buttonHeight);
		this.add(startGame);

		exitGame = new JButton("Quit Match");
		exitGame.addActionListener(e -> quitMatch());
		exitGame.setBounds(leftAligned, bottom, buttonWidth, buttonHeight);
		this.add(exitGame);

		playButton = new JButton("Send Play");
		playButton.addActionListener(e -> play());
		playButton.setBounds(rightAligned, bottom, buttonWidth, buttonHeight);
		this.add(playButton);

		typeHere = new JTextArea();
		textArea = new JScrollPane(typeHere);
		textArea.setBounds(leftAligned, HEIGHT/2, WIDTH - 2*spacing, HEIGHT/3);
		this.add(textArea);

		map = new JLabel[3][15];
		final int tileWidth = 48;
		final int tileHeight = 66;

		ground = new ImageIcon(
			getClass().getClassLoader().getResource("ground.png")
		);
		ground = new ImageIcon(ground.getImage().getScaledInstance(
			tileWidth, tileHeight, Image.SCALE_DEFAULT
		));

		rock = new ImageIcon(
			getClass().getClassLoader().getResource("rock.png")
		);
		rock = new ImageIcon(rock.getImage().getScaledInstance(
			tileWidth, tileHeight, Image.SCALE_DEFAULT
		));

		fireball = new ImageIcon(
			getClass().getClassLoader().getResource("fireball.png")
		);
		fireball = new ImageIcon(fireball.getImage().getScaledInstance(
			tileWidth, tileHeight, Image.SCALE_DEFAULT
		));

		wizardUp = new ImageIcon(
			getClass().getClassLoader().getResource("up.png")
		);
		wizardUp = new ImageIcon(wizardUp.getImage().getScaledInstance(
			tileWidth, tileHeight, Image.SCALE_DEFAULT
		));

		wizardLeft = new ImageIcon(
			getClass().getClassLoader().getResource("left.png")
		);
		wizardLeft = new ImageIcon(wizardLeft.getImage().getScaledInstance(
			tileWidth, tileHeight, Image.SCALE_DEFAULT
		));

		wizardDown = new ImageIcon(
			getClass().getClassLoader().getResource("down.png")
		);
		wizardDown = new ImageIcon(wizardDown.getImage().getScaledInstance(
			tileWidth, tileHeight, Image.SCALE_DEFAULT
		));

		wizardRight = new ImageIcon(
			getClass().getClassLoader().getResource("right.png")
		);
		wizardRight = new ImageIcon(wizardRight.getImage().getScaledInstance(
			tileWidth, tileHeight, Image.SCALE_DEFAULT
		));

		for (int i = 0; i < map.length; ++i) {
			for (int j = 0; j < map[i].length; ++j) {
				map[i][j] = new JLabel();
				map[i][j].setBounds(
					spacing + j*tileWidth,
					buttonHeight + i*tileHeight,
					tileWidth,
					tileHeight
				);
				map[i][j].setIcon(ground);
				this.add(map[i][j]);
			}
		}

		// @DEBUG: testing
		GameObject[][] test = new GameObject[map.length][map[0].length];
		for (int i = 0; i < test.length; ++i)
			for (int j = 0; j < test[i].length; ++j)
				test[i][j] = new GameObject(GameObject.Type.EMPTY, null);
		Wizard p1 = new Wizard("a", 0, 0);
		test[1][0] = new GameObject(GameObject.Type.WIZARD, p1);
		p1.rotate(Wizard.Direction.DOWN);
		Wizard p2 = new Wizard("a", 0, 0);
		test[1][test[1].length-1] = new GameObject(GameObject.Type.WIZARD, p2);
		p2.rotate(Wizard.Direction.DOWN);
		test[1][9] = new GameObject(GameObject.Type.ROCK, null);
		test[1][10] = new GameObject(GameObject.Type.FIREBALL, null);
		update(test);

		setVisible(true);
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
		hostButton.setVisible(true);
		connectButton.setVisible(true);
		disconnectButton.setVisible(false);
		startGame.setVisible(false);
		exitGame.setVisible(false);
		playButton.setVisible(false);
		textArea.setVisible(false);
		typeHere.setVisible(false);
		setMapVisible(false);
		this.repaint();
	}

	@Override
	public void showSession() {
		hostButton.setVisible(false);
		connectButton.setVisible(false);
		disconnectButton.setVisible(true);
		startGame.setVisible(true);
		exitGame.setVisible(false);
		playButton.setVisible(false);
		textArea.setVisible(false);
		typeHere.setVisible(false);
		setMapVisible(false);
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
		setMapVisible(true);
		this.repaint();
	}

	@Override
	public void update(GameObject[][] world) { // @TODO: test this
		for (int i = 0; i < map.length; ++i) {
			for (int j = 0; j < map[i].length; ++j) {
				ImageIcon updated = null;
				switch (world[i][j].type()) {
					case WIZARD:
						switch (((Wizard) world[i][j].object()).rotation()) {
							case UP:
								updated = wizardUp;
								break;
							case DOWN:
								updated = wizardDown;
								break;
							case LEFT:
								updated = wizardLeft;
								break;
							case RIGHT:
								updated = wizardRight;
								break;
						}
						break;
					case FIREBALL:
						updated = fireball;
						break;
					case ROCK:
						updated = rock;
						break;
					case EMPTY: default:
						updated = ground;
						break;
				}
				map[i][j].setIcon(updated);
			}
		}
		this.repaint();
	}


	/**************************** PRIVATE METHODS *****************************/

	private void setMapVisible(boolean visible) {
		for (int i = 0; i < map.length; ++i)
			for (int j = 0; j < map[i].length; ++j)
				map[i][j].setVisible(visible);
	}

	private void makeSession() {
		final String ip = JOptionPane.showInputDialog(
			this,
			"Enter IP address where session will be hosted:",
			"localhost"
		);
		if (ip != null && (connection = network.makeSession(this, ip)) != null)
			showSession();
	}

	private void joinSession() {
		final String ip = JOptionPane.showInputDialog(
			this,
			"Enter IP address of session to be joined:",
			"127.0.0.1"
		);
		if (ip != null && (connection = network.joinSession(this, ip)) != null)
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
