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
package org.epochx.gp.op.mutation;

import org.epochx.gp.model.GPAbstractModel;
import org.epochx.gp.op.init.GrowInitialiser;
import org.epochx.gp.representation.*;
import org.epochx.life.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.random.RandomNumberGenerator;

/**
 * This class performs a subtree mutation on a <code>GPCandidateProgram</code>.
 * 
 * <p>A mutation point is randomly selected anywhere in the program tree. Then 
 * the node at that point is replaced with a newly generated program tree, 
 * which is created using a grow strategy.
 */
public class SubtreeMutation implements GPMutation {

	// The controlling model.
	private GPAbstractModel model;
	
	// The maximum depth of the new subtree.
	private int maxSubtreeDepth;
	
	// Grow initialiser to build our replacement subtrees.
	private GrowInitialiser grower;
	
	private RandomNumberGenerator rng;
	
	/**
	 * Simple constructor for subtree mutation using a default maximum depth 
	 * of 4 for new subtrees.
	 * 
	 * @param model The controlling model which provides any configuration 
	 * parameters for the run.
	 */
	public SubtreeMutation(GPAbstractModel model) {
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
	public SubtreeMutation(GPAbstractModel model, int maxSubtreeDepth) {
		this.model = model;
		this.maxSubtreeDepth = maxSubtreeDepth;
		
		grower = new GrowInitialiser(model);
		
		// Configure parameters from the model.
		model.getLifeCycleManager().addConfigListener(new ConfigAdapter() {
			@Override
			public void onConfigure() {
				configure();
			}
		});
	}
	
	/*
	 * Configure component with parameters from the model.
	 */
	private void configure() {
		rng = model.getRNG();
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
		int mutationPoint = rng.nextInt(length);
		
		// Grow a new subtree using the GrowInitialiser.
		Node subtree = grower.buildGrowNodeTree(maxSubtreeDepth);
		
		// Set the new subtree.
		program.setNthNode(mutationPoint, subtree);
		
		return program;
	}
	
}
