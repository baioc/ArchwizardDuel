package br.ufsc.ine.archwizardduel;

/**
 * Represents the client-side interface of multiplayer sessions in the game.
 */
interface Session {

	/**
	 * Tried to start the match.
	 *
	 * @return true if the match really started, false otherwise
	 */
	public boolean startMatch(Client display);

	/**
	 * Quits the currently running match (if any).
	 */
	public void quitMatch();

	/**
	 * Sends a play which will only be accepted if it is the local user's turn.
	 */
	public void push(String code);

}
