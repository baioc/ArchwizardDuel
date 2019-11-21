package br.ufsc.ine.archwizardduel;

import br.ufsc.ine.archwizardduel.Value.Type;

class DefinitionExpression implements Expression {

	private final String definiendum;
	private final Expression definition;
	private static final Value nil = new Value(Type.VOID, null);

	public DefinitionExpression(String symbol, Expression definition) {
		definiendum = symbol;
		this.definition = definition;
	}

	@Override
	public Value evaluate(Environment env) {
		Value definiens = definition.evaluate(env);
		env.define(definiendum, definiens);
		return nil;
	}

	@Override
	public String toString() {
		return "(define " + definiendum + " " + definition.toString() + ")";
	}

}
