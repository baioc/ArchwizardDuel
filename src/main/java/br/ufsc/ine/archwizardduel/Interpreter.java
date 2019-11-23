package br.ufsc.ine.archwizardduel;

import br.ufsc.ine.archwizardduel.Token.Type;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.math.BigDecimal;

class Interpreter { // @TODO

	private Environment globals;

	public Interpreter() {
		globals = new Environment(new Frame());
	}

	public Expression parse(String code) {
		return buildExpression(tokenize("(begin " + code + ")"));
	}

	public void interpret(Expression expr) {
		expr.evaluate(globals);
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
					tokens.add(new Token(Token.Type.LPAREN, "(", i));
				} else if (ch == ')') {
					tokens.add(new Token(Token.Type.RPAREN, ")", i));
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
							tokens.add(new Token(Token.Type.BEGIN, word, i));
							break;
						case "if":
							tokens.add(new Token(Token.Type.IF, word, i));
							break;
						case "define":
							tokens.add(new Token(Token.Type.DEFINE, word, i));
							break;
						case "lambda":
							tokens.add(new Token(Token.Type.LAMBDA, word, i));
							break;
						default:
							try {
								BigDecimal number = new BigDecimal(word);
								tokens.add(new Token(Token.Type.NUMBER, number, i));
							} catch (NumberFormatException _) {
								tokens.add(new Token(Token.Type.VARIABLE, word, i));
							}
							break;
					}

					begin = end - 1;
				}
			}
		}

		return tokens;
	}

	protected static Expression buildExpression(List<Token> tokens) {
		// check for atomic expressions
		Token first = tokens.get(0);
		switch (first.getType()) {
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
					"Parse error at line " + first.getLine() + ": " +
					first.toString() + "is an invalid start of expression."
				);
		}

		// otherwise, parse some compound expression
		switch (tokens.get(1).getType()) { // @TODO: what if got EOF? in get or remove
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
				return new DefinitionExpression(
					(String) symbol.getValue(),
					definition
				);
			}
			case IF: {
				consume(tokens, Token.Type.LPAREN);
				consume(tokens, Token.Type.IF);
				Expression condition = buildExpression(tokens);
				Expression consequence = buildExpression(tokens);
				Expression alternative = buildExpression(tokens);
				consume(tokens, Token.Type.RPAREN);
				return new ConditionalExpression(
					condition,
					consequence,
					alternative
				);
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
				List<Expression> body = buildExpressionList(tokens);
				consume(tokens, Token.Type.RPAREN);
				return new LambdaExpression(
					params,
					new SequentialExpression(body)
				);
			}
			default: { // case APPLICATION:
				consume(tokens, Token.Type.LPAREN);
				Expression operator = buildExpression(tokens);
				List<Expression> operands = buildExpressionList(tokens);
				consume(tokens, Token.Type.RPAREN);
				return new ApplicationExpression(operator, operands);
			}
		}
	}

	protected static List<Expression> buildExpressionList(List<Token> tokens) {
		List<Expression> exprs = new ArrayList<>();
		while (tokens.get(0).getType() != Token.Type.RPAREN)
			exprs.add(buildExpression(tokens));
		return exprs;
	}

	protected static Token consume(List<Token> tokens, Token.Type expected) {
		Token symbol = tokens.remove(0);
		if (symbol.getType() == expected)
			return symbol;
		else
			throw new IllegalArgumentException(
				"Parse error at line " + symbol.getLine() + ": " +
				"expected " + expected.name() + ", found " + symbol.toString() + "."
			);
	}

}
