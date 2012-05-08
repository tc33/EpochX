package org.epochx.epox;

/**
 * The <tt>VariableNode</tt> class provides a wrapper for <tt>Variable</tt>
 * objects. Each <tt>Node</tt> must only appear in one place in a tree, so to
 * allow the same variable instance to be reused in multiple places, variables
 * are not themselves nodes. Instead, a <tt>VariableNode</tt> wrapper is used,
 * where multiple instances of this wrapper may wrap the same variable object.
 * 
 * @see Variable
 */
public class VariableNode extends Node {

	private Variable variable;

	/**
	 * Constructs a new <tt>VariableNode</tt> wrapper for the given variable
	 * 
	 * @param variable the <tt>Variable</tt> object to wrap
	 */
	public VariableNode(Variable variable) {
		if (variable == null) {
			throw new IllegalArgumentException("variable cannot be null");
		}
		
		this.variable = variable;
	}

	/**
	 * Returns the <tt>Variable</tt> object that this node is a wrapper for
	 * 
	 * @return the variable
	 */
	public Variable getVariable() {
		return variable;
	}

	/**
	 * Returns the value of the variable
	 * 
	 * @return the variable's value
	 */
	@Override
	public Object evaluate() {
		return variable.getValue();
	}

	/**
	 * Returns the name of the variable
	 * 
	 * @return the name of the variable
	 */
	@Override
	public String getIdentifier() {
		return variable.getName();
	}

	/**
	 * Returns the data-type of the variable
	 * 
	 * @return the data-type of the variable's value
	 */
	@Override
	public Class<?> dataType(Class<?> ... inputTypes) {
		if (inputTypes.length != 0) {
			throw new IllegalArgumentException("variables do not have input types");
		}

		return variable.getDataType();
	}

	/**
	 * Returns a string representation of the variable
	 * 
	 * @return a string representation of the variable
	 */
	@Override
	public String toString() {
		return variable.getName();
	}

	/**
	 * Compares this <tt>VariableNode</tt> to the given object for equality. Two
	 * <tt>VariableNode</tt> objects are only considered to be equal if they
	 * refer to the same variable instance.
	 * 
	 * @return <tt>true</tt> if the given object refers to the same variable
	 *         instance as this node and <tt>false</tt> otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof VariableNode) && (((VariableNode) obj).variable == this.variable);
	}
}
