package br.ufsc.ine.archwizardduel;

import br.ufsc.ine.archwizardduel.Token.Type;
import java.util.LinkedList;
import java.math.BigDecimal;

class Interpreter { // @TODO

	private Environment globals;

	public Interpreter() {
		globals = new Environment(new Frame());
	}

	public Expression parse(String code) {
		return new VariableExpression(code); // @XXX: testing
		// return parse(tokenize("(begin " + code + ")"));
	}

	public void interpret(Expression expr) {
		expr.evaluate(globals);
	}

	public static LinkedList<Token> tokenize(String code) { // @XXX: protect
		LinkedList<Token> tokens = new LinkedList<>();

		String[] input = code.split("\n");
		lines: for (int i = 0; i < input.length; i++) {
			String line = input[i];
			for (int begin = 0; begin < line.length(); begin++) {
				char ch = line.charAt(begin);
				if (Character.isWhitespace(ch)) {
					continue;
				} if (ch == ';') {
					break;
				} else if (ch == '(') {
					tokens.addLast(new Token(Type.LPAREN, "(", i));
				} else if (ch == ')') {
					tokens.addLast(new Token(Type.LPAREN, ")", i));
				} else {
					int end = begin + 1;
					for (; end < line.length(); end++) {
						char curr = line.charAt(end);
						if (Character.isWhitespace(curr) || curr == ')' || curr == ';')
							break;
					}

					String word = line.substring(begin, end);
					switch (word) {
						case "begin":
							tokens.addLast(new Token(Type.BEGIN, word, i));
							break;

						case "if":
							tokens.addLast(new Token(Type.IF, word, i));
							break;

						case "define":
							tokens.addLast(new Token(Type.DEFINE, word, i));
							break;

						case "lambda":
							tokens.addLast(new Token(Type.LAMBDA, word, i));
							break;

						default:
							try {
								BigDecimal number = new BigDecimal(word);
								tokens.addLast(new Token(Type.NUMBER, number, i));
							} catch (NumberFormatException _) {
								tokens.addLast(new Token(Type.VARIABLE, word, i));
							}
							break;
					}

					begin = end - 1;
				}
			}
		}

		return tokens;
	}

	private static Token consume(List<Token> tokens, Token.Type expected) {
		Token symbol = tokens.remove(0);
		if (symbol.getType() == expected)
			return symbol;
		else
			throw new IllegalArgumentException(
				"Parse error at line " + symbol.getLine() + ": " +
				" expected " + expected.name() +
				", found " + symbol + "."
			);
	}

	private static List<Expression> buildExpressionList(List<Token> tokens) {
		List<Expression> expList = new ArrayList<>();
		while (tokens.get(0).getType() != Token.Type.RPAREN)
			expList.add(buildExpression(tokens));
		return expList;
	}

	private static Expression buildExpression(List<Token> tokens) {
		// compound
		if (tokens.get(0).getType() == Token.Type.LPAREN) {
			switch (tokens.get(1).getType()) {
				case BEGIN: {
					consume(tokens, Token.Type.LPAREN);
					consume(tokens, Token.Type.BEGIN);
					List<Expression> sequence = buildExpressionList(tokens);
					consume(tokens, Token.Type.RPAREN);
					return new SequentialExpression(sequence);
				}

				case DEFINE: {
					consume(tokens, Token.Type.LPAREN);
					consume(tokens, Token.Type.DEFINE);
					Token symbol = consume(tokens, Token.Type.VARIABLE);
					Expression definition = buildExpression(tokens);
					consume(tokens, Token.Type.RPAREN);
					return new DefinitionExpression(symbol.toString(), definition);
				}

				case IF: {
					consume(tokens, Token.Type.LPAREN);
					consume(tokens, Token.Type.IF);
					Expression condition = buildExpression(tokens);
					Expression consequence = buildExpression(tokens);
					Expression alternative = buildExpression(tokens); // @TODO: what if not ternary
					consume(tokens, Token.Type.RPAREN);
					return new ConditionalExpression(condition, consequence, alternative);
				}

				case LAMBDA: {
					consume(tokens, Token.Type.LPAREN);
					consume(tokens, Token.Type.LAMBDA);

					// build formal parameters
					consume(tokens, Token.Type.LPAREN);
					List<Expression> params = buildExpressionList(tokens);
					consume(tokens, Token.Type.RPAREN);

					// build body
					List<Expression> body = buildExpressionList(tokens);

					consume(tokens, Token.Type.RPAREN);
					return new LambdaExpression(params, new SequentialExpression(body));
				}

				default: {
					consume(tokens, Token.Type.LPAREN);
					Token operator = consume(tokens, Token.Type.VARIABLE);
					List<Expression> operands = buildExpressionList(tokens);
					consume(tokens, Token.Type.RPAREN);
					return new ApplicationExpression(operator, operands);
				}
			}

		// atomic
		} else {
			Token atom = consume(tokens, Token.Type.VARIABLE, Token.Type.NUMBER);
			return new Expression(atom);
		}
	}

	public static void main(String[] args) {
		String input =
			  "(begin"
			+ "    (define a 1.5)"
			+ "    (define var b)"
			+ "    (lambda (x y) (+ x y))"
			+ "    (define sum (lambda (x y) (+ x y)))"
			+ "    (define twentytwo 22)"
			+ "    (define avg (lambda (x y) (/ (+ x y) 2)))"
			+ "    (define distance (lambda (x1 y1 x2 y2) (sqrt (+ (exp (- x1 x2) 2) (exp (- y1 y2) 2)))))"
					// out-of-range 64bit integer literal (for java)
			+ "    (define HUGE! 10000000000000000000000000000000000000000000000000000000028132130913223456)"
			+ ")";

		System.out.println(buildExpression(Interpreter.tokenize(input)).toString());
	}

}
