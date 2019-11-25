package br.ufsc.ine.archwizardduel;

/**
 * Class representing the actual wizard characters in the game.
 */
class Wizard {

	private final String name;
	private int health;
	private int mana;
	private int[] position;
	private Direction rotation;

	public static enum Direction { UP, LEFT, DOWN, RIGHT }

	/**
	 * Summons a Wizard with the given parameters.
	 *
	 * @param name sorcerer name
	 * @param x    x initial position
	 * @param y    y initial position
	 */
	public Wizard(String name) {
		this.name = name;
		this.health = 100;
		this.mana = 100;
		position = new int[]{0, 0};
		rotation = Direction.DOWN;
	}

	/**
	 * Gets the name assigned to this Wizard.
	 */
	public String name() {
		return name;
	}

	/**
	 * Whether or not this Wizard is dead.
	 *
	 * @return true when he is, false otherwise
	 */
	public boolean isDead() {
		return health <= 0;
	}

	/**
	 * Gets Wizard's current health resource.
	 */
	public int health() {
		return health;
	}

	/**
	 * This Wizard takes damage, losing some life.
	 *
	 * @param damage damage to be taken
	 */
	public void damage(int damage) {
		health = Math.max(0, health - damage);
	}

	/**
	 * Gets Wizard's current mana resource.
	 */
	public int mana() {
		return mana;
	}

	/**
	 * Spends some of the magic resources.
	 *
	 * @param mp mana spent
	 */
	public void spend(int mp) {
		mana = Math.max(0, mana - mp);
	}

	/**
	 * Regenerates all of this Wizard's mana.
	 */
	public void restore() {
		mana = 100;
	}

	/**
	 * Gets the Wizard's current position vector.
	 *
	 * @return an int array containing coordinates {x, y}
	 */
	public int[] position() {
		return position;
	}

	/**
	 * Moves the Wizard to another position.
	 *
	 * @param x x position
	 * @param y y position
	 */
	public void move(int x, int y) {
		position[0] = x;
		position[1] = y;
	}

	/**
	 * Gets the Wizard's current facing direction.
	 *
	 * @return Direction enum.
	 */
	public Direction rotation() {
		return rotation;
	}

	/**
	 * Rotates the Wizard so it faces the given direction.
	 */
	public void rotate(Direction dir) {
		rotation = dir;
	}

}
