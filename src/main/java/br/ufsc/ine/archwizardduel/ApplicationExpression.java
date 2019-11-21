package br.ufsc.ine.archwizardduel;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;

public class ApplicationExpression extends Expression {

	private final Expression operator;
	private final List<Expression> operands;

	public ApplicationExpression(Expression operator, List<Expression> operands) {
		this.operator = operator;
		this.operands = operands;
	}

	@Override
	public Value evaluate(Environment env) throws ClassCastException {
		final Function<List<Value>,Value> proc =
			(Function<List<Value>,Value>) operator.evaluate(env).get();

		List<Value> arguments = new ArrayList<>();
		for (Expression op : operands)
			arguments.add(op.evaluate(env));

		return proc.apply(arguments);
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("(" + operator.toString() + " ");

		for (Expression expr : operands) {
			str.append(expr);
			str.append(' ');
		}

		str.deleteCharAt(str.length() - 1);
		str.append(')');

		return str.toString();
	}

}
