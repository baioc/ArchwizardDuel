package br.ufsc.ine.archwizardduel;

import br.ufsc.inf.leobr.cliente.Jogada;

public interface Expression extends Jogada {

	public Value evaluate(Environment env);

}
