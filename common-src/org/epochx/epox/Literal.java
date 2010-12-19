package org.epochx.epox;

import org.apache.commons.lang.ObjectUtils;


public class Literal extends Node {

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
	
	@Override
	public Class<?> getReturnType() {
		return value.getClass();
	}
	
	/**
	 * @param value the value to set
	 */
	protected void setValue(Object value) {
		this.value = value;
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
	public Literal newInstance() {
		return clone();
	}
}
