package org.epochx.gx.representation;

import org.epochx.gx.op.init.*;

public class BinaryExpression implements Expression {

	private ProgramGenerator generator;
	
	private Expression leftExpression;
	private Expression rightExpression;
	
	private String operator;
	
	private DataType dataType;
	
	public BinaryExpression(ProgramGenerator generator, String operator, Expression leftExpression, Expression rightExpression, DataType dataType) {
		this.generator = generator;
		this.operator = operator;
		this.leftExpression = leftExpression;
		this.rightExpression = rightExpression;
		this.dataType = dataType;
	}
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		
		buffer.append('(');
		buffer.append(leftExpression);
		buffer.append(' ');
		buffer.append(operator);
		buffer.append(' ');
		buffer.append(rightExpression);
		buffer.append(')');
		
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

	@Override
	public DataType getDataType() {
		return dataType;
	}

	@Override
	public void modifyExpression(double probability) {
		//TODO Should use model's RNG.
		double rand = Math.random();
		
		if (rand < probability) {
			leftExpression = generator.getExpression(leftExpression.getDataType());
		} else {
			leftExpression.modifyExpression(probability);
		}
		
		rand = Math.random();
		if (rand < probability) {
			rightExpression = generator.getExpression(rightExpression.getDataType());
		} else {
			rightExpression.modifyExpression(probability);
		}
	}
}
