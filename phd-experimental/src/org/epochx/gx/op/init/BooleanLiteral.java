package org.epochx.gx.op.init;

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
	
}
