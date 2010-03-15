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

import org.epochx.life.*;
import org.epochx.model.Model;
import org.epochx.op.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.stats.StatsManager;

/**
 * This component is responsible for handling the crossover operation and for 
 * raising crossover events.
 * 
 * <p>
 * Use of the crossover operation will generate the following events:
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
 *         <td>onCrossoverStart</td>
 *         <td>no</td>
 *         <td>no</td>
 *         <td>Before the crossover operation is carried out.
 *         </td>
 *     </tr>
 *     <tr>
 *         <td>onCrossover</td>
 *         <td><strong>yes</strong></td>
 *         <td><strong>yes</strong></td>
 *         <td>Immediately after crossover is carried out, giving the listener 
 *         the opportunity to request a revert which will cause a re-selection 
 *         of the parent programs and crossing over of those programs which 
 *         will result in this event being raised again.
 *         </td>
 *     </tr>
 *     <tr>
 *         <td>onCrossoverEnd</td>
 *         <td>no</td>
 *         <td>no</td>
 *         <td>After the crossover operation has been completed.
 *         </td>
 *     </tr>
 * </table>
 */
public class CrossoverManager {

	// The controlling model.
	private final Model model;
	
	// The selector for choosing parents.
	private ProgramSelector programSelector;
	
	// The crossover operator that will perform the actual operation.
	private Crossover crossover;
	
	// The number of times the crossover was rejected by the model.
	private int reversions;
	
	/**
	 * Constructs an instance of CrossoverManager which will setup the crossover 
	 * operation. Note that the actual crossover operation will be performed by 
	 * the subclass of <code>Crossover</code> returned by the models 
	 * getCrossover() method.
	 * 
	 * @param model the Model which defines the Crossover operator and 
	 * 				ProgramSelector to use to perform one act of crossover on 
	 * 				a population.
	 * @see Crossover
	 */
	public CrossoverManager(final Model model) {
		this.model = model;
		
		// Initialise parameters.
		initialise();
		
		LifeCycleManager.getLifeCycleManager().addGenerationListener(new GenerationAdapter() {
			@Override
			public void onGenerationStart() {
				initialise();
			}
		});
	}
	
	/*
	 * Initialises Crossover, in particular all parameters from the model should
	 * be refreshed incase they've changed since the last call.
	 */
	private void initialise() {
		programSelector = model.getProgramSelector();
		crossover = model.getCrossover();
	}

	/**
	 * Selects two parents by calling <code>getProgramSelector()</code> on the 
	 * instance of <code>GPModel</code> given at construction and submits them 
	 * to the <code>Crossover</code> operator which is obtained by calling 
	 * <code>getCrossover()</code> on the model. 
	 * 
	 * <p>After a crossover is made, the controlling model is requested to 
	 * confirm the crossover by a call to <code>acceptCrossover()</code>. This 
	 * gives the model total control over whether a crossover is allowed to 
	 * proceed. If <code>acceptCrossover()</code> returns <code>false</code> 
	 * then the children are discarded and two new parents are selected and 
	 * attempted for crossover. The number of times the crossover was reverted 
	 * before being accepted is available through a call to 
	 * <code>getRevertedCount()</code>.
	 * 
	 * <p>Even after a crossover has been accepted by the model, it may still 
	 * be prevented from proceeding if the program depth of either of the 
	 * children exceeds the max depth that the model defines. In the case that 
	 * the children do exceed the limit then the parents are returned as the 
	 * result. This does not count towards the number of reversions.
	 * 
	 * @return an array of CandidatePrograms generated through crossover. This 
	 * 		   is typically 2 child programs, but could in theory be any number 
	 * 	 	   as returned by the Crossover operator in use.
	 */
	public CandidateProgram[] crossover() {
		// Inform everyone we're about to start crossover.
		LifeCycleManager.getLifeCycleManager().onCrossoverStart();
		
		// Record the start time.
		final long crossoverStartTime = System.nanoTime();

		CandidateProgram parent1;
		CandidateProgram parent2;

		CandidateProgram clone1;
		CandidateProgram clone2;

		CandidateProgram[] parents = null;
		CandidateProgram[] children = null;
		
		reversions = 0;
		do {
			// Select the parents for crossover.
			parent1 = programSelector.getProgram();
			parent2 = programSelector.getProgram();

			clone1 = (CandidateProgram) parent1.clone();
			clone2 = (CandidateProgram) parent2.clone();
			parents = new CandidateProgram[]{parent1, parent2};
			
			// Attempt crossover.
			children = crossover.crossover(clone1, clone2);
			
			// Start the loop again if all the children are not valid.
			if (children == null || !allValid(children)) {
				children = null;
				continue;
			}
			
			// Ask life cycle listener to confirm the crossover.
			children = LifeCycleManager.getLifeCycleManager().onCrossover(parents, children);
			
			// If reverted then increment reversion counter.
			if (children == null) {
				reversions++;
			}
		} while(children == null);
		
		final long runtime = System.nanoTime() - crossoverStartTime;

		StatsManager.getStatsManager().addMutationData(CROSSOVER_PARENTS, parents);
		StatsManager.getStatsManager().addMutationData(CROSSOVER_CHILDREN, children);
		StatsManager.getStatsManager().addMutationData(CROSSOVER_REVERSIONS, reversions);
		StatsManager.getStatsManager().addMutationData(CROSSOVER_TIME, runtime);
		
		LifeCycleManager.getLifeCycleManager().onCrossoverEnd();
		
		return children;
	}
	
	/*
	 * Tests whether all of the given array of CandidatePrograms is valid. If
	 * one or more are invalid then it returns false otherwise it returns true.
	 */
	private boolean allValid(CandidateProgram[] programs) {
		boolean valid = true;
		for (CandidateProgram p: programs) {
			if (!p.isValid()) {
				valid = false;
				break;
			}
		}
		return valid;
	}
}
