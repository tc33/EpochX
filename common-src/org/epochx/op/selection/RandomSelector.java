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
package org.epochx.op.selection;

import java.util.*;

import org.epochx.core.*;
import org.epochx.model.Model;
import org.epochx.op.*;
import org.epochx.representation.*;


/**
 * A random selector is a program and pool selector which provides no selection 
 * pressure. No reference is made to the programs' fitness scores.
 */
public class RandomSelector implements ProgramSelector, PoolSelector {

	// The current controlling model.
	private Model model;
	
	// The current population from which programs should be chosen.
	private List<CandidateProgram> pop;
	
	public RandomSelector(Model model) {
		this.model = model;
	}
	
	/**
	 * This method should only be called by the GP system. It is used to 
	 * provide the population for the current generation.
	 * 
	 * @param pop the current population for this generation.
	 */
	@Override
	public void setSelectionPool(List<CandidateProgram> pop) {
		this.pop = pop;
	}
	
	/**
	 * Randomly chooses and returns a program from the population with no bias.
	 * 
	 * @return a randomly selected program.
	 */
	@Override
	public CandidateProgram getProgram() {		
		return pop.get(model.getRNG().nextInt(pop.size()));
	}

	/**
	 * Randomly chooses programs from the given population up to a total of 
	 * <code>poolSize</code> and returns them as a list. The generated pool may 
	 * contain duplicate programs, and as such the pool size can be greater 
	 * than the population size.
	 * 
	 * @param pop the population of CandidatePrograms from which the programs 
	 * 			  in the pool should be chosen.
	 * @param poolSize the number of programs that should be selected from the 
	 * 			 	   population to form the pool. If poolSize is less than 
	 * 				   zero then no selection takes place and the given  
	 * 				   population is returned unaltered.
	 * @return the randomly selected pool of candidate programs.
	 */
	@Override
	public List<CandidateProgram> getPool(List<CandidateProgram> pop, int poolSize) {
		// If poolSize is 0 or less then we use the whole population.
		if (poolSize <= 0) {
			return pop;
		}
		
		// Construct our pool.
		List<CandidateProgram> pool = new ArrayList<CandidateProgram>(poolSize);
		for (int i=0; i<poolSize; i++) {
			pool.add(pop.get(model.getRNG().nextInt(pop.size())));
		}
		
		return pool;
	}
}
