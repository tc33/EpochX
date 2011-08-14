package org.epochx.epox.lang;

import org.epochx.epox.Node;

/**
 * A function node which chains three nodes together in sequence, called SEQ3.
 */
public class Seq3Function extends SeqNFunction {

	/**
	 * Constructs a Seq3Function with three <code>null</code> children.
	 */
	public Seq3Function() {
		this(null, null, null);
	}

	/**
	 * Constructs a Seq3Function with three child nodes.
	 * 
	 * @param child1 The first child node.
	 * @param child2 The second child node.
	 * @param child3 The third child node.
	 */
	public Seq3Function(final Node child1, final Node child2, final Node child3) {
		super(child1, child2, child3);
	}

	/**
	 * Returns the identifier of this function which is SEQ3.
	 */
	@Override
	public String getIdentifier() {
		return "SEQ3";
	}
}
