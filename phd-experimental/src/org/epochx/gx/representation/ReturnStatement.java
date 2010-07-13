package org.epochx.gx.representation;

import org.epochx.tools.random.*;

public class ReturnStatement implements Cloneable {

	private Variable expression;
	
	private DataType returnType;
	
	/**
	 * Currently expression should just be a variable.
	 * 
	 * @param expression
	 */
	public ReturnStatement(DataType returnType, Variable expression) {
		this.setReturnType(returnType);
		this.expression = expression;
	}
	
	public Object evaluate(VariableHandler vars) {
		return expression.evaluate(vars);
	}

	
	public void modifyExpression(double probability, RandomNumberGenerator rng,
			VariableHandler vars) {
		
	}
	
	/**
	 * @param returnType the returnType to set
	 */
	public void setReturnType(DataType returnType) {
		this.returnType = returnType;
	}

	/**
	 * @return the returnType
	 */
	public DataType getReturnType() {
		return returnType;
	}
	
	@Override
	public String toString() {
		return "return " + expression.toString() + ';';
	}
	
	@Override
	public ReturnStatement clone() {
		ReturnStatement clone = null;
		try {
			clone = (ReturnStatement) super.clone();
		} catch (CloneNotSupportedException e) {
			assert false;
		}
		
		clone.expression = this.expression.clone();
		clone.returnType = this.returnType;
			
		return clone;
	}

}
