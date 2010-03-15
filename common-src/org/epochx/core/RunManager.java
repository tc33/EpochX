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

import java.util.List;

import org.epochx.life.GenerationAdapter;
import org.epochx.life.LifeCycleManager;
import org.epochx.model.Model;
import org.epochx.representation.CandidateProgram;
import org.epochx.stats.*;

/**
 * Instances of this class are responsible for executing single evolutionary 
 * runs. Execution will be controlled by parameters retrieved from the 
 * <code>Model</code> provided to the constructor.
 * 
 * <p>
 * Users of the EpochX framework would not typically create and use instances 
 * of this class directly, but rather would through use of the 
 * <code>Controller's</code> <code>run</code> class method which will execute 
 * an instance of this class multiple times.
 * 
 * <p>
 * Instances of this class will update the {@link StatsManager} class with data
 * about the run as the run progresses. For the statistics available from the 
 * <code>StatsManager</code>, view the {@link StatField} class and any 
 * extending classes.
 * 
 */
public class RunManager {
		
	// Core components.
	private final GenerationManager generation;
	private final InitialisationManager initialisation;
	
	// The best program found so far during the run.
	private CandidateProgram bestProgram;
	
	// The fitness of the best program found so far during the run.
	private double bestFitness;
	
	private int noGenerations;
	
	private double terminationFitness;
	
	/**
	 * Constructs an instance of RunManager to be controlled by parameters 
	 * retrieved from the given <code>Model</code>. Fitness evaluation will 
	 * also be diverted to the given model.
	 * 
	 * @param model the model which will control the run with the parameters 
	 * 				and fitness function to use.
	 */
	public RunManager() {
		// Setup core components.
		generation = new GenerationManager();
		initialisation = new InitialisationManager();
		
		// Update the model each generation.
		LifeCycleManager.getLifeCycleManager().addGenerationListener(new GenerationAdapter() {
			@Override
			public void onGenerationStart() {
				updateModel();
			}
		});
	}
	
	/*
	 * Initialise the run manager for a new run.
	 */
	private void setup() {
		bestProgram = null;
		bestFitness = Double.POSITIVE_INFINITY;
	}
	
	private void updateModel() {
		noGenerations = Controller.getModel().getNoGenerations();
		terminationFitness = Controller.getModel().getTerminationFitness();
	}
	
	/**
	 * Executes a single evolutionary run of this <code>RunManager's</code> 
	 * <code>Model</code>.
	 * 
	 * @param runNo the sequential number which identifies this run out of the 
	 * 				set of runs being performed.
	 */
	public void run(final int runNo) {
		// Setup the run manager for a new run
		setup();
		
		// Inform everyone we're starting a run.
		LifeCycleManager.getLifeCycleManager().onRunStart();
		
		// Record the start time.
		final long startTime = System.nanoTime();

		// Add the run number to the available stats data.
		StatsManager.getStatsManager().addRunData(RUN_NUMBER, runNo);
		
		// Perform initialisation.
		List<CandidateProgram> pop = initialisation.initialise();
		
		// Record best program so far and its fitness.
		updateBestProgram(pop);

		// Execute each generation.
		for (int gen=1; gen<=noGenerations; gen++) {
			// Perform the generation.
			pop = generation.generation(gen, pop);
			
			// Keep track of the best program and fitness.
			updateBestProgram(pop);
			
			// We might be finished?
			if (bestFitness <= terminationFitness) {
				LifeCycleManager.getLifeCycleManager().onSuccess();
				break;
			}
		}

		// Inform everyone the run has ended.
		LifeCycleManager.getLifeCycleManager().onRunEnd();
		
		// Calculate how long the run took.
		final long runtime = System.nanoTime() - startTime;
		
		// Add run time to stats data.
		StatsManager.getStatsManager().addRunData(RUN_TIME, runtime);
	}

	/* 
	 * Update new best program.
	 * Note this forces us to evaluate all programs, which we might
	 * not have done if using TournamentSelection. 
	 */
	private void updateBestProgram(final List<CandidateProgram> pop) {
		for (CandidateProgram program: pop) {
			final double fitness = program.getFitness();
			if (fitness < bestFitness) {
				bestFitness = fitness;
				bestProgram = program;
				
				// Update the stats.
				StatsManager.getStatsManager().addRunData(RUN_FITNESS_MIN, bestFitness);
				StatsManager.getStatsManager().addRunData(RUN_FITTEST_PROGRAM, bestProgram);
			}
		}
	}
}
