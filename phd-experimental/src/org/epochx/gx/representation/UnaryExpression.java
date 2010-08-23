package org.epochx.gx.representation;

import org.epochx.gx.representation.Operator.*;
import org.epochx.tools.random.*;


public class UnaryExpression implements Expression {

	private Expression expression;
	
	private Operator operator;
	
	private DataType dataType;
	
	public UnaryExpression(Operator operator, Expression expression, DataType dataType) {
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
	public void modifyExpression(double probability, RandomNumberGenerator rng, VariableHandler vars, int level) {		
		if (rng.nextDouble() < probability) {
			//expression = ProgramGenerator.getExpression(rng, vars, expression.getDataType());
			expression = vars.getActiveVariable(dataType);
		} else {
			expression.modifyExpression(probability, rng, vars, level+1);
		}
	}

	@Override
	public Object evaluate(VariableHandler vars) {
		Object value = null;
		// This is more complicated because have to update the actual variable value etc.
		if (operator.getOp().equals("!")) {
			value = expression.evaluate(vars);
			value = !((Boolean) value);
		} else if (operator.getOp().equals("++") && (operator.getOperatorType() == OperatorType.UNARY_PRE)) {
			Variable v = (Variable) expression;
			value = ((Integer) v.getValue()) + 1;
			v.setValue(value);
		} else if (operator.getOp().equals("++") && (operator.getOperatorType() == OperatorType.UNARY_POST)) {
			Variable v = (Variable) expression;
			value = (Integer) v.getValue();
			v.setValue(((Integer) value).intValue() + 1);
		} else if (operator.getOp().equals("--") && (operator.getOperatorType() == OperatorType.UNARY_PRE)) {
			Variable v = (Variable) expression;
			value = ((Integer) v.getValue()) - 1;
			v.setValue(value);
		} else if (operator.getOp().equals("--") && (operator.getOperatorType() == OperatorType.UNARY_POST)) {
			Variable v = (Variable) expression;
			value = (Integer) v.getValue();
			v.setValue(((Integer) value).intValue() - 1);
		}
		
		return value;
	}
}
