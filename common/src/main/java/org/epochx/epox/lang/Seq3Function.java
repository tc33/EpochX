package org.epochx.epox.lang;

import org.epochx.epox.Node;

/**
 * A node which chains three nodes together in sequence, called <tt>SEQ3</tt>
 */
public class Seq3Function extends SeqNFunction {

	/**
	 * Constructs a Seq3Function with three <tt>null</tt> children.
	 */
	public Seq3Function() {
		this(null, null, null);
	}

	/**
	 * Constructs a <tt>Seq3Function</tt> with three child nodes
	 * 
	 * @param child1 the first child node
	 * @param child2 the second child node
	 * @param child3 the third child node
	 */
	public Seq3Function(Node child1, Node child2, Node child3) {
		super(child1, child2, child3);
	}

	/**
	 * Returns the identifier of this function which is <tt>SEQ3</tt>
	 */
	@Override
	public String getIdentifier() {
		return "SEQ3";
	}
}
