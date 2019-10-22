package br.ufsc.ine.archwizardduel;

class Session {

	private final Server server;
	private RemotePlayer remotePlayer = null;

	Session(Server server) {
		this.server = server;
	}

	Arena makeMatch(LocalPlayer localPlayer) {
		return new Arena(new Player[]{localPlayer, remotePlayer}, 0);
	}

	// give-up match.
	void quitMatch() {
		server.quitMatch();
	}

	// tried to connect with not enough players.
	void dropMatch() {
		server.tratarPartidaNaoIniciada("Not enought players!");
	}

	// send through proxy.
	void sendCode(Expression code) {
		server.sendCode(code);
	}

	// mailbox.
	void receiveCode(Expression code) {
		remotePlayer.receiveMail(code);
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
}