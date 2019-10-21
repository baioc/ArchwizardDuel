package br.ufsc.ine.archwizardduel;

public abstract class Player {

	final String userName;

	Player(String userName) {
		this.userName = userName;
	}

	String getName() {
		return userName;
	}

	abstract Expression getNextPlay();

}