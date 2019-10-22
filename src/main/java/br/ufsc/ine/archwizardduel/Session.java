package br.ufsc.ine.archwizardduel;

class Session {

	private final Server server;
	private final boolean localHost;
	private RemotePlayer remotePlayer = null;

	Session(Server server, boolean localHost) {
		this.server = server;
		this.localHost = localHost;
	}

	Arena makeMatch(LocalPlayer localPlayer) {
		server.makeMatch();
		return new Arena(new Player[]{localPlayer, remotePlayer}, 0);
	}

	Arena joinMatch(LocalPlayer localPlayer) {
		return new Arena(new Player[]{localPlayer, remotePlayer}, 1);
	}

	// give-up match.
	void quitMatch() {
		server.quitMatch();
	}

	// send through proxy.
	void sendCode(Expression code) {
		server.sendCode(code);
	}

	// mailbox.
	void receiveCode(Expression code) {
		remotePlayer.receiveMail(code); // player not yet involved.
		remotePlayer.notifyAll();
	}

	// REMOTE PLAYER INTERFACE.

	boolean enoughParticipants() {
		return !(remotePlayer == null);
	}

	String getRemotePlayerName() {
		return remotePlayer.getName();
	}

	void setRemotePlayer(RemotePlayer remotePlayer) {
		this.remotePlayer = remotePlayer;
	}

	boolean amIHost() {
		return localHost;
	}
}