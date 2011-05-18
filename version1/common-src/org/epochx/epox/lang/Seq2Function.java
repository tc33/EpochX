package org.epochx.epox.lang;

import org.epochx.epox.Node;

/**
 * A function node which chains two nodes together in sequence, called SEQ2.
 */
public class Seq2Function extends SeqNFunction {

	/**
	 * Constructs a Seq2Function with two <code>null</code> children.
	 */
	public Seq2Function() {
		this(null, null);
	}

	/**
	 * Constructs a Seq2Function with two child nodes.
	 * 
	 * @param child1 The first child node.
	 * @param child2 The second child node.
	 */
	public Seq2Function(final Node child1, final Node child2) {
		super(child1, child2);
	}

	/**
	 * Returns the identifier of this function which is SEQ2.
	 */
	@Override
	public String getIdentifier() {
		return "SEQ2";
	}
}
