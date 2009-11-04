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
package org.epochx.core;

import org.epochx.life.*;
import org.epochx.op.crossover.*;
import org.epochx.op.selection.*;
import org.epochx.representation.*;
import org.epochx.stats.CrossoverStats;

/**
 * This class performs the very simple task of linking together parent 
 * selection and crossover. The actual tasks of crossover and selection are 
 * performed by Crossover and ProgramSelector implementations respectively.

 * @see Crossover
 * @see UniformPointCrossover
 * @see KozaCrossover
 * @see ProgramSelector
 */ 
public class GPCrossover<TYPE> implements GenerationListener {
	/* 
	 * TODO Either this class or another new class needs to encapsulate all the 
	 * details of a crossover event in the same way as GPRun exists after it has 
	 * been executed as a record of how it progressed. The same thing will be 
	 * needed for mutation too.
	 */
	
	// The controlling model.
	private GPModel<TYPE> model;
	
	// Manager of life cycle events.
	private LifeCycleManager<TYPE> lifeCycle;
	
	// The selector for choosing parents.
	private ProgramSelector<TYPE> programSelector;
	
	// The crossover operator that will perform the actual operation.
	private Crossover<TYPE> crossover;
	
	// Gather crossover statistics.
	private CrossoverStats<TYPE> crossoverStats;
	
	// The maximum allowable program depth after crossover.
	private int maxProgramDepth;
	
	// The number of times the crossover was rejected by the model.
	private int reversions;
	
	/**
	 * Constructs an instance of GPCrossover which will setup the crossover 
	 * operation. Note that the actual crossover operation will be performed by 
	 * the subclass of <code>Crossover</code> returned by the models 
	 * getCrossover() method.
	 * 
	 * @param model the GPModel which defines the Crossover operator and 
	 * 				ProgramSelector to use to perform one act of crossover on 
	 * 				a population.
	 * @see Crossover
	 */
	public GPCrossover(GPModel<TYPE> model) {
		this.model = model;
		
		// Register interest in generation events so we can reset.
		lifeCycle = GPController.getLifeCycleManager();
		lifeCycle.addGenerationListener(this);
		
		crossoverStats = new CrossoverStats<TYPE>();
		
		// Setup the listener for crossover statistics.
		crossoverStats.addCrossoverStatListener(model.getCrossoverStatListener());
		
		initialise();
	}
	
	/*
	 * Initialises GPCrossover, in particular all parameters from the model should
	 * be refreshed incase they've changed since the last call.
	 */
	private void initialise() {
		maxProgramDepth = model.getMaxProgramDepth();
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
	public CandidateProgram<TYPE>[] crossover() {
		long crossoverStartTime = System.nanoTime();

		CandidateProgram<TYPE> parent1;
		CandidateProgram<TYPE> parent2;

		CandidateProgram<TYPE> clone1;
		CandidateProgram<TYPE> clone2;

		CandidateProgram<TYPE>[] parents = null;
		CandidateProgram<TYPE>[] children = null;
		
		reversions = -1;
		do {
			// Select the parents for crossover.
			parent1 = programSelector.getProgram();
			parent2 = programSelector.getProgram();

			clone1 = (CandidateProgram<TYPE>) parent1.clone();
			clone2 = (CandidateProgram<TYPE>) parent2.clone();
			parents = new CandidateProgram[]{parent1, parent2};
			
			// Attempt crossover.
			children = crossover.crossover(clone1, clone2);
			
			// Ask life cycle listener to confirm the crossover.
			children = lifeCycle.onCrossover(parents, children);
			reversions++;
		} while(children == null);
		
		//TODO This is actually horrible - it's so unflexible, theres no way 
		//for a user to control whether/how this happens. Also, it doesn't even
		//seem like the right thing to be doing. Why use the parents?!
		int replacement = 0;
		for (int i=0; i<children.length; i++) {
			if (children[i].getProgramDepth() > maxProgramDepth) {
				if (replacement >= parents.length) {
					replacement = 0;
				}
				children[i] = (CandidateProgram<TYPE>) parents[replacement].clone();
				replacement++;
			}
		}
		
		long runtime = System.nanoTime() - crossoverStartTime;
		crossoverStats.addCrossover(parents, children, runtime, reversions);
		
		return children;
	}
	
	/**
	 * <p>After a crossover is made, the controlling model is requested to 
	 * confirm the crossover by a call to <code>acceptCrossover()</code>. This 
	 * gives the model total control over whether a crossover is allowed to 
	 * proceed. If <code>acceptCrossover()</code> returns <code>false</code> 
	 * then the children are discarded and two new parents are selected and 
	 * attempted for crossover. The number of times the crossover was reverted 
	 * before being accepted is available through a call to this method.
	 * 
	 * @return the number of times the crossover was rejected by the model.
	 */
	public int getRevertedCount() {
		return reversions;
	}
	
	/**
	 * Called after each generation. For each generation we should reset all 
	 * parameters taken from the model incase they've changed. The generation
	 * event is then CONFIRMed.
	 */
	@Override
	public void onGenerationStart() {
		// Reset GPCrossover.
		initialise();
	}
}
