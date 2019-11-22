package br.ufsc.ine.archwizardduel;

import java.util.function.Function;
import java.util.List;

class Value {

	public enum Type {
		VOID,
		CLOSURE,
		NUMBER,
		BOOLEAN,
	}

	private final Type type;
	private final Object datum;

	public Value(Type type, Object datum) {
		this.type = type;
		this.datum = datum;
	}

	// @NOTE: this constructor provides lambda syntax sugar closure Values
	public Value(Function<List<Value>,Value> procedure) {
		this.type = Type.CLOSURE;
		this.datum = procedure;
	}

	public Type type() {
		return type;
	}

	public Object get() {
		return datum;
	}

	public boolean isFalse() {
		return type == Type.BOOLEAN && ((Boolean) datum).booleanValue() == false;
	}

	@Override
	public String toString() {
		return datum.toString();
	}

}
