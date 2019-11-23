package br.ufsc.ine.archwizardduel;

import java.math.BigDecimal;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

class Interpreter {

	private Environment globals;

	public static class ParseException extends Exception {
		public ParseException(String message) {
			super(message);
		}
	}

	public Interpreter() {
		Frame primitives = new Frame();
		primitives.define("false",
			new Value(Value.Type.BOOLEAN, new Boolean(false))
		);
		primitives.define("true",
			new Value(Value.Type.BOOLEAN, new Boolean(true))
		);
		primitives.define("+",
			new Value(args -> {
				BigDecimal a = (BigDecimal) args.get(0).get();
				BigDecimal b = (BigDecimal) args.get(1).get();
				return new Value(Value.Type.NUMBER, a.add(b));
			}
		));
		primitives.define("-",
			new Value(args -> {
				BigDecimal a = (BigDecimal) args.get(0).get();
				BigDecimal b = (BigDecimal) args.get(1).get();
				return new Value(Value.Type.NUMBER, a.subtract(b));
			}
		));
		globals = new Environment(primitives);
	}

	public Expression parse(String code) throws ParseException {
		List<Token> lexed = tokenize("(begin " + code + ")");
		Expression parsed = null;

		try {
			parsed = compile(lexed);
		} catch (IllegalArgumentException parsingError) {
			throw new ParseException(parsingError.getMessage());
		} catch (IndexOutOfBoundsException endOfFile) {
			throw new ParseException(
				"Syntactic error: found unexpected end of file."
			);
		}

		if (!lexed.isEmpty()) {
			Token tail = lexed.get(0);
			throw new ParseException(
				"Syntactic error near line " + tail.getLine() + ": " +
				tail.toString() + " is an invalid start of expression."
			);
		}

		return parsed;
	}

	public String interpret(Expression expr) {
		return expr.evaluate(globals).toString();
	}

	protected static List<Token> tokenize(String code) {
		List<Token> tokens = new LinkedList<>();

		String[] input = code.split("\n");
		lines: for (int i = 0; i < input.length; i++) {
			String line = input[i];
			for (int begin = 0; begin < line.length(); begin++) {
				char ch = line.charAt(begin);
				if (Character.isWhitespace(ch)) {
					continue;
				} if (ch == ';') {
					break; // continue lines
				} else if (ch == '(') {
					tokens.add(new Token(Token.Type.LPAREN, "(", i+1));
				} else if (ch == ')') {
					tokens.add(new Token(Token.Type.RPAREN, ")", i+1));
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
							tokens.add(new Token(Token.Type.BEGIN, word, i+1));
							break;
						case "if":
							tokens.add(new Token(Token.Type.IF, word, i+1));
							break;
						case "define":
							tokens.add(new Token(Token.Type.DEFINE, word, i+1));
							break;
						case "lambda":
							tokens.add(new Token(Token.Type.LAMBDA, word, i+1));
							break;
						default:
							try {
								BigDecimal number = new BigDecimal(word);
								tokens.add(new Token(Token.Type.NUMBER, number, i+1));
							} catch (NumberFormatException _) {
								tokens.add(new Token(Token.Type.VARIABLE, word, i+1));
							}
							break;
					}

					begin = end - 1;
				}
			}
		}

		return tokens;
	}

	protected static Expression compile(
		List<Token> tokens
	) throws IndexOutOfBoundsException, IllegalArgumentException {
		// check for atomic expressions
		Token head = tokens.get(0);
		switch (head.getType()) {
			case NUMBER:
				Token number = consume(tokens, Token.Type.NUMBER);
				return new NumericExpression((BigDecimal) number.getValue());

			case VARIABLE:
				Token variable = consume(tokens, Token.Type.VARIABLE);
				return new VariableExpression((String) variable.getValue());

			case LPAREN:
				// not atomic
				break;

			default:
				throw new IllegalArgumentException(
					"Syntactic error near line " + head.getLine() + ": " +
					head.toString() + " is an invalid start of expression."
				);
		}

		// otherwise, parse some compound expression
		switch (tokens.get(1).getType()) {
			case BEGIN: {
				consume(tokens, Token.Type.LPAREN);
				consume(tokens, Token.Type.BEGIN);
				List<Expression> sequence = compileTail(tokens);
				consume(tokens, Token.Type.RPAREN);
				return new SequentialExpression(sequence);
			}

			case DEFINE: {
				consume(tokens, Token.Type.LPAREN);
				consume(tokens, Token.Type.DEFINE);
				Token name = consume(tokens, Token.Type.VARIABLE);
				Expression value = compile(tokens);
				consume(tokens, Token.Type.RPAREN);
				return new DefinitionExpression((String) name.getValue(), value);
			}

			case IF: {
				consume(tokens, Token.Type.LPAREN);
				consume(tokens, Token.Type.IF);
				Expression condition = compile(tokens);
				Expression consequence = compile(tokens);
				Expression alternative = compile(tokens);
				consume(tokens, Token.Type.RPAREN);
				return new ConditionalExpression(condition, consequence, alternative);
			}

			case LAMBDA: {
				consume(tokens, Token.Type.LPAREN);
				consume(tokens, Token.Type.LAMBDA);
				consume(tokens, Token.Type.LPAREN);
				List<String> params = new ArrayList<>();
				while (tokens.get(0).getType() != Token.Type.RPAREN) {
					params.add(
						(String) consume(tokens, Token.Type.VARIABLE).getValue()
					);
				}
				consume(tokens, Token.Type.RPAREN);
				List<Expression> body = compileTail(tokens);
				consume(tokens, Token.Type.RPAREN);
				return new LambdaExpression(params, new SequentialExpression(body));
			}

			default: { // case APPLICATION:
				consume(tokens, Token.Type.LPAREN);
				Expression operator = compile(tokens);
				List<Expression> operands = compileTail(tokens);
				consume(tokens, Token.Type.RPAREN);
				return new ApplicationExpression(operator, operands);
			}
		}
	}

	private static List<Expression> compileTail(
		List<Token> tokens
	) throws IndexOutOfBoundsException {
		List<Expression> exprs = new ArrayList<>();
		while (tokens.get(0).getType() != Token.Type.RPAREN)
			exprs.add(compile(tokens));
		return exprs;
	}

	private static Token consume(
		List<Token> tokens,
		Token.Type expected
	) throws IllegalArgumentException, IndexOutOfBoundsException {
		Token symbol = tokens.remove(0);
		if (symbol.getType() == expected)
			return symbol;
		else
			throw new IllegalArgumentException(
				"Syntactic error near line " + symbol.getLine() + ": " +
				"expected " + expected.name() + ", found " + symbol.toString() + "."
			);
	}

}
