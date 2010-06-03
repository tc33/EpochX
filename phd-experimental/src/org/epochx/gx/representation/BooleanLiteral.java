package org.epochx.gx.representation;

public class BooleanLiteral implements Literal {

	private boolean value;
	
	public BooleanLiteral(boolean value) {
		this.value = value;
	}
	
	public boolean getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return Boolean.toString(value);
	}
	
	@Override
	public BooleanLiteral clone() {
		BooleanLiteral clone = null;
		try {
			clone = (BooleanLiteral) super.clone();
		} catch (CloneNotSupportedException e) {
			assert false;
		}
		
		clone.value = this.value;
		
		return clone;
	}
	
}
