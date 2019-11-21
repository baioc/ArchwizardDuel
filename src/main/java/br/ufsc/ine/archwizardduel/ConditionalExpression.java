package br.ufsc.ine.archwizardduel;

class ConditionalExpression extends Expression {

	private final Expression condition;
	private final Expression consequence;
	private final Expression alternative;

	public ConditionalExpression(
		Expression condition,
		Expression consequence,
		Expression alternative
	) {
		this.condition = condition;
		this.consequence = consequence;
		this.alternative = alternative;
	}

	public ConditionalExpression(Expression condition, Expression consequence) {
		// sets a default alternative to allow for ifs with consequence only
		this(condition, consequence, new VariableExpression("false"));
	}

	@Override
	public Value evaluate(Environment env) {
		if (condition.evaluate(env).isFalse())
			return alternative.evaluate(env);
		else
			return consequence.evaluate(env);
	}

	@Override
	public String toString() {
		return "(if " + condition.toString() + " " +
		                consequence.toString() + " " +
		                alternative.toString() + ")";
	}

}
