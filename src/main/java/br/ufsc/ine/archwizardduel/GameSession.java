package br.ufsc.ine.archwizardduel;

import java.util.List;

/**
 * Multiplayer sessions with both their server-side and client-side interfaces.
 */
class GameSession implements Session {

	private final GameServer server;
	private final boolean localHost;
	private Player localPlayer;
	private Player remotePlayer;
	private Arena match;


	/************************** SERVER INTERFACE ******************************/

	public GameSession(GameServer server, boolean localHost) {
		this.server = server;
		this.localHost = localHost;
		localPlayer = null;
		remotePlayer = null;
		match = null;
	}

	/**
	 * Check whether or not the session host is the local user.
	 *
	 * @return true when it is, false otherwise
	 */
	public boolean isLocallyHosted() {
		return localHost;
	}

	/**
	 * Tries starting a match, which will display itself in the given interface.
	 *
	 * @param display client interface handle
	 * @return        true when the match was started, false otherwise
	 */
	public boolean makeMatch(Client display) {
		final List<Player> participants = server.players();
		if (participants != null) {
			// @XXX: fixed number of players
			localPlayer = participants.get(0);
			remotePlayer = participants.get(1);
			match = new Arena(participants, localHost ? 0 : 1, display);
			return true;
		}
		return false;
	}

	/**
	 * Receives a play from the server and moves to the next turn.
	 *
	 * @param code play from the remote player
	 */
	public void pull(SerializedExpression code) {
		final Expression play = match.makePlay(code.toString());
		remotePlayer.setNextPlay(play);
		match.nextTurn();
	}


	/*************************** CLIENT INTERFACE *****************************/

	@Override
	public boolean startMatch(Client display) {
		if (localHost && makeMatch(display)) {
			server.beginMatch();
			return true;
		}
		return false;
	}

	@Override
	public void quitMatch() {
		server.quitMatch();
		localPlayer = null;
		remotePlayer = null;
		match = null;
	}

	@Override
	public void push(String code) {
		if (match.isLocalTurn()) {
			final Expression play = match.makePlay(code);
			if (play != null) {
				localPlayer.setNextPlay(play);
				match.nextTurn();
				server.send(new SerializedExpression(play));
			}
		}
	}

}
