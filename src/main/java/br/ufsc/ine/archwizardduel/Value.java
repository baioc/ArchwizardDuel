package br.ufsc.ine.archwizardduel;

class Value { // @TODO

	enum Type {
		INTEGER,
		CLOSURE,
	}

	private Type type;
	private Object datum;

	public Value(Type type, Object datum) {
		this.type = type;
		this.datum = datum;
	}

}
