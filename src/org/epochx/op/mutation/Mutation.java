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
package org.epochx.op.mutation;

import org.epochx.core.*;
import org.epochx.representation.*;

/**
 * This interface defines the structure which specific mutation operators can
 * implement to provide different methods of mutating a <code>GPCandidateProgram</code>. 
 * Mutation instances are used by the core GPMutation class to perform a single 
 * mutation operation.
 * 
 * @see GPMutation
 */
public interface Mutation<TYPE> {

	/**
	 * Implementations should perform some form of alteration to the genetic 
	 * material of the given GPCandidateProgram, returning the resultant program.
	 * 
	 * @param program The GPCandidateProgram selected to undergo this mutation 
	 * 				  operation.
	 * @return A GPCandidateProgram that was the result of altering the provided 
	 * GPCandidateProgram.
	 */
	public GPCandidateProgram<TYPE> mutate(GPCandidateProgram<TYPE> program);
	
}
