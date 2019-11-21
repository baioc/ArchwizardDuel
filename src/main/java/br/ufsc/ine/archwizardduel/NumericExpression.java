package br.ufsc.ine.archwizardduel;

import br.ufsc.ine.archwizardduel.Value.Type;
import java.math.BigDecimal;

class NumericExpression extends Expression {

	private final Value number;

	public NumericExpression(BigDecimal flonum) {
		number = new Value(Type.NUMBER, flonum);
	}

	@Override
	public Value evaluate(Environment env) {
		return number;
	}

	@Override
	public String toString() {
		return ((BigDecimal) number.get()).toString();
	}

}
