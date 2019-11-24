package br.ufsc.ine.archwizardduel;

import java.util.List;

/**
 * Special expression representing sequential operations.
 */
class SequentialExpression implements Expression {

	private final List<Expression> sequence;

	/**
	 * Constructs a new SequentialExpression with the given expressions.
	 *
	 * @param sequence contains one or more expressions to be sequentialized,
	 *                 the last of which evals to the result of this expression
	 * @throws Exception when the sequence is empty
	 */
	public SequentialExpression(List<Expression> sequence) throws Exception {
		if (sequence.size() < 1)
			throw new Exception("Empty sequential expression.");
		this.sequence = sequence;
	}

	/**
	 * @return the result of the last subexpression evaluated
	 */
	@Override
	public Value evaluate(Environment env) throws Exception {
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
			str.append(' ');
		}
		str.deleteCharAt(str.length() - 1);
		str.append(')');
		return str.toString();
	}

}
