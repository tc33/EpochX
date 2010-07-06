package org.epochx.gx.representation;

import org.epochx.tools.random.*;

public class Variable implements Expression {
	
	private DataType datatype;
	private String variableName;
	private Object value;
	
	public Variable(DataType datatype, String variableName) {
		this(datatype, variableName, null);
	}
	
	public Variable(DataType datatype, String variableName, Object value) {
		this.datatype = datatype;
		this.variableName = variableName;
		this.value = value;
	}
	
	public DataType getDataType() {
		return datatype;
	}
	
	public String getVariableName() {
		return variableName;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public Object getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return variableName;
	}
	
	@Override
	public Variable clone() {
		Variable clone = null;
		try {
			clone = (Variable) super.clone();
		} catch (CloneNotSupportedException e) {
			assert false;
		}
		
		clone.datatype = this.datatype;
		clone.variableName = this.variableName;
		clone.value = this.value;
		
		return clone;
	}

	@Override
	public void modifyExpression(double probability, RandomNumberGenerator rng, VariableHandler vars) {
		// Nothing to be done here - no expressions.
	}

	@Override
	public Object evaluate(VariableHandler vars) {
		return value;
	}

}
