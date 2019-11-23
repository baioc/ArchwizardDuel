package br.ufsc.ine.archwizardduel;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
import br.ufsc.ine.archwizardduel.Interpreter.ParseException;

public class LispTest {

	@Test
	public void testExecution() {
		Interpreter evaluator = new Interpreter();

		String code = "(define  foo (lambda (   pred ) ; \n" +
		              "  (if pred; testing comments \n" +
		              "      (+ 1 2)\n   " +
		              "      (- 1 2))))\n" +
		              "\n" +
		              "(define bar false)\n" +
		              "\n" +
		              "(foo bar)";

		Expression compiled = null;
		try {
			compiled = evaluator.parse(code);
		} catch (ParseException e) {
			assumeNoException(e);
		}

		assertEquals(
			"(begin " +
			  "(define foo (lambda (pred) (begin " +
			    "(if pred (+ 1 2) (- 1 2))))) " +
			  "(define bar false) " +
			  "(foo bar))",
			compiled.toString()
		);

		assertEquals("-1", evaluator.interpret(compiled));
	}

	@Test
	public void testMissingParenthesis() {
		Interpreter evaluator = new Interpreter();

		String code = "(if +4.5e-9 (+ 1 2) (- 1 2)"; // closing parenthesis missing

		ParseException ex = null;
		try {
			Expression compiled = evaluator.parse(code);
		} catch (ParseException e) {
			ex = e;
		}

		assertNotNull(ex);
	}

	@Test
	public void testExtraParenthesis() {
		Interpreter evaluator = new Interpreter();

		String code = ") \n(if +4.5e-9 (+ 1 2) (- 1 2))"; // extra parenthesis added

		ParseException ex = null;
		try {
			Expression compiled = evaluator.parse(code);
		} catch (ParseException e) {
			ex = e;
		}

		assertNotNull(ex);
	}

	@Test
	public void testKeywords() {
		Interpreter evaluator = new Interpreter();

		String code = "(if begin (define lambda))";

		ParseException ex = null;
		try {
			Expression compiled = evaluator.parse(code);
		} catch (ParseException e) {
			ex = e;
		}

		assertNotNull(ex);
	}

}
