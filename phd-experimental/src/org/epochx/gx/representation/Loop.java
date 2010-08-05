package org.epochx.gx.representation;

import java.util.*;

import org.epochx.gx.op.init.*;
import org.epochx.tools.random.*;

public class Loop implements Statement {

	private Expression condition;
	
	private Block body;
	
	public Loop(Expression condition, Block body) {
		this.condition = condition;
		this.body = body;
	}
	
	@Override
	public void apply(VariableHandler vars) {
		// Variable scope will hide any new variables here so do nothing.
	}

	@Override
	public void evaluate(VariableHandler vars) {
		boolean changed = true;
		int maxIterations = 100; // Get from model as parameter.
		int i=0;
		while ((Boolean) condition.evaluate(vars) && changed && i<maxIterations) {
			// Record state of variables before.
			List<Variable> varsBefore = vars.getActiveVariablesCopy();
			
			body.evaluate(vars);
			
			// Have variables changed?
			List<Variable> varsAfter = vars.getActiveVariables();
			changed = !varsBefore.equals(varsAfter);
			
			i++;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		
		buffer.append("while (");
		buffer.append(condition.toString());
		buffer.append(")");
		buffer.append(body.toString());
		
		return buffer.toString();
	}
	
	@Override
	public Loop clone() {
		Loop clone = null;
		try {
			clone = (Loop) super.clone();
		} catch (CloneNotSupportedException e) {
			assert false;
		}
		
		clone.condition = this.condition.clone();
		clone.body = this.body.clone();
			
		return clone;
	}

	@Override
	public void modifyExpression(double probability, RandomNumberGenerator rng, VariableHandler vars) {
		double rand = rng.nextDouble();
		
		if (rand < probability) {
			condition = ProgramGenerator.getExpression(rng, vars, condition.getDataType(), 0);
		} else {
			condition.modifyExpression(probability, rng, vars, 0);
		}
		
		body.modifyExpression(probability, rng, vars);
	}
}
