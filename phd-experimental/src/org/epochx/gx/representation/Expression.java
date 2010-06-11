package org.epochx.gx.representation;

public interface Expression extends Cloneable {

	public DataType getDataType();
	
	public Expression clone();
	
	public void modifyExpression(double probability);
	
}
