package br.ufsc.ine.archwizardduel;

import br.ufsc.ine.archwizardduel.Value.Type;
import java.util.List;

/**
 * Represents the glue to Lisp-like languages: lambda expressions.
 */
class LambdaExpression implements Expression {

	private final List<String> parameters;
	private final Expression body;

	/**
	 * Builds a LambdaExpression with the given information.
	 *
	 * @param parameters formal arguments of this lambda expression
	 * @param body       expression whose evaluation is delayed until this
	 *                   lambda is evoked
	 */
	public LambdaExpression(List<String> parameters, Expression body) {
		this.parameters = parameters;
		this.body = body;
	}

	/**
	 * @return a closure object created in the current evaluation environment
	 */
	@Override
	public Value evaluate(Environment env) throws Exception {
		return new Value(Type.CLOSURE, new Procedure(parameters, body, env));
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("(lambda (");

		for (String param : parameters)
			str.append(param + ' ');

		int last = str.length() - 1;
		if (str.charAt(last) == ' ')
			str.deleteCharAt(last);

		str.append(") ");
		str.append(body);
		str.append(')');

		return str.toString();
	}

}
