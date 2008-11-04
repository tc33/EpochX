/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of Epoch X - (The Genetic Programming Analysis Software)
 *
 *  Epoch X is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Epoch X is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with Epoch X.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.epochx.core.representation;

import com.epochx.core.*;

/**
 * Subclasses of <code>Node</code> should ensure they call the superclass 
 * constructor with all child Nodes so information such as the arity of the
 * Node can be maintained. Concrete subclasses must also implement evaluate().
 * 
 * A Node is a vertex in a tree structure which represents a program. A Node 
 * can be thought of as an expression in a computer programming language.
 * Evaluating a Node will involve evaluating any children and potentially 
 * performing some operation and/or returning a value. A program
 * is maintained by the <code>CandidateProgram</code> class.
 *  
 * @see FunctionNode
 * @see TerminalNode
 */
public abstract class Node<TYPE> implements Cloneable {
	
	private Node<?>[] children;
	private int nodeCounter;
	private GPProgramAnalyser gPA;
	
	/**
	 * 
	 * @param children
	 */
	public Node(Node<?> ... children) {
		this.children = children;
	}
	
	/**
	 * Performs some operation and/or returns a value associated 
	 * with this Node.
	 * @return
	 */
	public abstract TYPE evaluate();
	
	/**
	 * 
	 * @return
	 */
	public Node<?>[] getChildren() {
		return children;
	}

	/**
	 * 
	 * @param children
	 */
	public void setChildren(Node<?>[] children) {
		this.children = children;
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public Node<?> getChild(int index) {
		return children[index];
	}
	
	public Node<?> getNthNode(Node rootNode, int n) {
		// check n is not greater than length
		gPA = new GPProgramAnalyser();
		if(n>gPA.getProgramLength(rootNode)) {
			throw new IllegalArgumentException("Nth NODE IS GREATER THAN NUMBER OF NODES");
		} else {
			nodeCounter = 0;
			int arity = rootNode.getArity();
			if(arity>0) {
				for(int i = 0; i<arity; i++) {
					nodeCounter++;
					if(nodeCounter==n) {
						return this.getChild(i);
					} else {
						return this.findNthNode(rootNode.getChild(i), n);
					}
				}
			}
		}
		return null;
	}
	
	private Node<?> findNthNode(Node rootNode, int n) {
		int arity = rootNode.getArity();			
		if(arity>0) {
			for(int i = 0; i<arity; i++) {
				nodeCounter++;
				if(nodeCounter==n) {
					return this.getChild(i);
				} else {
					return this.findNthNode(rootNode.getChild(i), n);
				}
			}
		}
		return null;
	}
	
	public void setNthNode(Node rootNode, Node newNode, int n) {
		// check n is not greater than length
		gPA = new GPProgramAnalyser();
		if(n>gPA.getProgramLength(rootNode)) {
			throw new IllegalArgumentException("Nth NODE IS GREATER THAN NUMBER OF NODES");
		} else {
			nodeCounter = 0;
			int arity = rootNode.getArity();
			if(arity>0) {
				for(int i = 0; i<arity; i++) {
					nodeCounter++;
					if(nodeCounter==n) {
						this.setChild(newNode, i);
					} else {
						this.replaceNthNode(rootNode.getChild(i), newNode, n);
					}
				}
			} else {
				rootNode = newNode;
			}
		}
	}
	
	private void replaceNthNode(Node rootNode, Node newNode, int n) {
		int arity = rootNode.getArity();			
		if(arity>0) {
			for(int i = 0; i<arity; i++) {
				nodeCounter++;
				if(nodeCounter==n) {
					this.setChild(newNode, i);
				} else {
					this.replaceNthNode(rootNode.getChild(i), newNode, n);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param child
	 * @param index
	 */
	public void setChild(Node<?> child, int index) {
		children[index] = child;
	}
	
	/**
	 * Returns the number of children this Node has. Only immediate children 
	 * of this Node are counted, not ALL children below this Node.
	 * 
	 * @return	the number of child Nodes.
	 */
	public int getArity() {
		return children.length;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Node<TYPE> clone = (Node<TYPE>) super.clone();
		
		clone.setChildren(this.children.clone());
		
		return clone;
	}	

	@Override
	public boolean equals(Object obj) {
		boolean equal = true;
		
		if (obj instanceof Node) {
			Node<TYPE> n = (Node<TYPE>) obj;
			
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
	public int hashCode() {
		int result = 17;
		for (Node<?> child: children) {
			result = 37 * result + child.hashCode();
		}
		return result;
	}
}
