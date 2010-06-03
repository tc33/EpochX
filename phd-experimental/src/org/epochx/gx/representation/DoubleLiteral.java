package org.epochx.gx.representation;

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
	
	@Override
	public DoubleLiteral clone() {
		DoubleLiteral clone = null;
		try {
			clone = (DoubleLiteral) super.clone();
		} catch (CloneNotSupportedException e) {
			assert false;
		}
		
		clone.value = this.value;
		
		return clone;
	}
}
