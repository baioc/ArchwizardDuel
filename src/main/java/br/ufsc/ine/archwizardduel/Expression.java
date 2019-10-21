package br.ufsc.ine.archwizardduel;

import java.util.List;

class Expression {

	private final List<Expression> subexpressions;

	Expression(List<Expression> subexpressions) {
		this.subexpressions = subexpressions;
	}

	Value evaluate(Environment env) {
		// @TODO
		return null;
	}

}
