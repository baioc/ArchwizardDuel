package br.ufsc.ine.archwizardduel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Client extends JFrame {

	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private LocalPlayer player;
	private Interpreter parser;
	private Server server;
	private Session link;
	private Arena match;
	private boolean onGoingMatch;
	private JButton connectButton;
	private JButton disconnectButton;
	private JButton exitGame;
	private JButton startGame;
	private JButton playButton;
	private JButton hostButton;
	private JScrollPane textArea;
	private JLabel placeHolder;

	public Client(Server server) {
		super("Archwizard Duel");

		player = null;
		link = null;

		this.server = server;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setSize(WIDTH, HEIGHT);
		int width = 200;
		int height = 30;

		player = new LocalPlayer(
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

		JTextArea typeHere = new JTextArea();
		textArea = new JScrollPane(typeHere);
		textArea.setBounds(0*WIDTH, HEIGHT/2, WIDTH, HEIGHT/3);

		Icon img = new ImageIcon(getClass().getClassLoader().getResource("wol.jpeg"));
		placeHolder = new JLabel(img);
		placeHolder.setBounds(0*WIDTH, 0*HEIGHT + 30, WIDTH, HEIGHT/3 + height);

		connectButton.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(ActionEvent e){
				showSession(onJoinSession());
			}
		});

		disconnectButton.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(ActionEvent e){
				showSession(false);
				server.quitSession();
			}
		});

		startGame.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(ActionEvent e){
				showMatch(true, true);
			}
		});

		exitGame.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(ActionEvent e){
				showMatch(false, false);
			}
		});

		hostButton.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (onMakeSession()) {
					showSession(true);
				}
			}
		});

		showBegin();
		this.add(connectButton);
		this.add(disconnectButton);
		this.add(startGame);
		this.add(exitGame);
		this.add(playButton);
		this.add(textArea);
		this.add(hostButton);
		this.add(placeHolder);
	}

	public Player getPlayer() {
		return player;
	}

	public void showMessage(String msg) {
		JOptionPane.showMessageDialog(this, msg);
	}

	/*
	public boolean onPlayerJoin(Player remotePlayer) {
		// TODO
	}

	public void onPlayerQuit(Player remotePlayer) {
		// TODO
	}
	*/

	public void onMatchEnd(Player winner) {
		JOptionPane.showMessageDialog(this,
            "Congratulations "+player+"! You did it!",
            "End of match",
            JOptionPane.PLAIN_MESSAGE);
	}

	public boolean onJoinSession() {
		while (link == null) {
			String ip = JOptionPane.showInputDialog(this, "Enter IP to join session", null);

			if ((link = server.joinSession(this, ip)) != null)
				return true;

			int n = JOptionPane.showConfirmDialog(
				this,
				"Failed to connect using ip "+ip+". Would you like to try again?",
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

	public boolean onMakeSession() {
		if (server.makeSession(this) == null) {
			return false;
		} return true;
	}

	public void onQuitSession() {
		server.quitSession();
		showBegin();
	}

	/*
		Update screen when you go back to the initial screen
	*/
	public void showBegin() {
		disconnectButton.setVisible(false);
		startGame.setVisible(false);
		exitGame.setVisible(false);
		playButton.setVisible(false);
		hostButton.setVisible(true);
		textArea.setVisible(false);
		placeHolder.setVisible(false);
		this.repaint();
	}

	/*
		Update screen when you join a session
	*/
	public void showSession(boolean onSession) {
		disconnectButton.setVisible(onSession);
		connectButton.setVisible(!onSession);
		startGame.setVisible(onSession);
		exitGame.setVisible(false);
		playButton.setVisible(false);
		hostButton.setVisible(!onSession);
		this.repaint();
	}

	/*
		Update screen when you join a match
	*/
	public void showMatch(boolean onMatch, boolean yourTurn) {
		placeHolder.setVisible(onMatch);
		startGame.setVisible(!onMatch);
		exitGame.setVisible(onMatch);
		playButton.setVisible(yourTurn);
        textArea.setVisible(onMatch);
		placeHolder.setVisible(onMatch);
		this.repaint();
    }
}