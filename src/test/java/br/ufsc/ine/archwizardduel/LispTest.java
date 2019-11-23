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

		String code = "(begin\n" +
		              "  (define  foo (lambda (   pred ) ; \n" +
		              "      (if pred; testing comments \n" +
		              "          (+ 1 2)(- 1 2))))\n" +
		              "  (define bar false)\n" +
		              "  (foo bar))";

		Expression compiled = Interpreter.buildExpression(Interpreter.tokenize(code));
		System.out.println(compiled.toString());
		System.out.println(compiled.evaluate(environment).toString());
	}

}
