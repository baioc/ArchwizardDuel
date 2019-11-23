package br.ufsc.ine.archwizardduel;

import br.ufsc.inf.leobr.cliente.Jogada;

/**
 * Provides the functional interface of all Lisp-like expressions.
 */
public interface Expression extends Jogada {

	/**
	 * Evaluates the expression with respect to a given environment.
	 *
	 * @param  env environment used during evaluation
	 * @return     the value resulting from this expression's evaluation
	 * @throws Exception if at any point during evaluation a runtime error takes
	 * place; possibilities include calling a procedure with an incorrect number
	 * of arguments, arithmetic exceptions such as division by zero, using
	 * undeclared variables or applying operations to unsupported types
	 */
	public Value evaluate(Environment env) throws Exception;

}
