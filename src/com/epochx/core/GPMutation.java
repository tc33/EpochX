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
package com.epochx.core;

import com.epochx.op.mutation.*;
import com.epochx.op.selection.*;
import com.epochx.representation.*;
import com.epochx.stats.*;

/**
 * This class performs the very simple task of linking together individual  
 * selection and mutation. The actual tasks of crossover and selection are 
 * performed by <code>Mutation</code> and <code>ProgramSelector</code> 
 * implementations respectively.
 * 
 * @see Mutation
 * @see PointMutation
 * @see SubtreeMutation
 * @see ProgramSelector
 */
public class GPMutation<TYPE> {
	/* 
	 * TODO Either this class or another new class needs to encapsulate all the 
	 * details of a mutation event in the same way as GPRun exists after it has 
	 * been executed as a record of how it progressed. The same thing will be 
	 * needed for crossover too.
	 */
	
	// The controlling model.
	private GPModel<TYPE> model;
	
	// The selector for choosing the individual to mutate.
	private ProgramSelector<TYPE> programSelector;
	
	// The mutation operator that will perform the actual operation.
	private Mutation<TYPE> mutator;
	
	// Gather mutation statistics.
	private MutationStats<TYPE> mutationStats;
	
	// The number of times the mutation was rejected by the model.
	private int reversions;
	
	/**
	 * Constructs an instance of GPMutation which will setup the mutation 
	 * operation. Note that the actual mutation operation will be performed by 
	 * the subclass of <code>Mutation</code> returned by the models 
	 * <code>getMutation()</code> method.
	 * 
	 * @param model the GPModel which defines the Mutation operator and 
	 * 				ProgramSelector to use to perform one act of mutation on 
	 * 				an individual in the population.
	 * @see Mutation
	 */
	public GPMutation(GPModel<TYPE> model) {
		this.model = model;
		this.programSelector = model.getProgramSelector();
		this.mutator = model.getMutator();
		
		mutationStats = new MutationStats<TYPE>();
		
		// Setup the listener for mutation statistics.
		mutationStats.addMutationStatListener(model.getMutationStatListener());
	}
	
	/**
	 * Selects a <code>CandidateProgram</code> from the population using the
	 * <code>ProgramSelector</code> returned by a call to 
	 * <code>getProgramSelector()</code> on the model given at construction and 
	 * submits it to the <code>Mutation</code> operator which is obtained by 
	 * calling <code>getMutation()</code> on the model. 
	 * 
	 * <p>After a mutation is made, the controlling model is requested to 
	 * confirm the mutation by a call to <code>acceptMutation()</code>. This 
	 * gives the model total control over whether a mutation is allowed to 
	 * proceed. If <code>acceptMutation()</code> returns <code>false</code> 
	 * then the new <code>CandidateProgram</code> is discarded and a new 
	 * individual is selected and attempted for mutation. The number of times 
	 * the mutation was reverted before being accepted is available through 
	 * a call to <code>getRevertedCount()</code>.
	 * 
	 * <p>Even after a mutation has been accepted by the model, it may still 
	 * be prevented from proceeding if the program depth of the new program 
	 * exceeds the max depth that the model defines. In the case that it does 
	 * exceed the limit then the original program is returned as the result. 
	 * This does not count towards the number of reversions.
	 * 
	 * @return a CandidateProgram generated through mutation by the Mutation
	 *         operator in use, or if the max depth limit was exceeded then 
	 *         the original selected program before mutation will be returned.
	 */
	public CandidateProgram<TYPE> mutate() {
		long crossoverStartTime = System.nanoTime();
		
		CandidateProgram<TYPE> parent = null;
		CandidateProgram<TYPE> child = null;

		reversions = -1;
		do {
			// Attempt mutation.
			parent = programSelector.getProgram();
			
			// Create child as a clone of parent.
			child = (CandidateProgram<TYPE>) parent.clone();
			
			// Mutate the child.
			child = mutator.mutate(child);
			
			// Allow the life cycle listener to confirm or modify.
			child = model.getLifeCycleListener().onMutation(parent, child);
			reversions++;
		} while(child == null);
		
		// If the new program is too deep, replace it with the original.
		//TODO As with crossover - is this really the right thing to be doing?
		if (child.getProgramDepth() > model.getMaxDepth()) {
			child = (CandidateProgram<TYPE>) parent.clone();
		}
		
		long runtime = System.nanoTime() - crossoverStartTime;
		mutationStats.addMutation(parent, child, runtime, reversions);
		
		return child;
	}
	
	/**
	 * <p>After a mutation is made, the controlling model is requested to 
	 * confirm the mutation by a call to <code>acceptMutation()</code>. This 
	 * gives the model total control over whether a mutation is allowed to 
	 * proceed. If <code>acceptMutation()</code> returns <code>false</code> 
	 * then the mutated program is discarded and a new 
	 * <code>CandidateProgram</code> selected to undergo mutation. The number 
	 * of times the mutation was reverted before being accepted is available 
	 * through a call to this method.
	 * 
	 * @return the number of times the mutation was rejected by the model.
	 */
	public int getRevertedCount() {
		return reversions;
	}
	
}
