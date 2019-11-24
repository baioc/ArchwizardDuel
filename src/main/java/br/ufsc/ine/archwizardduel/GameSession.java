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

	public boolean isLocallyHosted() {
		return localHost;
	}

	public boolean makeMatch(Client display) {
		final List<Player> participants = server.players();
		if (participants != null) {
			localPlayer = participants.get(0);
			remotePlayer = participants.get(1);
			match = new Arena(participants, localHost ? 0 : 1, display); // @FIXME
			return true;
		}
		return false;
	}

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
				server.send(new SerializedExpression(play));
				match.nextTurn();
			}
		}
	}

}
