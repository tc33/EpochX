package org.epochx.gx.representation;

import java.util.*;

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
	
	public void modifyExpression(double probability) {
		for (Statement s: statements) {
			s.modifyExpression(probability);
		}
	}
}
