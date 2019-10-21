package br.ufsc.ine.archwizardduel;

class Value {
    enum Type {
        INTEGER,
        CLOSURE,
    }

    Type type;
    Object datum;

    Value(Type type, Object datum) {
        this.type = type;
        this.datum = datum;
    }
}