package org.epochx.gx.representation;

import org.epochx.gx.op.init.*;

public class UnaryExpression implements Expression {

	private ProgramGenerator generator;
	
	private Expression expression;
	
	private String operator;
	
	private DataType dataType;
	
	public UnaryExpression(ProgramGenerator generator, String operator, Expression expression, DataType dataType) {
		this.generator = generator;
		this.operator = operator;
		this.expression = expression;
		this.dataType = dataType;
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

	@Override
	public DataType getDataType() {
		return dataType;
	}
	
	@Override
	public void modifyExpression(double probability) {
		//TODO Should use model's RNG.
		double rand = Math.random();
		
		if (rand < probability) {
			expression = generator.getExpression(expression.getDataType());
		} else {
			expression.modifyExpression(probability);
		}
	}
}
