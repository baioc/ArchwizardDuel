package br.ufsc.ine.archwizardduel;

import java.util.List;

public class Procedure {

	private final List<String> parameters;
	private final Expression body;
	private final Environment definitionEnvironment;

	public Procedure(List<String> parameters, Expression body, Environment env) {
		this.parameters = parameters;
		this.body = body;
		this.definitionEnvironment = env;
	}

	public Value apply(List<Value> arguments) {
		// @TODO: check whether arguments match procedure arity
		Frame substitutions = new Frame();
		for (int i = 0; i < parameters.size(); ++i)
			substitutions.define(parameters.get(i), arguments.get(i));
		return body.evaluate(definitionEnvironment.enclose(substitutions));
	}

}
