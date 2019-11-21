package br.ufsc.ine.archwizardduel;

import br.ufsc.inf.leobr.cliente.Jogada;

public abstract class Expression implements Jogada {

	public abstract Value evaluate(Environment env) ;

}
