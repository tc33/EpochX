package org.epochx.epox;

import org.apache.commons.lang.ObjectUtils;

/**
 * Literal values are nodes with a specific value. Normally, the value of a
 * literal would not change. The difference between a <tt>Literal</tt> node
 * and a <tt>Variable</tt> node is subtle, but important. The same
 * <tt>Variable</tt> instance is likely to appear multiple times in a program
 * tree, so changing the value of one occurence changes all occurences of that
 * variable. But, like most other nodes, each literal instance should be
 * independent of the others and appear only once within a program tree. To
 * support these differences, literals and variables handle cloning and creating
 * new instances differently.
 * 
 * @see Variable
 */
public class Literal extends Node {

	private Object value;

	/**
	 * Constructs a new <tt>Literal</tt> node with the given value. Evaluation
	 * of this literal will return the value set here. The data-type of this
	 * node will be determined by the type of the object specified here.
	 * 
	 * @param value the value of this literal
	 */
	public Literal(Object value) {
		this.value = value;
	}

	/**
	 * Returns the value of this literal. Has the same functionality as calling
	 * <tt>getValue</tt>.
	 * 
	 * @return the value of this literal
	 */
	@Override
	public Object evaluate() {
		return value;
	}

	/**
	 * Returns this node's string representation, which is the string
	 * representation of the literal value
	 * 
	 * @return a <tt>String</tt> representation
	 */
	@Override
	public String getIdentifier() {
		return toString();
	}

	/**
	 * Returns the data-type of this node. The data-type of a literal node
	 * is the data-type of its value. If no value has been set then
	 * <tt>null</tt> will be returned.
	 * 
	 * @param inputTypes the data-types of the node's inputs. Literals
	 *        take no inputs so this should be an empty array.
	 * @return the data-type of this node
	 * @throws IllegalArgumentException if <tt>argumentDataTypes</tt> is
	 *         anything other than an empty array
	 */
	@Override
	public Class<?> dataType(Class<?> ... inputTypes) {
		if (inputTypes.length != 0) {
			throw new IllegalArgumentException("literals have no input types");
		}

		if (value != null) {
			return value.getClass();
		} else {
			return null;
		}
	}

	/**
	 * Sets the value of this literal. Implementations of this class may
	 * wish to use this method to delay the setting of a literal's value.
	 * 
	 * @param value the value to set for this literal
	 */
	protected void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Returns the current value of this literal
	 * 
	 * @return the value of this literal
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Returns this node's string representation, which is the string
	 * representation of the literal value. If no value is set then an empty
	 * string will be returned.
	 * 
	 * @return a <tt>String</tt> representation.
	 */
	@Override
	public String toString() {
		if (value != null) {
			return value.toString();
		} else {
			return "";
		}
	}

	/**
	 * An object is equal to this literal if it is an instance of
	 * <tt>Literal</tt> and its value is equal to this literal's value.
	 * 
	 * @return <tt>true</tt> if the two objects are equal, <tt>false</tt>
	 *         otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Literal)) {
			return false;
		}

		Object objVal = ((Literal) obj).value;
		Object thisVal = value;

		return ObjectUtils.equals(objVal, thisVal);
	}

	/**
	 * Creates a new <tt>Literal</tt> instance which is a copy of this instance.
	 * The clone will contain a value which is a reference to the same object as
	 * this literal's value. For mutable object values, users should consider
	 * extending this class to provide a clone method which can provide an
	 * appropriate deep clone.
	 * 
	 * @return a new <tt>Literal</tt> instance which is a clone of this object
	 */
	@Override
	public Literal clone() {
		final Literal clone = (Literal) super.clone();

		clone.value = value;

		return clone;
	}

	/**
	 * Constructs a new instance of this literal. This implementation is 
	 * equivalent to calling <tt>clone()</tt>.
	 * 
	 * @return a new <tt>Literal</tt> instance which is a clone of this object
	 */
	@Override
	public Literal newInstance() {
		return clone();
	}
}
