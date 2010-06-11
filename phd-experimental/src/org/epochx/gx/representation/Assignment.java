package org.epochx.gx.representation;

import java.util.*;

import org.epochx.gx.op.init.*;


public class Assignment implements Statement {

	private ProgramGenerator generator;
	
	private Variable variable;
	
	private Expression expression;
	
	public Assignment(ProgramGenerator generator, Variable variable, Expression expression) {
		this.generator = generator;
		this.variable = variable;
		this.expression = expression;
	}
	
	@Override
	public void apply(Stack<Variable> variables) {
		// We're not interested in variable values so do nothing.
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
