package org.epochx.gx.representation;

import java.util.*;

import org.epochx.tools.random.*;

public class AST implements Cloneable {

	private List<Statement> statements;
	
	private ReturnStatement returnStatement;
	
	public AST() {
		statements = new ArrayList<Statement>();
	}
	
	public void insertStatement(int index, Statement statement) {
		if (index == statements.size()) {
			addStatement(statement);
		} else {
			statements.add(index, statement);
		}
	}
	
	public void addStatement(Statement statement) {
		statements.add(statement);
	}
	
	public void addReturnStatement(ReturnStatement returnStatement) {
		this.returnStatement = returnStatement;
	}
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		
		for (Statement s: statements) {
			buffer.append(s.toString());
			buffer.append('\n');
		}
		
		buffer.append(returnStatement.toString());
		
		return buffer.toString();
	}
	
	public List<Statement> getStatements() {
		return statements;
	}
	
	public void modifyExpression(double probability, VariableHandler vars, RandomNumberGenerator rng) {
		for (Statement s: statements) {
			s.modifyExpression(probability, rng, vars);
			s.apply(vars);
		}
	}
		
	public Object evaluate(VariableHandler vars) {
		// Evaluate each statement.
		for (Statement s: statements) {
			s.evaluate(vars);
		}
		
		return returnStatement.evaluate(vars);
	}
	
	@Override
	public Object clone() {
		AST clone = null;
		try {
			clone = (AST) super.clone();
		} catch (CloneNotSupportedException e) {
			assert false;
		}
		
		clone.statements = new ArrayList<Statement>();
		for (Statement s: this.statements) {
			clone.statements.add((Statement) s.clone());
		}
		
		return clone;
	}
	
}
