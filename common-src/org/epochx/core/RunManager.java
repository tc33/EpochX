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

import java.util.List;

import org.epochx.gp.model.GPModel;
import org.epochx.model.Model;
import org.epochx.representation.CandidateProgram;

public class RunManager {
	
	// The model describing the problem to be evolved.
	private Model model;
	
	// Core components.
	private GenerationManager generation;
	private InitialisationManager initialisation;
	
	// The best program found so far during the run.
	private CandidateProgram bestProgram;
	
	// The fitness of the best program found so far during the run.
	private double bestFitness;
	
	/*
	 * Private constructor. The static factory method run(GPModel) should be 
	 * used to create objects of GPRun and simultaneously execute them.
	 */
	public RunManager() {
		// Initialise the run.
		bestProgram = null;
		bestFitness = Double.POSITIVE_INFINITY;
		
		// Setup core components.
		generation = new GenerationManager(model);
		initialisation = new InitialisationManager(model);
	}
	
	/**
	 * Construct a new GPRun object and execute it. The GPModel passed in is
	 * used to provide the control parameters for the run.
	 * @param <TYPE> the type of <code>GPCandidateProgram</code> to be evolved.
	 * @param model  the model which will control the run with the parameters 
	 * 				 and fitness function to use.
	 * @return 		 The GPRun object that was executed, containing retrievable 
	 * 				 details about the run.
	 * @see GPModel
	 */
	public void run(Model model, int runNo) {
		// Tell life cycle listener that a run is starting.
		Controller.getLifeCycleManager().onRunStart();
		
		long runStartTime = System.nanoTime();

		Controller.getStatsManager().addRunData(RUN_NUMBER, runNo);
		
		// Perform initialisation.
		List<CandidateProgram> pop = initialisation.initialise();
		
		// Keep track of the best program and fitness.
		updateBestProgram(pop);
		
		// Will be set to true when program of termination fitness achieved.
		boolean fitnessSuccess = false;
		
		// Execute each generation.
		for (int gen=1; gen<=model.getNoGenerations(); gen++) {			
			pop = generation.generation(gen, pop);
			
			// Keep track of the best program and fitness.
			updateBestProgram(pop);
			
			// We might be finished?
			if (bestFitness <= model.getTerminationFitness()) {
				fitnessSuccess = true;
				break;
			}
		}
		
		if (fitnessSuccess) {
			Controller.getLifeCycleManager().onRunEnd();
		} else {
			Controller.getLifeCycleManager().onRunEnd();
		}
		
		long runtime = System.nanoTime() - runStartTime;
		
		Controller.getStatsManager().addRunData(RUN_TIME, runtime);
	}

	/* 
	 * Update new best program.
	 * Note this forces us to evaluate all programs, which we might
	 * not have done if using TournamentSelection. 
	 */
	private void updateBestProgram(List<CandidateProgram> pop) {
		for (CandidateProgram program: pop) {
			double fitness = program.getFitness();
			if (fitness < bestFitness) {
				bestFitness = fitness;
				bestProgram = program;
				
				// Update the stats.
				Controller.getStatsManager().addRunData(RUN_FITNESS_MIN, bestFitness);
				Controller.getStatsManager().addRunData(RUN_FITTEST_PROGRAM, bestProgram);
			}
		}
	}
}
