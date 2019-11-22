package br.ufsc.ine.archwizardduel;

import org.junit.Test;
import static org.junit.Assert.*;
import br.ufsc.ine.archwizardduel.Value.Type;
import java.util.Arrays;
import java.math.BigDecimal;

public class LispTest {

	@Test
	public void testExecution() {
		Frame primitives = new Frame();
		primitives.define("false", new Value(Type.BOOLEAN, new Boolean(false)));
		primitives.define("true", new Value(Type.BOOLEAN, new Boolean(true)));
		primitives.define("+", new Value(args -> {
			BigDecimal a = (BigDecimal) args.get(0).get();
			BigDecimal b = (BigDecimal) args.get(1).get();
			return new Value(Type.NUMBER, a.add(b));
		}));
		primitives.define("-", new Value(args -> {
			BigDecimal a = (BigDecimal) args.get(0).get();
			BigDecimal b = (BigDecimal) args.get(1).get();
			return new Value(Type.NUMBER, a.subtract(b));
		}));
		Environment environment = new Environment(primitives);

		Expression expr = new SequentialExpression(Arrays.asList(
			new DefinitionExpression("foo",
				new LambdaExpression(
					Arrays.asList(
						"pred"
					),
					new ConditionalExpression(
						new VariableExpression("pred"),
						new ApplicationExpression(
							new VariableExpression("-"),
							Arrays.asList(
								new NumericExpression(new BigDecimal("1")),
								new NumericExpression(new BigDecimal("2"))
							)
						)
					)
				)
			),
			new DefinitionExpression("bar",
				new VariableExpression("true")
			),
			new ApplicationExpression(
				new VariableExpression("foo"),
				Arrays.asList(
					new VariableExpression("bar")
				)
			)
		));

		System.out.println(expr.toString());
		System.out.println(expr.evaluate(environment).toString());
	}

	@Test
	public void testLexer() {
		String code = "(begin\n" +
		              "  (define  foo (lambda (   pred ) ; \n" +
		              "      (if pred; testing comments \n" +
		              "          (- 1 2)(+ 1 2))))\n" +
		              "  (define bar true)\n" +
		              "  (foo bar))";

		System.out.println(Interpreter.tokenize(code).toString());
	}

}
