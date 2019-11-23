package br.ufsc.ine.archwizardduel;

import java.util.LinkedList;

/**
 * Evaluation environment for language expressions.
 */
class Environment {

	private LinkedList<Frame> scopes;

	/**
	 * Builds an empty environment.
	 */
	public Environment() {
		scopes = new LinkedList<>();
	}

	/**
	 * Constructs an environment with some base frame.
	 *
	 * @param primitives scope containing global definitions
	 */
	public Environment(Frame primitives) {
		this();
		scopes.add(primitives);
	}

	/**
	 * Builds a new environment using this a base and adding an extra frame.
	 *
	 * @param scope new scope to be added
	 * @return      a new Environment that shares definitions with this one and
	 *              encloses the new scope as well
	 */
	public Environment enclose(Frame scope) {
		Environment inner = new Environment(scope);
		inner.scopes.addAll(this.scopes);
		return inner;
	}

	/**
	 * Inserts a new definition in the innermost frame of this environment.
	 *
	 * @param var variable name
	 * @param val value to be assigned
	 * @return    the previous value associated with this variable, or null if
	 *            it did not exist.
	 */
	public Value define(String var, Value val) {
		return scopes.get(0).define(var, val);
	}

	/**
	 * Wheter or not this environment contains a definition for given variable.
	 *
	 * @param name variable name
	 */
	public boolean contains(String name) {
		for (Frame frame : scopes) {
			if (frame.contains(name))
				return true;
		}
		return false;
	}

	/**
	 * Finds a variable definition in the evaluation environment.
	 *
	 * @param symbol variable name
	 * @return       the first definition found for this variable, starting
	 *               by the innermost scopes
	 * @throws Exception if this environment contains no definition for symbol
	 */
	public Value lookup(String symbol) throws Exception {
		for (Frame frame : scopes) {
			if (frame.contains(symbol))
				return frame.lookup(symbol);
		}
		throw new Exception(
			"Evaluation error: variable " + symbol + " is undefined."
		);
	}

}
