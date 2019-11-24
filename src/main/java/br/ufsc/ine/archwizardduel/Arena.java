package br.ufsc.ine.archwizardduel;

import java.util.List;
import java.util.ArrayList;

class Arena {

	private final Interpreter evaluator;
	private List<Player> players;
	private List<Wizard> characters;
	private int current;
	private Client user;
	private volatile boolean gameOver;

	public Arena(List<Player> participants, int first, Client user) {
		evaluator = new Interpreter();
		players = participants;
		current = first;
		this.user = user;
		gameOver = false;
		characters = new ArrayList<>(participants.size());
		for (Player p : participants)
			characters.add(new Wizard(p.getName(), 0, 0)); // @TODO: game world
	}

	public boolean isLocalTurn() {
		return current == 0;
	}

	public Expression makePlay(String code) {
		try {
			return evaluator.parse(code);
		} catch (Exception e) {
			user.notify(e.getMessage());
		}
		return null;
	}

	public void nextTurn() { // @TODO: game over condition
		Player currentPlayer = players.get(current);
		Expression play = currentPlayer.getNextPlay();
		current = (current + 1) % players.size();
		try {
			evaluator.interpret(play);
		} catch (Exception e) {
			user.notify(
				"Invalid play from " + currentPlayer.getName() +
				": " + e.getMessage()
			);
		}
	}

}
