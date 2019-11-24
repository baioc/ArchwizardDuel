package br.ufsc.ine.archwizardduel;

import br.ufsc.inf.leobr.cliente.Proxy;
import br.ufsc.inf.leobr.cliente.OuvidorProxy;
import br.ufsc.inf.leobr.cliente.exception.JahConectadoException;
import br.ufsc.inf.leobr.cliente.exception.NaoPossivelConectarException;
import br.ufsc.inf.leobr.cliente.exception.ArquivoMultiplayerException;
import br.ufsc.inf.leobr.cliente.exception.NaoConectadoException;
import br.ufsc.inf.leobr.cliente.exception.NaoJogandoException;
import br.ufsc.inf.leobr.cliente.Jogada;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents the game server and both (@FIXME) its client-side and session-side
 * interfaces.
 */
public class Server implements OuvidorProxy {

	private Proxy proxy;
	private Client user;
	private Session connection;
	private boolean hasQuit;

	public Server() {
		proxy = Proxy.getInstance();
		proxy.addOuvinte(this);
		connection = null;
	}


	/*************************** CLIENT INTERFACE *****************************/

	public Session makeSession(Client host, String ip) {
		return connect(host, ip, true);
	}

	public Session joinSession(Client client, String ip) {
		return connect(client, ip, false);
	}

	private Session connect(Client user, String ip, boolean localHost) {
		try {
			proxy.conectar(ip, user.getPlayer().getName());
			this.user = user;
			if (localHost && (getPlayers() != null)) {
				leaveSession();
				return null;
			}
			hasQuit = false;
			connection = new Session(this, localHost);
		} catch (JahConectadoException e) {
			e.printStackTrace();
		} catch (NaoPossivelConectarException | ArquivoMultiplayerException e) {
			user.notify("Could not connect to " + ip + ":\n" + e.getMessage());
		}
		return connection;
	}

	public void leaveSession() {
		try {
			proxy.desconectar();
		} catch (NaoConectadoException e) {
			e.printStackTrace();
		}
		connection = null;
	}


	/************************** SESSION INTERFACE *****************************/

	public void beginMatch() {
		try{
			proxy.iniciarPartida(2);
		} catch (NaoConectadoException e) {
			e.printStackTrace();
		}
	}

	public void quitMatch() {
		hasQuit = true;
		try {
			proxy.finalizarPartida();
		} catch (NaoConectadoException | NaoJogandoException e) {
			e.printStackTrace();
		}
	}

	public void send(SerializedExpression code) {
		try {
			proxy.enviaJogada((Jogada) code);
			user.notify("Sent!"); // @XXX: needed?
		} catch (NaoJogandoException e) {
			e.printStackTrace();
		}
	}

	public List<Player> getPlayers() {
		final List<String> remotes = proxy.obterNomeAdversarios();

		if (remotes.size() < 1)
			return null;

		ArrayList<Player> participants = new ArrayList<>(remotes.size() + 1);
		participants.add(user.getPlayer());
		for (String name : remotes)
			participants.add(new Player(name));

		return participants;
	}


	/**************************** Ouvidor Proxy *******************************/

	@Override
	public void iniciarNovaPartida(Integer posicao) {
		if (connection.makeMatch(user))
			user.showMatch();
	}

	@Override
	public void receberJogada(Jogada jogada) {
		final SerializedExpression expr = (SerializedExpression) jogada;
		user.notify("Received!"); // @XXX: needed?
		connection.pull(expr);
	}

	@Override
	public void finalizarPartidaComErro(String message) {
		if (hasQuit) {
			hasQuit = false;
			return;
		}

		if (connection.isLocallyHosted()) {
			user.notify("Remote connection error:\n" + message);
			user.showSession();
		} else {
			user.notify("Host connection error:\n" + message);
			user.showBegin();
			leaveSession();
		}
	}

	@Override
	public void tratarConexaoPerdida() {
		finalizarPartidaComErro("Lost connection.");
	}

	@Override
	public void receberMensagem(String msg) {
		user.notify(msg);
	}

	@Override
	public void tratarPartidaNaoIniciada(String message) {}

}
