package br.ufsc.ine.archwizardduel;

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
