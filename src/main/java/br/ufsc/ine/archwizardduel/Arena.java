package br.ufsc.ine.archwizardduel;

import java.util.concurrent.ThreadLocalRandom;

class Arena {
    private Player[] participants = new Player[2];
    private Wizard[] characters = new Wizard[2];
    private final Interpreter executor = new Interpreter();
    private int currentTurn;

    Arena(Player[] participants) {
        this.participants = participants;
        characters[0] = new Wizard(participants[0].getName());
        characters[1] = new Wizard(participants[1].getName());
        currentTurn = coinflip();
    }

    private int coinflip() {
        return ThreadLocalRandom.current().nextInt(0, 2);
    }

    public boolean myTurn() {
        return currentTurn == 0;
    }
}