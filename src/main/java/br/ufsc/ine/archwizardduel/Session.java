package br.ufsc.ine.archwizardduel;

import java.util.List;
import java.util.ArrayList;

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

	public boolean makeMatch() {
		final List<Player> participants = server.getPlayers();
		if (participants != null) {
			localPlayer = participants.get(0);
			remotePlayer = participants.get(1);
			match = new Arena(participants, localHost ? 0 : 1);
			return true;
		}
		return false;
	}

	public void pull(Expression code) {
		remotePlayer.setNextPlay(code);
		match.nextTurn();
	}


	/*************************** CLIENT INTERFACE *****************************/

	public boolean startMatch() {
		if (localHost && makeMatch()) {
			server.beginMatch();
			return true;
		}
		return false;
	}

	public void quitMatch() {
		server.quitMatch();
		localPlayer = null;
		remotePlayer = null;
		match = null;
	}

	public void push(String code) {
		if (match.isLocalTurn()) {
			final Expression play = match.makePlay(code);
			server.send(play);
			localPlayer.setNextPlay(play);
			match.nextTurn();
		}
	}

}
