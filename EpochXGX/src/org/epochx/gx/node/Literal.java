package org.epochx.gx.node;


import org.apache.commons.lang.*;


public class Literal extends Expression {

	private Object value;

	public Literal(final Object value) {
		this.value = value;
	}

	@Override
	public Object evaluate() {
		return value;
	}

	@Override
	public String getIdentifier() {
		return toString();
	}
	
	protected void setValue(final Object value) {
		this.value = value;
	}
	
	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		// No inputs should be provided for a terminal.
		if (inputTypes.length != 0) {
			throw new IllegalArgumentException("literals have no input types");
		}

		if (value != null) {
			return value.getClass();
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		return value.toString();
	}

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

	@Override
	public Literal clone() {
		final Literal clone = (Literal) super.clone();

		clone.value = value;

		return clone;
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
