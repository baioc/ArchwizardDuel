package br.ufsc.ine.archwizardduel;

import br.ufsc.ine.archwizardduel.Wizard.Direction;

/**
 * Class representing the spells in the game.
 */
class Fireball {

	private final Wizard caster;
	private final Direction rotation;
	private int[] position;

	/**
	 * Summons a Fireball with the given parameters.
	 *
	 * @param caster who cast this spell
	 * @param x      initial position
	 * @param y      initial position
	 * @param dir    cast direction
	 */
	public Fireball(Wizard caster, int x, int y, Direction dir) {
		this.caster = caster;
		position = new int[]{x, y};
		rotation = dir;
	}

	/**
	 * Gets the Wizard who cast this Fireball.
	 */
	public Wizard caster() {
		return caster;
	}

	/**
	 * Gets the Fireball's current position vector.
	 *
	 * @return an int array containing coordinates {x, y}
	 */
	public int[] position() {
		return position;
	}

	/**
	 * Moves the Fireball to the next position, depending on its direction.
	 */
	public void move() {
		switch (rotation) {
			case UP:
				position[1] -= 1;
				break;

			case LEFT:
				position[0] -= 1;
				break;

			case DOWN:
				position[1] += 1;
				break;

			case RIGHT:
				position[0] += 1;
				break;
		}
	}

}
