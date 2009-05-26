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
package com.epochx.core.mutation;

import java.util.List;

import com.epochx.core.representation.*;

/**
 * Point mutation method
 */
public class PointMutation<TYPE> implements Mutation<TYPE> {

	private List<Node<TYPE>> syntax;	
	private double pointProbability;
	
	/**
	 * Creates a point mutation with a point probability of 0.01. It's generally 
	 * recommended that the PointMutation(double) constructor is used.
	 * @param syntax The syntax to be used in the mutation
	 */
	public PointMutation(List<Node<TYPE>> syntax) {
		this(syntax, 0.01);
	}
	
	/**
	 * Contains a point mutation with user specified probability
	 * @param syntax The syntax to be used
	 * @param pointProbability The probability of mutation
	 */
	public PointMutation(List<Node<TYPE>> syntax, double pointProbability) {
		this.syntax = syntax;
		this.pointProbability = pointProbability;
	}
	
	@Override
	public CandidateProgram<TYPE> mutate(CandidateProgram<TYPE> program) {
		int length = program.getProgramLength();
	
		for (int i=0; i<length; i++) { 
			// Perform a point mutation at the ith node, pointProbability% of time.
			if (Math.random() < pointProbability) {
				// Get the ith node.
				Node<TYPE> node = (Node<TYPE>) program.getNthNode(i);

				int arity = node.getArity();
				
				// Find a different function/terminal with same arity - start from random position.
				int rand = (int) Math.floor(Math.random() * syntax.size());
				for (int j=0; j<syntax.size(); j++) {
					int index = (j + rand) % syntax.size();
					
					Node<TYPE> n = syntax.get(index);
					
					//TODO Need to check we're not replacing with the same thing.
					if (n.getArity() == arity) {
						n = (Node<TYPE>) n.clone();
						
						// Attach the old node's children.
						for (int k=0; k<arity; k++) {
							n.setChild(node.getChild(k), k);
						}
						// Then set the new node back into the program.
						program.setNthNode(n, i);
						
						// No need to keep looking.
						break;
					}
				}
				// If none were found then we fall out the bottom and consider the next node.
			}
		}
		
		return program;
	}
}
