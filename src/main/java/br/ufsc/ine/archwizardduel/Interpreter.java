package br.ufsc.ine.archwizardduel;

class Interpreter { // @TODO

	private Environment globals;

	public Interpreter(Frame primitives) {
		globals = new Environment(primitives);
	}

	public Expression parse(String code) {
		return new VariableExpression(code); // @XXX: testing
	}

	public void interpret(Expression expr) {
		expr.evaluate(globals);
	}

}
