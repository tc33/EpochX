/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming for research
 *
 *  EpochX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EpochX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EpochX.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.epochx.core;

import java.util.*;

import com.epochx.life.*;
import com.epochx.representation.*;

/**
 * This class has the task of retrieving the set of elites from a population.
 */
public class GPElitism<TYPE> implements GenerationListener {

	// The controlling model.
	private GPModel<TYPE> model;
	
	// Manager of life cycle events.
	private LifeCycleManager<TYPE> lifeCycle;

	// The number of elites to be used.
	private int noElites;
	
	/**
	 * Constructs an instance of GPElitism which will perform the elitism 
	 * operation. 
	 * 
	 * @param model the GPModel which defines the run parameters and life
	 * 				cycle listeners.
	 */
	public GPElitism(GPModel<TYPE> model) {
		this.model = model;
		
		// Register interest in generation events so we can reset.
		lifeCycle = GPController.getLifeCycleManager();
		lifeCycle.addGenerationListener(this);
		
		// Initialise parameters.
		initialise();
	}
	
	/*
	 * Initialises GPElitism, in particular all parameters from the model should
	 * be refreshed incase they've changed since the last call.
	 */
	private void initialise() {
		// Discover how many elites we need.
		noElites = model.getNoElites();
		int popSize = model.getPopulationSize();
		noElites = (noElites < popSize) ? noElites : popSize;
	}
	
	/**
	 * Gets the best <code>CandidatePrograms</code> from the given population 
	 * and returns them. The number of programs returned will be determined by 
	 * a call to the model's <code>getNoElites()</code> method. If this method 
	 * returns a value greater than the allowable population size then the 
	 * population size will be used. Elites in EpochX are defined as the very 
	 * best programs in a population.
	 * 
	 * <p>After selection and before returning, the model's life cycle listener
	 * will be informed of the elitism operation with a call to 
	 * <code>onElitism()</code>. Unlike many of the other life cycle methods, 
	 * it is not possible to 'revert' an elitism event by returning null. This 
	 * is because elitism is a deterministic operation, and so re-running would
	 * lead to the same result.
	 * 
	 * @param pop	 	the population from which elites need to be retrieved.
	 * @return a list containing the best CandidatePrograms determined by 
	 * 		   fitness. If the models required number of elites is equal to or 
	 * 		   greater than the population size then the returned list will 
	 * 		   contain all CandidatePrograms from the population sorted.
	 */
	public List<CandidateProgram<TYPE>> getElites(List<CandidateProgram<TYPE>> pop) {		
		// Construct an array for elites.
		List<CandidateProgram<TYPE>> elites;
		
		if (noElites > 0) {			
			// Sort the population and scoop off the best noElites.
			Collections.sort(pop);
			elites = new ArrayList<CandidateProgram<TYPE>>(pop.subList(pop.size()-noElites, pop.size()));
		} else {
			elites = new ArrayList<CandidateProgram<TYPE>>();
		}
		
		// Allow life cycle listener to confirm or modify.
		elites = lifeCycle.onElitism(elites);
		
		return elites;
	}
	
	/**
	 * Called after each generation. For each generation we should reset all 
	 * parameters taken from the model incase they've changed. The generation
	 * event is then CONFIRMed.
	 */
	@Override
	public void onGenerationStart() {
		// Reset.
		initialise();
	}
	
}
