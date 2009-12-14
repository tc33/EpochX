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

import org.epochx.representation.*;


/**
 * PoolSelectors are for selecting a group of individuals from a population of 
 * programs. Most selectors will choose the individuals based in some way upon 
 * the fitness of the programs. In many circumstances PoolSelectors will work 
 * in the same way as ProgramSelectors and often implemented by the same class.
 */
public interface PoolSelector<TYPE> {

	
	/**
	 * Select a <code>GPCandidateProgram</code> from the current population of 
	 * programs. The method of selection would normally be based upon the 
	 * fitness of the program but there is no need for it to be, and there are 
	 * exceptions.
	 * 
	 * @param pop the population from which the pool should be constructed.
	 * @param poolSize the number of programs that should be selected to create
	 * 				   the pool. 
	 * @return a List of CandidatePrograms selected from the current population 
	 * of programs to form a program pool.
	 */
	public List<GPCandidateProgram<TYPE>> getPool(List<GPCandidateProgram<TYPE>> pop, int poolSize);
	
}
