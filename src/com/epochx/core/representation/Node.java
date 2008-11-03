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
		
		clone.setChildren(children.clone());
		
		return clone;
	}
}
