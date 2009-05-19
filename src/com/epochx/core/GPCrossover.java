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

import com.epochx.core.crossover.*;
import com.epochx.core.representation.CandidateProgram;
import com.epochx.core.selection.*;
import com.epochx.stats.CrossoverStats;

/**
 * This class performs the very simple task of linking together parent 
 * selection and crossover. The actual tasks of crossover and selection are 
 * performed by Crossover and ParentSelector implementations respectively.
 * 
 * TODO Either this class or another new class needs to encapsulate all the 
 * details of a crossover event in the same way as GPRun exists after it has 
 * been executed as a record of how it progressed. The same thing will be 
 * needed for mutation too.
 * 
 * @see Crossover
 * @see UniformPointCrossover
 * @see KozaCrossover
 * @see ParentSelector
 * @see TournamentSelector
 */
public class GPCrossover<TYPE> {

	// The controlling model.
	private GPModel<TYPE> model;
	
	// The selector for choosing parents.
	private ParentSelector<TYPE> parentSelector;
	
	// The crossover operator that will perform the actual operation.
	private Crossover<TYPE> crossover;
	
	// Gather crossover statistics.
	private CrossoverStats<TYPE> crossoverStats;
	
	// The number of times the crossover was rejected by the model.
	private int reversions;
	
	/**
	 * Constructs an instance of GPCrossover which will setup the crossover 
	 * operation. Note that the actual crossover operation will be performed by 
	 * the subclass of <code>Crossover</code> returned by the models 
	 * getCrossover() method.
	 * 
	 * @param model the GPModel which defines the Crossover operator and 
	 * 				ParentSelector to use to perform one act of crossover on 
	 * 				a population.
	 * @see Crossover
	 */
	public GPCrossover(GPModel<TYPE> model) {
		this.model = model;
		this.parentSelector = model.getParentSelector();
		this.crossover = model.getCrossover();
		
		crossoverStats = new CrossoverStats<TYPE>();
		
		// Setup the listener for crossover statistics.
		crossoverStats.addCrossoverStatListener(model.getCrossoverStatListener());
	}

	/**
	 * Selects two parents by calling <code>getParentSelector()</code> on the 
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
		boolean accepted = true;
		do {
			// Attempt crossover.
			parent1 = parentSelector.getParent();
			parent2 = parentSelector.getParent();
			clone1 = (CandidateProgram<TYPE>) parent1.clone();
			clone2 = (CandidateProgram<TYPE>) parent2.clone();
			parents = new CandidateProgram[]{parent1, parent2};
			children = crossover.crossover(clone1, clone2);
			
			// Ask model whether it accepts this crossover.
			accepted = model.acceptCrossover(parents, children);
			reversions++;
		} while(!accepted);
		
		//TODO Need to be more careful here, potential for array out of bounds if crossover returns an array with more than 2 elements.
		//TODO This is actually horrible - it's so unflexible, theres no way for a user to control whether/how this happens.
		for (int i=0; i<children.length; i++) {
			if (children[i].getProgramDepth() > model.getMaxDepth()) {
				children[i] = (CandidateProgram<TYPE>) parents[i].clone();
			}
		}
		
		long runtime = System.nanoTime() - crossoverStartTime;
		crossoverStats.addCrossover(parents, children, runtime);
		
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
}
