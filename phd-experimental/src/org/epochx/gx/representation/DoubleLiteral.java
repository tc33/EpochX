package org.epochx.gx.representation;

import org.epochx.tools.random.*;

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

	@Override
	public DataType getDataType() {
		return DataType.DOUBLE;
	}

	@Override
	public void modifyExpression(double probability, RandomNumberGenerator rng, VariableHandler vars) {
		//TODO Could modify the double value.
	}

	@Override
	public Object evaluate(VariableHandler vars) {
		return value;
	}

	public static Literal getDoubleLiteral(RandomNumberGenerator rng) {
		DoubleLiteral literal = new DoubleLiteral(rng.nextDouble());
		
		return literal;
	}
}
