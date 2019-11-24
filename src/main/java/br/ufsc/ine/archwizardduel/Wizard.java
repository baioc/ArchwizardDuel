package br.ufsc.ine.archwizardduel;

/**
 * Class representing the actual wizard characters in the game.
 */
class Wizard {

	private final String name;
	private int health;
	private int mana;
	private double[] position;

	/**
	 * Summons a Wizard with the given parameters.
	 *
	 * @param name sorcerer name
	 * @param x    x initial position
	 * @param y    y initial position
	 */
	public Wizard(String name, double x, double y) {
		this.name = name;
		this.health = 100;
		this.mana = 100;
		position = new double[]{x, y};
	}

	/**
	 * Gets the name assigned to this Wizard.
	 */
	public String name() {
		return name;
	}

	/**
	 * Gets Wizard's current health resource.
	 */
	public int health() {
		return health;
	}

	/**
	 * Gets Wizard's current mana resource.
	 */
	public int mana() {
		return mana;
	}

	/**
	 * Gets the Wizard's current position as a normalized vector.
	 *
	 * @return a double array containing coordinates {x, y}
	 */
	public double[] position() {
		return position;
	}

	/**
	 * Moves the Wizard to a normalized position.
	 *
	 * @param x x position
	 * @param y y position
	 */
	public void move(double x, double y) {
		position[0] = x;
		position[1] = y;
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
	 * Whether or not this Wizard is dead.
	 *
	 * @return true when he is, false otherwise
	 */
	public boolean isDead() {
		return health <= 0;
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
	 * Regenerates some mana.
	 *
	 * @param mp mana points to be restored
	 */
	public void restore(int mp) {
		mana = Math.min(100, mana + mp);
	}

}
