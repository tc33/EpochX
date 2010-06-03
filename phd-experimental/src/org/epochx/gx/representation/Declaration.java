package org.epochx.gx.representation;


public class Declaration implements Statement {

	private DataType type;
	
	private Variable variable;
	
	private Expression expression;
	
	public Declaration(DataType type, Variable variable, Expression expression) {
		this.type = type;
		this.variable = variable;
		this.expression = expression;
	}
	
	@Override
	public String toString() {
		return type.toString().toLowerCase() + ' ' + variable.getVariableName() + " = " + expression.toString() + ';';
	}
	
	@Override
	public Declaration clone() {
		Declaration clone = null;
		try {
			clone = (Declaration) super.clone();
		} catch (CloneNotSupportedException e) {
			assert false;
		}
		
		clone.type = this.type;
		clone.variable = this.variable.clone();
		clone.expression = this.expression.clone();
		
		return clone;
	}
	
}
