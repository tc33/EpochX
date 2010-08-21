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
public class StatField {

	/**
	 * Returns an <code>Integer</code> which is the sequential identifier of the
	 * last run in the series of runs. The first run will be run
	 * number 0.
	 */
	public static final String RUN_NUMBER = "run-number";

	/**
	 * Returns a <code>Long</code> which is the length of time in nanoseconds
	 * that the last run took to complete.
	 */
	public static final String RUN_TIME = "run-time";

	/**
	 * Returns a <code>Double</code> which is the lowest fitness score achieved
	 * by a <code>CandidateProgram</code> in the last run. Note that
	 * standardised fitness is used, so the best program has the minimum
	 * fitness.
	 */
	public static final String RUN_FITNESS_MIN = "run-fitness-min";

	/**
	 * Returns the <code>CandidateProgram</code> which obtained the fitness
	 * score in the RUN_FITNESS_MIN field.
	 */
	public static final String RUN_FITTEST_PROGRAM = "run-fittest-program";

	/**
	 * Returns an <code>Integer</code> which is the number of times the last
	 * initialisation operation was reverted.
	 */
	public static final String INIT_REVERSIONS = "init-reversions";

	/**
	 * Returns an <code>Integer</code> which is the sequential identifier of the
	 * last generation where generation 0 is the initialisation phase.
	 */
	public static final String GEN_NUMBER = "gen-number";

	/**
	 * Returns a <code>double[]</code> which contains the fitnesses of all the
	 * <code>CandidateProgram</code>s in the population at the end of the
	 * previous generation. The fitnesses are arbitrarily ordered.
	 */
	public static final String GEN_FITNESSES = "gen-fitnesses";

	/**
	 * Returns a <code>Double</code> which is the lowest fitness score of a
	 * <code>CandidateProgram</code> in the last generation. Note that
	 * standardised fitness is used, so the best program has the minimum
	 * fitness.
	 */
	public static final String GEN_FITNESS_MIN = "gen-fitness-min";

	/**
	 * Returns a <code>Double</code> which is the highest fitness score of a
	 * <code>CandidateProgram</code> in the last generation. Note that
	 * standardised fitness is used, so the best program has the minimum
	 * fitness.
	 */
	public static final String GEN_FITNESS_MAX = "gen-fitness-max";

	/**
	 * Returns a <code>Double</code> which is the average fitness score of all
	 * the <code>CandidateProgram</code>s in the population at the end of the
	 * previous generation.
	 */
	public static final String GEN_FITNESS_AVE = "gen-fitness-ave";

	/**
	 * Returns a <code>Double</code> which is the standard deviation of the
	 * fitness scores of all the <code>CandidateProgram</code>s in the
	 * population at the end of the previous generation.
	 */
	public static final String GEN_FITNESS_STDEV = "gen-fitness-stdev";

	/**
	 * Returns a <code>Double</code> which is the median value of all the
	 * fitness scores from the population of <code>CandidateProgram</code>s at
	 * the end of the previous generation.
	 */
	public static final String GEN_FITNESS_MEDIAN = "gen-fitness-median";

	/**
	 * Returns a <code>Double</code> which is the 95% confidence interval either
	 * side of the fitness mean for the population of
	 * <code>CandidateProgram</code>s at the end of the previous generation.
	 */
	public static final String GEN_FITNESS_CI95 = "gen-fitness-ci95";

	/**
	 * Returns the <code>CandidateProgram</code> which obtained the fitness
	 * score in the GEN_FITNESS_MIN field.
	 */
	public static final String GEN_FITTEST_PROGRAM = "gen-fittest-program";

	/**
	 * Returns an <code>Integer</code> which is the number of times the previous
	 * generation was reverted.
	 */
	public static final String GEN_REVERSIONS = "gen-reversions";

	/**
	 * Returns a <code>List&lt;CandidateProgram&gt;</code> which is the
	 * population of <code>CandidateProgram</code>s at the end of the previous
	 * generation.
	 */
	public static final String GEN_POPULATION = "gen-population";

	/**
	 * Returns a <code>Long</code> which is the length of time in nanoseconds
	 * that the last generation took to complete.
	 */
	public static final String GEN_TIME = "gen-time";

	/**
	 * Returns a <code>CandidateProgram</code> which is a copy of the program
	 * which underwent mutation as it was <b>before</b> the mutation operation
	 * was applied.
	 */
	public static final String MUTATION_PROGRAM_BEFORE = "mutation-program-before";

	/**
	 * Returns a <code>CandidateProgram</code> which is the program that is the
	 * result of the last mutation operation.
	 */
	public static final String MUTATION_PROGRAM_AFTER = "mutation-program-after";

	/**
	 * Returns a <code>Long</code> which is the length of time in nanoseconds
	 * that the last mutation operation took to complete.
	 */
	public static final String MUTATION_TIME = "mutation-time";

	/**
	 * Returns an <code>Integer</code> which is the number of times the last
	 * mutation operation was reverted.
	 */
	public static final String MUTATION_REVERSIONS = "mutation-reversions";

	/**
	 * Returns a <code>CandidateProgram[]</code> which contains a copy of the
	 * programs which underwent crossover as they were <b>before</b> the
	 * crossover operation was applied.
	 **/
	public static final String CROSSOVER_PARENTS = "crossover-parents";

	/**
	 * Returns a <code>CandidateProgram[]</code> which contains the programs
	 * that are the result of the last crossover operation.
	 **/
	public static final String CROSSOVER_CHILDREN = "crossover-children";

	/**
	 * Returns a <code>Long</code> which is the length of time in nanoseconds
	 * that the last crossover operation took to complete.
	 */
	public static final String CROSSOVER_TIME = "crossover-time";

	/**
	 * Returns an <code>Integer</code> which is the number of times the last
	 * crossover operation was reverted.
	 */
	public static final String CROSSOVER_REVERSIONS = "crossover-reversions";

	/**
	 * Returns an <code>Integer</code> which is the number of times the last
	 * pool selection operation was reverted.
	 */
	public static final String POOL_REVERSIONS = "pool-reversions";

	/**
	 * Returns an <code>Integer</code> which is the number of times the last
	 * reproduction operation was reverted.
	 */
	public static final String REP_REVERSIONS = "rep-reversions";
}
