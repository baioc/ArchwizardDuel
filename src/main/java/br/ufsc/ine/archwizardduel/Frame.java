package br.ufsc.ine.archwizardduel;

import java.util.Map;
import java.util.HashMap;

/**
 * A class that represents single scopes for our Lisp-like language.
 */
class Frame {

	private Map<String,Value> symbolTable = new HashMap<>();

	/**
	 * Inserts a new definition in this scope.
	 *
	 * @param var variable name
	 * @param val value to be assigned
	 * @return    the previous value associated with this variable, or null if
	 *            it did not exist.
	 */
	public Value define(String var, Value val) {
		return symbolTable.put(var, val);
	}

	/**
	 * Wheter or not this scope contains a definition for given variable.
	 *
	 * @param name variable name
	 */
	public boolean contains(String name) {
		return symbolTable.containsKey(name);
	}

	/**
	 * Looks up a variable definition in this scope's symbol table.
	 *
	 * @param symbol variable name
	 * @return       the value assigned to this variable, or null if no such a
	 *               definition exists
	 */
	public Value lookup(String symbol) {
		return symbolTable.get(symbol);
	}

}
