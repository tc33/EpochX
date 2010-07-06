package org.epochx.gx.representation;

import org.epochx.tools.random.*;


public class BinaryExpression implements Expression {

	private Expression leftExpression;
	private Expression rightExpression;
	
	private Operator op;
	
	private DataType dataType;
	
	public BinaryExpression(Operator op, Expression leftExpression, Expression rightExpression, DataType dataType) {
		this.op = op;
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
		buffer.append(op);
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
		clone.op = this.op;
		
		return clone;
	}

	@Override
	public DataType getDataType() {
		return dataType;
	}

	@Override
	public void modifyExpression(double probability, RandomNumberGenerator rng, VariableHandler vars) {
		//TODO Should use model's RNG.
		double rand = Math.random();
		
		if (rand < probability) {
			leftExpression = AST.getExpression(leftExpression.getDataType(), rng, vars);
		} else {
			leftExpression.modifyExpression(probability, rng, vars);
		}
		
		rand = Math.random();
		if (rand < probability) {
			rightExpression = AST.getExpression(rightExpression.getDataType(), rng, vars);
		} else {
			rightExpression.modifyExpression(probability, rng, vars);
		}
	}

	@Override
	public Object evaluate(VariableHandler vars) {		
		Object l = leftExpression.evaluate(vars);
		Object r = rightExpression.evaluate(vars);

		return op.evaluateBinaryOperator(l, r);
	}

	public static Expression getBinaryExpression(DataType dataType, RandomNumberGenerator rng, VariableHandler vars) {
		Operator op = Operator.getBinaryOperator(rng, dataType);
		
		Expression leftExpression = AST.getExpression(dataType, rng, vars);
		Expression rightExpression = AST.getExpression(dataType, rng, vars);
		
		return new BinaryExpression(op, leftExpression, rightExpression, dataType);
	}
}
