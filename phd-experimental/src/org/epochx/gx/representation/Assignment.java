package org.epochx.gx.representation;

import org.epochx.gx.op.init.*;
import org.epochx.tools.random.*;


public class Assignment implements Statement {

	private Variable variable;
	
	private Expression expression;
	
	public Assignment(Variable variable, Expression expression) {
		this.variable = variable;
		this.expression = expression;
	}
	
	@Override
	public void apply(VariableHandler vars) {
		// We're not interested in variable values so do nothing.
	}
	
	@Override
	public void evaluate(VariableHandler vars) {
		Object value = expression.evaluate(vars);
		
		// Update variable value.
		variable.setValue(value);
	}
	
	@Override
	public String toString() {
		return variable.getVariableName() + " = " + expression.toString() + ';';
	}
	
	@Override
	public Assignment clone() {
		Assignment clone = null;
		try {
			clone = (Assignment) super.clone();
		} catch (final CloneNotSupportedException e) {
			assert false;
		}
		
		clone.variable = this.variable.clone();
		clone.expression = this.expression.clone();
		
		return clone;
	}

	@Override
	public void modifyExpression(double probability, RandomNumberGenerator rng, VariableHandler vars) {
		//TODO Should use model's RNG.
		double rand = Math.random();
		
		if (rand < probability) {
			expression = ProgramGenerator.getExpression(rng, vars, expression.getDataType());
		} else {
			expression.modifyExpression(probability, rng, vars);
		}
	}
}
