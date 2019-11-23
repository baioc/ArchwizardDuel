package br.ufsc.ine.archwizardduel;

/**
 * Expression containing atomic variable names.
 */
class VariableExpression implements Expression {

	private final String name;

	public VariableExpression(String name) {
		this.name = name;
	}

	/**
	 * @return this variable's definition in the innermost environment scope.
	 * @throws Exception when this variable is undefined.
	 */
	@Override
	public Value evaluate(Environment env) throws Exception {
		return env.lookup(name);
	}

	@Override
	public String toString() {
		return name;
	}

}
