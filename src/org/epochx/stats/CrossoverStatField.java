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
package org.epochx.stats;

/**
 * This enum gives all the available crossover statistics that can be 
 * requested. Crossover statistics are those statistics that are generated 
 * per crossover operation. Each CrossoverStatField has a datatype related 
 * to it which is the type which the data will be returned in.
 * @see org.epochx.stats.CrossoverStatListener
 * @see org.epochx.stats.GenerationStatField
 * @see org.epochx.stats.MutationStatField
 * @see org.epochx.stats.RunStatField
 */
public enum CrossoverStatField {
	/** 
	 * Requests a GPCandidateProgram[] (typically with 2 elements) which are 
	 * the parents that were crossed-over to give the children.
	 **/
	PARENTS,
	
	/**
	 * Requests a GPCandidateProgram[] (typically with 2 elements) which are 
	 * the children that resulted from crossing over the parents.
	 **/
	CHILDREN,
	
	/**
	 * Requests a Long which is the length of time in nanoseconds that the 
	 * crossover operation took to complete.
	 */
	RUN_TIME,
	
	/**
	 * Requests an Integer which is the number of crossovers that were 
	 * reverted due to model rejection.
	 */
	REVERTED_CROSSOVERS
}