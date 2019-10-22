package br.ufsc.ine.archwizardduel;

class Arena {
    private Player[] participants = new Player[2];
    private Wizard[] characters = new Wizard[2];
    private final Interpreter executor = new Interpreter();
    private int currentTurn;

    Arena(Player[] participants, int goesFirst) {
        this.participants = participants;
        characters[0] = new Wizard(participants[0].getName());
        characters[1] = new Wizard(participants[1].getName());
        currentTurn = goesFirst;
    }

    public boolean myTurn() {
        return currentTurn == 0; // localhost turn.
    }

    public void nextTurn() {
        currentTurn = (currentTurn + 1) % 2;
    }
}