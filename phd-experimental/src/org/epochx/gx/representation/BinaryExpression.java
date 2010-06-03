package org.epochx.gx.representation;

public class BinaryExpression implements Expression {

	private Expression leftExpression;
	private Expression rightExpression;
	
	private String operator;
	
	public BinaryExpression(String operator, Expression leftExpression, Expression rightExpression) {
		this.operator = operator;
		this.leftExpression = leftExpression;
		this.rightExpression = rightExpression;
	}
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		
		buffer.append(leftExpression);
		buffer.append(' ');
		buffer.append(operator);
		buffer.append(' ');
		buffer.append(rightExpression);
		
		return buffer.toString();
	}
}
