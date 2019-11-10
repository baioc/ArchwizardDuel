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

public class Server implements OuvidorProxy {

	private Proxy proxy;
	private Client user;
	private Session connection;
	private boolean hasQuit;
	private String ip;

	public Server() {
		proxy = Proxy.getInstance();
		proxy.addOuvinte(this);
		connection = null;
	}


	/*************************** CLIENT INTERFACE *****************************/

	public Session makeSession(Client host) {
		return connect(host, "localhost", true); // @FIXME: host session on variable IP
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
			this.ip = ip;
			hasQuit = false;
			connection = new Session(this, localHost);
		} catch (JahConectadoException ex) {
			ex.printStackTrace();
		} catch (NaoPossivelConectarException | ArquivoMultiplayerException ex) {
			user.showMessage("Could not connect to " + ip + ":\n" + ex.getMessage());
		} finally {
			return connection;
		}
	}

	public void leaveSession() {
		try {
			proxy.desconectar();
		} catch (NaoConectadoException ex) {
			ex.printStackTrace();
		}
		connection = null;
	}


	/************************** SESSION INTERFACE *****************************/

	public void beginMatch() {
		try{
			proxy.iniciarPartida(2);
		} catch (NaoConectadoException ex) {
			ex.printStackTrace();
		}
	}

	public void quitMatch() {
		hasQuit = true;
		try {
			proxy.finalizarPartida();
		} catch (NaoConectadoException | NaoJogandoException ex) {
			ex.printStackTrace();
		}
	}

	public void send(Expression code) {
		// @TODO: test valid code?
		try {
			proxy.enviaJogada((Jogada) code);
			user.showMessage("Sent!");
		} catch (NaoJogandoException ex) {
			ex.printStackTrace();
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
		if (connection.makeMatch())
			user.showMatch();
	}

	@Override
	public void receberJogada(Jogada jogada) {
		final Expression expr = (Expression) jogada;
		user.showMessage("Received:\n" + expr.toString());
		connection.pull(expr);
	}

	@Override
	public void finalizarPartidaComErro(String message) {
		if (hasQuit) {
			hasQuit = false;
			return;
		}

		if (connection.isLocallyHosted()) {
			user.showMessage("Remote connection error:\n" + message);
			user.showSession();
		} else {
			user.showMessage("Host connection error:\n" + message);
			user.showBegin();
			leaveSession();
		}
	}

	@Override
	public void tratarConexaoPerdida() {
		finalizarPartidaComErro("Lost connection.");
	}

	@Override
	public void receberMensagem(String msg) {}

	@Override
	public void tratarPartidaNaoIniciada(String message) {}

}
