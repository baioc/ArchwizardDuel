package br.ufsc.ine.archwizardduel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * Lisp evaluator, in Java.
 */
class Interpreter {

	private Environment globals;

	/**
	 * Constructs an Interpreter with a default global evaluation environment.
	 */
	public Interpreter() {
		this(new Frame());
	}

	/**
	 * Constructs an Interpreter with some extra primitives, these can be
	 * overwritten by default language definitions if conflicts arise.
	 *
	 * @param primitives frame containing additional definitions to be used
	 */
	public Interpreter(Frame primitives) {
		primitives.define("false",
			new Value(Value.Type.BOOLEAN, new Boolean(false))
		);
		primitives.define("true",
			new Value(Value.Type.BOOLEAN, new Boolean(true))
		);
		primitives.define("+",
			new Value(args -> {
				BigDecimal a = (BigDecimal) args.get(0).value();
				BigDecimal b = (BigDecimal) args.get(1).value();
				return new Value(Value.Type.NUMBER, a.add(b));
			}
		));
		primitives.define("-",
			new Value(args -> {
				BigDecimal a = (BigDecimal) args.get(0).value();
				BigDecimal b = (BigDecimal) args.get(1).value();
				return new Value(Value.Type.NUMBER, a.subtract(b));
			}
		));
		primitives.define("=",
			new Value(args -> {
				BigDecimal a = (BigDecimal) args.get(0).value();
				BigDecimal b = (BigDecimal) args.get(1).value();
				return new Value(
					Value.Type.BOOLEAN,
					new Boolean(a.compareTo(b) == 0)
				);
			}
		));
		primitives.define("<",
			new Value(args -> {
				BigDecimal a = (BigDecimal) args.get(0).value();
				BigDecimal b = (BigDecimal) args.get(1).value();
				return new Value(
					Value.Type.BOOLEAN,
					new Boolean(a.compareTo(b) < 0)
				);
			}
		));
		primitives.define(">",
			new Value(args -> {
				BigDecimal a = (BigDecimal) args.get(0).value();
				BigDecimal b = (BigDecimal) args.get(1).value();
				return new Value(
					Value.Type.BOOLEAN,
					new Boolean(a.compareTo(b) > 0)
				);
			}
		));
		primitives.define("not",
			new Value(args -> {
				Value pred = args.get(0);
				if (pred.isFalse())
					return new Value(Value.Type.BOOLEAN, new Boolean(true));
				else
					return new Value(Value.Type.BOOLEAN, new Boolean(false));
			}
		));
		primitives.define("<=",
			new Value(args -> {
				BigDecimal a = (BigDecimal) args.get(0).value();
				BigDecimal b = (BigDecimal) args.get(1).value();
				return new Value(
					Value.Type.BOOLEAN,
					new Boolean(a.compareTo(b) <= 0)
				);
			}
		));
		primitives.define(">=",
			new Value(args -> {
				BigDecimal a = (BigDecimal) args.get(0).value();
				BigDecimal b = (BigDecimal) args.get(1).value();
				return new Value(
					Value.Type.BOOLEAN,
					new Boolean(a.compareTo(b) >= 0)
				);
			}
		));
		primitives.define("*",
			new Value(args -> {
				BigDecimal a = (BigDecimal) args.get(0).value();
				BigDecimal b = (BigDecimal) args.get(1).value();
				return new Value(Value.Type.NUMBER, a.multiply(b));
			}
		));
		primitives.define("/",
			new Value(args -> {
				BigDecimal a = (BigDecimal) args.get(0).value();
				BigDecimal b = (BigDecimal) args.get(1).value();
				return new Value(
					Value.Type.NUMBER,
					a.divide(b, RoundingMode.HALF_EVEN)
				);
			}
		));
		primitives.define("quotient",
			new Value(args -> {
				BigDecimal a = (BigDecimal) args.get(0).value();
				BigDecimal b = (BigDecimal) args.get(1).value();
				return new Value(
					Value.Type.NUMBER,
					a.divideToIntegralValue(b)
				);
			}
		));
		primitives.define("remainder",
			new Value(args -> {
				BigDecimal a = (BigDecimal) args.get(0).value();
				BigDecimal b = (BigDecimal) args.get(1).value();
				return new Value(
					Value.Type.NUMBER,
					a.divideAndRemainder(b)[1]
				);
			}
		));
		primitives.define("boolean?",
			new Value(args -> {
				return new Value(
					Value.Type.BOOLEAN,
					new Boolean(args.get(0).type() == Value.Type.BOOLEAN)
				);
			}
		));
		primitives.define("number?",
			new Value(args -> {
				return new Value(
					Value.Type.BOOLEAN,
					new Boolean(args.get(0).type() == Value.Type.NUMBER)
				);
			}
		));
		primitives.define("round",
			new Value(args -> {
				BigDecimal a = (BigDecimal) args.get(0).value();
				return new Value(Value.Type.NUMBER, new BigDecimal(a.intValue()));
			}
		));
		globals = new Environment(primitives);
	}

	/**
	 * Tries "compiling" some code into an Expression, succeeding only if it had
	 * a valid syntax.
	 *
	 * @throws Exception if the code has syntactic errors
	 */
	public Expression parse(String code) throws Exception {
		// lex
		List<Token> lexed = tokenize("(begin " + code + ')');
		Expression parsed = null;

		// parse (and convert exceptions if its the case)
		try {
			parsed = compile(lexed);
		} catch (IllegalArgumentException parsingError) {
			throw new Exception(parsingError.getMessage());
		} catch (IndexOutOfBoundsException endOfFile) {
			throw new Exception(
				"Syntactic error: found unexpected end of file."
			);
		}

		// check whether or not all tokens were consumed, and they should be
		if (!lexed.isEmpty()) {
			Token tail = lexed.get(0);
			throw new Exception(
				"Syntactic error near line " + tail.line() + ": " +
				tail.toString() + " is an invalid start of expression."
			);
		}

		return parsed;
	}

	/**
	 * Interprets a previously generated Expression.
	 *
	 * @throws Exception if runtime errors occcur
	 */
	public void interpret(Expression expr) throws Exception {
		expr.evaluate(globals);
	}

	/**
	 * Performs lexical separation of an input string.
	 *
	 * @param code input code
	 * @return     list of tokens equivalent given code
	 */
	protected static List<Token> tokenize(String code) {
		List<Token> tokens = new LinkedList<>();

		String[] input = code.split("\n");
		for (int i = 0; i < input.length; ++i) {
			String line = input[i];
			for (int begin = 0; begin < line.length(); ++begin) {
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
					for (; end < line.length(); ++end) {
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
							} catch (NumberFormatException ignored) {
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

	/**
	 * Performs syntactic analysis over a list of tokens.
	 * The list is mutated and each token is consumed in parsing order.
	 *
	 * @param tokens list of tokens to be compiled into an expression
	 * @return       compiled expression from given tokens (if valid)
	 * @throws IllegalArgumentException  when parsing errors occur
	 * @throws IndexOutOfBoundsException in case tokens end unexpectedly
	 */
	protected static Expression compile(
		List<Token> tokens
	) throws IndexOutOfBoundsException, IllegalArgumentException {
		// check for atomic expressions
		Token head = tokens.get(0);
		switch (head.type()) {
			case NUMBER:
				Token number = consume(tokens, Token.Type.NUMBER);
				return new NumericExpression((BigDecimal) number.value());

			case VARIABLE:
				Token variable = consume(tokens, Token.Type.VARIABLE);
				return new VariableExpression((String) variable.value());

			case LPAREN:
				// not atomic
				break;

			default:
				throw new IllegalArgumentException(
					"Syntactic error near line " + head.line() + ": " +
					head.toString() + " is an invalid start of expression."
				);
		}

		// otherwise, parse some compound expression
		head = tokens.get(1);
		switch (head.type()) {
			case BEGIN: {
				consume(tokens, Token.Type.LPAREN);
				consume(tokens, Token.Type.BEGIN);
				List<Expression> sequence = compileTail(tokens);
				consume(tokens, Token.Type.RPAREN);
				try {
					return new SequentialExpression(sequence);
				} catch (Exception e) {
					throw new IllegalArgumentException(
						"Syntactic error near line " + head.line() +
						": " + e.getMessage()
					);
				}
			}

			case DEFINE: {
				consume(tokens, Token.Type.LPAREN);
				consume(tokens, Token.Type.DEFINE);
				Token name = consume(tokens, Token.Type.VARIABLE);
				Expression value = compile(tokens);
				consume(tokens, Token.Type.RPAREN);
				return new DefinitionExpression((String) name.value(), value);
			}

			case IF: {
				consume(tokens, Token.Type.LPAREN);
				consume(tokens, Token.Type.IF);
				Expression condition = compile(tokens);
				Expression consequence = compile(tokens);
				Expression alternative = null; // "else" is optional
				if (tokens.get(0).type() != Token.Type.RPAREN)
					alternative = compile(tokens);
				consume(tokens, Token.Type.RPAREN);
				return new ConditionalExpression(
					condition,
					consequence,
					alternative != null ? alternative
					                    : new VariableExpression("false")
				);
			}

			case LAMBDA: {
				consume(tokens, Token.Type.LPAREN);
				consume(tokens, Token.Type.LAMBDA);
				consume(tokens, Token.Type.LPAREN);
				List<String> params = new ArrayList<>();
				while (tokens.get(0).type() != Token.Type.RPAREN) {
					params.add(
						(String) consume(tokens, Token.Type.VARIABLE).value()
					);
				}
				consume(tokens, Token.Type.RPAREN);
				List<Expression> body = compileTail(tokens);
				consume(tokens, Token.Type.RPAREN);
				try {
					return new LambdaExpression(
						params,
						new SequentialExpression(body)
					);
				} catch (Exception e) {
					throw new IllegalArgumentException(
						"Syntactic error near line " + head.line() +
						": " + e.getMessage()
					);
				}
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
		while (tokens.get(0).type() != Token.Type.RPAREN)
			exprs.add(compile(tokens));
		return exprs;
	}

	private static Token consume(
		List<Token> tokens,
		Token.Type expected
	) throws IllegalArgumentException, IndexOutOfBoundsException {
		Token symbol = tokens.remove(0);
		if (symbol.type() == expected)
			return symbol;
		else
			throw new IllegalArgumentException(
				"Syntactic error near line " + symbol.line() + ": " +
				"expected " + expected.name() + ", found " + symbol.toString() + '.'
			);
	}

}
