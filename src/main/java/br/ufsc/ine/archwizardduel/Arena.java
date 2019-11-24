package br.ufsc.ine.archwizardduel;

import br.ufsc.ine.archwizardduel.GameObject;
import br.ufsc.ine.archwizardduel.Wizard;
import java.util.List;
import java.util.ArrayList;

/**
 * Where wizard dueling takes place.
 */
class Arena {

	private final Interpreter evaluator;
	private GameObject[][] map;
	private List<Wizard> characters;
	private List<Player> players;
	private int current;
	private Client gui;

	/**
	 * Builds an arena, effectively starting a match with the given parameters.
	 *
	 * @param participants the list of players participating in the match
	 * @param first        the player with initial priority, should be a valid
	 *                     index of the participants list
	 * @param gui          a handle to the GUI where the match will be displayed
	 */
	public Arena(List<Player> participants, int first, Client gui) {
		this.gui = gui;
		current = first;
		players = participants;

		// @XXX: fixed number of players
		characters = new ArrayList<>(2);
		characters.add(new Wizard(players.get(0).getName(), 1, 0));
		characters.add(new Wizard(players.get(1).getName(), 13, 2));

		evaluator = new Interpreter(); // @TODO: extra primitives

		map = new GameObject[3][15];
		for (int i = 0; i < map.length; ++i) {
			for (int j = 0; j < map[i].length; ++j)
				map[i][j] = new GameObject(GameObject.Type.EMPTY, null);
		}
		/*
		-X--O------O--O
		-O---------O---
		------O------X-
		X: player, O:rock, -:empty
		*/
		map[0][1].set(GameObject.Type.WIZARD, characters.get(0));
		map[0][4].set(GameObject.Type.ROCK, null);
		map[0][11].set(GameObject.Type.ROCK, null);
		map[0][14].set(GameObject.Type.ROCK, null);
		map[1][1].set(GameObject.Type.ROCK, null);
		map[1][11].set(GameObject.Type.ROCK, null);
		map[2][6].set(GameObject.Type.ROCK, null);
		map[2][13].set(GameObject.Type.WIZARD, characters.get(1));

		gui.update(map);
	}

	/**
	 * Checks if it is the local user's turn.
	 *
	 * @return true when it is, false otherwise
	 */
	public boolean isLocalTurn() {
		return current == 0;
	}

	/**
	 * Tries to compile some code into a valid play.
	 * This method will notify the local user of any syntactic errors.
	 *
	 * @return null if the code was found invalid, or an Expression if it wasn't
	 */
	public Expression makePlay(String code) {
		try {
			return evaluator.parse(code);
		} catch (Exception e) {
			gui.notify(e.getMessage());
		}
		return null;
	}

	/**
	 * Moves on to the next player's turn. The player's next play is executed
	 * and the resulting game state is then graphically displayed. If the game
	 * ends, @TODO: then what?.
	 */
	public void nextTurn() {
		Player currentPlayer = players.get(current);
		Expression play = currentPlayer.getNextPlay();
		try {
			evaluator.interpret(play);
		} catch (Exception e) {
			if (isLocalTurn())
				gui.notify(e.getMessage());
		}
		gui.update(map);
		current = (current + 1) % players.size();
	}

}
