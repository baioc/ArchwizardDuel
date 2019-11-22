package br.ufsc.ine.archwizardduel;

class Token {

	public static enum Type {
		VARIABLE,
		NUMBER,
		LPAREN,
		RPAREN,
		BEGIN,
		IF,
		DEFINE,
		LAMBDA,
	}

	private final Type type;
	private final Object value;
	private final int line;

	public Token(Type type, Object value, int line) {
		this.type = type;
		this.value = value;
		this.line = line;
	}

	public Type getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	public int getLine() {
		return line;
	}

	@Override
	public String toString() {
		return value.toString();
	}

}
