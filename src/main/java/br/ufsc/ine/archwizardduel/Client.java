package br.ufsc.ine.archwizardduel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Client extends JFrame {

	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;

	public Client() {
		super("Archwizard Duel");

		this.setSize(WIDTH, HEIGHT);

		int width = 200;
		int height = 30;

		JButton connectButton = new JButton("Conectar a Sessao");
		connectButton.setBounds(WIDTH*0, HEIGHT - 2*height, width, height);

		JButton disconnectButton = new JButton("Desconectar da Sessao");
		disconnectButton.setBounds(WIDTH*0, HEIGHT - 2*height, width, height);
		disconnectButton.setVisible(false);

		JButton startGame = new JButton("Iniciar Partida");
		startGame.setBounds(WIDTH/3, HEIGHT - 2*height, width, height);
		startGame.setVisible(false);

		JButton exitGame = new JButton("Desistir da Partida");
		exitGame.setBounds(WIDTH/3, HEIGHT - 2*height, width, height);
		exitGame.setVisible(false);

		JButton playButton = new JButton("Efetuar Jogada");
		playButton.setBounds(WIDTH - width, HEIGHT - 2*height, width, height);
		playButton.setVisible(false);

		JButton hostButton = new JButton("Criar Sessao");
		hostButton.setBounds(WIDTH - width, HEIGHT - 2*height, width, height);
		hostButton.setVisible(true);

		JTextArea typeHere = new JTextArea();
		JScrollPane textArea = new JScrollPane(typeHere);
		textArea.setBounds(0*WIDTH, HEIGHT/2, WIDTH, HEIGHT/3);
		textArea.setVisible(false);

		Icon img = new ImageIcon(getClass().getClassLoader().getResource("wol.jpeg"));
		JLabel placeHolder = new JLabel(img);
		placeHolder.setVisible(false);
		placeHolder.setBounds(0*WIDTH, 0*HEIGHT + 30, WIDTH, HEIGHT/3 + height);

		connectButton.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(ActionEvent e){
				disconnectButton.setVisible(true);
				connectButton.setVisible(false);
				startGame.setVisible(true);
				hostButton.setVisible(false);
			}
		});

		disconnectButton.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(ActionEvent e){
				disconnectButton.setVisible(false);
				connectButton.setVisible(true);
				startGame.setVisible(false);
				exitGame.setVisible(false);
				playButton.setVisible(false);
				hostButton.setVisible(true);
			}
		});

		startGame.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(ActionEvent e){
				startGame.setVisible(false);
				exitGame.setVisible(true);
				playButton.setVisible(true);
				textArea.setVisible(true);
				placeHolder.setVisible(true);
			}
		});

		exitGame.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(ActionEvent e){
				startGame.setVisible(true);
				exitGame.setVisible(false);
				playButton.setVisible(false);
				textArea.setVisible(false);
				placeHolder.setVisible(false);
			}
		});

		hostButton.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(ActionEvent e){
				disconnectButton.setVisible(true);
				connectButton.setVisible(false);
				startGame.setVisible(true);
				hostButton.setVisible(false);
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
	}

}
