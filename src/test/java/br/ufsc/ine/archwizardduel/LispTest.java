package br.ufsc.ine.archwizardduel;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

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
		} catch (Exception e) {
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

		try {
			evaluator.interpret(compiled);
		} catch (Exception e) {
			assumeNoException(e);
		}
	}

	@Test
	public void testInvalidSyntax() {
		Interpreter evaluator = new Interpreter();

		{ // closing parenthesis missing
			String code = "(if +4.5e-9 (+ 1 2) (- 1 2)";
			Exception ex = null;
			try {
				evaluator.parse(code);
			} catch (Exception e) {
				ex = e;
			}
			assertNotNull(ex);
			// System.err.println(ex.getMessage());
		}

		{ // extra parenthesis added
			String code = ") \n(if +4.5e-9 (+ 1 2) (- 1 2))";
			Exception ex = null;
			try {
				evaluator.parse(code);
			} catch (Exception e) {
				ex = e;
			}
			assertNotNull(ex);
			// System.err.println(ex.getMessage());
		}

		{ // usage of reserved words
			String code = "(if begin (define lambda))";
			Exception ex = null;
			try {
				evaluator.parse(code);
			} catch (Exception e) {
				ex = e;
			}
			assertNotNull(ex);
			// System.err.println(ex.getMessage());
		}
	}

	@Test
	public void testRuntimeError() {
		Interpreter evaluator = new Interpreter(new Frame());

		{ // operation over unexpected types
			String code = "(+ 1 false)";
			Expression compiled = null;
			try {
				compiled = evaluator.parse(code);
			} catch (Exception e) {
				assumeNoException(e);
			}
			assertEquals("(begin (+ 1 false))", compiled.toString());
			Exception ex = null;
			try {
				evaluator.interpret(compiled);
			} catch (Exception e) {
				ex = e;
			}
			assertNotNull(ex);
			// System.err.println(ex.getMessage());
		}

		{ // undefined variable
			String code = "(* x x)";
			Expression compiled = null;
			try {
				compiled = evaluator.parse(code);
			} catch (Exception e) {
				assumeNoException(e);
			}
			assertEquals("(begin (* x x))", compiled.toString());
			Exception ex = null;
			try {
				evaluator.interpret(compiled);
			} catch (Exception e) {
				ex = e;
			}
			assertNotNull(ex);
			// System.err.println(ex.getMessage());
		}

		{ // invalid number of arguments
			String code = "(define abs (lambda (x) (if (>= x 0) x (* -1 x))))\n" +
			              "(abs)";
			Expression compiled = null;
			try {
				compiled = evaluator.parse(code);
			} catch (Exception e) {
				assumeNoException(e);
			}
			assertEquals(
				"(begin (define abs (lambda (x) (begin (if (>= x 0) x (* -1 x))))) (abs))",
				compiled.toString()
			);
			Exception ex = null;
			try {
				evaluator.interpret(compiled);
			} catch (Exception e) {
				ex = e;
			}
			assertNotNull(ex);
			// System.err.println(ex.getMessage());
		}
	}

}
