package org.epochx.gr.op.init;

import java.util.*;

public class Program {

	private List<Statement> statements;
	
	public Program() {
		statements = new ArrayList<Statement>();
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
	
}
