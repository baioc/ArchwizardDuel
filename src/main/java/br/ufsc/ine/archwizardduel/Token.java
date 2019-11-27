package br.ufsc.ine.archwizardduel;

/**
 * Class used to represent lexical analysis tokens.
 */
class Token {

	/**
	 * Possible token types.
	 */
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

	/**
	 * Constructs a Token with given parameters.
	 *
	 * @param type type assigned to this token, matching its value (if any)
	 * @param value object assigned to this token, matching its type when a
	 *              literal is represented, otherwise its actual symbol.
	 * @param line source code line where this token was originally found
	 */
	public Token(Type type, Object value, int line) {
		this.type = type;
		this.value = value;
		this.line = line;
	}

	/**
	 * @return type assigned to this token, matching its value (if any)
	 */
	public Type type() {
		return type;
	}

	/**
	 * @return object assigned to this token, matching its type
	 */
	public Object value() {
		return value;
	}

	/**
	 * @return source code line where this token was originally found
	 */
	public int line() {
		return line;
	}

	@Override
	public String toString() {
		return value != null ? value.toString() : type.name();
	}

}
