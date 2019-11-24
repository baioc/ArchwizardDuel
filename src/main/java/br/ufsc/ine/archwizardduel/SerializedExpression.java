package br.ufsc.ine.archwizardduel;

import br.ufsc.inf.leobr.cliente.Jogada;

/**
 * Represents a serialization of Expression.
 */
public class SerializedExpression implements Jogada {

	private final String raw;

	/**
	 * @param code expression to be serialized
	 */
	public SerializedExpression(Expression code) {
		raw = code.toString();
	}

	/**
	 * @return string representation of this SerializedExpression
	 */
	@Override
	public String toString() {
		return raw;
	}

}
