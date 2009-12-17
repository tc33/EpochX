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
 * This enum gives all the available run statistics that can be 
 * requested. Run statistics are those statistics that are generated 
 * at the end of each run. Each RunStatField has a datatype related 
 * to it which is the type which the data will be returned in.
 * @see org.epochx.stats.RunStatListener
 * @see org.epochx.stats.CrossoverStatField
 * @see org.epochx.stats.MutationStatField
 * @see org.epochx.stats.GenerationStatField
 */
public enum RunStatField {
	/**
	 * Requests a GPCandidateProgram which has the 'best' fitness over a whole 
	 * run. This usually means the program with the lowest fitness score. 
	 * If elitism is not being used, the BEST_PROGRAM may not be in the final 
	 * generation. 
	 */
	BEST_PROGRAM,
	
	/**
	 * Requests a Double which is the fitness of the BEST_PROGRAM.
	 */
	BEST_FITNESS,
	
	/**
	 * Requests a Long which is the length of time in nanoseconds that the 
	 * run took to complete.
	 */
	RUN_TIME
}
