package br.ufsc.ine.archwizardduel;

import br.ufsc.ine.archwizardduel.Value.Type;
import java.math.BigDecimal;

/**
 * Used to represent literal numeric expressions.
 */
class NumericExpression implements Expression {

	private final Value number;

	/**
	 * Constructs a NumericExpression with the given literal.
	 *
	 * @param flonum bignum implementation of decimal values.
	 */
	public NumericExpression(BigDecimal flonum) {
		number = new Value(Type.NUMBER, flonum);
	}

	@Override
	public Value evaluate(Environment env) throws Exception {
		return number;
	}

	@Override
	public String toString() {
		return number.toString();
	}

}
