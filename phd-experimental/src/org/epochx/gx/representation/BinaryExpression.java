package org.epochx.gx.representation;

import org.epochx.gx.op.init.*;
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
	public void modifyExpression(double probability, RandomNumberGenerator rng, VariableHandler vars, int level) {
		if (rng.nextDouble() < probability) {
			leftExpression = ProgramGenerator.getExpression(rng, vars, leftExpression.getDataType(), level+1);
		} else {
			leftExpression.modifyExpression(probability, rng, vars, level+1);
		}
		
		//TODO Need to be able to modify the operator too.
		
		if (rng.nextDouble() < probability) {
			rightExpression = ProgramGenerator.getExpression(rng, vars, rightExpression.getDataType(), level+1);
		} else {
			rightExpression.modifyExpression(probability, rng, vars, level+1);
		}
	}

	@Override
	public Object evaluate(VariableHandler vars) {		
		Object l = leftExpression.evaluate(vars);
		Object r = rightExpression.evaluate(vars);

		return op.evaluateBinaryOperator(l, r);
	}
}
