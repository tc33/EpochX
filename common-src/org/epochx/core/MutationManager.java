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

import static org.epochx.stats.StatField.*;

import org.epochx.life.ConfigAdapter;
import org.epochx.op.*;
import org.epochx.representation.CandidateProgram;

/**
 * This component is responsible for handling the mutation operation and for 
 * raising mutation events.
 * 
 * <p>
 * Use of the mutation operation will generate the following events:
 * 
 * <p>
 * <table border="1">
 *     <tr>
 *         <th>Event</th>
 *         <th>Revert</th>
 *         <th>Modify</th>
 *         <th>Raised when?</th>
 *     </tr>
 *     <tr>
 *         <td>onMutationStart</td>
 *         <td>no</td>
 *         <td>no</td>
 *         <td>Before the mutation operation is carried out.
 *         </td>
 *     </tr>
 *     <tr>
 *         <td>onMutation</td>
 *         <td><strong>yes</strong></td>
 *         <td><strong>yes</strong></td>
 *         <td>Immediately after mutation is carried out, giving the listener 
 *         the opportunity to request a revert which will cause a re-selection 
 *         of a program and mutation of that program which will result in this 
 *         event being raised again.
 *         </td>
 *     </tr>
 *     <tr>
 *         <td>onMutationEnd</td>
 *         <td>no</td>
 *         <td>no</td>
 *         <td>After the mutation operation has been completed.
 *         </td>
 *     </tr>
 * </table>
 */
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
	 * Constructs an instance of MutationManager which will setup the mutation 
	 * operation. Note that the actual mutation operation will be performed by 
	 * the subclass of <code>Mutation</code> returned by the model's 
	 * <code>getMutation()</code> method.
	 * 
	 * @param model the Model which defines the Mutation operator and 
	 * 				ProgramSelector to use to perform one act of mutation on 
	 * 				an individual in the population.
	 * @see Mutation
	 */
	public MutationManager(Model model) {
		this.model = model;
		
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
		programSelector = model.getProgramSelector();
		mutator = model.getMutation();
	}
	
	/**
	 * Selects a <code>CandidateProgram</code> from the population using the
	 * <code>ProgramSelector</code> returned by a call to 
	 * <code>getProgramSelector()</code> on the model given at construction and 
	 * submits it to the <code>Mutation</code> operator which is obtained by 
	 * calling <code>getMutation()</code> on the model. 
	 * 
	 * @return a GPCandidateProgram generated through mutation by the Mutation
	 *         operator in use, or if the max depth limit was exceeded then 
	 *         the original selected program before mutation will be returned.
	 */
	public CandidateProgram mutate() {
		model.getLifeCycleManager().onMutationStart();
		
		final long crossoverStartTime = System.nanoTime();
		
		CandidateProgram parent = null;
		CandidateProgram child = null;

		reversions = 0;
		do {
			// Attempt mutation.
			parent = programSelector.getProgram();
			
			// Create child as a clone of parent.
			child = (CandidateProgram) parent.clone();
			
			// Mutate the child.
			child = mutator.mutate(child);
			
			// Start the loop again if the program is not valid.
			if (child == null || !child.isValid()) {
				child = null;
				continue;
			}

			// Allow the life cycle listener to confirm or modify.
			child = model.getLifeCycleManager().onMutation(parent, child);
			
			if (child == null) {
				reversions++;
			}
		} while(child == null);
		
		final long runtime = System.nanoTime() - crossoverStartTime;
		
		// Store the stats from the mutation.
		model.getStatsManager().addMutationData(MUTATION_PROGRAM_BEFORE, parent);
		model.getStatsManager().addMutationData(MUTATION_PROGRAM_AFTER, child);
		model.getStatsManager().addMutationData(MUTATION_TIME, runtime);
		model.getStatsManager().addMutationData(MUTATION_REVERSIONS, reversions);
		
		model.getLifeCycleManager().onMutationEnd();
		
		return child;
	}
}
