package org.epochx.gx.node;


public class Variable extends Expression {

	private final String identifier;

	private Object value;

	private Class<?> datatype;
	
	// Flag to indicate whether it is available for modification. The variable 
	// can still be changed if set false, but shouldn't be in most cases.
	private boolean mutable;
	
	public Variable(final String identifier, Class<?> datatype) {
		this.identifier = identifier;
		this.datatype = datatype;
		
		mutable = true;
	}

	public Variable(final String identifier, final Object value) {	
		this.identifier = identifier;
		this.value = value;
		this.datatype = value.getClass();
		
		mutable = true;
	}

	/**
	 * Once set, the data type of a Variable cannot be changed, and only values
	 * which are instances of subclasses of the original datatype may be used.
	 * @param value
	 */
	public void setValue(final Object value) {
		if (value != null && !datatype.isAssignableFrom(value.getClass())) {
			throw new IllegalArgumentException("Variables may not change data-type");
		}
		
		this.value = value;
	}
	
	public Object getValue() {
		return value;
	}
	
	public boolean isMutable() {
		return mutable;
	}
	
	public void setMutable(boolean mutable) {
		this.mutable = mutable;
	}

	@Override
	public Object evaluate() {
		return value;
	}
	
	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		// No inputs should be provided for a terminal.
		if (inputTypes.length != 0) {
			throw new IllegalArgumentException("variables have no input types");
		}

		return datatype;
	}

	@Override
	public String toString() {
		return identifier;
	}

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
	
	@Override
	public Class<? extends ASTNode>[] getChildTypes() {
		return (Class<ASTNode>[]) new Class<?>[]{};
	}

	@Override
	public String toJava() {
		return toString();
	}
}
