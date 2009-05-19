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
package com.epochx.stats;

/**
 * This enum gives all the available run statistics that can be 
 * requested. Run statistics are those statistics that are generated 
 * at the end of each run. Each RunStatField has a datatype related 
 * to it which is the type which the data will be returned in.
 * @see com.epochx.stats.RunStatListener
 * @see com.epochx.stats.CrossoverStats.CrossoverStatField
 * @see com.epochx.stats.MutationStats.MutationStatField
 * @see com.epochx.stats.GenerationStats.GenerationStatField
 */
public enum RunStatField {
	/**
	 * Requests a CandidateProgram which has the 'best' fitness over a whole 
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
