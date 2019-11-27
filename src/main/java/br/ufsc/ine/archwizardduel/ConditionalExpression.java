package br.ufsc.ine.archwizardduel;

/**
 * Represents conditional (if-then-else) expressions.
 */
class ConditionalExpression implements Expression {

	private final Expression condition;
	private final Expression consequence;
	private final Expression alternative;

	/**
	 * Builds a ConditionalExpression with the given parameters.
	 *
	 * @param condition   is always evaluated first and its value is checked to
	 *                    decided the whole expression's result
	 * @param consequence when condition yields a non-false value, this is
	 *                    evaluated
	 * @param alternative when condition yields a false value, this is evaluated
	 *                    instead
	 */
	public ConditionalExpression(
		Expression condition,
		Expression consequence,
		Expression alternative
	) {
		this.condition = condition;
		this.consequence = consequence;
		this.alternative = alternative;
	}

	@Override
	public Value evaluate(Environment env) throws Exception {
		if (!condition.evaluate(env).isFalse())
			return consequence.evaluate(env);
		else
			return alternative.evaluate(env);
	}

	@Override
	public String toString() {
		return "(if " + condition.toString() + ' ' +
		                consequence.toString() + ' ' +
		                alternative.toString() + ')';
	}

}
