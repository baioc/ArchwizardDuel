package br.ufsc.ine.archwizardduel;

import java.util.List;
import java.util.LinkedList;

class Environment {

	private List<Frame> scopes = new LinkedList<>();

	public Value define(String var, Value val) {
		return scopes.get(0).define(var, val);
	}

	public boolean contains(String name) {
		for (Frame frame : scopes) {
			if (frame.contains(name))
				return true;
		}
		return false;
	}

	public Value lookup(String symbol) {
		for (Frame frame : scopes) {
			if (frame.contains(symbol))
				return frame.lookup(symbol);
		}
		return null; // @TODO: lookup undefined variable?
	}

	public Environment enclose(Frame scope) {
		Environment inner = new Environment();
		inner.scopes.addAll(this.scopes);
		inner.scopes.add(0, scope);
		return inner;
	}

}
