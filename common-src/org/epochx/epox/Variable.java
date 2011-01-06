package org.epochx.epox;

/**
 * Variables are nodes with a changeable value. The difference between a 
 * <code>Literal</code> node and a <code>Variable</code> node is subtle. 
 * <code>Variable</code> instances cannot be copied, instead, all references to 
 * the same variable should refer to the same <code>Variable</code> instance. In
 * contrast, <code>Literal</code> instances may be cloned and behave like most 
 * other node types.
 * 
 * @see Literal
 */
public class Variable extends Node {

	// The name of the variable.
	private final String identifier;

	// The variables value.
	private Object value;

	// The value's data-type.
	private Class<?> datatype;
	
	/**
	 * Constructs a new variable with a <code>null</code> value. The variable's
	 * name and data-type must be provided.
	 * 
	 * @param identifier
	 * @param datatype
	 */
	public Variable(final String identifier, Class<?> datatype) {
		this.identifier = identifier;
		this.datatype = datatype;
	}

	/**
	 * Constructs a new variable with the given value. The variable's name is 
	 * provided but the data-type is determined by the type of the given value.
	 * 
	 * @param identifier the name of the variable.
	 * @param value the starting value of the variable.
	 */
	public Variable(final String identifier, final Object value) {	
		this.identifier = identifier;
		this.value = value;
		this.datatype = value.getClass();
	}

	/**
	 * Sets the value of the variable. The data-type of a Variable cannot be 
	 * changed after construction, and only values which are instances of 
	 * subclasses of the original data-type may be used.
	 * @param value the value to set for the variable.
	 */
	public void setValue(final Object value) {
		if (!datatype.isAssignableFrom(value.getClass())) {
			throw new IllegalArgumentException("Variables may not change data-type");
		}		
		
		this.value = value;
	}
	
	/**
	 * Returns this variable's value.
	 * @return this variable's value.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Returns the value of this variable.
	 * @return this variable's value.
	 */
	@Override
	public Object evaluate() {
		return value;
	}
	
	/**
	 * Returns the name of this variable.
	 * @return the name of this variable.
	 */
	@Override
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Returns the data-type of this variable, as set upon construction. This
	 * variable is guaranteed to evaluate to a value of this given data-type.
	 * 
	 * @return the data-type of this variable's value.
	 */
	@Override
	public Class<?> getReturnType() {
		return datatype;
	}

	/**
	 * Returns a String representation of this variable.
	 * 
	 * @return a String representation of this variable.
	 */
	@Override
	public String toString() {
		return identifier;
	}

	/**
	 * Compares this variable to the given object for equality. Two variables 
	 * are only considered to be equal if they refer to the same variable 
	 * instance.
	 * 
	 * @return true if the given object refers to the same instance as this 
	 * object, and false otherwise.
	 */
	@Override
	public boolean equals(final Object obj) {
		return (obj == this);
	}

	/**
	 * Variable objects cannot be cloned. Calls to this method will return a 
	 * reference to this same instance. 
	 * @return a reference to this variable instance.
	 */
	@Override
	public Variable clone() {
		return this;
	}
	
	/**
	 * It is not possible to construct a new instance from this variable. 
	 * Calling this method will return a reference to this same instance.
	 * @return a reference to this variable instance.
	 */
	@Override
	public Variable newInstance() {
		return clone();
	}
}
