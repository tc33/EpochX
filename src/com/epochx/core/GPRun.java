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

import org.apache.log4j.*;

import com.epochx.core.representation.*;

/**
 * A GPRun object has 2 responsibilities. Firstly - to execute a GP run based upon 
 * control parameters provided. Secondly - after executing to provide basic details 
 * of how that run went. 
 * 
 * <p>Objects of this class should be immutable - however the 
 * underlying objects, in particular GPModel may not be.
 */
public class GPRun<TYPE> {

	static Logger logger = Logger.getLogger(GPRun.class);
	
	// The model describing the problem to be evolved.
	private GPModel<TYPE> model;
	
	// Information about how the run went.
	private CandidateProgram<TYPE> bestProgram;
	private double bestFitness;
	
	/*
	 * Private constructor. The static factory method run(GPModel) should be 
	 * used to create objects of GPRun and simultaneously execute them.
	 */
	private GPRun(GPModel<TYPE> model) {
		this.model = model;

		bestProgram = null;
		bestFitness = Double.POSITIVE_INFINITY;
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
		GPRun<TYPE> runner = new GPRun<TYPE>(model);
		runner.run();
		
		return runner;
	}
	
	/*
	 * This is the private method which actually does the work in this class. 
	 * It is also the workhorse of the whole API as it creates the initial 
	 * population, initiates the genetic operators and performs any elitism 
	 * or poule selection in use.
	 */
	private void run() {
		// Set things up.
		logger.info("Setting up GPCrossover");
		GPCrossover<TYPE> crossover = new GPCrossover<TYPE>(model);
		
		// Initialisation
		logger.info("Performing initialisation");
		List<CandidateProgram<TYPE>> pop = model.getInitialiser().getInitialPopulation();
		
		// For each generation.
		logger.info("Starting evolution");
		for (int i=1; i<=model.getNoGenerations(); i++) {
			logger.debug("Working on generation " + i);
			List<CandidateProgram<TYPE>> nextPop = new ArrayList<CandidateProgram<TYPE>>();
			
			// Perform elitism.
			List<CandidateProgram<TYPE>> elites = GPElitism.getElites(pop, model.getNoElites());
			for (CandidateProgram<TYPE> e: elites) {
				if (nextPop.size() < model.getPopulationSize())
					nextPop.add(e);
			}
			
			// Construct a breeding pool.
			List<CandidateProgram<TYPE>> poule = model.getPouleSelector().getPoule(pop, model.getPouleSize());
			
			// Fill the population by performing genetic operations.
			while(nextPop.size() < model.getPopulationSize()) {
				// Pick a genetic operator using Pr, Pc and Pm.
				double random = Math.random();
				double pm = model.getMutationProbability();
				double pe = model.getCrossoverProbability();
				
				if (random < pe) {
					// Do crossover.
					CandidateProgram<TYPE>[] children = crossover.crossover(poule);
					for (CandidateProgram<TYPE> c: children) {
						if (nextPop.size() < model.getPopulationSize())
							nextPop.add(c);
					}
				} else if (random < pe+pm) {
					// Do mutation.
					//TODO Implement mutation.
				} else {
					// Do reproduction. - Should this use clone?
					nextPop.add(poule.get((int) Math.floor(Math.random()*poule.size())));
				}
			}
			
			// Update new best program.
			for (CandidateProgram<TYPE> p: pop) {
				double fitness = model.getFitness(p);
				if (fitness < bestFitness) {
					bestFitness = fitness;
					bestProgram = p;
				}
			}
			
			pop = nextPop;
		}
		
		logger.info("Best program: " + bestProgram);
		logger.info("Fitness: " + bestFitness);
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
}
