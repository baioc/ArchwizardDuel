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

		primitives.define("+", new Value((List<Value> arglist) -> {
			BigDecimal a = (BigDecimal) arglist.get(0).getDatum();
			BigDecimal b = (BigDecimal) arglist.get(1).getDatum();
			return new Value(a.add(b));
		}));

		primitives.define("-", new Value((List<Value> arglist) -> {
			BigDecimal a = (BigDecimal) arglist.get(0).getDatum();
			BigDecimal b = (BigDecimal) arglist.get(1).getDatum();
			return new Value(a.subtract(b));
		}));

		Environment environment = new Environment(primitives);
		Expression expr = new Expression(
			new ArrayList<>(Arrays.asList(
				new Expression("if"),
				new Expression("false"),
				new Expression("1"),
				new Expression("0")
				// new Expression(
				// 	new ArrayList<>(Arrays.asList(
				// 		new Expression("+"),
				// 		new Expression("1"),
				// 		new Expression("2")
				// 	))
				// ),
				// new Expression(
				// 	new ArrayList<>(Arrays.asList(
				// 		new Expression("-"),
				// 		new Expression("2"),
				// 		new Expression("1")
				// 	))
				// )
			))
		);
		System.out.println(expr.toString());
		Value result = expr.evaluate(environment);
		System.out.println(result.getDatum().toString());
	}

}
