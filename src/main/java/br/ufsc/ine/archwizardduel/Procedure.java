package br.ufsc.ine.archwizardduel;

import java.util.function.Function;
import java.util.List;
import java.lang.IllegalArgumentException;

/**
 * A functional object used to represent lexically scoped procedures: closures.
 * @NOTE: errors taking place during apply are thrown as RuntimeExceptions.
 */
public class Procedure implements Function<List<Value>,Value> {

	private final List<String> parameters;
	private final Expression body;
	private final Environment definitionEnvironment;

	/**
	 * Creates a new closure that remembers the environment it was defined at.
	 *
	 * @param parameters formal arguments of this procedure
	 * @param body       expression representing the actual function instance
	 * @param env        environment where this procedure was defined
	 */
	public Procedure(List<String> parameters, Expression body, Environment env) {
		this.parameters = parameters;
		this.body = body;
		this.definitionEnvironment = env;
	}

	/**
	 * @throws RuntimeException when evaluation error happens
	 */
	@Override
	public Value apply(List<Value> arguments) throws RuntimeException {
		if (arguments.size() != parameters.size()) {
			throw new IllegalArgumentException(
				"Given arguments " + arguments.toString() +
				" failed to match formal parameter list " +
				parameters.toString() + "."
			);
		}

		Frame substitutions = new Frame();
		for (int i = 0; i < parameters.size(); ++i)
			substitutions.define(parameters.get(i), arguments.get(i));

		Environment extended = definitionEnvironment.enclose(substitutions);
		try {
			return body.evaluate(extended);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

}
