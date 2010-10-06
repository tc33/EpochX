package org.epochx.gx.representation;

import java.util.*;

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
	
	/**
	 * @return the expression
	 */
	public Expression getExpression() {
		return expression;
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
		double rand = rng.nextDouble();
		
		if (rand < probability) {
			expression = ProgramGenerator.getExpression(rng, vars, expression.getDataType(), 0);
		} else {
			expression.modifyExpression(probability, rng, vars, 0);
		}
	}

	@Override
	public int getNoStatements() {
		return 1;
	}
	
	@Override
	public Statement getStatement(int index) {
		if (index == 0) {
			return this;
		} else {
			return null;
		}
	}

	@Override
	public Set<Variable> getDeclaredVariables() {
		// No declared variables here so return empty set.
		return new HashSet<Variable>();
	}

	@Override
	public Set<Variable> getUsedVariables() {
		Set<Variable> usedVars = new HashSet<Variable>();
		usedVars.add(variable);
		usedVars.addAll(expression.getUsedVariables());
		
		return usedVars;
	}
}
