package br.ufsc.ine.archwizardduel;

import br.ufsc.ine.archwizardduel.GameObject;
import br.ufsc.ine.archwizardduel.Wizard;
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
		// @DEBUG: testing
		GameObject[][] test = new GameObject[3][15];
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
		user.update(test);

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
