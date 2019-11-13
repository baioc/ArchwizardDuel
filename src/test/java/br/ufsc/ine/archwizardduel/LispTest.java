package br.ufsc.ine.archwizardduel;

import org.junit.Test;
import static org.junit.Assert.*;
import br.ufsc.ine.archwizardduel.Value.Type;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class LispTest {

	@Test
	public void testLisp() {
		Frame primitives = new Frame();

		primitives.define("+", new Value(args -> {
			BigDecimal a = (BigDecimal) args.get(0).getDatum();
			BigDecimal b = (BigDecimal) args.get(1).getDatum();
			return new Value(a.add(b));
		}));

		primitives.define("-", new Value(args -> {
			BigDecimal a = (BigDecimal) args.get(0).getDatum();
			BigDecimal b = (BigDecimal) args.get(1).getDatum();
			return new Value(a.subtract(b));
		}));

		Environment environment = new Environment(primitives);
		Expression expr = new Expression(
			new Expression("begin"),

			new Expression(
				new Expression("define"),
				new Expression("foo"),
				new Expression(
					new Expression("lambda"),
					new Expression(
						new Expression("pred")
					),
					new Expression(
						new Expression("if"),
						new Expression("pred"),
						new Expression(
							new Expression("+"),
							new Expression("1"),
							new Expression("2")
						),
						new Expression(
							new Expression("-"),
							new Expression("1"),
							new Expression("2")
						)
					)
				)
			),

			new Expression(
				new Expression("define"),
				new Expression("bar"),
				new Expression("false")
			),

			new Expression(
				new Expression("foo"),
				new Expression("bar")
			)
		);

		System.out.println(expr.toString());
		System.out.println(expr.evaluate(environment).getDatum().toString());
	}

}
