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
 * Game server which implements both client-side and network-side interfaces.
 */
public class GameServer implements Server, OuvidorProxy {

	private Proxy proxy;
	private Client user;
	private GameSession connection;
	private boolean hasQuit;

	public GameServer() {
		proxy = Proxy.getInstance();
		proxy.addOuvinte(this);
		user = null;
		connection = null;
	}


	/*************************** CLIENT INTERFACE *****************************/

	@Override
	public Session makeSession(Client host, String ip) {
		return connect(host, ip, true);
	}

	@Override
	public Session joinSession(Client client, String ip) {
		return connect(client, ip, false);
	}

	private Session connect(Client user, String ip, boolean localHost) {
		try {
			proxy.conectar(ip, user.getPlayer().getName());

			List<String> online = proxy.obterNomeAdversarios();
			if (localHost && online.size() > 0) {
				user.notify("Failed to host session: someone is already there.");
				leaveSession();
				return null;
			} else if (!localHost && online.size() < 1) {
				user.notify("Failed to join session: there is nobody there.");
				leaveSession();
				return null;
			}

			this.user = user;
			connection = new GameSession(this, localHost);
			hasQuit = false;
		} catch (JahConectadoException e) {
			e.printStackTrace();
		} catch (NaoPossivelConectarException | ArquivoMultiplayerException e) {
			user.notify("Could not connect to " + ip + ":\n" + e.getMessage());
		}

		return connection;
	}

	@Override
	public void leaveSession() {
		try {
			proxy.desconectar();
		} catch (NaoConectadoException e) {
			e.printStackTrace();
		}
		connection = null;
	}


	/************************** SESSION INTERFACE *****************************/

	/**
	 * Starts the remote match.
	 */
	public void beginMatch() {
		try{
			proxy.iniciarPartida(2);
		} catch (NaoConectadoException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Leaves the remote match.
	 */
	public void quitMatch() {
		hasQuit = true;
		try {
			proxy.finalizarPartida();
		} catch (NaoConectadoException | NaoJogandoException e) {
			e.printStackTrace();
		}
		user.showSession();
	}

	/**
	 * Sends a local play to the remote match.
	 */
	public void send(SerializedExpression play) {
		try {
			proxy.enviaJogada((Jogada) play);
		} catch (NaoJogandoException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the list of players in the remote match.
	 *
	 * @return null when there aren't enough connected players, otherwise a
	 *         List with those players, where the first player is local.
	 */
	public List<Player> players() {
		final List<String> remotes = proxy.obterNomeAdversarios();

		if (remotes.size() < 1)
			return null;

		// @XXX: fixed number of players
		ArrayList<Player> participants = new ArrayList<>(2);
		participants.add(user.getPlayer());
		participants.add(new Player(remotes.get(0)));

		return participants;
	}


	/**************************** Ouvidor Proxy *******************************/

	@Override
	public void iniciarNovaPartida(Integer posicao) {
		connection.makeMatch(user);
		user.showMatch();
	}

	@Override
	public void receberJogada(Jogada jogada) {
		final SerializedExpression play = (SerializedExpression) jogada;
		connection.pull(play);
	}

	@Override
	public void finalizarPartidaComErro(String message) {
		// check if we were the ones who quit
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
