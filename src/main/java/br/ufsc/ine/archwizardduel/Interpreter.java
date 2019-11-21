package br.ufsc.ine.archwizardduel;

import java.util.List;
import java.util.ArrayList;

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

	public static List<String> tokenize(String code) { // @XXX: protect
		List<String> tokens = new ArrayList<>();

		String[] input = code.split("\n");
		lines: for (String line : input) {
			for (int begin = 0; begin < line.length(); begin++) {
				char ch = line.charAt(begin);
				switch (ch) {
					case ';':
						continue lines;

					case '(':
						tokens.add("(");
						break;

					case ')':
						tokens.add(")");
						break;

					default:
						if (Character.isWhitespace(ch))
							break;

						int end = begin + 1;
						for (; end < line.length(); end++) {
							char curr = line.charAt(end);
							if (Character.isWhitespace(curr) || curr == ')' || curr == ';')
								break;
						}
						String word = line.substring(begin, end);
						tokens.add(word);
						begin = end - 1;
				}
			}
		}

		return tokens;
	}

}
