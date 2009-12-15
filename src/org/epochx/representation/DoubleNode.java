package org.epochx.representation;

public abstract class DoubleNode extends Node {

	public DoubleNode(Node ... children) {
		super(children);
	}
	
	@Override
	public abstract Double evaluate();
	
}
