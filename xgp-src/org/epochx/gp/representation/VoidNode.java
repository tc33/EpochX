package org.epochx.representation;

public abstract class VoidNode extends Node {

	public VoidNode(Node ... children) {
		super(children);
	}
	
	@Override
	public abstract Void evaluate();
	
}
