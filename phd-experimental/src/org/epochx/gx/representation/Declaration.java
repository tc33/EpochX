package org.epochx.gx.representation;

import java.util.*;

import org.epochx.gx.op.init.*;


public class Declaration implements Statement {

	private ProgramGenerator generator;
	
	private DataType type;
	
	private Variable variable;
	
	private Expression expression;
	
	public Declaration(ProgramGenerator generator, DataType type, Variable variable, Expression expression) {
		this.generator = generator;
		this.type = type;
		this.variable = variable;
		this.expression = expression;
	}
	
	@Override
	public void apply(Stack<Variable> variables) {
		variables.add(variable);
	}
	
	@Override
	public String toString() {
		return type.toString().toLowerCase() + ' ' + variable.getVariableName() + " = " + expression.toString() + ';';
	}
	
	@Override
	public Declaration clone() {
		Declaration clone = null;
		try {
			clone = (Declaration) super.clone();
		} catch (CloneNotSupportedException e) {
			assert false;
		}
		
		clone.type = this.type;
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
