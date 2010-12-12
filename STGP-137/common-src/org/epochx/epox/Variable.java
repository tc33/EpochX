package org.epochx.epox;


public class Variable extends Node {

	private final String identifier;

	private Object value;

	private Class<?> datatype;
	
	public Variable(final String identifier, Class<?> datatype) {
		this.identifier = identifier;
		this.datatype = datatype;
	}

	public Variable(final String identifier, final Object value) {	
		this.identifier = identifier;
		this.value = value;
		this.datatype = value.getClass();
	}

	/**
	 * Once set, the data type of a Variable cannot be changed, and only values
	 * which are instances of subclasses of the original datatype may be used.
	 * @param value
	 */
	public void setValue(final Object value) {
		if (!datatype.isAssignableFrom(value.getClass())) {
			throw new IllegalArgumentException("Variables may not change data-type");
		}		
		
		this.value = value;
	}
	
	public Object getValue() {
		return value;
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
	public Class<?> getReturnType() {
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

	@Override
	public Variable clone() {
		return this;
	}
	
}
