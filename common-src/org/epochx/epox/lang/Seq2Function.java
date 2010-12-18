package org.epochx.epox.lang;

import org.epochx.epox.Node;


public class Seq2Function extends SeqNFunction {

	public Seq2Function() {
		this(null, null);
	}

	public Seq2Function(final Node child1, final Node child2) {
		super(child1, child2);
	}
	
	@Override
	public String getIdentifier() {
		return "SEQ2";
	}	
}
