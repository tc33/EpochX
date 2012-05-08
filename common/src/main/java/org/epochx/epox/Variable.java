package org.epochx.epox;

/**
 * Instances of <tt>Variable</tt> model named values for use in a program tree.
 * Variables are <b>not</b> nodes, so they must be wrapped in a
 * <tt>VariableNode</tt> to be used in a program. The data-type of a variable is
 * determined at construction and must not then be changed.
 * 
 * @see VariableNode
 */
public class Variable {

	private final Class<?> datatype;
	private final String name;

	private Object value;

	/**
	 * Constructs a new variable with a <tt>null</tt> value. The variable's
	 * name and data-type must be provided. The given <tt>name</tt> and
	 * <tt>datatype</tt> must be non-<tt>null</tt>.
	 * 
	 * @param name the name of the variable
	 * @param datatype the widest data-type of the values to be assigned to this
	 *        variable
	 */
	public Variable(String name, Class<?> datatype) {
		if (name == null || datatype == null) {
			throw new IllegalArgumentException("identifier and data-type must be non-null");
		}

		this.name = name;
		this.datatype = datatype;
	}

	/**
	 * Constructs a new variable with the given value. The variable's name is
	 * provided but the data-type is determined by the type of the given value.
	 * The given <tt>name</tt> and <tt>value</tt> must be non-<tt>null</tt>. If
	 * the value is unknown then use the alternative constructor to provide the
	 * data-type instead of a value.
	 * 
	 * @param name a name for the variable
	 * @param value the initial value of the variable
	 */
	public Variable(String name, Object value) {
		if (name == null || value == null) {
			throw new IllegalArgumentException("identifier and value must be non-null");
		}

		this.name = name;
		this.value = value;
		datatype = value.getClass();
	}

	/**
	 * Sets the value of this variable. The data-type of a variable cannot be
	 * changed after construction, and only values which are instances of
	 * subclasses of the original data-type may be used. A <tt>null</tt> value
	 * is considered valid for a variable of any data-type.
	 * 
	 * @param value the value to set for the variable
	 */
	public void setValue(Object value) {
		if (value != null && !datatype.isAssignableFrom(value.getClass())) {
			throw new IllegalArgumentException("variables may not change data-type");
		}

		this.value = value;
	}

	public Class<?> getDataType() {
		return datatype;
	}

	/**
	 * Returns this variable's value
	 * 
	 * @return this variable's value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Returns the name of this variable
	 * 
	 * @return the name of this variable
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns a string representation of this variable
	 * 
	 * @return a string representation of this variable
	 */
	@Override
	public String toString() {
		return name;
	}
}
