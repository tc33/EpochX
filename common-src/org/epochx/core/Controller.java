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

import javax.swing.JFormattedTextField;

import org.epochx.core.Controller;
import org.epochx.life.*;
import org.epochx.model.Model;
import org.epochx.stats.*;

/**
 * The entry point for evolution in <a href="http://www.epochx.org" 
 * target="_parent">EpochX</a>. This class provides a number of <code>static
 * </code> methods to start execution of an evolutionary run (or multiple 
 * runs) and to interact with a run while it proceeds.
 * 
 * <p>
 * An evolutionary run can be started with a call to the class's 
 * <code>Controller.run(Model)</code> method. This will trigger a series of 
 * executions which will proceed according to the parameters retrieved from the
 * provided {@link Model}. Prior to calling the <code>run</code> method it is 
 * typical to arrange for some form of output to be generated each generation, 
 * each run or even each crossover or mutation. A wide range of statistics are 
 * available from the {@link StatsManager}. An instance of <code>StatsManager
 * </code> is retrievable through this class's <code>getStatsManager()</code>
 * <code>static</code> method. A listener model is employed through the 
 * {@link LifeCycleManager} to allow events such as a generation starting, or 
 * crossover being carried out, to be handled and responded to. This is 
 * commonly combined with the <code>StatsManager</code> to output statistics 
 * each generation or run.
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
	
	// The run controller.
	private static RunManager run;
	
	// The manager that keeps track of life cycle events and their listeners.
	private LifeCycleManager lifeCycle;
	
	// The manager that holds and processes run data.
	private StatsManager stats;
	
	/*
	 * Private constructor. Execution should be through the static methods.
	 */
	private Controller() {
		// Setup primary components.
		lifeCycle = new LifeCycleManager();
		stats = new StatsManager();
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
	public static void run(Model model) {
		// Ensure our singleton instance has been constructed.
		if (controller == null) {
			controller = new Controller();
		}
		
		// Set the stats engine straight away so it can be used.
		controller.stats.setStatsEngine(model.getStatsEngine());
		
		// Execute all the runs.
		for (int i=0; i<model.getNoRuns(); i++) {
			run.run(model, i);
		}
	}
	
	/**
	 * Returns the life cycle manager which handles all life cycle events 
	 * throughout execution of the <code>run</code> method. The life cycle 
	 * manager receives details of all events and then informs the necessary 
	 * listeners. Most use is through the <code>addXXXListener</code> methods,
	 * and typically with an anonymous class.
	 * 
	 * <h4>Example use of <code>LifeCycleManager's</code> listener model:</h4>
	 * 
	 * <pre>
     * Controller.getLifeCycleManager().addRunListener(new RunAdapter() {
	 *     public void onRunStart() {
	 *         //... do something ...
	 *     }
	 * });
	 * </pre>
	 * 
	 * @return the life cycle manager instance that manages life cycle events 
	 * throughout execution with this <code>Controller</code>.
	 */
	public static LifeCycleManager getLifeCycleManager() {
		// Ensure our singleton instance has been constructed.
		if (controller == null) {
			controller = new Controller();
		}

		return controller.lifeCycle;
	}
	
	/**
	 * Returns the stats manager which is responsible for generating and 
	 * distributing data and statistics about runs as they progress. 
	 * 
	 * <p>
	 * It would be very common to combine use of the stats manager with use of 
	 * the life cycle manager in order to retrieve statistics each generation, 
	 * each run or upon some other event.
	 * 
	 * <h4>Example use of statistics generation:</h4>
	 * 
	 * <pre>
     * Controller.getLifeCycleManager().addRunListener(new RunAdapter() {
	 *     public void onRunStart() {
	 *         Controller.getStatsManager().printRunStats(new String[]{RUN_NUMBER});
	 *     }
	 * });
	 * </pre>
	 * 
	 * <p>
	 * The above code example will print the run number to the console at the 
	 * start of each run.
	 * 
	 * @return the stats manager which handles data and statistics about runs 
	 * performed with this <code>Controller</code>.
	 */
	public static StatsManager getStatsManager() {
		// Ensure our singleton instance has been constructed.
		if (controller == null) {
			controller = new Controller();
		}
		
		return controller.stats;
	}
}
