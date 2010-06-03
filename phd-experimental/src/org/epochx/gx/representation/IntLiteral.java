package org.epochx.gx.representation;

public class IntLiteral implements Literal {

	private int value;
	
	public IntLiteral(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return Integer.toString(value);
	}
}
