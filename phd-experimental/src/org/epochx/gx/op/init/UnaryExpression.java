package org.epochx.gx.op.init;

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
	
}
