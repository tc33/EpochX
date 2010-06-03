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

	@Override
	public BinaryExpression clone() {
		BinaryExpression clone = null;
		try {
			clone = (BinaryExpression) super.clone();
		} catch (CloneNotSupportedException e) {
			assert false;
		}
		
		clone.leftExpression = this.leftExpression.clone();
		clone.rightExpression = this.rightExpression.clone();
		clone.operator = this.operator;
		
		return clone;
	}
}
