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

import java.util.*;

import org.epochx.life.LifeCycleManager;
import org.epochx.random.RandomNumberGenerator;
import org.epochx.representation.CandidateProgram;

/**
 * This class is responsible for performing a generation in a GP run.
 */
public class GPGeneration<TYPE> {

	// The controlling model.
	private GPModel<TYPE> model;
	
	// Manager of life cycle events.
	private LifeCycleManager<TYPE> lifeCycle;
	
	// Core components.
	private GPElitism<TYPE> elitism;
	private GPPoolSelection<TYPE> poolSelection;
	private GPCrossover<TYPE> crossover;
	private GPMutation<TYPE> mutation;
	private GPReproduction<TYPE> reproduction;
	private RandomNumberGenerator rng;

	// Operator probabilities.
	private double mutationProbability;
	private double crossoverProbability;
	
	/**
	 * Constructs a generation component for performing each generation.
	 * 
	 * @param model 
	 */
	public GPGeneration(GPModel<TYPE> model) {
		this.model = model;
		
		// Get a reference to the life cycle manager.
		lifeCycle = GPController.getLifeCycleManager();
		
		// Setup core components.
		elitism = new GPElitism<TYPE>(model);
		poolSelection = new GPPoolSelection<TYPE>(model);
		crossover = new GPCrossover<TYPE>(model);
		mutation = new GPMutation<TYPE>(model);
		reproduction = new GPReproduction<TYPE>(model);
	}
	
	/*
	 * Reset the component with parameters from the model which might have 
	 * changed since the last generation.
	 */
	private void reset() {
		rng = model.getRNG();
		mutationProbability = model.getMutationProbability();
		crossoverProbability = model.getCrossoverProbability();
	}
	
	/**
	 * Performs one generation of a GP run. The method receives the previous 
	 * population and then performs one generation and returns the resultant
	 * population. 
	 * 
	 * <p>A generation consists of the following sequence of events:
	 * 
	 * <ol>
	 *   <li>Select the elites and put them into the next population.</li>
	 *   <li>Select a breeding pool of programs.</li>
	 *   <li>Randomly choose an operator based upon probablities from the model:
	 *   	<ul>
	 *   		<li>Crossover - pass control to crossover component.</li>
	 *   		<li>Mutation - pass control to mutation component.</li>
	 *   		<li>Reproduction - pass control to reproduction component.</li>
	 *   	</ul>
	 *   </li>
	 *   <li>Insert the result of the operator into the next population.</li>
	 *   <li>Start back at 3. until the next population is full.</li>
	 *   <li>Return the new population.</li>
	 * </ol>
	 * 
	 * <p>The necessary events trigger life cycle events.
	 * 
	 * @param previousPop
	 * @return
	 */
	public List<CandidateProgram<TYPE>> generation(List<CandidateProgram<TYPE>> previousPop) {
		reset();
		
		// Tell life cycle manager we're starting a new generation.
		lifeCycle.onGenerationStart();
		
		// Create next population to fill.
		int popSize = model.getPopulationSize();
		List<CandidateProgram<TYPE>> pop = new ArrayList<CandidateProgram<TYPE>>(popSize);
		
		// Perform elitism.
		pop.addAll(elitism.getElites(previousPop));
		
		// Construct a breeding pool.
		List<CandidateProgram<TYPE>> pool = poolSelection.getPool(previousPop);
		
		// Tell the parent selector what selection pool to use.
		model.getProgramSelector().setSelectionPool(pool);
		
		// Fill the population by performing genetic operations.
		while(pop.size() < popSize) {
			// Pick a genetic operator using Pr, Pc and Pm.
			double random = rng.nextDouble();
			
			if (random < crossoverProbability) {
				// Perform crossover.
				CandidateProgram<TYPE>[] children = crossover.crossover();
				for (CandidateProgram<TYPE> c: children) {
					if (pop.size() < popSize)
						pop.add(c);
				}
			} else if (random < crossoverProbability+mutationProbability) {
				// Perform mutation.
				pop.add(mutation.mutate());
			} else {
				// Perform reproduction.
				pop.add(reproduction.reproduce());
			}
		}
		
		return pop;
	}
}
