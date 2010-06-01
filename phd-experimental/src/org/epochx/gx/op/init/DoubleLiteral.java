package org.epochx.gx.op.init;

public class DoubleLiteral implements Literal {
	
	private double value;
	
	public DoubleLiteral(double value) {
		this.value = value;
	}
	
	public double getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return Double.toString(value);
	}
}
