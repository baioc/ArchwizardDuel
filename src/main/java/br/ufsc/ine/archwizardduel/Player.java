package br.ufsc.ine.archwizardduel;

/**
 * Class used to represent dueling players.
 */
public class Player {

	final String userName;
	private Expression play;

	public Player(String name) {
		userName = name;
		play = null;
	}

	public String getName() {
		return userName;
	}

	/**
	 * Sets this player's next play.
	 */
	public void setNextPlay(Expression code) {
		play = code;
	}

	/**
	 * Gets this player's next play.
	 */
	public Expression getNextPlay() {
		return play;
	}

}
