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

import org.epochx.life.*;
import org.epochx.model.Model;
import org.epochx.representation.CandidateProgram;

/**
 * This component handles the elitism operation to ensure the survival of the 
 * most fit programs in a generation of an evolutionary run. This class 
 * performs this task of scooping off and returning the best programs from a 
 * population. The number of programs taken is decided by a call to the 
 * model's <code>getNoElites</code> method.
 * 
 * <p>
 * Use of the elitism operation will generate the following events:
 * 
 * <table border="1">
 *     <tr>
 *         <th>Event</th>
 *         <th>Revert</th>
 *         <th>Modify</th>
 *         <th>Raised when?</th>
 *     </tr>
 *     <tr>
 *         <td>onElitismStart</td>
 *         <td>no</td>
 *         <td>no</td>
 *         <td>Before the elitism operation is carried out.
 *         </td>
 *     </tr>
 *     <tr>
 *         <td>onElitism</td>
 *         <td>no</td>
 *         <td><strong>yes</strong></td>
 *         <td>After the elitism operation is carried out.
 *         </td>
 *     </tr>
 *     <tr>
 *         <td>onElitismEnd</td>
 *         <td>no</td>
 *         <td>no</td>
 *         <td>After the elitism operation has been completed.
 *         </td>
 *     </tr>
 * </table>
 */
public class ElitismManager {

	// The number of elites to be used.
	private int noElites;
	
	/**
	 * Constructs an instance of <code>Elitism</code> which will perform the 
	 * evolutionary operation of elitism. 
	 * 
	 * @param model the Model which defines the run parameters such as number
	 * 				of elites to use.
	 */
	public ElitismManager() {		
		// Initialise on each generation.
		LifeCycleManager.getLifeCycleManager().addGenerationListener(new GenerationAdapter() {
			@Override
			public void onGenerationStart() {
				updateModel();
			}
		});
	}
	
	/*
	 * Initialises Elitism, in particular all parameters from the model should
	 * be refreshed in case they've changed since the last call.
	 */
	private void updateModel() {
		// Discover how many elites we need.
		noElites = Controller.getModel().getNoElites();
		int popSize = Controller.getModel().getPopulationSize();
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
	 * @param pop the population from which elites are to be retrieved.
	 * @return a list containing the best CandidatePrograms determined by 
	 * 		   fitness. If the models required number of elites is equal to or 
	 * 		   greater than the population size then the returned list will 
	 * 		   contain all CandidatePrograms from the population sorted.
	 */
	public List<CandidateProgram> elitism(final List<CandidateProgram> pop) {
		LifeCycleManager.getLifeCycleManager().onElitismStart();
		
		// Construct an array for elites.
		List<CandidateProgram> elites;
		
		if (noElites > 0) {			
			// Sort the population and scoop off the best noElites.
			Collections.sort(pop);
			elites = new ArrayList<CandidateProgram>(pop.subList(pop.size()-noElites, pop.size()));
		} else {
			elites = new ArrayList<CandidateProgram>();
		}
		
		// Allow life cycle listener to confirm or modify.
		elites = LifeCycleManager.getLifeCycleManager().onElitism(elites);
		
		LifeCycleManager.getLifeCycleManager().onElitismEnd();
		
		return elites;
	}

}
