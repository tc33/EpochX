/*
 * Copyright 2007-2013
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
package org.epochx.gr.operator;

import org.epochx.Individual;
import org.epochx.event.OperatorEvent;
import org.epochx.grammar.NonTerminalSymbol;

/**
 * An event fired at the end of a subtree mutation
 * 
 * @see SubtreeMutation
 * 
 * @since 2.0
 */
public class SubtreeMutationEndEvent extends OperatorEvent.EndOperator {

	private NonTerminalSymbol subtree;
	private int mutationPoint;

	/**
	 * Constructs a <code>SubtreeMutationEndEvent</code> with the details of the event
	 * 
	 * @param operator the operator that performed the crossover
	 * @param parents an array containing one individual that the operator was performed on
	 */
	public SubtreeMutationEndEvent(SubtreeMutation operator, Individual[] parents) {
		super(operator, parents);
	}

	/**
	 * Returns an integer which is the index of the point chosen for the subtree mutation 
	 * operation. The index is the position in the whole parse tree as would be returned 
	 * by calling <code>getNthSymbol</code> on the root node.
	 * 
	 * @return an integer which is the index of the mutation point
	 */
	public int getMutationPoint() {
		return mutationPoint;
	}
	
	/**
	 * Sets the index of the symbol in the parse tree that was mutated
	 * 
	 * @param mutationPoint index used as the mutation point
	 */
	public void setMutationPoint(int mutationPoint) {
		this.mutationPoint = mutationPoint;
	}

	/**
	 * Returns the new subtree that was generated as a replacement for the subtree originally
	 * at the mutation point
	 * 
	 * @return the root node of the new subtree
	 */
	public NonTerminalSymbol getSubtree() {
		return subtree;
	}

	/**
	 * Sets the new subtree that was generated as a replacement for the subtree originally
	 * at the mutation point
	 * 
	 * @param subtree the root node of the first subtree
	 */
	public void setSubtree(NonTerminalSymbol subtree) {
		this.subtree = subtree;
	}
}
