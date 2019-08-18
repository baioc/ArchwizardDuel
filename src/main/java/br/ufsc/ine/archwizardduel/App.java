package br.ufsc.ine.archwizardduel;

import br.ufsc.inf.leobr.cliente.OuvidorProxy;
import br.ufsc.inf.leobr.cliente.Jogada;

public final class App implements OuvidorProxy {
	public void iniciarNovaPartida(Integer integer) {}

	public void finalizarPartidaComErro(String s) {}

	public void receberMensagem(String s) {}

	public void receberJogada(Jogada jogada) {}

	public void tratarConexaoPerdida() {}

	public void tratarPartidaNaoIniciada(String s) {}

	public static void main(String[] args) {
		System.out.println("Hello, Maven World!");
	}
}
