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
package org.epochx.core;

import org.epochx.life.*;
import org.epochx.model.*;
import org.epochx.op.*;
import org.epochx.representation.*;

import static org.epochx.stats.StatField.*;

public class MutationManager {
	
	// The controlling model.
	private Model model;
	
	// The selector for choosing the individual to mutate.
	private ProgramSelector programSelector;
	
	// The mutation operator that will perform the actual operation.
	private Mutation mutator;
	
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
	public MutationManager(Model model) {
		this.model = model;

		// Initialise parameters.
		initialise();
		
		Controller.getLifeCycleManager().addGenerationListener(new GenerationAdapter() {
			@Override
			public void onGenerationStart() {
				initialise();
			}
		});

		initialise();
	}
	
	/*
	 * Initialises GPMutation, in particular all parameters from the model should
	 * be refreshed incase they've changed since the last call.
	 */
	private void initialise() {
		programSelector = model.getProgramSelector();
		mutator = model.getMutation();
	}
	
	/**
	 * Selects a <code>GPCandidateProgram</code> from the population using the
	 * <code>ProgramSelector</code> returned by a call to 
	 * <code>getProgramSelector()</code> on the model given at construction and 
	 * submits it to the <code>Mutation</code> operator which is obtained by 
	 * calling <code>getMutation()</code> on the model. 
	 * 
	 * <p>After a mutation is made, the controlling model is requested to 
	 * confirm the mutation by a call to <code>acceptMutation()</code>. This 
	 * gives the model total control over whether a mutation is allowed to 
	 * proceed. If <code>acceptMutation()</code> returns <code>false</code> 
	 * then the new <code>GPCandidateProgram</code> is discarded and a new 
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
	 * @return a GPCandidateProgram generated through mutation by the Mutation
	 *         operator in use, or if the max depth limit was exceeded then 
	 *         the original selected program before mutation will be returned.
	 */
	public CandidateProgram mutate() {
		Controller.getLifeCycleManager().onMutationStart();
		
		long crossoverStartTime = System.nanoTime();
		
		CandidateProgram parent = null;
		CandidateProgram child = null;

		reversions = -1;
		do {
			// Attempt mutation.
			parent = programSelector.getProgram();
			
			// Create child as a clone of parent.
			child = (CandidateProgram) parent.clone();
			
			// Mutate the child.
			child = mutator.mutate(child);

			// Allow the life cycle listener to confirm or modify.
			child = Controller.getLifeCycleManager().onMutation(parent, child);
			reversions++;
		} while(child == null);
		
		long runtime = System.nanoTime() - crossoverStartTime;
		
		Controller.getStatsManager().addMutationData(MUTATION_PROGRAM_BEFORE, parent);
		Controller.getStatsManager().addMutationData(MUTATION_PROGRAM_AFTER, child);
		Controller.getStatsManager().addMutationData(MUTATION_REVERTED, reversions);
		Controller.getStatsManager().addMutationData(MUTATION_TIME, runtime);
		
		Controller.getLifeCycleManager().onMutationEnd();
		
		return child;
	}
	
	/**
	 * <p>After a mutation is made, the controlling model is requested to 
	 * confirm the mutation by a call to <code>acceptMutation()</code>. This 
	 * gives the model total control over whether a mutation is allowed to 
	 * proceed. If <code>acceptMutation()</code> returns <code>false</code> 
	 * then the mutated program is discarded and a new 
	 * <code>GPCandidateProgram</code> selected to undergo mutation. The number 
	 * of times the mutation was reverted before being accepted is available 
	 * through a call to this method.
	 * 
	 * @return the number of times the mutation was rejected by the model.
	 */
	public int getReversions() {
		return reversions;
	}
}
