package br.ufsc.ine.archwizardduel;

import java.util.Map;
import java.util.HashMap;

class Frame {

	private Map<String,Value> symbolTable = new HashMap<>();

	public Value define(String var, Value val) {
		return symbolTable.put(var, val);
	}

	public boolean contains(String name) {
		return symbolTable.containsKey(name);
	}

	public Value lookup(String symbol) {
		return symbolTable.get(symbol);
	}

}
