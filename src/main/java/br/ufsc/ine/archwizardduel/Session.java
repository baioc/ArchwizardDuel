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
	 * Sends a local play to the running match. This shall only be accepted if
	 * it is the local user's turn and it is a valid play.
	 *
	 * @param code unvalidated play from local player
	 */
	public void push(String code);

}
