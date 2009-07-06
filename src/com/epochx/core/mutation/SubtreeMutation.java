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

import com.epochx.core.*;
import com.epochx.core.initialisation.*;
import com.epochx.core.representation.*;

/**
 * This class performs a subtree mutation on a <code>CandidateProgram</code>.
 * 
 * <p>A mutation point is randomly selected anywhere in the program tree. Then 
 * the node at that point is replaced with a newly generated program tree, 
 * which is created using a grow strategy.
 */
public class SubtreeMutation<TYPE> implements Mutation<TYPE> {

	// The current controlling model.
	private GPModel<TYPE> model;
	
	// The maximum depth of the new subtree.
	private int maxSubtreeDepth;
	
	/**
	 * Simple constructor for subtree mutation using a default maximum depth 
	 * of 4 for new subtrees.
	 * 
	 * @param model The controlling model which provides any configuration 
	 * parameters for the run.
	 */
	public SubtreeMutation(GPModel<TYPE> model) {
		// 4 is a slightly arbitrary choice but we had to choose something.
		this(model, 4);
	}
	
	/**
	 * Subtree mutation constructor with control for the maximum depth of new
	 * subtrees.
	 * 
	 * @param model The controlling model which provides any configuration 
	 * parameters for the run.
	 * @param maxSubtreeDepth The maximum depth of the inserted subtree.
	 */
	public SubtreeMutation(GPModel<TYPE> model, int maxSubtreeDepth) {
		this.model = model;
		this.maxSubtreeDepth = maxSubtreeDepth;
	}
	
	/**
	 * Perform subtree mutation on the given CandidateProgram. A mutation point 
	 * is randomly selected anywhere in the program tree. Then the node at that 
	 * point is replaced with a newly generated program tree, which is created 
	 * using a grow strategy.
	 * 
	 * @param program The CandidateProgram selected to undergo this mutation 
	 * 				  operation.
	 * @return A CandidateProgram that was the result of a point mutation on 
	 * the provided CandidateProgram.
	 */
	@Override
	public CandidateProgram<TYPE> mutate(CandidateProgram<TYPE> program) {
		// Randonly choose a mutation point.
		int length = program.getProgramLength();
		int mutationPoint = (int) Math.floor(Math.random() * length);
		
		// Grow a new subtree using the GrowInitialiser.
		GrowInitialiser<TYPE> init = new GrowInitialiser<TYPE>(model);
		Node<TYPE> subtree = init.buildGrowNodeTree(maxSubtreeDepth);
		
		// Set the new subtree.
		program.setNthNode(subtree, mutationPoint);
		
		return program;
	}

}
