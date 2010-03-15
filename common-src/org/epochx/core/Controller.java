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

import org.epochx.life.LifeCycleManager;
import org.epochx.model.Model;
import org.epochx.stats.StatsManager;

/**
 * The entry point for evolution in the <a href="http://www.epochx.org" 
 * target="_parent">EpochX</a> framework. 
 * 
 * <p>
 * This class simply provides a <code>static</code> method to start execution 
 * of an evolutionary run (or multiple runs). An evolutionary run can be 
 * started with a call to the class's <code>Controller.run(Model)</code> 
 * method. This will trigger a series of executions which will proceed 
 * according to the parameters retrieved from the provided {@link Model}. 
 * 
 * Prior to calling the <code>run</code> method it is typical to arrange for 
 * some form of output to be generated each generation, each run or even each 
 * crossover or mutation. A wide range of statistics are 
 * available from the {@link StatsManager}. A listener model is employed 
 * through the {@link LifeCycleManager} to allow events such as a generation 
 * starting, or crossover being carried out, to be handled and responded to. 
 * This is commonly combined with the <code>StatsManager</code> to output 
 * statistics each generation or run.
 * 
 * <p>
 * Note that use of this class is not thread safe and use of threads with 
 * EpochX in general is highly discouraged.
 * 
 * @see RunManager
 */
public class Controller {

	// Singleton controller instance.
	private static Controller controller;
	
	// The current controlling model.
	private Model model;
	
	// The run component.
	private RunManager run;
	
	/*
	 * Private constructor. Execution should be through the static methods.
	 */
	private Controller() {
		// The run controller.
		run = new RunManager();
	}
	
	/**
	 * Executes one or more evolutionary runs configured according to the 
	 * provided <code>Model</code>. The number of runs and other control 
	 * parameters are loaded from the <code>Model</code> and so each run will
	 * use the same parameters unless the model returns different values 
	 * throughout execution. All parameters will be reloaded at the start of 
	 * each generation allowing dynamic parameters to be used.
	 * 
	 * <p>
	 * In order to interact with the progress of the evolution, use of the 
	 * <code>StatsManager</code> and potentially the <code>LifeCycleManager
	 * </code> will be required prior to calling this method.
	 * 
	 * @param model  the <code>model</code> which defines the control 
	 * 				 parameters for all the runs.
	 * @see RunManager
	 */
	public static void run(final Model model) {
		// Ensure our singleton instance has been constructed.
		if (controller == null) {
			controller = new Controller();
		}
		
		// Set the model.
		controller.setModel(model);
		
		// Set the stats engine straight away so it can be used.
		StatsManager.getStatsManager().setStatsEngine(model.getStatsEngine());
		
		// Execute the runs.
		controller.execute();
	}
	
	public static Model getModel() {
		return controller.model;
	}
	
	/*
	 * Set the controlling model.
	 */
	private void setModel(Model model) {
		this.model = model;
	}	

	/*
	 * Execute the runs.
	 */
	private void execute() {
		// Execute all the runs.
		for (int i=0; i<model.getNoRuns(); i++) {
			run.run(i);
		}
	}
}
