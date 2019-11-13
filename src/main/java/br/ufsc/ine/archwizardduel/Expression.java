package br.ufsc.ine.archwizardduel;

import br.ufsc.ine.archwizardduel.Value.Type;
import br.ufsc.inf.leobr.cliente.Jogada;
import java.util.List;
import java.util.LinkedList;
import java.math.BigDecimal;
import java.util.function.Function;

class Expression implements Jogada {

	private final List<Expression> subexpressions;
	private final String atom;
	// @NOTE: memoization may be a good idea, but beware of side-effects

	public Expression(String atom) {
		subexpressions = null;
		this.atom = atom;
	}

	public Expression(List<Expression> subexpressions) {
		this.subexpressions = subexpressions;
		atom = null;
	}

	@Override
	public String toString() {
		if (atom != null)
			return atom;

		StringBuilder builer = new StringBuilder("( ");
		for (Expression expr : subexpressions) {
			builer.append(expr);
			builer.append(' ');
		}
		builer.append(')');
		return builer.toString();
	}

	public Value evaluate(Environment env) {
		// atomic expression?
		if (atom != null) {
			// numbers are self-evaluating
			try {
				BigDecimal number = new BigDecimal(atom);
				return new Value(Type.NUMBER, number);
			} catch (NumberFormatException _) {}

			// booleans are self-evaluating
			if (atom.equals("true") || atom.equals("false"))
				return new Value(Type.BOOLEAN, new Boolean(atom));

			// symbols are looked up in the environment
			return env.lookup(atom); // @TODO: what if not found?

		// compound expression?
		} else {
			// check for special forms
			final String reservedWord = subexpressions.get(0).toString();
			switch (reservedWord) {
				case "begin":
					Value result = null;
					for (Expression expr : subexpressions)
						result = expr.evaluate(env);
					return result;

				case "define":
					env.define(
						subexpressions.get(1).atom,         // definiendum
						subexpressions.get(2).evaluate(env) // definiens
					);
					return new Value();

				case "if":
					if (subexpressions.get(1).evaluate(env).isFalse()) // condition
						return subexpressions.get(3).evaluate(env);    // alternative // @XXX: what if binary IF
					else
						return subexpressions.get(2).evaluate(env);    // consequence

				case "lambda":
					LinkedList<String> parameters = new LinkedList<>();
					for (Expression param : subexpressions.get(1).subexpressions)
						parameters.add(param.atom);
					List<Expression> body = subexpressions.subList(2, subexpressions.size());
					body.add(0, new Expression("begin"));
					return new Value(Type.CLOSURE, new Procedure(parameters, new Expression(body), env));

				// otherwise, apply a procedure
				default:
					final Function<List<Value>,Value> proc = (Function<List<Value>,Value>) subexpressions.get(0).evaluate(env).getDatum();
					List<Value> arguments = new LinkedList<>();
					for (int arg = 1; arg < subexpressions.size(); ++arg)
						arguments.add(subexpressions.get(arg).evaluate(env));
					return proc.apply(arguments);
			}
		}
	}

}
