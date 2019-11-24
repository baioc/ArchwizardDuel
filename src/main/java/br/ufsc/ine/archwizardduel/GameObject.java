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

	private Type type;
	private Object obj;

	/**
	 * Generic type constructor.
	 * Make sure its parameters match.
	 *
	 * @param type this game object's type
	 * @param obj  encapsulated Object, should match the given Type
	 */
	public GameObject(Type type, Object obj) {
		set(type, obj);
	}

	/**
	 * Assigns new values to this container. Make sure its parameters match.
	 *
	 * @param type this game object's type
	 * @param obj  encapsulated Object, should match the given Type
	 */
	public void set(Type type, Object obj) {
		this.type = type;
		this.obj = obj;
	}

	/**
	 * Gets this game object's type.
	 *
	 * @return this object's type
	 */
	public Type type() {
		return type;
	}

	/**
	 * Gets the data contained herein, should be cast according to type().
	 *
	 * @return raw object
	 */
	public Object object() {
		return obj;
	}

}
