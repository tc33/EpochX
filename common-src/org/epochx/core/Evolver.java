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

import java.util.*;

import org.epochx.life.Life;
import org.epochx.stats.Stats;


/**
 * An evolver is responsible for execution of models. It maintains the life 
 * cycle and stats for the models it is executing.
 */
public class Evolver {

	// The life cycle manager for all models.
	private final Life life;
	
	// Separate stats instance for each model.
	private final Map<Model, Stats> stats;

	// The sequential list of models to be executed.
	private final List<Model> models;
	
	// Manager for all evolutionary runs.
	private final RunManager runner;
	
	/**
	 * Constructs an <code>Evolver</code> with any provided models enqueued.
	 * @param models any models to be enqueued for execution.
	 */
	public Evolver(Model ... models) {
		// Setup infrastructure.
		life = new Life();
		stats = new HashMap<Model, Stats>();
		runner = new RunManager(this);
		
		this.models = new ArrayList<Model>(models.length);
		
		// Enqueue each model individually.
		for (Model model: models) {
			enqueue(model);
		}
	}
	
	/**
	 * Adds the given model to the end of the queue of models to be executed.
	 * 
	 * @param model a model to be executed.
	 */
	public void enqueue(Model model) {
		models.add(model);
	}
	
	/**
	 * Sequentially executes each model.
	 */
	public void run() {
		for (int i=0; i<models.size(); i++) {
			Model model = models.remove(0);
			
			// Configure all components and operators for the model.
			life.fireConfigureEvent(model);
			
			// Validate the model.
			if (!model.isRunnable()) {
				throw new IllegalStateException("model is not in a runnable state");
			}
			
			// Execute all the runs.
			int noRuns = model.getNoRuns();
			for (int runNo = 0; runNo < noRuns; runNo++) {
				runner.run(runNo);
			}
			
			// Clean up model.
			
		}
	}
	
	/**
	 * Returns the life cycle manager for all models undergoing evolution.
	 * 
	 * @return the Life instance that is managing life cycle events for the 
	 * models undergoing evolution.
	 */
	public Life getLife() {
		return null;
	}
	
	/**
	 * Returns the stats manager for the specified model, or null if the given
	 * model is not undergoing evolution. Each model undergoing evolution has 
	 * its own Stats instance.
	 * 
	 * @param model the model to obtain the stats for.
	 * @return the Stats instance for the given model, or <code>null</code> if 
	 * the model is unknown.
	 */
	public Stats getStats(Model model) {
		return stats.get(model);
	}
}
