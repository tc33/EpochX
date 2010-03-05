/*  
 *  Copyright 2007-2010 Tom Castle & Lawrence Beadle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming software for research
 *
 *  EpochX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EpochX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 *  The latest version is available from: http:/www.epochx.org
 */
package org.epochx.core;

import static org.epochx.stats.StatField.*;

import java.util.*;

import org.epochx.model.Model;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.random.RandomNumberGenerator;

public class GenerationManager {
	
	// The controlling model.
	private Model model;
	
	// Core components.
	private ElitismManager elitism;
	private PoolSelectionManager poolSelection;
	private CrossoverManager crossover;
	private MutationManager mutation;
	private ReproductionManager reproduction;
	
	private RandomNumberGenerator rng;

	// Operator probabilities.
	private double mutationProbability;
	private double crossoverProbability;
	
	private int reversions;
	
	/**
	 * Constructs a generation component for performing each generation.
	 * 
	 * @param model 
	 */
	public GenerationManager(Model model) {
		this.model = model;
		
		// Setup core components.
		elitism = new ElitismManager(model);
		poolSelection = new PoolSelectionManager(model);
		crossover = new CrossoverManager(model);
		mutation = new MutationManager(model);
		reproduction = new ReproductionManager(model);
		
		reversions = 0;
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
	public List<CandidateProgram> generation(int generationNumber, List<CandidateProgram> previousPop) {
		Controller.getLifeCycleManager().onGenerationStart();
		
		reset();
		
		long startTime = System.nanoTime();
		
		Controller.getStatsManager().addGenerationData(GEN_NUMBER, generationNumber);
		
		// Create next population to fill.
		int popSize = model.getPopulationSize();
		List<CandidateProgram> pop = new ArrayList<CandidateProgram>(popSize);
		
		reversions = -1;
		do {
			// Perform elitism.
			pop.addAll(elitism.elitism(previousPop));
			
			// Construct a breeding pool.
			List<CandidateProgram> pool = poolSelection.getPool(previousPop);
			
			// Tell the parent selector what selection pool to use.
			model.getProgramSelector().setSelectionPool(pool);
			
			// Fill the population by performing genetic operations.
			while(pop.size() < popSize) {
				// Pick a genetic operator using Pr, Pc and Pm.
				double random = rng.nextDouble();
				
				if (random < crossoverProbability) {
					// Perform crossover.
					CandidateProgram[] children = crossover.crossover();
					for (CandidateProgram c: children) {
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
			
			pop = Controller.getLifeCycleManager().onGeneration(pop);
			
			reversions++;
		} while(pop == null);
		
		Controller.getStatsManager().addGenerationData(GEN_POPULATION, pop);
		Controller.getStatsManager().addGenerationData(GEN_TIME, (System.nanoTime() - startTime));
		
		// Tell everyone the generation has ended.
		Controller.getLifeCycleManager().onGenerationEnd();
		
		return pop;
	}
	
	public int getReversions() {
		return reversions;
	}	
}
