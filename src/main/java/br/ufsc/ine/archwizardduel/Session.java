package br.ufsc.ine.archwizardduel;

import java.util.List;

class Session {

	private final Server server;
	private List<Player> contestants = null;

	Session(Server server) {
		this.server = server;
	}

	Arena makeMatch() {
		// @TODO
		return null;
	}

	void leaveMatch() {
		// @TODO
	}

	// Expression receive();
	// boolean send(); // ack?

}