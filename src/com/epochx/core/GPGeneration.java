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

import java.util.*;

import com.epochx.life.LifeCycleManager;
import com.epochx.random.RandomNumberGenerator;
import com.epochx.representation.CandidateProgram;

/**
 * 
 */
public class GPGeneration<TYPE> {

	private GPModel<TYPE> model;
	
	private LifeCycleManager<TYPE> lifeCycle;
	
	// Core components.
	private GPElitism<TYPE> elitism;
	private GPPoolSelection<TYPE> poolSelection;
	private GPCrossover<TYPE> crossover;
	private GPMutation<TYPE> mutation;
	private GPReproduction<TYPE> reproduction;
	
	private List<CandidateProgram<TYPE>> pop;
	
	private RandomNumberGenerator rng;
	private double mutationProbability;
	private double crossoverProbability;
	
	public GPGeneration(GPModel<TYPE> model) {
		this.model = model;
		
		lifeCycle = GPController.getLifeCycleManager();
		
		// Setup core components.
		elitism = new GPElitism<TYPE>(model);
		poolSelection = new GPPoolSelection<TYPE>(model);
		crossover = new GPCrossover<TYPE>(model);
		mutation = new GPMutation<TYPE>(model);
		reproduction = new GPReproduction<TYPE>(model);
	}
	
	private void initialise() {
		rng = model.getRNG();
		mutationProbability = model.getMutationProbability();
		crossoverProbability = model.getCrossoverProbability();
	}
	
	public List<CandidateProgram<TYPE>> generation(List<CandidateProgram<TYPE>> previousPop) {
		initialise();
		
		// Tell life cycle manager we're starting a new generation.
		lifeCycle.onGenerationStart();
		
		// Create next population to fill.
		int popSize = model.getPopulationSize();
		pop = new ArrayList<CandidateProgram<TYPE>>(popSize);
		
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
