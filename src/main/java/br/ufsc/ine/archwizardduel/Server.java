package br.ufsc.ine.archwizardduel;

import br.ufsc.inf.leobr.cliente.*;
import br.ufsc.inf.leobr.cliente.exception.*;

public class Server implements OuvidorProxy {

	private Proxy proxy;
	private Client user;
	private Session connection;
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
			localHost = true;
			proxy.conectar("localhost", user.getPlayer().getName());
			connection = new Session(this);
		} catch (JahConectadoException e) {
			e.printStackTrace(); // Should not be possible.
		} catch (NaoPossivelConectarException e) {
			user.showMessage(
				"Não foi possível conectar, certifique-se de que o " +
				"servidor NetGames ('server.jar') está rodando."
			);
		} catch (ArquivoMultiplayerException e) {
			user.showMessage(
				"Não foi possível ler o arquivo de configuração " +
				"do NetGames ('jogoMultiPlayer.properties')."
			);
		} finally {
			return connection;
		}
	}

	public Session joinSession(Client client, String ip) {
		user = client;
		System.out.println("connecting to" + ip + ": "+ user.getPlayer().getName());
		try {
			final String userName = user.getPlayer().getName();
			localHost = false;
			proxy.conectar(ip, userName);
			proxy.receberMensagem("JOIN: " + userName);
			connection = new Session(this);
		} catch (JahConectadoException e) {
			e.printStackTrace(); // Should not be possible.
		} catch (NaoPossivelConectarException e) {
			user.showMessage("Não foi possível conectar ao servidor.");
		} catch (ArquivoMultiplayerException e) {
			user.showMessage(
				"Não foi possível ler do arquivo de configuração " +
				"do NetGames ('jogoMultiPlayer.properties')."
			);
		} finally {
			return connection;
		}
	}

	public void quitSession() {
		if (localHost)
			proxy.tratarPerdaConexao();
		else
			proxy.receberMensagem("QUIT: " + user.getPlayer().getName());

		connection = null;
		try {
			proxy.desconectar();
		} catch (NaoConectadoException e) {
			e.printStackTrace(); // Should not be possible.
		}
		user = null;
	}

	/**************************************************************************/
	/**************************** Ouvidor Proxy *******************************/
	/**************************************************************************/

	@Override
	public void receberMensagem(String msg) {
		if (msg.contains("JOIN: ")) {
			final String userName = msg.substring(6);
			if (!userName.equals(proxy.getNomeJogador())) {
				user.showMessage(userName + " entrou na sessão.");
				user.showMatch(true, true); // enable match begin with enough players
			}

		} else if (msg.contains("QUIT: ")) {
			final String userName = msg.substring(6);
			if (!userName.equals(proxy.getNomeJogador())) {
				user.showMessage(userName + " saiu da sessão.");
				// @TODO: disable match begin with not enough players
			}
		}
	}

	@Override
	public void tratarConexaoPerdida() {
		user.showMessage("A sessão foi interrompida.");

		connection = null;
		try {
			proxy.desconectar();
		} catch (NaoConectadoException e) {}

		user.showBegin(); // disable session interface
		user = null;
	}

	@Override
	public void iniciarNovaPartida(Integer posicao) {
		// @TODO
	}

	@Override
	public void tratarPartidaNaoIniciada(String message) {
		tratarConexaoPerdida();
	}

	@Override
	public void finalizarPartidaComErro(String message) {
		// @TODO
	}

	@Override
	public void receberJogada(Jogada jogada) {
		// @TODO
	}

}
