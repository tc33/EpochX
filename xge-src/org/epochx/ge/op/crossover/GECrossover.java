/*  
 *  Copyright 2009 Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of XGE: grammatical evolution for research
 *
 *  XGE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  XGE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with XGE.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.epochx.ge.op.crossover;

import org.epochx.op.Crossover;
import org.epochx.representation.CandidateProgram;

/**
 * This interface defines the structure which specific crossover operations can
 * implement to provide different methods of crossing over two 
 * <code>CandidatePrograms</code>. GPCrossover instances are used by the core 
 * GECrossover class to perform a single crossover operation.
 * 
 * @see org.epochx.ge.core.GECrossover
 */
public interface GECrossover extends Crossover {

	/**
	 * Implementations should perform some form of exchange of material between 
	 * the two children, returning the resultant children.
	 * 
	 * @param parent1 The first GECandidateProgram selected to undergo this 
	 * 				  crossover operation.
	 * @param parent2 The second GECandidateProgram selected to undergo this 
	 * 				  crossover operation.
	 * @return An array of the child CandidatePrograms that were the result of 
	 * an exchange of genetic material between the two parents.
	 */
	@Override
	public CandidateProgram[] crossover(CandidateProgram parent1, CandidateProgram parent2);
	
}
