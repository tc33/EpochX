package org.epochx.representation;

public class DoubleLiteral extends DoubleNode {

	private double value;
	
	public DoubleLiteral(double value) {
		this.value = value;
	}
	
	@Override
	public Double evaluate() {
		return value;
	}

	@Override
	public String getIdentifier() {
		return Double.toString(value);
	}
	
}
