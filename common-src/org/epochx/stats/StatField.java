/* 
 * Copyright 2007-2010 Tom Castle & Lawrence Beadle
 * Licensed under GNU General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.stats;

public class StatField {

	/**
	 * 
	 */
	public static final String RUN_NUMBER = "run-number";
	
	/**
	 * 
	 */
	public static final String RUN_TIME = "run-time";
	
	/**
	 * Will be the best fitness in in the run so far.
	 */
	public static final String RUN_FITNESS_MIN = "run-fitness-min";
	
	/**
	 * Will be program with the best fitness in the run so far.
	 */
	public static final String RUN_FITTEST_PROGRAM = "run-fittest-program";
	
	/**
	 * 
	 */
	public static final String GEN_NUMBER = "gen-number";
	
	/**
	 * 
	 */
	public static final String GEN_FITNESSES = "gen-fitnesses";
	
	/**
	 * Requests a Double which is the minimum fitness of all the 
	 * CandidatePrograms in that generation. Note that if using standardised 
	 * fitness the 'best' fitness may be the minimum.
	 */
	public static final String GEN_FITNESS_MIN = "gen-fitness-min";
	
	/**
	 * Requests a Double which is the maximum fitness of all the 
	 * CandidatePrograms in that generation. Note that if using standardised 
	 * fitness the 'best' fitness may be the minimum.
	 */
	public static final String GEN_FITNESS_MAX = "gen-fitness-max";
	
	/**
	 * Requests a Double which is the average fitness of all the 
	 * CandidatePrograms in that generation.
	 */
	public static final String GEN_FITNESS_AVE = "gen-fitness-ave";
	
	/**
	 * Requests a Double which is the standard deviation of the fitnesses of 
	 * all the CandidatePrograms in that generation.
	 */
	public static final String GEN_FITNESS_STDEV = "gen-fitness-stdev";

	/**
	 * Requests a Double which is the median of all the fitnesses of all the 
	 * CandidatePrograms in that generation.
	 */
	public static final String GEN_FITNESS_MEDIAN = "gen-fitness-median";
	
	/**
	 * Requests a Double which is the confidence interval at 95% of the 
	 * fitnesses.
	 */
	public static final String GEN_FITNESS_CI95 = "gen-fitness-ci95";
	
	/**
	 * Requests a CandidateProgram which has the 'best' fitness in the 
	 * generation. This is usually the lowest fitness score.
	 */
	public static final String GEN_FITTEST_PROGRAM = "gen-fittest-program";
	
	/**
	 * Requests a List<CandidateProgram> which is the population at the end 
	 * of this generation.
	 */
	public static final String GEN_POPULATION = "gen-population";
	
	/**
	 * Requests a Long which is the length of time in nanoseconds that the
	 * generation lasted.
	 */
	public static final String GEN_TIME = "gen-time";
	
	/**
	 * Requests a GPCandidateProgram which is a clone of the program as it 
	 * was <b>before</b> the mutation operation was applied.
	 */
	public static final String MUTATION_PROGRAM_BEFORE = "mutation-program-before";
	
	/**
	 * Requests a GPCandidateProgram which is the program as it exists 
	 * <b>after</b> the mutation operation was applied.
	 */
	public static final String MUTATION_PROGRAM_AFTER = "mutation-program-after";
	
	/**
	 * Requests a Long which is the length of time in nanoseconds that the 
	 * mutation operation took to complete.
	 */
	public static final String MUTATION_TIME = "mutation-time";
	
	/**
	 * Requests an Integer which is the number of mutations that were 
	 * reverted due to model rejection.
	 */
	public static final String MUTATION_REVERTED = "mutation-reverted";
	
	/** 
	 * Requests a GPCandidateProgram[] (typically with 2 elements) which are 
	 * the parents that were crossed-over to give the children.
	 **/
	public static final String CROSSOVER_PARENTS = "crossover-parents";
	
	/**
	 * Requests a GPCandidateProgram[] (typically with 2 elements) which are 
	 * the children that resulted from crossing over the parents.
	 **/
	public static final String CROSSOVER_CHILDREN = "crossover-children";
	
	/**
	 * Requests a Long which is the length of time in nanoseconds that the 
	 * crossover operation took to complete.
	 */
	public static final String CROSSOVER_TIME = "crossover-time";
	
	/**
	 * Requests an Integer which is the number of crossovers that were 
	 * reverted due to model rejection.
	 */
	public static final String CROSSOVER_REVERTED = "crossover-reverted";
}
