package org.epochx.gx.representation;

public class UnaryExpression implements Expression {

	private Expression expression;
	
	private String operator;
	
	public UnaryExpression(String operator, Expression expression) {
		this.operator = operator;
		this.expression = expression;
	}
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		
		buffer.append(operator);
		buffer.append(expression);
		
		return buffer.toString();
	}
	
	@Override
	public UnaryExpression clone() {
		UnaryExpression clone = null;
		try {
			clone = (UnaryExpression) super.clone();
		} catch (CloneNotSupportedException e) {
			assert false;
		}
		
		clone.expression = this.expression.clone();
		clone.operator = this.operator;
		
		return clone;
	}
	
}
