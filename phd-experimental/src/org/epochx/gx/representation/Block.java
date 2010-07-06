package org.epochx.gx.representation;

import java.util.*;

import org.epochx.tools.random.*;

public class Block implements Cloneable {

	private List<Statement> statements;
	
	public Block(List<Statement> statements) {
		this.statements = statements;
	}
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(" {\n");
		
		for (Statement s: statements) {
			buffer.append(s.toString());
		}
		
		buffer.append("\n}");

		return buffer.toString();
	}
	
	@Override
	public Block clone() {
		Block clone = null;
		try {
			clone = (Block) super.clone();
		} catch (CloneNotSupportedException e) {
			assert false;
		}
		
		clone.statements = new ArrayList<Statement>();
		for (Statement s: this.statements) {
			clone.statements.add((Statement) s.clone());
		}
		
		return clone;
	}
	
	public void modifyExpression(double probability, RandomNumberGenerator rng, VariableHandler vars) {
		for (Statement s: statements) {
			s.modifyExpression(probability, rng, vars);
		}
	}
	
	public void evaluate(VariableHandler vars) {
		// Evaluate each statement.
		for (Statement s: statements) {
			s.evaluate(vars);
		}
	}

	public static Block getBlock(RandomNumberGenerator rng, VariableHandler vars) {
		// Record number of variables to return to.
		int noVariables = vars.getNoActiveVariables();
		
		List<Statement> statements = new ArrayList<Statement>();

		//int noStatements = rng.nextInt(10);
		int noStatements = 1;
		
		for (int i=0; i<noStatements; i++) {
			statements.add(AST.getStatement(rng, vars));
		}
		
		Block result = new Block(statements);
		
		// Pop off any newly declared variables to return to size before block.
		vars.setNoActiveVariables(noVariables);
		
		return result;
	}
}
