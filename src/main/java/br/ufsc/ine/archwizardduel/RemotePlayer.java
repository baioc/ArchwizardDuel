package br.ufsc.ine.archwizardduel;

class RemotePlayer extends Player {

	private final Session remoteInterface ;

	RemotePlayer(String userName, Session remoteInterface) {
		super(userName);
		this.remoteInterface = remoteInterface;
	}

	@Override
	Expression getNextPlay() {
		// @TODO
		return null;
	}

}