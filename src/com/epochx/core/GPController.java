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

import org.apache.log4j.*;


/**
 * The GPController class provides the method for executing multiple GP runs 
 * and so can be considered the key class for starting using EpochX. Even when 
 * only executing one GP run, this method should be used rather than directly 
 * calling <code>GPRun.run(GPModel)</code>.
 * 
 * <p>To evolve a solution with EpochX the <code>GPController.run(GPModel)</code> 
 * method should be called, passing in a GPModel which defines the control 
 * parameters.
 */
public class GPController {
	
	static Logger logger = Logger.getLogger(GPController.class);

	/**
	 * Executes one or more GP runs. The number of runs is set within the model 
	 * provided as an argument. This GPModel also supplies all the other 
	 * control parameters for the runs - each run is executed using this same 
	 * model object so will have identical parameters for comparison unless 
	 * changed during execution, for example, in another thread. (Although note 
	 * that unless stated elsewhere, this version of EpochX is not considered 
	 * thread safe).
	 * @param <TYPE> The return type of the <code>CandidatePrograms</code> 
	 * 				 generated.
	 * @param model  The GPModel which defines the control parameters for the 
	 * 				 runs.
	 * @return An array of the GPRun objects which were executed on the model.
	 * 		   The details of each run can be retrieved from this object.
	 */
	public static <TYPE> GPRun<TYPE>[] run(GPModel<TYPE> model) {
		// Setup logging for API.
		PropertyConfigurator.configure("logger_config");
		
		logger.info("Starting execution of EpochX controller");
		
		// Log details of the control parameters.
		logger.debug("Terminals = " + model.getTerminals());
		logger.debug("Functions = " + model.getFunctions());
		logger.debug("No runs = " + model.getNoRuns());
		logger.debug("No generations = " + model.getNoGenerations());
		logger.debug("Crossover probability = " + model.getCrossoverProbability());
		logger.debug("Reproduction probability = " + model.getReproductionProbability());
		logger.debug("Mutation probability = " + model.getMutationProbability());
		logger.debug("Population size = " + model.getPopulationSize());
		logger.debug("Poule size = " + model.getPouleSize());
		logger.debug("Max depth = " + model.getMaxDepth());
		logger.debug("No elites = " + model.getNoElites());

		// Execute multiple runs, stashing the GPRun object for retrieval of run details.
		GPRun<TYPE>[] runs = new GPRun[model.getNoRuns()];
		
		for (int i=0; i<model.getNoRuns(); i++) {
			logger.info("Starting GP run " + i);
			runs[i] = GPRun.run(model);
		}
		logger.info("Completed execution of EpochX controller");
		
		return runs;
	}
}
