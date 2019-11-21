package br.ufsc.ine.archwizardduel;

class VariableExpression extends Expression {

	private final String name;

	public VariableExpression(String name) {
		this.name = name;
	}

	@Override
	public Value evaluate(Environment env) {
		return env.lookup(name);
	}

	@Override
	public String toString() {
		return name;
	}

}
