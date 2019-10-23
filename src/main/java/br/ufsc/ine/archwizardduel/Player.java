package br.ufsc.ine.archwizardduel;

public class Player {

	final String userName;
	private Expression play;

	public Player(String name) {
		userName = name;
		play = null;
	}

	public String getName() {
		return userName;
	}

	public synchronized void setNextPlay(Expression code) {
		play = code;
		notify();
	}

	public synchronized Expression getNextPlay() {
		try {
			while (play == null)
				wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			final Expression inbox = play;
			play = null;
			return inbox;
		}
	}

}
