package org.epochx.gx.representation;

import java.util.*;

import org.epochx.gx.op.init.*;

public class IfStatement implements Statement {

	private ProgramGenerator generator;
	
	private Expression condition;
	
	private Block ifCode;
	
	private Block elseCode;
	
	public IfStatement(ProgramGenerator generator, Expression condition, Block ifCode, Block elseCode) {
		this.generator = generator;
		this.condition = condition;
		this.ifCode = ifCode;
		this.elseCode = elseCode;
	}
	
	public IfStatement(ProgramGenerator generator, Expression condition, Block ifCode) {
		this(generator, condition, ifCode, null);
	}
	
	@Override
	public void apply(Stack<Variable> variables) {
		// Variable scope will hide any new variables here so do nothing.
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
	public void modifyExpression(double probability) {
		//TODO Should use model's RNG.
		double rand = Math.random();
		
		if (rand < probability) {
			condition = generator.getExpression(condition.getDataType());
		} else {
			condition.modifyExpression(probability);
		}
		
		ifCode.modifyExpression(probability);
		
		if (elseCode != null) {
			elseCode.modifyExpression(probability);
		}
	}
	
}
