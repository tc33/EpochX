/*
 * Copyright 2007-2011 Tom Castle & Lawrence Beadle
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http://www.epochx.org
 */
package org.epochx.epox;

import java.util.*;

import org.apache.commons.lang.ObjectUtils;
import org.epochx.tools.util.TypeUtils;

/**
 * A Node is a vertex in a tree structure which represents a program. A Node
 * can be thought of as an expression in a computer programming language.
 * Evaluating a Node will involve evaluating any children and optionally
 * returning a value.
 * 
 * Subclasses of <code>Node</code> should ensure they call the superclass
 * constructor with all child Nodes so information such as the arity of the
 * node can be maintained. Concrete subclasses must also implement evaluate().
 * Nodes which support mixed type args, or terminal nodes with no-args must also
 * override the getReturnType(Class&lt;?&gt;) method to indicate their return
 * type. The <code>clone</code> and <code>newInstance</code> methods are also
 * heavily used, so implementations should ensure they are sufficient.
 */
public abstract class Node implements Cloneable {

	// For a terminal node this will be empty.
	private Node[] children;

	/**
	 * Constructs a new Node with the given child nodes. The arity of the node
	 * will be the number of child nodes provided. If unknown, then the child
	 * nodes may be null, but evaluation is unlikely to be possible until they
	 * have been set. Terminal nodes are simply nodes with no children.
	 * 
	 * @param children child nodes to this node.
	 */
	public Node(final Node ... children) {
		this.children = children;
	}

	/**
	 * Subclasses should implement this method to perform some operation with
	 * respect to its children and optionally return a result.
	 * 
	 * @return the result of evaluating the node tree rooted at this node.
	 */
	public abstract Object evaluate();

	/**
	 * Returns an array of this node's children.
	 * 
	 * @return the children of this node.
	 */
	public Node[] getChildren() {
		return children;
	}

	/**
	 * Sets this node's children.
	 * 
	 * @param children the new children to be set.
	 */
	public void setChildren(final Node[] children) {
		this.children = children;
	}

	/**
	 * Returns a specific child by index.
	 * 
	 * @param index the index of the child to be returned, valid indexes run
	 *        from zero to arity-1.
	 * @return the child node at the specified index.
	 */
	public Node getChild(final int index) {
		return children[index];
	}

	/**
	 * Returns the element at the specified position in the node tree, where
	 * this node is considered to be the root - that is the 0th node. The tree
	 * is traversed in pre-order (depth first).
	 * 
	 * @param n the index of the node to be returned.
	 * @return the node at the specified position in this node tree.
	 * @throws IndexOutOfBoundsException if n is out of range.
	 */
	public Node getNthNode(final int n) {
		if (n >= 0) {
			return getNthNode(n, 0);
		} else {
			throw new IndexOutOfBoundsException("attempt to get node at negative index");
		}
	}

	/*
	 * Recursive helper for the public getNthNode(int).
	 */
	private Node getNthNode(final int n, int current) {
		// Is this the nth node?
		if (n == current) {
			return this;
		}

		Node node = null;
		for (final Node child: children) {
			final int childLength = child.getLength();

			// Only look at the subtree if it contains the right range of nodes.
			if (n <= childLength + current) {
				node = child.getNthNode(n, current + 1);
				if (node != null) {
					break;
				}
			}

			current += childLength;
		}

		// If node is null now then the index did not exist within any children.
		if (node == null) {
			throw new IndexOutOfBoundsException("attempt to get node at index >= length");
		}

		return node;
	}

	/**
	 * Replaces the node at the specified position in this node tree, where
	 * this node is considered to be the root node - that is, the 0th node.
	 * The tree is traversed in pre-order (depth first). It is not possible to
	 * set the 0th node, since it does not make sense for an object to be able
	 * to replace itself.
	 * 
	 * @param n the index of the node to replace.
	 * @param newNode the node to be stored at the specified position.
	 * @throws IndexOutOfBoundsException if n is out of range.
	 */
	public void setNthNode(final int n, final Node newNode) {
		if (n > 0) {
			setNthNode(n, newNode, 0);
		} else if (n == 0) {
			throw new IndexOutOfBoundsException("attempt to set node at index 0, cannot replace self");
		} else {
			throw new IndexOutOfBoundsException("attempt to set node at negative index");
		}
	}

	/*
	 * Recursive helper for the public setNthNode(int, Node).
	 */
	private void setNthNode(final int n, final Node newNode, int current) {
		final int arity = getArity();
		for (int i = 0; i < arity; i++) {
			if (current + 1 == n) {
				setChild(i, newNode);
				return;
			}

			final Node child = getChild(i);
			final int childLength = child.getLength();

			// Only look at the subtree if it contains the right range of nodes.
			if (n <= childLength + current) {
				child.setNthNode(n, newNode, current + 1);
				return;
			}

			current += childLength;
		}

		// If we get to here then the index was larger than was available.
		throw new IndexOutOfBoundsException("attempt to set node at index >= length");
	}

	/**
	 * Returns the index of the nth function node, where this node is considered
	 * to be the root - that is the 0th node. The tree's nodes are counted in
	 * pre-order (depth first) to locate the nth function, and return its index
	 * within all nodes. Will throw an exception if the index is out of bounds,
	 * which will be the case for all indexes when called upon a terminal node.
	 * 
	 * @param n the function to find the index of.
	 * @return the index of the nth function node.
	 * @throws IndexOutOfBoundsException if n is out of range.
	 */
	public int getNthFunctionNodeIndex(final int n) {
		final int index = getNthFunctionNodeIndex(n, 0, 0, this);

		// Test if it was found, if not, then index was out of bounds.
		if (index < 0) {
			throw new IndexOutOfBoundsException("attempt to get function node index at index out of range");
		}

		return index;
	}

	/*
	 * Recursive helper function for getNthFunctionNodeIndex.
	 */
	private int getNthFunctionNodeIndex(final int n, int functionCount, int nodeCount, final Node current) {
		// Found the nth function node.
		if ((current.getArity() > 0) && (n == functionCount)) {
			return nodeCount;
		}

		final int result = -1;
		for (final Node child: current.children) {
			final int noNodes = child.getLength();
			final int noFunctions = child.getNoFunctions();

			// Only look at the subtree if it contains the right range of nodes.
			if (n <= noFunctions + functionCount) {
				final int childResult = getNthFunctionNodeIndex(n, functionCount + 1, nodeCount + 1, child);
				if (childResult != -1) {
					return childResult;
				}
			}

			// Skip the correct number of nodes from the subtree.
			functionCount += noFunctions;
			nodeCount += noNodes;
		}

		return result;
	}

	/**
	 * Returns the index of the nth terminal node, where this node is considered
	 * to be the root - that is the 0th node. The tree's nodes are counted in
	 * pre-order (depth first) to locate the nth terminal, and return its index
	 * within all nodes.
	 * 
	 * @param n the terminal to find the index of.
	 * @return the index of the nth terminal node.
	 * @throws IllegalArgumentException if n is out of bounds.
	 */
	public int getNthTerminalNodeIndex(final int n) {
		final int index = getNthTerminalNodeIndex(n, 0, 0, this);

		// Test if it was found, if not, then index was out of bounds.
		if (index < 0) {
			throw new IndexOutOfBoundsException("attempt to get terminal node index at index out of range");
		}

		return index;
	}

	/*
	 * Recursive helper function for getNthTerminalNodeIndex.
	 */
	private int getNthTerminalNodeIndex(final int n, int terminalCount, int nodeCount, final Node current) {
		// Found the nth terminal node.
		if (current.getArity() == 0) {
			if (n == terminalCount++) {
				return nodeCount;
			}
		}

		final int result = -1;
		for (final Node child: current.getChildren()) {
			final int noNodes = child.getLength();
			final int noTerminals = child.getNoTerminals();

			// Only look at the subtree if it contains the right range of nodes.
			if (n <= noTerminals + terminalCount) {
				final int childResult = getNthTerminalNodeIndex(n, terminalCount, nodeCount + 1, child);
				if (childResult != -1) {
					return childResult;
				}
			}

			// Skip the correct number of nodes from the subtree.
			terminalCount += noTerminals;
			nodeCount += noNodes;
		}

		return result;
	}

	/**
	 * Retrieves all the nodes in the node tree at a specified depth from this
	 * current node. This node is considered to be at depth zero.
	 * 
	 * @param depth the specified depth of the nodes.
	 * @return a List of all the nodes at the specified depth.
	 */
	public List<Node> getNodesAtDepth(final int depth) {
		final List<Node> nodes = new ArrayList<Node>((depth + 1) * 3);
		if (depth >= 0) {
			getNodesAtDepth(nodes, depth, 0);
		} else {
			throw new IndexOutOfBoundsException("attempt to get nodes at negative depth");
		}

		if (nodes.isEmpty()) {
			throw new IndexOutOfBoundsException("attempt to get nodes at depth greater than maximum depth");
		}

		return nodes;
	}

	/*
	 * A helper function for getNodesAtDepth(int), to recurse down the node
	 * tree and populate the nodes array when at the correct depth.
	 */
	private void getNodesAtDepth(final List<Node> nodes, final int d, final int current) {
		if (d == current) {
			nodes.add(this);
		} else {
			for (final Node child: children) {
				// Get the nodes at the right depth down each branch.
				child.getNodesAtDepth(nodes, d, current + 1);
			}
		}
	}

	/**
	 * Replaces the child node at the specified index with the given node.
	 * 
	 * @param index the index of the child to replace, from 0 to arity-1.
	 * @param child the child node to be stored at the specified position.
	 */
	public void setChild(final int index, final Node child) {
		children[index] = child;
	}

	/**
	 * Returns the number of immediate children this <code>Node</code> has. This
	 * is effectively the number of inputs the node has. A node with arity
	 * zero, is considered to be a terminal node.
	 * 
	 * @return the number of child <code>Node</code>s to this <code>Node</code>.
	 */
	public int getArity() {
		return children.length;
	}

	/**
	 * Returns a count of how many terminal nodes are in the node tree below
	 * this node.
	 * 
	 * @return the number of terminal nodes in this node tree.
	 */
	public int getNoTerminals() {
		final int arity = getArity();
		if (arity == 0) {
			return 1;
		} else {
			int result = 0;
			for (int i = 0; i < arity; i++) {
				result += getChild(i).getNoTerminals();
			}
			return result;
		}
	}

	/**
	 * Returns a count of how many unique terminal nodes are in the node tree
	 * below this node. This depends upon each terminal node's equals method
	 * being implemented correctly, so care should be taken when overriding
	 * equals.
	 * 
	 * @return the number of unique terminal nodes in this node tree.
	 */
	public int getNoDistinctTerminals() {
		// Get a list of terminals.
		final List<Node> terminals = getTerminalNodes();

		// Remove duplicates.
		final Set<Node> terminalHash = new HashSet<Node>(terminals);

		// The number left is how many distinct terminals.
		return terminalHash.size();
	}

	/**
	 * Returns a list of all the terminal nodes in this node tree.
	 * 
	 * @return a <code>List</code> of all the terminal nodes in this node tree.
	 */
	public List<Node> getTerminalNodes() {
		final List<Node> terminals = new ArrayList<Node>();

		final int arity = getArity();
		if (arity == 0) {
			terminals.add(this);
		} else {
			for (int i = 0; i < arity; i++) {
				terminals.addAll(getChild(i).getTerminalNodes());
			}
		}
		return terminals;
	}

	/**
	 * Returns a count of how many function nodes are in this node tree.
	 * 
	 * @return the number of function nodes in this node tree.
	 */
	public int getNoFunctions() {
		final int arity = getArity();
		if (arity == 0) {
			return 0;
		} else {
			int result = 1;
			for (int i = 0; i < arity; i++) {
				result += getChild(i).getNoFunctions();
			}
			return result;
		}
	}

	/**
	 * Returns a count of how many unique function nodes are in this node tree.
	 * 
	 * @return the number of unique function nodes in this node tree.
	 */
	public int getNoDistinctFunctions() {
		// Get a list of functions.
		final List<Node> functions = getFunctionNodes();

		// Remove duplicates - where a duplicate is a function of the same type.
		// We cannot use the FunctionNode's equals function because that will
		// compare children too.
		final List<String> functionNames = new ArrayList<String>();
		for (final Node f: functions) {
			final String name = f.getIdentifier();
			if (!functionNames.contains(name)) {
				functionNames.add(name);
			}
		}

		// The number left is how many distinct functions.
		return functionNames.size();
	}

	/**
	 * Returns a list of all the function nodes in this node tree.
	 * 
	 * @return a List of all the function nodes in this node tree.
	 */
	public List<Node> getFunctionNodes() {
		// Alternatively we could use an array, which is quicker/more efficient
		// in this situation?
		final List<Node> functions = new ArrayList<Node>();

		final int arity = getArity();
		if (arity > 0) {
			// Add this node as a function and search its child nodes.
			functions.add(this);

			for (int i = 0; i < arity; i++) {
				functions.addAll(getChild(i).getFunctionNodes());
			}
		}

		return functions;
	}

	/**
	 * Returns the depth of deepest node in the node tree, given that this node
	 * is at depth zero.
	 * 
	 * @return the depth of the deepest node in the node tree.
	 */
	public int getDepth() {
		return countDepth(this, 0, 0);
	}

	/*
	 * A private helper function for getDepth() which recurses down the node
	 * tree to determine the deepest node's depth.
	 */
	private int countDepth(final Node rootNode, final int currentDepth, int depth) {
		// set current depth to maximum if need be
		if (currentDepth > depth) {
			depth = currentDepth;
		}
		// get children and recurse
		final int arity = rootNode.getArity();
		if (arity > 0) {
			for (int i = 0; i < arity; i++) {
				final Node childNode = rootNode.getChild(i);
				depth = countDepth(childNode, (currentDepth + 1), depth);
			}
		}
		return depth;
	}

	/**
	 * Returns the number of nodes in the node tree.
	 * 
	 * @return the number of nodes in the node tree.
	 */
	public int getLength() {
		return countLength(this, 0);
	}

	/*
	 * A private recursive helper function for getLength() which traverses the
	 * the node tree counting the number of nodes.
	 */
	private int countLength(final Node rootNode, int length) {
		// increment length and count through children
		length++;
		// get children and recurse
		final int arity = rootNode.getArity();
		if (arity > 0) {
			for (int i = 0; i < arity; i++) {
				final Node childNode = rootNode.getChild(i);
				length = countLength(childNode, length);
			}
		}
		return length;
	}

	/**
	 * Should be implemented to return an indentifier for this node. For 
	 * functions, where this is effectively the function name, this would 
	 * normally be unique within the given problem.
	 * 
	 * @return a <code>String</code> identifier for this node.
	 */
	public abstract String getIdentifier();

	/**
	 * Returns the data-type of this node based upon the child nodes that are
	 * currently set. If any of the child nodes are currently <code>null</code>
	 * or their data-types invalid then the return type will also be
	 * <code>null</code>.
	 * 
	 * @return the return type of this node or null if any of its children
	 *         remain unset or are of an invalid data-type.
	 */
	public final Class<?> getReturnType() {
		final Class<?>[] argTypes = new Class<?>[getArity()];
		for (int i = 0; i < getArity(); i++) {
			final Node child = getChild(i);
			if (child != null) {
				argTypes[i] = child.getReturnType();
			} else {
				return null;
			}
		}
		return getReturnType(argTypes);
	}

	/**
	 * Returns this function node's return type for the given child input types.
	 * Default implementation for a function is that the node will enforce the
	 * closure requirement, and its return type will be the same as its input
	 * types. The default return value for a terminal is Void. Mixed type
	 * function nodes and most terminal nodes should override this method. If
	 * the input types are invalid then <code>null</code> should be returned.
	 * 
	 * @param inputTypes the set of input data-types for which to get the return
	 *        type.
	 * @return the return type of this node given the provided input types, or
	 *         null if the set of input types is invalid.
	 */
	public Class<?> getReturnType(final Class<?> ... inputTypes) {
		if (getArity() == 0) {
			// Is a terminal.
			return Void.class;
		} else {
			// Either the widest type or null if not valid.
			return TypeUtils.getSuper(inputTypes);
		}
	}

	/**
	 * Returns true if this node has an arity of greater than 0.
	 * 
	 * @return true if this node is a function, and false otherwise.
	 */
	public boolean isFunction() {
		return (getArity() > 0);
	}

	/**
	 * Returns true if this node has an arity of 0.
	 * 
	 * @return true if this node is a terminal, and false otherwise.
	 */
	public boolean isTerminal() {
		return (getArity() == 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int result = getIdentifier().hashCode();
		for (final Node child: children) {
			if (child != null) {
				result = 37 * result + child.hashCode();
			}
		}
		return result;
	}

	/**
	 * Create a deep copy of this node tree. Each child node will be cloned. Use
	 * this method for copying a node tree. Some implementations of this class
	 * may need to override this method.
	 * 
	 * @return a copy of this <code>Node</code>.
	 */
	@Override
	public Node clone() {
		try {
			final Node clone = (Node) super.clone();

			clone.children = children.clone();
			for (int i = 0; i < children.length; i++) {
				clone.children[i] = children[i]; // TODO Don't think we need
													// this line.
				if (clone.children[i] != null) {
					clone.children[i] = clone.children[i].clone();
				}
			}

			return clone;
		} catch (final CloneNotSupportedException e) {
			assert false;
			// This shouldn't ever happen - if it does then everythings going to
			// blow up anyway.
		}
		return null;
	}

	/**
	 * This method should be used instead of clone when creating a new instance
	 * of this node type. Rather than copying the node tree, the node is copied
	 * without children. In the case of many terminal nodes the bahaviour of
	 * this method will be the same as the clone method. By default this method
	 * will simply return a new instance of the same type in the same manner as
	 * clone, but with all children removed. Note that there is no requirement
	 * that implementations must not return the same instance.
	 * 
	 * @return a copy of this <code>Node</code> with all children removed.
	 */
	public Node newInstance() {
		try {
			final Node n = (Node) super.clone();
			n.children = new Node[children.length];
			return n;
		} catch (final CloneNotSupportedException e) {
			assert false;
		}

		return null;
	}

	/**
	 * Compare an object for equality. Two nodes may be considered equal if
	 * they have equal arity, equal identifiers, and their children are also
	 * equal (and in the same order). Some nodes may wish to enforce a stricter
	 * contract.
	 * 
	 * @param obj {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		boolean equal = true;

		if (obj instanceof Node) {
			final Node n = (Node) obj;

			if (n.getArity() != getArity()) {
				equal = false;
			} else if (!getIdentifier().equals(n.getIdentifier())) {
				equal = false;
			} else {
				for (int i = 0; (i < n.getArity()) && equal; i++) {
					final Node thatChild = n.getChild(i);
					final Node thisChild = getChild(i);

					equal = ObjectUtils.equals(thisChild, thatChild);
				}
			}
		} else {
			equal = false;
		}
		return equal;
	}

	/**
	 * Returns a string representation of this node. The default implementation
	 * is output of the form:
	 * 
	 * <pre>
	 * identifier(children)
	 * </pre>
	 * 
	 * where <code>identifier</code> is the nodes identifier as returned by
	 * <code>getIdentifier</code>, and <code>children</code> is a space
	 * separated list of child nodes, according to their <code>toString</code>
	 * representation.
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder(getIdentifier());
		builder.append('(');
		final Node[] children = getChildren();
		for (int i = 0, n = children.length; i < n; i++) {
			final Node c = children[i];
			if (i != 0) {
				builder.append(' ');
			}

			if (c == null) {
				builder.append('X');
			} else {
				builder.append(c.toString());
			}
		}
		builder.append(')');
		return builder.toString();
	}

}
