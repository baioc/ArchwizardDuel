package br.ufsc.ine.archwizardduel;

class RemotePlayer extends Player {

	private final Session remoteInterface;
	private Expression mailbox = null;

	RemotePlayer(String userName, Session remoteInterface) {
		super(userName);
		this.remoteInterface = remoteInterface;
	}

	public void receiveMail(Expression email) {
		mailbox = email;
	}

	@Override
	Expression getNextPlay() {
		try {
			while (mailbox == null)
				this.wait();
		} catch (InterruptedException ignored) {}

		Expression inmail = mailbox;
		mailbox = null;

		return inmail;
	}
}