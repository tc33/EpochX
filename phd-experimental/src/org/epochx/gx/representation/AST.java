package org.epochx.gx.representation;

import java.util.*;

public class AST implements Cloneable {

	private List<Statement> statements;
	
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
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		
		for (Statement s: statements) {
			buffer.append(s.toString());
			buffer.append('\n');
		}
		
		return buffer.toString();
	}
	
	public List<Statement> getStatements() {
		return statements;
	}
	
	public void modifyExpression(double probability) {
		for (Statement s: statements) {
			s.modifyExpression(probability);
		}
	}
	
	/*public static Program getProgram(int noStatements) {
		Program p = new Program();
		
		for (int i=0; i<noStatements; i++) {
			p.statements.add(Statement.getStatement());
		}
		
		return p;
	}
	
	public static Program getProgram() {
		return Program.getProgram(rng.nextInt(10) + 1);
	}*/
	
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
