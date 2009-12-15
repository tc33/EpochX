/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming for research
 *
 *  EpochX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EpochX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EpochX.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.epochx.representation;

/**
 * Represents a function node within a candidate program.
 * 
 * @see Node
 * @see TerminalNode
 */
public abstract class FunctionNode extends Node {

	/**
	 * Constructor for function node with dynamic number of children 
	 * depending on arity of function.
	 * @param children the child nodes.
	 */
	public FunctionNode(Node ... children) {
		super(children);
	}
	
	/**
	 * The value returned from this function should be unique for a specific 
	 * function type within a run.
	 * 
	 * @return a unique function name.
	 */
	public abstract String getIdentifier();
	
	/**
	 * Returns a string representation of the function node. Since the function 
	 * node is dependent upon its children, their string representations will 
	 * form part of this.
	 * 
	 * @return a string representation of this function node.
	 */
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
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj) && (obj.getClass().equals(this.getClass()));
	}
}
