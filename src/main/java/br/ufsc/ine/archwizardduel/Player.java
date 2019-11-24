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
	 * Produces this player's next play.
	 *
	 * @param code actual play, should never be null
	 */
	public synchronized void setNextPlay(Expression code) {
		play = code;
		notify();
	}

	/**
	 * Consumes this player's next play, waiting if there isn't one.
	 */
	public synchronized Expression getNextPlay() {
		try {
			while (play == null)
				wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		final Expression inbox = play;
		play = null;
		return inbox;
	}

}
