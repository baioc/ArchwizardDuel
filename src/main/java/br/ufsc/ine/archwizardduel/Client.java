package br.ufsc.ine.archwizardduel;

/**
 * Represents the game's Graphical User Interface.
 */
interface Client {

	/**
	 * Gets a handle to the local player.
	 */
	public Player getPlayer();

	/**
	 * Notifies the user of some event.
	 */
	public void notify(String msg);

	/**
	 * Shows the initial GUI configuration.
	 */
	public void showBegin();

	/**
	 * Shows the currently active session.
	 */
	public void showSession();

	/**
	 * Shows the current running match.
	 */
	public void showMatch();

	/**
	 * Updates the game view with the given world grid.
	 *
	 * @param world array containing object types to be drawn at that position
	 */
	public void update(GameObject[][] world);

}
