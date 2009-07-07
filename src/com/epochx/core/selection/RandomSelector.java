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
package com.epochx.core.selection;

import java.util.*;

import com.epochx.core.representation.*;

/**
 * A random selector is a program and pool selector which provides no selection 
 * pressure. No reference is made to the programs' fitness scores.
 */
public class RandomSelector<TYPE> implements ProgramSelector<TYPE>, PoolSelector<TYPE> {

	// The current population from which programs should be chosen.
	private List<CandidateProgram<TYPE>> pop;
	
	/**
	 * This method should only be called by the GP system. It is used to 
	 * provide the population for the current generation.
	 * 
	 * @param pop the current population for this generation.
	 */
	@Override
	public void onGenerationStart(List<CandidateProgram<TYPE>> pop) {
		this.pop = pop;
	}
	
	/**
	 * Randomly chooses and returns a program from the population with no bias.
	 * 
	 * @return a randomly selected program.
	 */
	@Override
	public CandidateProgram<TYPE> getProgram() {		
		return pop.get((int) Math.floor(Math.random()*pop.size()));
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
	public List<CandidateProgram<TYPE>> getPool(List<CandidateProgram<TYPE>> pop, int poolSize) {
		// If poolSize is 0 or less then we use the whole population.
		if (poolSize <= 0) {
			return pop;
		}
		
		// Construct our pool.
		List<CandidateProgram<TYPE>> pool = new ArrayList<CandidateProgram<TYPE>>(poolSize);
		for (int i=0; i<poolSize; i++) {
			pool.add(pop.get((int) Math.floor(Math.random()*pop.size())));
		}
		
		return pool;
	}
}