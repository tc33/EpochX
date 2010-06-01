package org.epochx.gx.op.init;

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
}
