package br.ufsc.ine.archwizardduel;

class LocalPlayer extends Player {

	private final Client userInterface;

	LocalPlayer(String userName, Client userInterface) {
		super(userName);
		this.userInterface = userInterface;
	}

	@Override
	Expression getNextPlay() {
		// @TODO
		return null;
	}

}