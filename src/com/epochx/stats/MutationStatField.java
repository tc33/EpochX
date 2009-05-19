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
 * This enum gives all the available mutation statistics that can be 
 * requested. Mutation statistics are those statistics that are generated 
 * per mtuation operation. Each MutationStatField has a datatype related 
 * to it which is the type which the data will be returned in.
 * @see com.epochx.stats.MutationStatListener
 * @see com.epochx.stats.GenerationStats.GenerationStatField
 * @see com.epochx.stats.CrossoverStats.CrossoverStatField
 * @see com.epochx.stats.RunStats.RunStatField
 */
public enum MutationStatField {
	
	/**
	 * Requests a CandidateProgram which is a clone of the program as it 
	 * was <b>before</b> the mutation operation was applied.
	 */
	PROGRAM_BEFORE,
	
	/**
	 * Requests a CandidateProgram which is the program as it exists 
	 * <b>after</b> the mutation operation was applied.
	 */
	PROGRAM_AFTER,
	
	/**
	 * Requests a Long which is the length of time in nanoseconds that the 
	 * mutation operation took to complete.
	 */
	RUN_TIME
}