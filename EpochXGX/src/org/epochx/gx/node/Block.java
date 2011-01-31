package org.epochx.gx.node;


import org.epochx.tools.random.*;
import org.epochx.tools.util.*;

public class Block extends ASTNode {

	private RandomNumberGenerator rng;
	
	private int minNoStatements;
	private int maxNoStatements;
	
	public Block(RandomNumberGenerator rng, int minNoStatements, int maxNoStatements) {
		this((Statement) null);
		
		this.maxNoStatements = maxNoStatements;
		this.minNoStatements = minNoStatements;
		this.rng = rng;
		
		int n = rng.nextInt(maxNoStatements-minNoStatements) + minNoStatements;
		
		setChildren(new Statement[n]);
	}
	
	public Block(int n) {
		this(new Statement[n]);
	}

	public Block(final Statement ... children) {
		super(children);
		
		this.maxNoStatements = -1;
		this.minNoStatements = -1;
		this.rng = null;
	}
	
	@Override
	public Void evaluate() {
		int arity = getArity();
		
		for (int i=0; i<arity; i++) {
			getChild(i).evaluate();
		}
		
		return null;
	}

	@Override
	public String getIdentifier() {
		return "BLOCK";
	}
	
	public Statement getStatement(int index) {
		return (Statement) getChild(index);
	}
	
	@Override
	public ASTNode newInstance() {
		Block block = (Block) super.newInstance();
		
		if (minNoStatements != -1) {
			int n = rng.nextInt(maxNoStatements-minNoStatements) + minNoStatements;
			
			block.setChildren(new Statement[n]);
		}
		
		return block;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		if (TypeUtils.allEqual(inputTypes, Void.class)) {
			return Void.class;
		} else {
			return null;
		}
	}
	
	@Override
	public Class<? extends ASTNode>[] getChildTypes() {
		int arity = getArity();
		
		Class<Statement>[] types = (Class<Statement>[]) new Class<?>[arity];
		for (int i=0; i<arity; i++) {
			types[i] = Statement.class;
		}
		
		return types;
	}

	@Override
	public String toJava() {
		StringBuilder buffer = new StringBuilder();
		buffer.append('{');
		for (int i=0; i<getArity(); i++) {
			buffer.append(((ASTNode) getChild(i)).toJava());
		}
		buffer.append('}');
		
		return buffer.toString();
	}

}
