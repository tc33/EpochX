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

import com.epochx.stats.*;


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
	 * @see GPRun
	 * @see GPModel
	 */
	public static <TYPE> GPRun<TYPE>[] run(GPModel<TYPE> model) {
		// Execute multiple runs, stashing the GPRun object for retrieval of run details.
		GPRun<TYPE>[] runs = new GPRun[model.getNoRuns()];
		
		// Keep track of the run stats.
		RunStats<TYPE> runStats = new RunStats<TYPE>();
		
		// This provides a shortcut for the common convention of making a model the listener.
		//TODO Actually might be better to allow models a way of giving their own listener for more flexibility.
		if (model instanceof RunStatListener) {
			runStats.addRunStatListener((RunStatListener) model);
		}
		
		for (int i=0; i<model.getNoRuns(); i++) {
			runs[i] = GPRun.run(model);
			
			// Generate stats for this run.
			runStats.addRun(runs[i], i+1);
		}

		return runs;
	}
}
