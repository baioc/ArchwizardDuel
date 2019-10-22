package br.ufsc.ine.archwizardduel;

class Session {

	private final Server server;
	private RemotePlayer remotePlayer = null;

	Session(Server server) {
		this.server = server;
	}

	Arena makeMatch(LocalPlayer localPlayer) {
		server.makeMatch();
		System.out.println("MAKEMATCH\n");
		return new Arena(new Player[]{localPlayer, remotePlayer}, 0);
	}

	Arena joinMatch(LocalPlayer localPlayer) {
		System.out.println("JOINMATCH\n");
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
}