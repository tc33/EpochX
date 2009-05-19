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
 * This enum gives all the available generation statistics that can be 
 * requested. Generation statistics are those statistics that are generated 
 * at the end of each generation. Each GenStatField has a datatype related 
 * to it which is the type which the data will be returned in.
 * @see com.epochx.stats.GenerationStatListener
 * @see com.epochx.stats.CrossoverStats.CrossoverStatField
 * @see com.epochx.stats.MutationStats.MutationStatField
 * @see com.epochx.stats.RunStats.RunStatField
 */
public enum GenerationStatField {
	/**
	 * Requests a Double which is the average depth of CandidatePrograms in 
	 * that generation.
	 */
	DEPTH_AVE,
	
	/**
	 * Requests a Double which is the standard deviation of depths of 
	 * CandidatePrograms in that generation.
	 */
	DEPTH_STDEV,
	
	/**
	 * Requests a Double which is maximum depth of all the CandidatePrograms in 
	 * that generation. Should always be a whole number.
	 */
	DEPTH_MAX,
	
	/**
	 * Requests a Double which is minimum depth of all the CandidatePrograms in 
	 * that generation. Should always be a whole number.
	 */
	DEPTH_MIN,
	
	/**
	 * Requests a Double[] which is of length DEPTH_MAX. Each element is the 
	 * average number of nodes of all CandidatePrograms in the population at 
	 * that depth.
	 */
	AVE_NODES_PER_DEPTH,
	
	/**
	 * Requests a Double which is the average length (that is - number of nodes) 
	 * of CandidatePrograms in that generation.
	 */
	LENGTH_AVE,
	
	/**
	 * Requests a Double which is the standard deviation of lengths (that is - 
	 * number of nodes) of CandidatePrograms in that generation.
	 */
	LENGTH_STDEV,
	
	/**
	 * Requests a Double which is maximum length (that is - number of nodes) 
	 * of all the CandidatePrograms in that generation. Should always be a 
	 * whole number.
	 */
	LENGTH_MAX,
	
	/**
	 * Requests a Double which is minimum length (that is - number of nodes) 
	 * of all the CandidatePrograms in that generation. Should always be a 
	 * whole number.
	 */
	LENGTH_MIN,
	
	/**
	 * Requests a Double which is the average number of terminal nodes in all 
	 * the CandidatePrograms of that generation.
	 */
	NO_TERMINALS_AVE,
	
	/**
	 * Requests a Double which is the standard deviation of the number of 
	 * terminal nodes in each CandidateProgram in that generation.
	 */
	NO_TERMINALS_STDEV,
	
	/**
	 * Requests a Double which is the maximum number of terminal nodes in a 
	 * CandidateProgram in that generation.
	 */
	NO_TERMINALS_MAX,
	
	/**
	 * Requests a Double which is the minimum number of terminal nodes in a 
	 * CandidateProgram in that generation.
	 */
	NO_TERMINALS_MIN,
	
	/**
	 * Requests a Double which is the average number of unique terminal nodes
	 * in a CandidateProgram in that generation.
	 */
	NO_DISTINCT_TERMINALS_AVE,
	
	/**
	 * Requests a Double which is the standard deviation of the number of 
	 * unique terminal nodes in each CandidateProgram in that generation.
	 */
	NO_DISTINCT_TERMINALS_STDEV,
	
	/**
	 * Requests a Double which is the maximum number of unique terminal nodes
	 * in a CandidateProgram in that generation.
	 */
	NO_DISTINCT_TERMINALS_MAX,
	
	/**
	 * Requests a Double which is the minimum number of unique terminal nodes
	 * in a CandidateProgram in that generation.
	 */
	NO_DISTINCT_TERMINALS_MIN,
	
	/**
	 * Requests a Double which is the average number of function nodes in all 
	 * the CandidatePrograms of that generation.
	 */
	NO_FUNCTIONS_AVE,
	
	/**
	 * Requests a Double which is the standard deviation of the number of 
	 * function nodes in each CandidateProgram in that generation.
	 */
	NO_FUNCTIONS_STDEV,
	
	/**
	 * Requests a Double which is the maximum number of function nodes in a 
	 * CandidateProgram in that generation.
	 */
	NO_FUNCTIONS_MAX,
	
	/**
	 * Requests a Double which is the minimum number of function nodes in a 
	 * CandidateProgram in that generation.
	 */
	NO_FUNCTIONS_MIN,
	
	/**
	 * Requests a Double which is the average number of unique function nodes
	 * in a CandidateProgram in that generation.
	 */
	NO_DISTINCT_FUNCTIONS_AVE,
	
	/**
	 * Requests a Double which is the standard deviation of the number of 
	 * unique function nodes in each CandidateProgram in that generation.
	 */
	NO_DISTINCT_FUNCTIONS_STDEV,
	
	/**
	 * Requests a Double which is the maximum number of unique function nodes
	 * in a CandidateProgram in that generation.
	 */
	NO_DISTINCT_FUNCTIONS_MAX,
	
	/**
	 * Requests a Double which is the minimum number of unique function nodes
	 * in a CandidateProgram in that generation.
	 */
	NO_DISTINCT_FUNCTIONS_MIN,
	
	/**
	 * Requests a Double which is the average fitness of all the 
	 * CandidatePrograms in that generation.
	 */
	FITNESS_AVE,
	
	/**
	 * Requests a Double which is the standard deviation of the fitnesses of 
	 * all the CandidatePrograms in that generation.
	 */
	FITNESS_STDEV,
	
	/**
	 * Requests a Double which is the maximum fitness of all the 
	 * CandidatePrograms in that generation. Note that if using standardised 
	 * fitness the 'best' fitness may be the minimum.
	 */
	FITNESS_MAX,
	
	/**
	 * Requests a Double which is the minimum fitness of all the 
	 * CandidatePrograms in that generation. Note that if using standardised 
	 * fitness the 'best' fitness may be the minimum.
	 */
	FITNESS_MIN,
	
	/**
	 * Requests a Double which is the median of all the fitnesses of all the 
	 * CandidatePrograms in that generation.
	 */
	FITNESS_MEDIAN,
	
	/**
	 * Requests a Double which is the confidence interval at 95% of the 
	 * fitnesses.
	 */
	FITNESS_CI_95,
	
	/**
	 * Requests a CandidateProgram which has the 'best' fitness in the 
	 * generation. This is usually the lowest fitness score.
	 */
	BEST_PROGRAM,
	
	/**
	 * Requests a List<CandidateProgram> which is the population at the end 
	 * of this generation.
	 */
	POPULATION,
	
	/**
	 * Requests a Long which is the length of time in nanoseconds that the
	 * generation lasted.
	 */
	RUN_TIME,
	
	/**
	 * Requests an Integer which is the number of crossovers that were 
	 * reverted due to model rejection.
	 */
	REVERTED_CROSSOVERS,
	
	/**
	 * Requests an Integer which is the number of mutations that were 
	 * reverted due to model rejection.
	 */
	REVERTED_MUTATIONS
}