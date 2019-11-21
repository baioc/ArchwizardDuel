package br.ufsc.ine.archwizardduel;

import org.junit.Test;
import static org.junit.Assert.*;
import br.ufsc.ine.archwizardduel.Value.Type;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.function.Function;
import java.util.List;

public class LispTest {

	@Test
	public void testLisp() {
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

		/* The following expression is equivalent to:
		(begin
		  (define foo (lambda (pred)
		    (if pred (- 1 2) false)))
		  (define bar true)
		  (foo bar))*/
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

}
