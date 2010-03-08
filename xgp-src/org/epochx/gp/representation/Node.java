/* 
 * Copyright 2007-2010 Tom Castle & Lawrence Beadle
 * Licensed under GNU General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.gp.representation;

import java.util.*;

/**
 * Subclasses of <code>Node</code> should ensure they call the superclass 
 * constructor with all child Nodes so information such as the arity of the
 * Node can be maintained. Concrete subclasses must also implement evaluate().
 * 
 * A Node is a vertex in a tree structure which represents a program. A Node 
 * can be thought of as an expression in a computer programming language.
 * Evaluating a Node will involve evaluating any children and potentially 
 * performing some operation and/or returning a value. A program
 * is maintained by the <code>GPCandidateProgram</code> class.
 *  
 * @see FunctionNode
 * @see TerminalNode
 */
public abstract class Node implements Cloneable {
	
	// For a terminal node this will be empty.
	private Node[] children;
	
	/**
	 * Node constructor.
	 * 
	 * @param children child nodes to this node.
	 */
	public Node(Node ... children) {
		this.children = children;
	}
	
	/**
	 * Performs some operation and/or returns a value associated 
	 * with this Node.
	 * 
	 * @return The result of evaluating the candidate program.
	 */
	public abstract Object evaluate();
	
	/**
	 * Returns an array of the children of this node.
	 * 
	 * @return the children of this node.
	 */
	public Node[] getChildren() {
		return children;
	}

	/**
	 * Sets the children of this node.
	 * 
	 * @param children the new children to be set.
	 */
	public void setChildren(Node[] children) {
		this.children = children;
	}
	
	/**
	 * Returns a specific child by index.
	 * 
	 * @param index the index (representing arity) of the child to be returned,
	 * 				indexes are from zero.
	 * @return the child node at the specified arity index.
	 */
	public Node getChild(int index) {
		return children[index];
	}
	
	/**
	 * Returns the element at the specified position in the node tree, where 
	 * this node is considered to be the root - that is the 0th node. The tree 
	 * is traversed in pre-order (depth first).
	 * 
	 * @param n the index of the node to be returned.
	 * @return the node at the specified position in this node tree.
	 */
	public Node getNthNode(int n) {
		return getNthNode(n, 0);
	}
	
	/*
	 * Recursive helper for the public getNthNode(int).
	 */
	private Node getNthNode(int n, int current) {
		// Is this the nth node?
		if (n == current)
			return this;
		
		Node node = null;
		for (Node child: children) {
			int childLength = child.getLength();
			
			// Only look at the subtree if it contains the right range of nodes.
			if (n <= childLength + current) {
				node = child.getNthNode(n, current+1);
				if (node != null) break;
			}
			
			current += childLength;
		}
		
		return node;
	}
	
	/**
	 * Replaces the node at the specified position in this node tree, where 
	 * this node is considered to be the root node - that is, the 0th node. 
	 * The tree is traversed in pre-order (depth first).
	 * 
	 * @param n the index of the node to replace.
	 * @param newNode the node to be stored at the specified position.
	 */
	public void setNthNode(int n, Node newNode) {		
		setNthNode(n, newNode, 0);
	}
	
	/*
	 * Recursive helper for the public setNthNode(int, Node).
	 */
	private void setNthNode(int n, Node newNode, int current) {
		int arity = getArity();
		for (int i=0; i<arity; i++) {
			if (current+1 == n) {
				setChild(i, newNode);
				break;
			}
			
			Node child = getChild(i);
			int childLength = child.getLength();
			
			// Only look at the subtree if it contains the right range of nodes.
			if (n <= childLength + current) {
				child.setNthNode(n, newNode, current+1);
			}
			
			current += childLength;
		}
	}
	
	/**
	 * Retrieves all the nodes in the node tree at a specified depth from this 
	 * current node. This node is considered to be depth zero.
	 * 
	 * @param depth the specified depth of the nodes.
	 * @return a List of all the nodes at the specified depth.
	 */
	public List<Node> getNodesAtDepth(int depth) {
		List<Node> nodes = new ArrayList<Node>();
		fillNodesAtDepth(nodes, depth, 0);
		
		return nodes;
	}
	
	/*
	 * A helper function for getNodesAtDepth(int), to recurse down the node 
	 * tree and populate the nodes array when at the correct depth.
	 */
	private void fillNodesAtDepth(List<Node> nodes, int d, int current){
		if (d == current) {
			nodes.add(this);
		} else {
			for (Node child: children) {
				// Get the nodes at the right depth down each branch.
				child.fillNodesAtDepth(nodes, d, current+1);
			}
		}
	}
	
	/**
	 * Replaces the child node at the specified index with the specified node.
	 * 
	 * @param index the index of the child to replace within the nodes arity.
	 * @param child the child node to be stored at the specified position.
	 */
	public void setChild(int index, Node child) {
		children[index] = child;
	}
	
	/**
	 * Returns the number of immediate children this Node has.
	 * 
	 * @return the number of child Nodes to this Node.
	 */
	public int getArity() {
		return children.length;
	}
	
	/**
	 * Returns a count of how many terminal nodes are in the node tree.
	 * 
	 * @return the number of terminal nodes in this node tree.
	 */
	public int getNoTerminals() {
		int arity = getArity();
		if (arity == 0) {
			return 1;
		} else {
			int result = 0;
			for (int i=0; i<arity; i++) {
				result += getChild(i).getNoTerminals();
			}
			return result;
		}
	}
	
	/**
	 * Returns a count of how many unique terminal nodes are in the node tree.
	 * 
	 * @return the number of unique terminal nodes in this node tree.
	 */
	public int getNoDistinctTerminals() {
		// Get a list of terminals.
		List<Node> terminals = getTerminalNodes();
		
		// Remove duplicates.
		Set<Node> terminalHash = new HashSet<Node>(terminals);
		
		// The number left is how many distinct terminals.
		return terminalHash.size();
	}
	
	/**
	 * Returns a list of all the terminal nodes in the this node tree.
	 * 
	 * @return a List of all the terminal nodes in the node tree.
	 */
	public List<Node> getTerminalNodes() {
		// Alternatively we could use an array, which is quicker/more efficient in this situation?
		List<Node> terminals = new ArrayList<Node>();
		
		int arity = getArity();		
		if (arity == 0) {
			terminals.add(this);
		} else {
			for (int i=0; i<arity; i++) {
				terminals.addAll(getChild(i).getTerminalNodes());
			}
		}
		return terminals;
	}
	
	/**
	 * Returns a count of how many function nodes are in the node tree.
	 * 
	 * @return the number of function nodes in this node tree.
	 */
	public int getNoFunctions() {
		int arity = getArity();
		if (arity == 0) {
			return 0;
		} else {
			int result = 1;
			for (int i=0; i<arity; i++) {
				result += getChild(i).getNoFunctions();
			}
			return result;
		}
	}
	
	/**
	 * Returns a count of how many unique function nodes are in the node tree.
	 * 
	 * @return the number of unique function nodes in this node tree.
	 */
	public int getNoDistinctFunctions() {
		// Get a list of functions.
		List<Node> functions = getFunctionNodes();
		
		// Remove duplicates - where a duplicate is a function of the same type.
		// We cannot use the FunctionNode's equals function because that will compare children too.
		List<String> functionNames = new ArrayList<String>();
		for (Node f: functions) {
			String name = f.getIdentifier();
			if (!functionNames.contains(name)) {
				functionNames.add(name);
			}
		}
		
		// The number left is how many distinct functions.
		return functionNames.size();
	}
	
	/**
	 * Returns a list of all the function nodes in the this node tree.
	 * 
	 * @return a List of all the function nodes in the node tree.
	 */
	public List<Node> getFunctionNodes() {
		// Alternatively we could use an array, which is quicker/more efficient in this situation?
		List<Node> functions = new ArrayList<Node>();
		
		int arity = getArity();
		if (arity > 0) {
			// Add this node as a function and search its child nodes.
			functions.add(this);
			
			for (int i=0; i<arity; i++) {
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
	private int countDepth(Node rootNode, int currentDepth, int depth) {
		// set current depth to maximum if need be
		if(currentDepth>depth) {
			depth = currentDepth;
		}
		// get children and recurse
		int arity = rootNode.getArity();
		if(arity>0) {
			for(int i = 0; i<arity; i++) {
				Node childNode = rootNode.getChild(i);
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
	private int countLength(Node rootNode, int length) {
		// increment length and count through children
		length++;
		// get children and recurse
		int arity = rootNode.getArity();
		if(arity>0) {
			for(int i = 0; i<arity; i++) {
				Node childNode = rootNode.getChild(i);
				length = countLength(childNode, length);
			}
		}
		return length;
	}
	
	public abstract String getIdentifier();

	@Override
	public int hashCode() {
		int result = 17;
		for (Node child: children) {
			result = 37 * result + child.hashCode();
		}
		return result;
	}
	
	/**
	 * Create a deep copy of this node tree. Each child node will be cloned.
	 * 
	 * @return a copy of this Node.
	 */
	@Override
	public Node clone() {
		try {
			Node clone = (Node) super.clone();
			
			clone.children = this.children.clone();
			for (int i=0; i<children.length; i++) {
				clone.children[i] = this.children[i];
				if (clone.children[i] != null)
					clone.children[i] = (Node) clone.children[i].clone();
			}
			
			return clone;
		} catch (CloneNotSupportedException e) {
			// This shouldn't ever happen - if it does then everythings going to 
			// blow up anyway.
		}
		return null;
	}

	/**
	 * Compare an object for equality. If the given object is a Node then it 
	 * may be equal if each Node in the tree is equal. Equality of individual 
	 * Nodes is dependant on the specific node type but typically will be 
	 * whether they are the same type and have the same children for function
	 * nodes and whether they have the same value or are the same variable for 
	 * terminal nodes.
	 * 
	 * @param obj an object to be compared for equivalence.
	 * @return true if this node tree is the same as the obj argument; 
	 * false otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		boolean equal = true;
		
		if (obj instanceof Node) {
			Node n = (Node) obj;
			
			if (n.getArity() != this.getArity()) {
				equal = false;
			}
			
			for(int i=0; i<n.getArity() && equal; i++) {
				Node thatChild = n.getChild(i);
				Node thisChild = this.getChild(i);
				
				if ((thisChild != null) ^ (thatChild != null)) {
					equal = false;
				} else {
					equal = ((thisChild == null) && (thatChild == null) || thisChild.equals(thatChild));
				}
			}
		} else {
			equal = false;
		}
		return equal;
	}
	
	@Override
	public String toString() {		
		StringBuilder builder = new StringBuilder(getIdentifier());
		builder.append('(');
		Node[] children = getChildren();
		for (int i=0, n=children.length; i<n; i++) {
			Node c = children[i];
			if (i!=0) builder.append(' ');
			
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
