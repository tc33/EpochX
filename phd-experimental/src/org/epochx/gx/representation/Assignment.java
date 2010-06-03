package org.epochx.gx.representation;


public class Assignment implements Statement {

	private Variable variable;
	
	private Expression expression;
	
	public Assignment(Variable variable, Expression expression) {
		this.variable = variable;
		this.expression = expression;
	}
	
	@Override
	public String toString() {
		return variable.getVariableName() + " = " + expression.toString() + ';';
	}
	
	@Override
	public Assignment clone() {
		Assignment clone = null;
		try {
			clone = (Assignment) super.clone();
		} catch (final CloneNotSupportedException e) {
			assert false;
		}
		
		clone.variable = this.variable.clone();
		clone.expression = this.expression.clone();
		
		return clone;
	}
	
}
