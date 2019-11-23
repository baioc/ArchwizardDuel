package br.ufsc.ine.archwizardduel;

import br.ufsc.ine.archwizardduel.Value.Type;

/**
 * An expression representing local abstraction through a symbolic definition.
 */
class DefinitionExpression implements Expression {

	private final String definiendum;
	private final Expression definition;
	private static final Value nil = new Value(Type.VOID, null);

	/**
	 * Constructs a DefinitionExpression with the given parameters.
	 *
	 * @param name       symbol whose value is to be defined
	 * @param definition expression whose evaluation yields the defining value
	 */
	public DefinitionExpression(String name, Expression definition) {
		definiendum = name;
		this.definition = definition;
	}

	/**
	 * @return undefined value, should not be used
	 */
	@Override
	public Value evaluate(Environment env) throws Exception {
		Value definiens = definition.evaluate(env);
		env.define(definiendum, definiens);
		return nil;
	}

	@Override
	public String toString() {
		return "(define " + definiendum + ' ' + definition.toString() + ')';
	}

}
