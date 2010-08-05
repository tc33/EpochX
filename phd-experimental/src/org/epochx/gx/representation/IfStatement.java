package org.epochx.gx.representation;

import org.epochx.gx.op.init.*;
import org.epochx.tools.random.*;

public class IfStatement implements Statement {

	private Expression condition;
	
	private Block ifCode;
	
	private Block elseCode;
	
	public IfStatement(Expression condition, Block ifCode, Block elseCode) {
		this.condition = condition;
		this.ifCode = ifCode;
		this.elseCode = elseCode;
	}
	
	public IfStatement(Expression condition, Block ifCode) {
		this(condition, ifCode, null);
	}
	
	@Override
	public void apply(VariableHandler vars) {
		// Variable scope will hide any new variables here so do nothing.
	}

	@Override
	public void evaluate(VariableHandler vars) {
		Boolean result = (Boolean) condition.evaluate(vars);
		if (result) {
			ifCode.evaluate(vars);
		} else if (elseCode != null){
			elseCode.evaluate(vars);
		}
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		
		buffer.append("if (");
		buffer.append(condition.toString());
		buffer.append(")");
		buffer.append(ifCode.toString());
		if (elseCode != null) {
			buffer.append(" else ");
			buffer.append(elseCode.toString());
		}
		
		return buffer.toString();
	}
	
	@Override
	public IfStatement clone() {
		IfStatement clone = null;
		try {
			clone = (IfStatement) super.clone();
		} catch (CloneNotSupportedException e) {
			assert false;
		}
		
		clone.condition = this.condition.clone();
		clone.ifCode = this.ifCode.clone();
		
		if (this.elseCode != null) {
			clone.elseCode = this.elseCode.clone();
		}
			
		return clone;
	}

	@Override
	public void modifyExpression(double probability, RandomNumberGenerator rng, VariableHandler vars) {
		//TODO Should use model's RNG.
		double rand = Math.random();
		
		if (rand < probability) {
			condition = ProgramGenerator.getExpression(rng, vars, condition.getDataType(), 0);
		} else {
			condition.modifyExpression(probability, rng, vars, 0);
		}
		
		ifCode.modifyExpression(probability, rng, vars);
		
		if (elseCode != null) {
			elseCode.modifyExpression(probability, rng, vars);
		}
	}
}
