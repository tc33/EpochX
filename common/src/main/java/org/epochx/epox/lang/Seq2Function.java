package org.epochx.epox.lang;

import org.epochx.epox.Node;

/**
 * A node which chains two nodes together in sequence, called <tt>SEQ2</tt>
 */
public class Seq2Function extends SeqNFunction {

	/**
	 * Constructs a <tt>Seq2Function</tt> with two <tt>null</tt> children
	 */
	public Seq2Function() {
		this(null, null);
	}

	/**
	 * Constructs a <tt>Seq2Function</tt> with two child nodes
	 * 
	 * @param child1 the first child node
	 * @param child2 the second child node
	 */
	public Seq2Function(Node child1, Node child2) {
		super(child1, child2);
	}

	/**
	 * Returns the identifier of this function which is <tt>SEQ2</tt>
	 */
	@Override
	public String getIdentifier() {
		return "SEQ2";
	}
}
