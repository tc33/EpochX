package org.epochx.gx.representation;

import java.util.*;

import org.epochx.gx.op.init.*;
import org.epochx.tools.random.*;

public class AST implements Cloneable {

	private List<Statement> statements;
	
	private ReturnStatement returnStatement;
	
	public AST() {
		statements = new ArrayList<Statement>();
	}
	
	public int getNoStatements() {
		List<Statement> topStatements = getStatements();
		int noStatements = 0;
		for (Statement s: topStatements) {
			noStatements += s.getNoStatements();
		}
		
		return noStatements;
	}
	
	/**
	 * Gets a statement indexed throughout all the statements at all levels.
	 * @param index
	 * @return
	 */
	public Statement getStatement(int index) {
		int i = 0;
		for (Statement s: statements) {
			if (i == index) {
				return s;
			} else {
				int noStatements = s.getNoStatements();
				if (index < i+noStatements) {
					return s.getStatement(index-i);
				}
				i += s.getNoStatements();
			}
		}
		return null;
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
		
		clone.returnStatement = this.returnStatement.clone();
		
		return clone;
	}

	public Statement deleteStatement(int deletePosition) {
		int current = 0;
		for (int i=0; i<statements.size(); i++) {
			if (current == deletePosition) {
				Statement toDelete = statements.get(i);
				if (toDelete instanceof Declaration) {
					return null;
				} else {
					return statements.remove(i);
				}
			}
			
			// Does index lie within this statement.
			Statement s = statements.get(i);
			int noStatements = s.getNoStatements();
			if (deletePosition < (current + noStatements)) {
				// Position is inside this statement.
				return s.deleteStatement(deletePosition-current-1);
			}
				
			current += noStatements;
		}
		
		return null;
	}

	public void insertStatement(double probability, VariableHandler vars, RandomNumberGenerator rng, int maxNoStatements) {
		// The number of insert points at this level.
		int noInsertPoints = statements.size() + 1;
		
		for (int i=0; i<noInsertPoints; i++) {
			if (getNoStatements() >= maxNoStatements) {
				break;
			}
			
			double rnd = rng.nextDouble();
			
			if (rnd < probability) {
				int maxNestedStatements = maxNoStatements - getNoStatements() - 1;
				
				// Generate new statement.
				Statement newStatement = ProgramGenerator.getStatement(rng, vars, 0, maxNestedStatements);
				
				// Insert at ith point.
				statements.add(i, newStatement);
				
				// The number of insert points has just increased.
				noInsertPoints++;
				i++;
			} else if (i < statements.size()) {
				Statement s = statements.get(i);
				if (s.hasBlock()) {
					/*
					 * Max allowed at next level is amount unused at this level, plus the amount that are already inside the block.
					 */
					int maxNestedStatements = (maxNoStatements - getNoStatements()) + (s.getNoStatements() - 1);
					
					// Step into the block.
					s.insertStatement(probability, rng, vars, maxNestedStatements);
				} else {
					// Apply the statement.
					s.apply(vars);
				}
			}
		}
	}
	
}
