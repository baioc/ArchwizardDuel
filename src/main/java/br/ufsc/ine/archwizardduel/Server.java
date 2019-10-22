package br.ufsc.ine.archwizardduel;

import br.ufsc.inf.leobr.cliente.Jogada;
import br.ufsc.inf.leobr.cliente.OuvidorProxy;
import br.ufsc.inf.leobr.cliente.Proxy;
import br.ufsc.inf.leobr.cliente.exception.ArquivoMultiplayerException;
import br.ufsc.inf.leobr.cliente.exception.JahConectadoException;
import br.ufsc.inf.leobr.cliente.exception.NaoConectadoException;
import br.ufsc.inf.leobr.cliente.exception.NaoJogandoException;
import br.ufsc.inf.leobr.cliente.exception.NaoPossivelConectarException;

public class Server implements OuvidorProxy {

	private Proxy proxy;
	private Client user;
	private Session connection;

	public Server() {
		proxy = Proxy.getInstance();
		proxy.addOuvinte(this);
		connection = null;
	}

	public boolean bothPlayersUp() {
		return proxy.obterNomeAdversarios().size() == 1;
	}

	public Session makeSession(Client host) {
		user = host;
		System.out.println("connecting to localhost: " + user.getPlayer().getName());
		try {
			proxy.conectar("localhost", user.getPlayer().getName());
			connection = new Session(this, true);
			connection.setRemotePlayer(new RemotePlayer("REMOTE", connection)); // FIXME: properly create remote player.

			return connection;
		} catch (JahConectadoException | NaoPossivelConectarException | ArquivoMultiplayerException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public Session joinSession(Client client, String ip) {
		user = client;
		System.out.println("connecting to" + ip + ": "+ user.getPlayer().getName());
		try {
			final String userName = user.getPlayer().getName();
			proxy.conectar(ip, userName);
			connection = new Session(this, false);
			// proxy.receberMensagem('J' + userName); create remotePlayer?
			connection.setRemotePlayer(new RemotePlayer(proxy.obterNomeAdversarios().get(0), connection));

			return connection;
		} catch (JahConectadoException |ArquivoMultiplayerException | NaoPossivelConectarException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public void quitSession() {
		user.showMessage("Disconnected!");

		try {
			proxy.desconectar();
		} catch (NaoConectadoException ex) { ex.printStackTrace(); }
		
		connection = null;
		user.showBegin();
		user = null;
	}

	public void makeMatch() {
		try {
			proxy.iniciarPartida(2);
		} catch (NaoConectadoException ex) { ex.printStackTrace(); }
	}

	public void quitMatch() {
		try {
			proxy.finalizarPartida();
		} catch (NaoConectadoException | NaoJogandoException ex) { ex.printStackTrace(); }
	}

	public void sendCode(Expression code) {
		try {
			proxy.enviaJogada((Jogada) code);
		} catch (NaoJogandoException ex) { ex.printStackTrace(); }
	}

	/**************************************************************************/
	/**************************** Ouvidor Proxy *******************************/
	/**************************************************************************/

	@Override
	public void receberMensagem(String msg) {
		// Unused.
	}

	@Override
	public void tratarConexaoPerdida() {
		// Unused.
	}

	@Override
	public void iniciarNovaPartida(Integer posicao) { // Who started the match shouldn't do anything here.
		if (!connection.amIHost()) {
			connection.joinMatch(user.getPlayer());			
			user.showMatch();
		}
	}

	@Override
	public void tratarPartidaNaoIniciada(String message) {
		// Should never happen.
	}

	@Override
	public void finalizarPartidaComErro(String message) {
		if (!bothPlayersUp()) {
			quitMatch();
		} else {
			user.showMessage("Match finished!");
			user.showSession();
		}
	}

	@Override
	public void receberJogada(Jogada jogada) { // mailbox.
		user.showMessage("Code received!");
		connection.receiveCode((Expression) jogada);
	}
}