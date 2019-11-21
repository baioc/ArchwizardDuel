package br.ufsc.ine.archwizardduel;

import java.util.List;

class SequentialExpression implements Expression {

	private final List<Expression> sequence;

	public SequentialExpression(List<Expression> sequence) {
		this.sequence = sequence;
	}

	@Override
	public Value evaluate(Environment env) {
		Value result = null;
		for (Expression expr : sequence)
			result = expr.evaluate(env);
		return result;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("(begin ");
		for (Expression expr : sequence) {
			str.append(expr);
			str.append(" ");
		}
		str.deleteCharAt(str.length() - 1);
		str.append(")");
		return str.toString();
	}

}
