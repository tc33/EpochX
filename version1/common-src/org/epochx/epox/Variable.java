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
	private final Class<?> datatype;

	/**
	 * Constructs a new variable with a <code>null</code> value. The variable's
	 * name and data-type must be provided. The given identifier and data-type 
	 * must be non-null.
	 * 
	 * @param identifier the name of the variable.
	 * @param datatype the data-type that all values will be assignable to.
	 */
	public Variable(final String identifier, final Class<?> datatype) {
		if (identifier == null || datatype == null) {
			throw new IllegalArgumentException("identifier and data-type must be non-null");
		}
		
		this.identifier = identifier;
		this.datatype = datatype;
	}

	/**
	 * Constructs a new variable with the given value. The variable's name is
	 * provided but the data-type is determined by the type of the given value.
	 * The given identifier and value must be non-null. If the value is unknown
	 * then use the alternative constructor and provide the data-type. If the 
	 * value really should be null, then use the other constructor, and then 
	 * call the <code>setValue(Object)</code> method which will allow the null
	 * value once the data-type has been set.
	 * 
	 * @param identifier the name of the variable.
	 * @param value the starting value of the variable.
	 */
	public Variable(final String identifier, final Object value) {
		if (identifier == null || value == null) {
			throw new IllegalArgumentException("identifier and value must be non-null");
		}
		
		this.identifier = identifier;
		this.value = value;
		datatype = value.getClass();
	}

	/**
	 * Sets the value of the variable. The data-type of a Variable cannot be
	 * changed after construction, and only values which are instances of
	 * subclasses of the original data-type may be used. A <code>null</code> 
	 * value is considered valid for a variable of any data-type.
	 * 
	 * @param value the value to set for the variable.
	 */
	public void setValue(final Object value) {
		if (value != null && !datatype.isAssignableFrom(value.getClass())) {
			throw new IllegalArgumentException("Variables may not change data-type");
		}

		this.value = value;
	}

	/**
	 * Returns this variable's value.
	 * 
	 * @return this variable's value.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Returns the value of this variable.
	 * 
	 * @return this variable's value.
	 */
	@Override
	public Object evaluate() {
		return value;
	}

	/**
	 * Returns the name of this variable.
	 * 
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
	public Class<?> getReturnType(final Class<?> ... inputTypes) {
		// No inputs should be provided for a terminal.
		if (inputTypes.length != 0) {
			throw new IllegalArgumentException("variables have no input types");
		}

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
	 *         object, and false otherwise.
	 */
	@Override
	public boolean equals(final Object obj) {
		return (obj == this);
	}

	/**
	 * Variable objects cannot be cloned. Calls to this method will return a
	 * reference to this same instance. That is:
	 * 
	 * <pre>
	 * x.clone() == x
	 * 
	 * <pre>
	 * 
	 * will be true.
	 * @return a reference to this variable instance.
	 */
	@Override
	public Variable clone() {
		return this;
	}

	/**
	 * It is not possible to construct a new instance from this variable.
	 * Calling this method will return a reference to this same instance.
	 * 
	 * @return a reference to this variable instance.
	 */
	@Override
	public Variable newInstance() {
		return clone();
	}
}
