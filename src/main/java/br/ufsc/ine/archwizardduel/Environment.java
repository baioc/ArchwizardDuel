package br.ufsc.ine.archwizardduel;

import java.util.List;

class Environment { // @TODO

	private List<Frame> scopes;

	public Value define(String var, Value val) {
		return null;
	}

	public boolean contains(String name) {
		return false;
	}

	public Value lookup(String symbol) {
		return null;
	}

	public Environment enclose(Frame scope) {
		return null;
	}

}
