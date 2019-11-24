package br.ufsc.ine.archwizardduel;

/**
 * Represents the union of drawable game objects.
 */
class GameObject {

	/**
	 * Possible object types.
	 */
	public enum Type {
		EMPTY,
		WIZARD,
		FIREBALL,
		ROCK,
	}

	private final Type type;
	private final Object obj;

	/**
	 * Generic type constructor.
	 * Make sure its parameters match.
	 *
	 * @param type this game object's type
	 * @param obj  encapsulated Object, should match the given Type
	 */
	public GameObject(Type type, Object obj) {
		this.type = type;
		this.obj = obj;
	}

	/**
	 * @return this object's type
	 */
	public Type type() {
		return type;
	}

	/**
	 * Actual data contained in this object, should be cast according to type().
	 */
	public Object object() {
		return obj;
	}

}
