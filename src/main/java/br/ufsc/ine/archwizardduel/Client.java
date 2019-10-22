package br.ufsc.ine.archwizardduel;

import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Client extends JFrame {

	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	
	public LocalPlayer localPlayer;
	public Server server;
	public Session connection;
	public Arena match; // later, synchronize.
	
	private JButton connectButton;
	private JButton disconnectButton;
	private JButton exitGame;
	private JButton startGame;
	private JButton playButton;
	private JButton hostButton;
	private JScrollPane textArea;
	private JLabel placeHolder;
	private JTextArea typeHere;

	public Client(Server server) {
		super("Archwizard Duel");

		this.server = server;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setSize(WIDTH, HEIGHT);
		int width = 200;
		int height = 30;

		localPlayer = new LocalPlayer(
				JOptionPane.showInputDialog(this,
					"Please enter your nickname for this match:"
				),
				this
			);

		connectButton = new JButton("Conectar a Sessao");
		connectButton.setBounds(WIDTH*0, HEIGHT - 2*height, width, height);

		disconnectButton = new JButton("Desconectar da Sessao");
		disconnectButton.setBounds(WIDTH*0, HEIGHT - 2*height, width, height);

		startGame = new JButton("Iniciar Partida");
		startGame.setBounds(WIDTH/3, HEIGHT - 2*height, width, height);

		exitGame = new JButton("Desistir da Partida");
		exitGame.setBounds(WIDTH/3, HEIGHT - 2*height, width, height);

		playButton = new JButton("Efetuar Jogada");
		playButton.setBounds(WIDTH - width, HEIGHT - 2*height, width, height);

		hostButton = new JButton("Criar Sessao");
		hostButton.setBounds(WIDTH - width, HEIGHT - 2*height, width, height);

		typeHere = new JTextArea();
		textArea = new JScrollPane(typeHere);
		textArea.setBounds(0*WIDTH, HEIGHT/2, WIDTH, HEIGHT/3);

		Icon img = new ImageIcon(getClass().getClassLoader().getResource("wol.jpeg"));
		placeHolder = new JLabel(img);
		placeHolder.setBounds(0*WIDTH, 0*HEIGHT + 30, WIDTH, HEIGHT/3 + height);

		connectButton.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(ActionEvent e){
				onConnectButton();
			}
		});

		disconnectButton.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(ActionEvent e){
				onDisconnectButton();
			}
		});

		startGame.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(ActionEvent e){
				onStartGame();
			}
		});

		exitGame.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(ActionEvent e){
				onExitGame();
			}
		});

		playButton.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(ActionEvent e){
				onPlayButton();
			}
		});

		hostButton.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(ActionEvent e){
				onHostButton();
			}
		});

		this.add(connectButton);
		this.add(disconnectButton);
		this.add(startGame);
		this.add(exitGame);
		this.add(playButton);
		this.add(textArea);
		this.add(hostButton);
		this.add(placeHolder);
		showBegin();
	}

	// PUBLIC INTERFACE

	public Player getPlayer() {
		return localPlayer;
	}

	public String getTextArea() {
		return typeHere.getText();
	}

	public void showMessage(String msg) {
		JOptionPane.showMessageDialog(this, msg, "Server update", JOptionPane.PLAIN_MESSAGE);
	}

	// PRIVATE INTERFACE

	private void onConnectButton() {
		if (joinSession()) // user managed to connect.
			showSession();
	}

	private void onDisconnectButton() {
		server.quitSession();
	}

	private void onExitGame() {
		connection.quitMatch();
		showSession();
	}

	private void onStartGame() { // Only host can start a match.
		if (!server.localHost) {
			showMessage("Only the host can start a match!");
			return;
		}

		if (server.connection.enoughParticipants()) {
			match = connection.makeMatch(localPlayer);
			showMatch();
		} else {
			showMessage("Not enough players!");
		}
	}

	private void onPlayButton() {
		if (match.myTurn()) {
			match.nextTurn();	// @TODO: this order won't work if next line get's fixed.
			connection.sendCode(null); // @TODO: add player expression & test for invalid code.
			showMessage("Jogada enviada!");
		}
	}

	private void onHostButton() {
		if ((connection = server.makeSession(this)) != null) // failed to host session.
			showSession();
	}
	
	// Connected?
	private boolean joinSession() {
		while (connection == null) {
			String ip = JOptionPane.showInputDialog(this, "Enter IP to join session", null);

			if ((connection = server.joinSession(this, ip)) != null)
				return true;

			int n = JOptionPane.showConfirmDialog(
				this,
				"Failed to connect using ip " + ip + ". Would you like to try again?",
				"Connection error",
				JOptionPane.YES_NO_OPTION
			);

			if (n == 0)
				continue;
			else
				return false;
		}
		return true;
	}

	/*
		Update screen when you go back to the initial screen
	*/
	public void showBegin() {
		connection = null;
		match = null;
		connectButton.setVisible(true);
		disconnectButton.setVisible(false);
		exitGame.setVisible(false);
		startGame.setVisible(false);
		playButton.setVisible(false);
		hostButton.setVisible(true);
		textArea.setVisible(false);
		placeHolder.setVisible(false);
		typeHere.setVisible(false);
		this.repaint();
	}

	/*
		Update screen when you join a session
	*/
	public void showSession() {
		match = null;
		connectButton.setVisible(false);
		disconnectButton.setVisible(true);
		exitGame.setVisible(false);
		startGame.setVisible(true);
		playButton.setVisible(false);
		hostButton.setVisible(false);
		textArea.setVisible(false);
		placeHolder.setVisible(false);
		typeHere.setVisible(false);
		this.repaint();
	}

	/*
		Update screen when you join a match
	*/
	public void showMatch() {
		connectButton.setVisible(false);
		disconnectButton.setVisible(false);
		exitGame.setVisible(true);
		startGame.setVisible(false);
		playButton.setVisible(true);
		hostButton.setVisible(false);
		textArea.setVisible(true);
		placeHolder.setVisible(true);
		typeHere.setVisible(true);
		this.repaint();
	}
}