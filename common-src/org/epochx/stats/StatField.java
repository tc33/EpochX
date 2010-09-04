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

/**
 * Provides constants to be used as keys to request statistics from the
 * StatsManager.
 */
public enum StatField {
	
	/**
	 * Returns an <code>Integer</code> which is the sequential identifier of the
	 * last run in the series of runs. The first run will be run number 0.
	 */
	RUN_NUMBER,

	/**
	 * Returns a <code>Long</code> which is the length of time in nanoseconds
	 * that the last run took to complete.
	 */
	RUN_TIME,

	/**
	 * Returns a <code>Double</code> which is the lowest fitness score achieved
	 * by a <code>CandidateProgram</code> in the last run. Note that
	 * standardised fitness is used, so the best program has the minimum
	 * fitness.
	 */
	RUN_FITNESS_MIN,

	/**
	 * Returns the <code>CandidateProgram</code> which obtained the fitness
	 * score in the RUN_FITNESS_MIN field.
	 */
	RUN_FITTEST_PROGRAM,

	/**
	 * Returns an <code>Integer</code> which is the number of times the last
	 * initialisation operation was reverted.
	 */
	INIT_REVERSIONS,

	/**
	 * Returns an <code>Integer</code> which is the sequential identifier of the
	 * last generation where generation 0 is the initialisation phase.
	 */
	GEN_NUMBER,

	/**
	 * Returns a <code>double[]</code> which contains the fitnesses of all the
	 * <code>CandidateProgram</code>s in the population at the end of the
	 * previous generation. The fitnesses are arbitrarily ordered.
	 */
	GEN_FITNESSES,

	/**
	 * Returns a <code>Double</code> which is the lowest fitness score of a
	 * <code>CandidateProgram</code> in the last generation. Note that
	 * standardised fitness is used, so the best program has the minimum
	 * fitness.
	 */
	GEN_FITNESS_MIN,

	/**
	 * Returns a <code>Double</code> which is the highest fitness score of a
	 * <code>CandidateProgram</code> in the last generation. Note that
	 * standardised fitness is used, so the best program has the minimum
	 * fitness.
	 */
	GEN_FITNESS_MAX,

	/**
	 * Returns a <code>Double</code> which is the average fitness score of all
	 * the <code>CandidateProgram</code>s in the population at the end of the
	 * previous generation.
	 */
	GEN_FITNESS_AVE,

	/**
	 * Returns a <code>Double</code> which is the standard deviation of the
	 * fitness scores of all the <code>CandidateProgram</code>s in the
	 * population at the end of the previous generation.
	 */
	GEN_FITNESS_STDEV,

	/**
	 * Returns a <code>Double</code> which is the median value of all the
	 * fitness scores from the population of <code>CandidateProgram</code>s at
	 * the end of the previous generation.
	 */
	GEN_FITNESS_MEDIAN,

	/**
	 * Returns a <code>Double</code> which is the 95% confidence interval either
	 * side of the fitness mean for the population of
	 * <code>CandidateProgram</code>s at the end of the previous generation.
	 */
	GEN_FITNESS_CI95,

	/**
	 * Returns the <code>CandidateProgram</code> which obtained the fitness
	 * score in the GEN_FITNESS_MIN field.
	 */
	GEN_FITTEST_PROGRAM,

	/**
	 * Returns an <code>Integer</code> which is the number of times the previous
	 * generation was reverted.
	 */
	GEN_REVERSIONS,

	/**
	 * Returns a <code>List&lt;CandidateProgram&gt;</code> which is the
	 * population of <code>CandidateProgram</code>s at the end of the previous
	 * generation.
	 */
	GEN_POPULATION,

	/**
	 * Returns a <code>Long</code> which is the length of time in nanoseconds
	 * that the last generation took to complete.
	 */
	GEN_TIME,

	/**
	 * Returns a <code>CandidateProgram</code> which is a copy of the program
	 * which underwent mutation as it was <b>before</b> the mutation operation
	 * was applied.
	 */
	MUTATION_PROGRAM_BEFORE,

	/**
	 * Returns a <code>CandidateProgram</code> which is the program that is the
	 * result of the last mutation operation.
	 */
	MUTATION_PROGRAM_AFTER,

	/**
	 * Returns a <code>Long</code> which is the length of time in nanoseconds
	 * that the last mutation operation took to complete.
	 */
	MUTATION_TIME,

	/**
	 * Returns an <code>Integer</code> which is the number of times the last
	 * mutation operation was reverted.
	 */
	MUTATION_REVERSIONS,

	/**
	 * Returns a <code>CandidateProgram[]</code> which contains a copy of the
	 * programs which underwent crossover as they were <b>before</b> the
	 * crossover operation was applied.
	 **/
	CROSSOVER_PARENTS,

	/**
	 * Returns a <code>CandidateProgram[]</code> which contains the programs
	 * that are the result of the last crossover operation.
	 **/
	CROSSOVER_CHILDREN,

	/**
	 * Returns a <code>Long</code> which is the length of time in nanoseconds
	 * that the last crossover operation took to complete.
	 */
	CROSSOVER_TIME,

	/**
	 * Returns an <code>Integer</code> which is the number of times the last
	 * crossover operation was reverted.
	 */
	CROSSOVER_REVERSIONS,

	/**
	 * Returns an <code>Integer</code> which is the number of times the last
	 * pool selection operation was reverted.
	 */
	POOL_REVERSIONS,

	/**
	 * Returns an <code>Integer</code> which is the number of times the last
	 * reproduction operation was reverted.
	 */
	REP_REVERSIONS
}
