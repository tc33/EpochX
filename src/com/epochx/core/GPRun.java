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

import com.epochx.representation.*;
import com.epochx.stats.*;

/**
 * A GPRun object has 2 responsibilities. Firstly, to execute a GP run based 
 * upon the control parameters provided. Secondly, after executing to 
 * provide basic details of how that run went. 
 * 
 * <p>Objects of this class should be immutable - however the 
 * underlying objects, in particular GPModel may not be.
 */
public class GPRun<TYPE> {

	// The model describing the problem to be evolved.
	private GPModel<TYPE> model;
	
	// Crossover module.
	private GPCrossover<TYPE> crossover;
	
	// Mutation module.
	private GPMutation<TYPE> mutation;
	
	// The best program found so far during the run.
	private CandidateProgram<TYPE> bestProgram;
	
	// The fitness of the best program found so far during the run.
	private double bestFitness;
	
	// The run start time in nano-seconds for measuring program length.
	private long runStartTime;
	
	// The run end time in nano-seconds for measuring program length.
	private long runEndTime;
	
	// Gather generation statistics.
	private GenerationStats<TYPE> genStats;
	
	/*
	 * Private constructor. The static factory method run(GPModel) should be 
	 * used to create objects of GPRun and simultaneously execute them.
	 */
	private GPRun(GPModel<TYPE> model) {
		this.model = model;
		
		// Initialise the run.
		runStartTime = System.nanoTime();
		runEndTime = -1;
		bestProgram = null;
		bestFitness = Double.POSITIVE_INFINITY;
		
		// Setup crossover and mutation.
		crossover = new GPCrossover<TYPE>(model);
		mutation = new GPMutation<TYPE>(model);
		
		// Create the statistics monitor.
		genStats = new GenerationStats<TYPE>();
		
		// Setup the listener for generation statistics.
		genStats.addGenerationStatListener(model.getGenerationStatListener());
	}
	
	/**
	 * Construct a new GPRun object and execute it. The GPModel passed in is
	 * used to provide the control parameters for the run.
	 * @param <TYPE> the type of <code>CandidateProgram</code> to be evolved.
	 * @param model  the model which will control the run with the parameters 
	 * 				 and fitness function to use.
	 * @return 		 The GPRun object that was executed, containing retrievable 
	 * 				 details about the run.
	 * @see GPModel
	 */
	public static <TYPE> GPRun<TYPE> run(GPModel<TYPE> model) {
		// Create the GPRun object.
		GPRun<TYPE> runner = new GPRun<TYPE>(model);
		
		// Run it.
		runner.run();
		
		return runner;
	}
	
	/*
	 * This is the private method which actually does the work in this class. 
	 * It is also the workhorse of the whole API as it creates the initial 
	 * population, initiates the genetic operators and performs any elitism 
	 * or pool selection in use.
	 */
	private void run() {
		// Tell our generation stats to record the start time.
		genStats.setStartTime();
		
		// Perform initialisation.
		List<CandidateProgram<TYPE>> pop = model.getInitialiser().getInitialPopulation();
		
		// Generate generation stats for the inital population.
		genStats.addGen(pop, 0, 0, 0);
		
		// Execute each generation.
		genloop: for (int i=1; i<=model.getNoGenerations(); i++) {			
			// Create next population to fill.
			int popSize = model.getPopulationSize();
			List<CandidateProgram<TYPE>> nextPop = new ArrayList<CandidateProgram<TYPE>>(popSize);
			
			// Perform elitism.
			int noElites = model.getNoElites();
			noElites = (noElites < popSize) ? noElites : popSize;
			nextPop.addAll(GPElitism.getElites(pop, noElites));
			
			// Construct a breeding pool.
			List<CandidateProgram<TYPE>> pool = model.getPoolSelector().getPool(pop, model.getPoolSize());
			
			// Count number of crossovers and mutations rejected by the model this gen.
			int crossoverReversions = 0;
			int mutationReversions = 0;
			
			// Tell the parent selector we're starting a new generation.
			model.getProgramSelector().onGenerationStart(pool);
			
			// Fill the population by performing genetic operations.
			while(nextPop.size() < model.getPopulationSize()) {
				// Pick a genetic operator using Pr, Pc and Pm.
				double random = model.getRNG().nextDouble();
				double pm = model.getMutationProbability();
				double pe = model.getCrossoverProbability();
				
				if (random < pe) {
					// Perform crossover.
					CandidateProgram<TYPE>[] children = crossover.crossover();
					for (CandidateProgram<TYPE> c: children) {
						if (nextPop.size() < model.getPopulationSize())
							nextPop.add(c);
					}
					// Add number of rejected crossovers.
					crossoverReversions += crossover.getRevertedCount();
				} else if (random < pe+pm) {
					// Perform mutation.
					nextPop.add(mutation.mutate());
					// Add number of rejected mutations.
					mutationReversions += mutation.getRevertedCount();
				} else {
					// Perform reproduction - Should this use clone?
					
					nextPop.add(pool.get(model.getRNG().nextInt(pool.size())));
				}
			}
			
			// Update new best program.
			for (CandidateProgram<TYPE> p: nextPop) {
				double fitness = model.getFitness(p);
				if (fitness < bestFitness) {
					bestFitness = fitness;
					bestProgram = p;
				}
			}
			
			// Generate stats for the current population.
			genStats.addGen(nextPop, i, crossoverReversions, mutationReversions);
			
			if (bestFitness <= model.getTerminationFitness()) {
				break genloop;
			}
			
			pop = nextPop;
		}
		
		runEndTime = System.nanoTime();
	}

	/**
	 * Retrieve the CandidateProgram with the best fitness found during the 
	 * run. This CandidateProgram may have been found in any of the generations.
	 * 
	 * @return the CandidateProgram with the best fitness score found.
	 */
	public CandidateProgram<TYPE> getBestProgram() {
		return bestProgram;
	}

	/**
	 * Retrieve the fitness score of the CandidateProgram returned by 
	 * getBestProgram(). A lower fitness score is considered better than a 
	 * higher fitness score.
	 * 
	 * @return the fitness score of the best CandidateProgram found during 
	 * 		   execution.
	 */
	public double getBestFitness() {
		return bestFitness;
	}
	
	/**
	 * Retrieve the time in nano-seconds of the run. This will be either the 
	 * length of the whole run if it's finished or the time up until now if 
	 * the run is not finished.
	 * @return The run time in nano-seconds.
	 */
	public long getRunTime() {
		if (runEndTime == -1) {
			return System.nanoTime() - runStartTime;
		} else {
			return runEndTime - runStartTime;
		}
	}
}
