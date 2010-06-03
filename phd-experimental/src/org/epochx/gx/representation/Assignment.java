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
	
}
