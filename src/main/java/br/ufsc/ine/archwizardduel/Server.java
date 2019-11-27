package br.ufsc.ine.archwizardduel;

/**
 * Represents the game server's client-side interface.
 */
interface Server {

	/**
	 * Starts a session in the server.
	 *
	 * @param host client handle
	 * @param ip   IP address where the session is to be hosted
	 * @return     session handle
	 */
	public Session makeSession(Client host, String ip);

	/**
	 * Joins an existing session in the server.
	 *
	 * @param client client handle
	 * @param ip     IP address where the session is
	 * @return       session handle
	 */
	public Session joinSession(Client client, String ip);

	/**
	 * Leaves the currently active session (if any).
	 */
	public void leaveSession();

}
