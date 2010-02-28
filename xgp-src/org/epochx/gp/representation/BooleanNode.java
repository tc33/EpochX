package org.epochx.gp.representation;

public abstract class BooleanNode extends Node {

	public BooleanNode(Node ... children) {
		super(children);
	}
	
	@Override
	public abstract Boolean evaluate();

}
