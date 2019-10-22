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
	public Session connection;
	private boolean localHost;


	public Server() {
		proxy = Proxy.getInstance();
		proxy.addOuvinte(this);
		connection = null;
	}

	public Session makeSession(Client host) {
		user = host;
		System.out.println("connecting to localhost: " + user.getPlayer().getName());
		try {
			proxy.conectar("localhost", user.getPlayer().getName());
			connection = new Session(this);
			localHost = true;
			connection.setRemotePlayer(new RemotePlayer("REMOTE", connection));

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
			connection = new Session(this);
			localHost = false;
			// proxy.receberMensagem('J' + userName); create remotePlayer?
			connection.setRemotePlayer(new RemotePlayer(proxy.obterNomeAdversarios().get(0), connection));

			return connection;
		} catch (JahConectadoException |ArquivoMultiplayerException | NaoPossivelConectarException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public void quitSession() {
		if (localHost)
			proxy.tratarPerdaConexao();

		connection = null;
		user.showBegin();

		try {
			proxy.desconectar();
		} catch (NaoConectadoException ex) { ex.printStackTrace(); } // Should not be possible.
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
		// switch(msg.charAt(0)) { how to pass messages?
		// 	case 'J':
		// 		connection.setRemotePlayer(new RemotePlayer(msg.substring(1), connection));
		// 	case 'Q':
		// 		break;
		// }
	}

	@Override
	public void tratarConexaoPerdida() {
		user.showMessage("A sessão foi interrompida.");

		connection = null;
		try {
			proxy.desconectar();
		} catch (NaoConectadoException ex) { ex.printStackTrace(); }

		user.showBegin(); // disable session interface
		user = null;
	}

	@Override
	public void iniciarNovaPartida(Integer posicao) { // only remote should end here.
		try {
			user.match = connection.joinMatch(user.localPlayer);	
		} catch (NullPointerException FIXME) { FIXME.printStackTrace(); }
		
		user.showMatch();
	}

	@Override
	public void tratarPartidaNaoIniciada(String message) {
		// Should never happen.
	}

	@Override
	public void finalizarPartidaComErro(String message) {
		user.showMessage("Partida finalizada com erro!");
		tratarConexaoPerdida();
	}

	@Override
	public void receberJogada(Jogada jogada) { // mailbox.
		user.showMessage("Jogada recebida!");
		connection.receiveCode((Expression) jogada);
	}
}