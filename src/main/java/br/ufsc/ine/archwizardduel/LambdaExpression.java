package br.ufsc.ine.archwizardduel;

import br.ufsc.ine.archwizardduel.Value.Type;
import java.util.List;

class LambdaExpression extends Expression {

	private final List<String> parameters;
	private final Expression body;

	public LambdaExpression(List<String> parameters, Expression body) {
		this.parameters = parameters;
		this.body = body;
	}

	@Override
	public Value evaluate(Environment env) {
		Procedure closure = new Procedure(parameters, body, env);
		return new Value(Type.CLOSURE, closure);
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("(lambda (");

		for (String param : parameters)
			str.append(param + " ");

		str.deleteCharAt(str.length() - 1);
		str.append(") ");

		str.append(body);
		str.append(")");

		return str.toString();
	}

}
