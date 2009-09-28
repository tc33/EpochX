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
package com.epochx.op.mutation;

import java.util.List;

import com.epochx.core.*;
import com.epochx.representation.*;

/**
 * This class performs a simple point mutation on a <code>CandidateProgram</code>.
 * 
 * <p>Each node in the program tree is considered for mutation, with the 
 * probability of that node being mutated given as an argument to the 
 * PointMutation constructor. If the node does undergo mutation then a 
 * replacement node is selected from the full syntax (function and terminal 
 * sets), at random.
 */
public class PointMutation<TYPE> implements Mutation<TYPE> {

	// The current controlling model.
	private GPModel<TYPE> model;
	
	// The probability that each node has of being mutated.
	private double pointProbability;
	
	/**
	 * Construct a point mutation with a default point probability of 0.01. It is
	 * generally recommended that the PointMutation(GPModel, double) constructor 
	 * is used instead.
	 * 
	 * @param model The current controlling model. Parameters such as full 
	 * syntax will be obtained from this.
	 */
	public PointMutation(GPModel<TYPE> model) {
		this(model, 0.01);
	}
	
	/**
	 * Construct a point mutation with user specified point probability.
	 * 
	 * @param model The current controlling model. Parameters such as full 
	 * syntax will be obtained from this.
	 * @param pointProbability The probability each node in a selected program 
	 * has of undergoing a mutation. 1.0 would result in all nodes being 
	 * changed, and 0.0 would mean no nodes were changed. A typical value 
	 * would be 0.01.
	 */
	public PointMutation(GPModel<TYPE> model, double pointProbability) {
		this.model = model;
		this.pointProbability = pointProbability;
	}
	
	/**
	 * Perform point mutation on the given CandidateProgram. Each node in the 
	 * program tree is considered in turn, with each having the given 
	 * probability of actually being exchanged. Given that a node is chosen 
	 * then a new function or terminal node of the same arity is used to 
	 * replace it.
	 * 
	 * @param program The CandidateProgram selected to undergo this mutation 
	 * 				  operation.
	 * @return A CandidateProgram that was the result of a point mutation on 
	 * the provided CandidateProgram.
	 */
	@Override
	public CandidateProgram<TYPE> mutate(CandidateProgram<TYPE> program) {
		// Get the syntax from which new nodes will be chosen.
		List<Node<TYPE>> syntax = model.getSyntax();
		
		// Iterate over each node in the program tree.
		int length = program.getProgramLength();
		for (int i=0; i<length; i++) { 
			// Only change pointProbability of the time.
			if (model.getRNG().nextDouble() < pointProbability) {
				// Get the arity of the ith node of the program.
				Node<TYPE> node = (Node<TYPE>) program.getNthNode(i);
				int arity = node.getArity();
				
				// Find a different function/terminal with same arity - start from random position.
				int rand = model.getRNG().nextInt(syntax.size());
				for (int j=0; j<syntax.size(); j++) {
					int index = (j + rand) % syntax.size();
					Node<TYPE> n = syntax.get(index);
					
					if ((n.getArity() == arity) && !nodesEqual(node, n)) {						
						n = (Node<TYPE>) n.clone();
						
						// Attach the old node's children.
						for (int k=0; k<arity; k++) {
							n.setChild(k, node.getChild(k));
						}
						// Then set the new node back into the program.
						program.setNthNode(i, n);
						
						// No need to keep looking.
						break;
					}
				}
				// If none were found then we fall out the bottom and consider the next node.
			}
		}
		
		return program;
	}
	
	/*
	 * Helper function to check node equivalence. We cannot just use Node's 
	 * equals() method because we don't want to compare children if it's a 
	 * function node.
	 */
	private boolean nodesEqual(Node<TYPE> nodeA, Node<TYPE> nodeB) {
		boolean equal = false;
		
		// Check they're the same type first.
		if (nodeA.getClass().equals(nodeB.getClass())) {
			if (nodeA instanceof FunctionNode) {
				// They're both the same function type.
				equal = true;
			} else if (nodeA instanceof TerminalNode) {
				equal = ((TerminalNode<TYPE>) nodeA).equals(nodeB);
			} else {
				// Unknown node type - somethings gone wrong.
			}
		}
		
		return equal;
	}
}
