package org.epochx.gx.representation;

import java.util.*;

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
	public void modifyExpression(double probability, RandomNumberGenerator rng, VariableHandler vars, int level) {
		//TODO Could modify the double value.
	}

	@Override
	public Object evaluate(VariableHandler vars) {
		return value;
	}
	
	@Override
	public Set<Variable> getUsedVariables() {
		return new HashSet<Variable>();
	}
}
