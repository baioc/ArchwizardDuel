package br.ufsc.ine.archwizardduel;

import java.util.function.Function;
import java.util.List;

/**
 * Represents the union of values pertaining to any type in the language.
 */
class Value {

	/**
	 * Possible value types.
	 */
	public enum Type {
		VOID,
		CLOSURE,
		NUMBER,
		BOOLEAN,
	}

	private final Type type;
	private final Object datum;

	/**
	 * Generic type constructor.
	 * Make sure its parameters match.
	 *
	 * @param type  this value's type
	 * @param datum encapsulated Object, should match the given Type
	 */
	public Value(Type type, Object datum) {
		this.type = type;
		this.datum = datum;
	}

	/**
	 * Constructor used to provide lambda syntax sugar for closure values.
	 */
	public Value(Function<List<Value>,Value> procedure) {
		this.type = Type.CLOSURE;
		this.datum = procedure;
	}

	/**
	 * @return this object's value type
	 */
	public Type type() {
		return type;
	}

	/**
	 * Actual data contained in this object, should be cast according to type().
	 */
	public Object value() {
		return datum;
	}

	/**
	 * Checks the truth-value of this value.
	 *
	 * @return true if and only if this is the boolean value "false"
	 */
	public boolean isFalse() {
		return type == Type.BOOLEAN && !((Boolean) datum).booleanValue();
	}

	@Override
	public String toString() {
		return type == Type.VOID ? "" : datum.toString();
	}

}
