package br.ufsc.ine.archwizardduel;

class Interpreter { // @TODO

	private Environment globals;

	public Expression parse(String code) {
		return new Expression(code); // @XXX: testing
	}

	public void interpret(Expression expr) {}

}
