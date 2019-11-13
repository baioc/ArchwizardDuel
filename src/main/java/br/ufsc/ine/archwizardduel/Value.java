package br.ufsc.ine.archwizardduel;

import java.math.BigDecimal;
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

	public Value() {
		this(Type.VOID, null);
	}

	public Value(Function<List<Value>,Value> closure) {
		this(Type.CLOSURE, closure);
	}

	public Value(BigDecimal number) {
		this(Type.NUMBER, number);
	}

	public Value(Boolean bool) {
		this(Type.BOOLEAN, bool);
	}

	public Type getType() {
		return type;
	}

	public Object getDatum() {
		return datum;
	}

	public boolean isFalse() {
		return type == Type.BOOLEAN && ((Boolean) datum).booleanValue() == false;
	}

}
