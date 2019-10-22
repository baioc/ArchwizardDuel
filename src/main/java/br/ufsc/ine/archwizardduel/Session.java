package br.ufsc.ine.archwizardduel;

class Session {

	private final Server server;
	private final boolean localHost;
	private RemotePlayer remotePlayer = null;
	private Arena match = null;

	Session(Server server, boolean localHost) {
		this.server = server;
		this.localHost = localHost;
	}

	void makeMatch(LocalPlayer localPlayer) {
		server.makeMatch();
		match = new Arena(new Player[]{localPlayer, remotePlayer}, 0);
	}

	void joinMatch(LocalPlayer localPlayer) {
		match = new Arena(new Player[]{localPlayer, remotePlayer}, 1);
	}

	// give-up match.
	void quitMatch() {
		server.quitMatch();
	}

	// send through proxy.
	void sendCode(Expression code) {
		match.nextTurn();
		server.sendCode(code);
	}

	// mailbox.
	void receiveCode(Expression code) {
		match.nextTurn();
		remotePlayer.receiveMail(code); // player not yet involved.
		remotePlayer.notifyAll();
	}
	
	boolean amIHost() {
		return localHost;
	}

	boolean myTurn() {
		return match.myTurn();
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
}