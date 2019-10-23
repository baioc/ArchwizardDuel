package br.ufsc.ine.archwizardduel;

import java.util.List;
import java.util.ArrayList;

class Arena {

	private final Interpreter executor = new Interpreter();
	private List<Player> players;
	private List<Wizard> characters;
	private int currentTurn;

	public Arena(List<Player> participants, int first) {
		players = participants;
		currentTurn = first;
		characters = new ArrayList<>(participants.size());
		for (Player p : participants)
			characters.add(new Wizard(p.getName()));
	}

	public Expression makePlay(String code) {
		return executor.parse(code);
	}

	public boolean isLocalTurn() {
		return currentTurn == 0;
	}
	// @FIXME: arena should be able to change turns by itself
	public void nextTurn() {
		currentTurn = (currentTurn + 1) % characters.size();
	}

}
