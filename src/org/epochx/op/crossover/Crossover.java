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
package org.epochx.op.crossover;

import org.epochx.representation.*;

/**
 * This interface defines the structure which specific crossover operations can
 * implement to provide different methods of crossing over two 
 * <code>CandidatePrograms</code>. Crossover instances are used by the core 
 * GPCrossover class to perform a single crossover operation.
 * 
 * @see org.epochx.core.GPCrossover
 */
public interface Crossover<TYPE> {

	/**
	 * Implementations should perform some form of exchange of material between 
	 * the two children, returning the resultant children.
	 * 
	 * @param parent1 The first CandidateProgram selected to undergo this 
	 * 				  crossover operation.
	 * @param parent2 The second CandidateProgram selected to undergo this 
	 * 				  crossover operation.
	 * @return An array of the child CandidatePrograms that were the result of 
	 * an exchange of genetic material between the two parents.
	 */
	public CandidateProgram<TYPE>[] crossover(CandidateProgram<TYPE> parent1, 
											  CandidateProgram<TYPE> parent2);
	
}
