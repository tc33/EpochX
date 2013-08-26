/*
 * Copyright 2007-2013
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX
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
package org.epochx.stgp.operator;

import org.epochx.Individual;
import org.epochx.epox.Node;
import org.epochx.event.OperatorEvent;

/**
 * An event fired at the end of a subtree mutation
 * 
 * @see SubtreeMutation
 */
public class SubtreeMutationEndEvent extends OperatorEvent.EndOperator {

	private Node subtree;
	private int point;

	/**
	 * Constructs a <tt>SubtreeMutationEndEvent</tt> with the details of the 
	 * event
	 * 
	 * @param operator the operator that performed the mutation
	 * @param parent the individual that the operator was performed on
	 * @param child the result of performing the mutation
	 * @param point the index of the mutation point in the parent program tree
	 * @param subtree the replacement subtree. This should be equivalent to the
	 * subtree found at the mutation point in the child
	 */
	public SubtreeMutationEndEvent(SubtreeMutation operator, Individual ... parents) {
		super(operator, parents);
	}

	/**
	 * Returns the index of the mutation point in the parent program tree
	 * 
	 * @return the mutation point
	 */
	public int getMutationPoint() {
		return point;
	}

	/**
	 * Returns the root node of the replacement subtree
	 * 
	 * @return the replacement subtree
	 */
	public Node getSubtree() {
		return subtree;
	}
	
	/**
	 * Sets the index of the mutation point in the parent program tree
	 * 
	 * @param points the mutation point
	 */
	public void setMutationPoint(int point) {
		this.point = point;
	}

	/**
	 * Sets the replacement subtree that was used in the mutation
	 * 
	 * @param subtree the subtree that was inserted in to the parent program 
	 * tree in the mutation
	 */
	public void setSubtree(Node subtree) {
		this.subtree = subtree;
	}
}
