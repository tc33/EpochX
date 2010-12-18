package org.epochx.epox.ant;

import org.epochx.epox.Node;


public class Seq3Function extends SeqNFunction {

	public Seq3Function() {
		this(null, null, null);
	}

	public Seq3Function(final Node child1, final Node child2, final Node child3) {
		super(child1, child2, child3);
	}
	
	@Override
	public String getIdentifier() {
		return "SEQ3";
	}	
}
