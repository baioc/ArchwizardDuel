package br.ufsc.ine.archwizardduel;

import java.util.function.Function;
import java.util.List;
import java.lang.IllegalArgumentException;

public class Procedure implements Function<List<Value>,Value> {

	private final List<String> parameters;
	private final Expression body;
	private final Environment definitionEnvironment;

	public Procedure(List<String> parameters, Expression body, Environment env) {
		this.parameters = parameters;
		this.body = body;
		this.definitionEnvironment = env;
	}

	@Override
	public Value apply(List<Value> arguments) throws IllegalArgumentException {
		if (arguments.size() != parameters.size()) {
			throw new IllegalArgumentException(
				"Could not match formal parameter list in apply " +
				parameters.toString() + " ."
			);
		}

		Frame substitutions = new Frame();
		for (int i = 0; i < parameters.size(); ++i)
			substitutions.define(parameters.get(i), arguments.get(i));

		Environment extended = definitionEnvironment.enclose(substitutions);
		return body.evaluate(extended);
	}

}
