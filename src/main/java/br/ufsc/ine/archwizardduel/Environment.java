package br.ufsc.ine.archwizardduel;

import java.util.List;
import java.util.LinkedList;
import java.lang.IllegalArgumentException;

class Environment {

	private List<Frame> scopes;

	public Environment(Frame primitives) {
		scopes = new LinkedList<>();
		scopes.add(primitives);
	}

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

	public Value lookup(String symbol) throws IllegalArgumentException {
		for (Frame frame : scopes) {
			if (frame.contains(symbol))
				return frame.lookup(symbol);
		}
		throw new IllegalArgumentException("Undefined variable " + symbol + ".");
	}

	public Environment enclose(Frame scope) {
		Environment inner = new Environment(scope);
		inner.scopes.addAll(this.scopes);
		return inner;
	}

}
