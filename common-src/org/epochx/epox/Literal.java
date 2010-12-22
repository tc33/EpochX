package org.epochx.epox;

import org.apache.commons.lang.ObjectUtils;

/**
 * Literal values are nodes with a specific value. Generally, the value of a 
 * literal should not change. The difference between a <code>Literal</code> node
 * and a <code>Variable</code> node is subtle. <code>Variable</code> instances 
 * cannot be copied, instead, all references to the same variable should refer
 * to the same <code>Variable</code> instance. In contrast, <code>Literal</code>
 * instances may be cloned and behave like most other node types.
 */
public class Literal extends Node {

	// The value of this literal.
	private Object value;

	/**
	 * Construct a new <code>Literal</code> node with the given value. 
	 * Evaluation of this literal will return the value set here. The data type
	 * of this node will be determined by the type of the object specified here.
	 * 
	 * @param value the value of this literal.
	 */
	public Literal(final Object value) {
		this.value = value;
	}

	/**
	 * Returns the value of this literal.
	 * 
	 * @return this literal's value
	 */
	@Override
	public Object evaluate() {
		return value;
	}

	/**
	 * Returns this node's string representation, which is the string 
	 * representation of the literal value.
	 * @return a String representation.
	 */
	@Override
	public String getIdentifier() {
		return toString();
	}
	
	/**
	 * Returns the data-type of this node. The data-type of a literal node
	 * is the type of its value.
	 * 
	 * @return the data-type of this node.
	 */
	@Override
	public Class<?> getReturnType() {
		return value.getClass();
	}
	
	/**
	 * Sets the value of this literal value. Implementations of this class may
	 * wish to use this method to delay the setting of a literal's value.
	 * @param value the value to set for this literal.
	 */
	protected void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Returns this node's string representation, which is the string 
	 * representation of the literal value.
	 * @return a String representation.
	 */
	@Override
	public String toString() {
		return value.toString();
	}

	/**
	 * An object is equal to this literal if it is an instance of Literal and 
	 * its value is equal to this literal's value.
	 * 
	 * @return true if the two objects are equal, false otherwise.
	 */
	@Override
	public boolean equals(final Object obj) {
		final boolean inst = (obj instanceof Literal);

		if (!inst) {
			return false;
		}

		final Object objVal = ((Literal) obj).value;
		final Object thisVal = value;		

		return ObjectUtils.equals(objVal, thisVal);
	}

	/**
	 * Creates a new Literal instance which is a copy of this instance. The 
	 * clone will contain a value which is a reference to the same object as 
	 * this literal's value. For mutable object values, users should consider 
	 * extending this class to provide a clone method which can provide an 
	 * appropriate deep clone.
	 * 
	 * @return a new Literal instance which is a clone of this one.
	 */
	@Override
	public Literal clone() {
		final Literal clone = (Literal) super.clone();

		clone.value = value;

		return clone;
	}
	
	/**
	 * Constructs a new instance of this literal. This implementation simply 
	 * returns a clone of this instance.
	 * 
	 * @return a new Literal instance which is a clone of this one.
	 */
	@Override
	public Literal newInstance() {
		return clone();
	}
}
