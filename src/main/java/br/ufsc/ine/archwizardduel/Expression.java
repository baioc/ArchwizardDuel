package br.ufsc.ine.archwizardduel;

import br.ufsc.ine.archwizardduel.Value.Type;
import br.ufsc.inf.leobr.cliente.Jogada;
import java.util.List;
import java.math.BigDecimal;
import java.util.LinkedList;

class Expression implements Jogada { // @TODO

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
			
		StringBuilder builer = new StringBuilder("(");
		for (Expression expr : subexpressions)
			builer.append(expr);
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
					Value result;
					for (Expression expr : subexpressions)
						result = expr.evaluate(env);
					return result;
				
				case "define":
					env.define(
						subexpressions.get(1).atom,         // definiendum // @XXX: test atomicity?
						subexpressions.get(2).evaluate(env) // definiens
					);
					return new Value(Type.VOID, null);
				
				case "if":
					if (subexpressions.get(1).evaluate(env).isFalse()) // condition
						return subexpressions.get(3).evaluate(env);    // alternative // @XXX: what if binary IF
					else
						return subexpressions.get(2).evaluate(env);    // consequence
				
				case "lambda":
					LinkedList<String> parameters = new LinkedList<>();
					for (Expression param : subexpressions.get(1).subexpressions) // @XXX: test listing?
						parameters.add(param.atom); // @XXX: test atomicity?
					return new Value(
						Type.CLOSURE,
						new Procedure(parameters, subexpressions.get(2), env)
					);
				
				// otherwise, apply a procedure
				default:
					// @TODO: check for primitive procedures?
					final Procedure proc = (Procedure) subexpressions.get(0).evaluate(env);
					LinkedList<Value> arguments = new LinkedList<>();
					for (int arg = 1; arg < subexpressions.size(); ++arg)
						arguments.add(subexpressions.get(arg).evaluate(env));
					return proc.apply(arguments);
			}
		}
	}

}
