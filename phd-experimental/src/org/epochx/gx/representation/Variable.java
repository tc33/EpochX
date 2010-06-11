package org.epochx.gx.representation;

public class Variable implements Expression {
	
	private DataType datatype;
	
	private String variableName;
	
	public Variable(DataType datatype, String variableName) {
		this.datatype = datatype;
		this.variableName = variableName;
	}
	
	public DataType getDataType() {
		return datatype;
	}
	
	public String getVariableName() {
		return variableName;
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
		
		return clone;
	}

	@Override
	public void modifyExpression(double probability) {
		// Nothing to be done here - no expressions.
	}
}
