package br.ufsc.ine.archwizardduel;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;

/**
 * An expression representing a procedure application.
 */
public class ApplicationExpression implements Expression {

	private final Expression operator;
	private final List<Expression> operands;

	/**
	 * Constructs the ApplicationExpression with given parameters.
	 *
	 * @param operator the expression whose evaluation yields the functional
	 *                 object to be applied
	 * @param operands actual arguments the procedure will be called with, these
	 *                 can be evaluated in any order, but always before the
	 *                 actual application
	 */
	public ApplicationExpression(Expression operator, List<Expression> operands) {
		this.operator = operator;
		this.operands = operands;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Value evaluate(Environment env) throws Exception {
		// evaluate operator
		Function<List<Value>,Value> proc = null;
		try {
			proc = (Function<List<Value>,Value>) operator.evaluate(env).value();
		} catch (ClassCastException e) {
			throw new Exception(
				"Evaluation error: " + operator.toString() + " is not a procedure."
			);
		}

		// evaluate operands
		List<Value> arguments = new ArrayList<>();
		for (Expression op : operands)
			arguments.add(op.evaluate(env));

		// return whatever value comes out of the function call
		try {
			return proc.apply(arguments);
		} catch (RuntimeException e) {
			throw new Exception(
				"Evaluation error during procedure application: " +
				e.getMessage()
			);
		}
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder('(' + operator.toString() + ' ');

		for (Expression expr : operands) {
			str.append(expr);
			str.append(' ');
		}

		str.deleteCharAt(str.length() - 1);
		str.append(')');

		return str.toString();
	}

}
