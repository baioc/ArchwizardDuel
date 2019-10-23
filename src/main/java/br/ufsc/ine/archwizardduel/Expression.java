package br.ufsc.ine.archwizardduel;

import br.ufsc.inf.leobr.cliente.Jogada;
import java.util.List;

class Expression implements Jogada { // @TODO

	private final List<Expression> subexpressions;
	private final String token;

	public Expression(String token) {
		subexpressions = null;
		this.token = token;
	}
	// @XXX: testing
	@Override
	public String toString() {
		return token;
	}

	public Value evaluate(Environment env) {
		return null;
	}

}
