package br.ufsc.ine.archwizardduel;

import java.util.List;

/**
 * Represents the multiplayer sessions and both (@FIXME) its server-side and
 * client-side interfaces.
 */
class Session {

	private final Server server;
	private final boolean localHost;
	private Player localPlayer;
	private Player remotePlayer;
	private Arena match;


	/************************** SERVER INTERFACE ******************************/

	public Session(Server server, boolean localHost) {
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
		final List<Player> participants = server.getPlayers();
		if (participants != null) {
			localPlayer = participants.get(0);
			remotePlayer = participants.get(1);
			match = new Arena(participants, localHost ? 0 : 1, display);
			new Thread(match).start();
			return true;
		}
		return false;
	}

	public void pull(SerializedExpression code) {
		remotePlayer.setNextPlay(match.makePlay(code.toString()));
	}


	/*************************** CLIENT INTERFACE *****************************/

	public boolean startMatch(Client display) {
		if (localHost && makeMatch(display)) {
			server.beginMatch();
			return true;
		}
		return false;
	}

	public void quitMatch() {
		server.quitMatch();
		localPlayer = null;
		remotePlayer = null;
		match.stop();
		match = null;
	}

	public void push(String code) {
		if (!match.isLocalTurn())
			return;

		final Expression play = match.makePlay(code);
		if (play != null) {
			localPlayer.setNextPlay(play);
			server.send(new SerializedExpression(play));
		}
	}

}
