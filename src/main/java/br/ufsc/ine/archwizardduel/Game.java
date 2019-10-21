package br.ufsc.ine.archwizardduel;

import javax.swing.JFrame;

public class Game {

	public static void main(String[] args) {
		JFrame gameWindow = new Client(new Server());
		gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameWindow.setLayout(null);
		gameWindow.setVisible(true);
	}

}
