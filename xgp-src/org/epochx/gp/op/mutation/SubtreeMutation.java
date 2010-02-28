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
package org.epochx.gp.op.mutation;

import org.epochx.gp.core.GPModel;
import org.epochx.gp.op.initialisation.GrowInitialiser;
import org.epochx.gp.representation.*;
import org.epochx.representation.CandidateProgram;

/**
 * This class performs a subtree mutation on a <code>GPCandidateProgram</code>.
 * 
 * <p>A mutation point is randomly selected anywhere in the program tree. Then 
 * the node at that point is replaced with a newly generated program tree, 
 * which is created using a grow strategy.
 */
public class SubtreeMutation implements GPMutation {

	// The current controlling model.
	private GPModel model;
	
	// The maximum depth of the new subtree.
	private int maxSubtreeDepth;
	
	/**
	 * Simple constructor for subtree mutation using a default maximum depth 
	 * of 4 for new subtrees.
	 * 
	 * @param model The controlling model which provides any configuration 
	 * parameters for the run.
	 */
	public SubtreeMutation(GPModel model) {
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
	public SubtreeMutation(GPModel model, int maxSubtreeDepth) {
		this.model = model;
		this.maxSubtreeDepth = maxSubtreeDepth;
	}
	
	/**
	 * Perform subtree mutation on the given GPCandidateProgram. A mutation point 
	 * is randomly selected anywhere in the program tree. Then the node at that 
	 * point is replaced with a newly generated program tree, which is created 
	 * using a grow strategy.
	 * 
	 * @param program The GPCandidateProgram selected to undergo this mutation 
	 * 				  operation.
	 * @return A GPCandidateProgram that was the result of a point mutation on 
	 * the provided GPCandidateProgram.
	 */
	@Override
	public GPCandidateProgram mutate(CandidateProgram p) {
		GPCandidateProgram program = (GPCandidateProgram) p;
		
		// Randonly choose a mutation point.
		int length = program.getProgramLength();
		int mutationPoint = model.getRNG().nextInt(length);
		
		// Grow a new subtree using the GrowInitialiser.
		GrowInitialiser init = new GrowInitialiser(model);
		Node subtree = init.buildGrowNodeTree(maxSubtreeDepth);
		
		// Set the new subtree.
		program.setNthNode(mutationPoint, subtree);
		
		return program;
	}

	@Override
	public Object[] getOperatorStats() {
		return null;
	}

}
