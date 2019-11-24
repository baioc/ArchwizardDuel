package br.ufsc.ine.archwizardduel;

import java.util.List;
import java.util.ArrayList;

class Arena implements Runnable {

	private final Interpreter evaluator;
	private List<Player> players;
	private List<Wizard> characters;
	private int current;
	private Client display;
	private volatile boolean gameOver;

	public Arena(List<Player> participants, int first, Client display) {
		evaluator = new Interpreter();
		players = participants;
		current = first;
		this.display = display;
		gameOver = false;
		characters = new ArrayList<>(participants.size());
		for (Player p : participants)
			characters.add(new Wizard(p.getName()));
	}

	public Expression makePlay(String code) {
		try {
			return evaluator.parse(code);
		} catch (Exception e) {
			display.notify(e.getMessage());
		}
		return null;
	}

	public boolean isLocalTurn() {
		return current == 0;
	}

	public void stop() {
		gameOver = true;
	}

	@Override
	public void run() {
		while (!gameOver) { // @TODO: game over condition
			int actual = current;
			current = (current + 1) % players.size();
			Expression play = players.get(current).getNextPlay();
			try {
				evaluator.interpret(play);
			} catch (Exception e) {
				display.notify(e.getMessage());
			}
		}
	}

}
