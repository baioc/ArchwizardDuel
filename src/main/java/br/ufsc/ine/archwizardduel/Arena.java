package br.ufsc.ine.archwizardduel;

import java.util.List;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Where wizard dueling takes place.
 */
class Arena {

	private Interpreter evaluator;
	private GameObject[][] map;
	private List<Wizard> characters;
	private List<Player> players;
	private int current;
	private Client gui;
	private static final Value nil = new Value(Value.Type.VOID, null);

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
		characters.add(new Wizard(players.get(0).getName()));
		characters.add(new Wizard(players.get(1).getName()));

		Frame primitives = new Frame();
		primitives.define("UP",
			new Value(Value.Type.NUMBER, Wizard.Direction.UP)
		);
		primitives.define("LEFT",
			new Value(Value.Type.NUMBER, Wizard.Direction.LEFT)
		);
		primitives.define("DOWN",
			new Value(Value.Type.NUMBER, Wizard.Direction.DOWN)
		);
		primitives.define("RIGHT",
			new Value(Value.Type.NUMBER, Wizard.Direction.RIGHT)
		);
		primitives.define("turn",
			new Value(args -> {
				Wizard.Direction dir = (Wizard.Direction) args.get(0).value();
				characters.get(current).rotate(dir);
				update();
				return nil;
			}
		));
		primitives.define("move",
			new Value(args -> {
				Wizard mage = characters.get(current);
				final int oldX = mage.position()[0];
				final int oldY = mage.position()[1];
				int x = oldX;
				int y = oldY;
				switch (mage.rotation()) {
					case UP:
						y = Math.max(0, oldY - 1);
						break;
					case LEFT:
						x = Math.max(0, oldX - 1);
						break;
					case DOWN:
						y = Math.min(map.length - 1, oldY + 1);
						break;
					case RIGHT:
						x = Math.min(map[oldY].length - 1, oldX + 1);
						break;
				}
				GameObject.Type type = map[y][x].type();
				Object object = map[y][x].object();
				if (type == GameObject.Type.EMPTY) {
					mage.move(x, y);
					map[oldY][oldX].set(type, object);
					map[y][x].set(GameObject.Type.WIZARD, mage);
				}
				update();
				return nil;
			}
		));
		evaluator = new Interpreter(primitives); // @TODO: extra primitives

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
		map[0][1].set(GameObject.Type.WIZARD, characters.get(first));
		characters.get(first).move(1, 0);
		map[0][4].set(GameObject.Type.ROCK, null);
		map[0][11].set(GameObject.Type.ROCK, null);
		map[0][14].set(GameObject.Type.ROCK, null);
		map[1][1].set(GameObject.Type.ROCK, null);
		map[1][11].set(GameObject.Type.ROCK, null);
		map[2][6].set(GameObject.Type.ROCK, null);
		map[2][13].set(GameObject.Type.WIZARD, characters.get((first + 1) % 2));
		characters.get((first + 1) % 2).move(13, 2);

		gui.redraw(map);
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
		current = (current + 1) % players.size();
	}

	private void update() {
		gui.redraw(map);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

}
