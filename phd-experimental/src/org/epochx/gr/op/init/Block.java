package org.epochx.gr.op.init;

import java.util.*;

public class Block {

	private List<Statement> statements;
	
	public Block(List<Statement> statements) {
		this.statements = statements;
	}
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("{\n");
		
		for (Statement s: statements) {
			buffer.append(s.toString());
		}
		
		buffer.append("\n}");

		return buffer.toString();
	}
	
}
