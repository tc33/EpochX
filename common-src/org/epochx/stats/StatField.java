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
	 * previous generation. The fitnesses are arranged in the same order as the
	 * <code>List&lt;CandidateProgram&gt;</code> returned for the 
	 * <code>GEN_POP</code> field.
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
	 * score in the <code>GEN_FITNESS_MIN</code> field. If more than one program
	 * obtained the same fitness then the one returned will be chosen 
	 * arbitrarily. See the similarly named <code>GEN_FITTEST_PROGRAMS</code> 
	 * field to obtain all the programs.
	 */
	GEN_FITTEST_PROGRAM,
	
	/**
	 * Returns a <code>List&lt;CandidateProgram&gt;</code> of all those programs
	 * which obtained the fitness score in the <code>GEN_FITNESS_MIN</code> 
	 * field. See the similarly named <code>GEN_FITTEST_PROGRAM</code> field for
	 * returning just one <code>CandidateProgram</code>.
	 */
	GEN_FITTEST_PROGRAMS,

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
	GEN_POP,
	
	/**
	 * Returns a <code>List&lt;CandidateProgram&gt;</code> which is the
	 * population of <code>CandidateProgram</code>s at the end of the previous
	 * generation. The programs are sorted according to fitness ascending from 
	 * lowest fitness score to highest.
	 */
	GEN_POP_SORTED,
	
	/**
	 * Returns a <code>List&lt;CandidateProgram&gt;</code> which is the
	 * population of <code>CandidateProgram</code>s at the end of the previous
	 * generation. The programs are sorted according to fitness descending from 
	 * highest fitness score to lowest.
	 */
	GEN_POP_SORTED_DESC,
	
	/**
	 * Returns a <code>double[]</code> which contains the fitnesses of all the
	 * <code>CandidateProgram</code>s in the population at the end of the
	 * previous generation. The fitnesses are sorted in ascending order from 
	 * lowest fitness score to highest. The order matches the  
	 * the <code>List&lt;CandidateProgram&gt;</code> returned for the 
	 * <code>GEN_POP_SORTED</code> field.
	 */
	GEN_FITNESSES_SORTED,
	
	/**
	 * Returns a <code>double[]</code> which contains the fitnesses of all the
	 * <code>CandidateProgram</code>s in the population at the end of the
	 * previous generation. The fitnesses are sorted in descending order from 
	 * highest fitness score to lowest. The order matches the 
	 * <code>List&lt;CandidateProgram&gt;</code> returned for the 
	 * <code>GEN_POP_SORTED_DESC</code> field.
	 */
	GEN_FITNESSES_SORTED_DESC,

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
	MUT_PARENT,

	/**
	 * Returns a <code>CandidateProgram</code> which is the program that is the
	 * result of the last mutation operation.
	 */
	MUT_CHILD,

	/**
	 * Returns a <code>Long</code> which is the length of time in nanoseconds
	 * that the last mutation operation took to complete.
	 */
	MUT_TIME,

	/**
	 * Returns an <code>Integer</code> which is the number of times the last
	 * mutation operation was reverted.
	 */
	MUT_REVERSIONS,

	/**
	 * Returns a <code>Double</code> which is the fitness score of the program
	 * selected for mutation prior to the mutation operation being performed.
	 */
	MUT_PARENT_FITNESS,
	
	/**
	 * Returns a <code>Double</code> which is the fitness score of the program
	 * selected for mutation after the mutation operation has been performed.
	 */
	MUT_CHILD_FITNESS,
	
	/**
	 * Returns a <code>Double</code> which is the amount by which the fitness of
	 * the mutated program has changed from before to after the operation. If 
	 * the fitness has decreased then the returned value will be negative, if 
	 * it has increased then it will be positive and it will be zero if the 
	 * fitness is unchanged.
	 */
	MUT_FITNESS_CHANGE,
	
	/**
	 * Returns a <code>CandidateProgram[]</code> which contains a copy of the
	 * programs which underwent crossover as they were <b>before</b> the
	 * crossover operation was applied.
	 **/
	XO_PARENTS,

	/**
	 * Returns a <code>CandidateProgram[]</code> which contains the programs
	 * that are the result of the last crossover operation.
	 **/
	XO_CHILDREN,

	/**
	 * Returns a <code>Long</code> which is the length of time in nanoseconds
	 * that the last crossover operation took to complete.
	 */
	XO_TIME,

	/**
	 * Returns an <code>Integer</code> which is the number of times the last
	 * crossover operation was reverted.
	 */
	XO_REVERSIONS,
	
	/**
	 * Returns a <code>double[]</code> which is an array of the fitnesses of the
	 * programs selected for crossover prior to the crossover operation being 
	 * performed. The order of the fitness scores is the same as for the 
	 * <code>CandidateProgram[]</code> returned for the <code>XO_PARENTS</code>
	 * field.
	 */
	XO_PARENT_FITNESSES,
	
	/**
	 * Returns a <code>double[]</code> which is an array of the fitnes scores 
	 * of the programs selected for crossover after the crossover operation has
	 * been performed. The order of the fitness scores is the same as for the 
	 * <code>CandidateProgram[]</code> returned for the <code>XO_CHILDREN</code>
	 * field.
	 */
	XO_CHILD_FITNESSES,
	
	/**
	 * Returns a <code>Double</code> which is the average value of the fitnesses
	 * of the parents selected for crossover prior to the crossover operation
	 * being performed.
	 */
	XO_PARENTS_FITNESS_AVE,
	
	/**
	 * Returns a <code>Double</code> which is the average value of the fitnesses
	 * of the programs selected for crossover after the crossover operation has
	 * been performed.
	 */
	XO_CHILDREN_FITNESS_AVE,

	/**
	 * Returns a <code>Double</code> which is the amount by which the average 
	 * fitnesses of the crossed over programs has changed from before to after 
	 * the operation. If the fitness has decreased then the returned value will 
	 * be negative, if it has increased then it will be positive and it will be
	 * zero if the fitness is unchanged.
	 */
	XO_FITNESS_AVE_CHANGE,
	
	/**
	 * Returns an <code>Integer</code> which is the number of times the last
	 * pool selection operation was reverted.
	 */
	POOL_REVERSIONS,

	/**
	 * Returns a <code>double[]</code> which contains the fitnesses of all the
	 * <code>CandidateProgram</code>s in the pool that was last constructed. 
	 * The fitnesses are arranged in the same order as the
	 *  <code>CandidateProgram[]</code> returned for the 
	 *  <code>POOL_PROGRAMS</code> field.
	 */
	POOL_FITNESSES,

	/**
	 * Returns a <code>Double</code> which is the lowest fitness score of a
	 * <code>CandidateProgram</code> in the last pool. Note that
	 * standardised fitness is used, so the best program has the minimum
	 * fitness.
	 */
	POOL_FITNESS_MIN,

	/**
	 * Returns a <code>Double</code> which is the highest fitness score of a
	 * <code>CandidateProgram</code> in the last pool. Note that
	 * standardised fitness is used, so the best program has the minimum
	 * fitness.
	 */
	POOL_FITNESS_MAX,

	/**
	 * Returns a <code>Double</code> which is the average fitness score of all
	 * the <code>CandidateProgram</code>s in the last breeding pool that was 
	 * selected.
	 */
	POOL_FITNESS_AVE,

	/**
	 * Returns a <code>Double</code> which is the standard deviation of the
	 * fitness scores of all the <code>CandidateProgram</code>s in the
	 * last breeding pool that was selected.
	 */
	POOL_FITNESS_STDEV,

	/**
	 * Returns a <code>Double</code> which is the median value of all the
	 * fitness scores from the pool of <code>CandidateProgram</code>s that was 
	 * last selected.
	 */
	POOL_FITNESS_MEDIAN,

	/**
	 * Returns a <code>Double</code> which is the 95% confidence interval either
	 * side of the fitness mean for the pool of <code>CandidateProgram</code>s 
	 * that was last selected.
	 */
	POOL_FITNESS_CI95,

	/**
	 * Returns the <code>CandidateProgram</code> which obtained the fitness
	 * score in the <code>POOL_FITNESS_MIN</code> field. If more than one 
	 * program obtained the same fitness then the one returned will be chosen 
	 * arbitrarily. See the similarly named <code>POOL_FITTEST_PROGRAMS</code> 
	 * field to obtain all the programs.
	 */
	POOL_FITTEST_PROGRAM,
	
	/**
	 * Returns a <code>List&lt;CandidateProgram&gt;</code> of all those programs
	 * which obtained the fitness score in the <code>POOL_FITNESS_MIN</code> 
	 * field. See the similarly named <code>POOL_FITTEST_PROGRAM</code> field 
	 * for returning just one <code>CandidateProgram</code>.
	 */
	POOL_FITTEST_PROGRAMS,

	/**
	 * Returns a <code>List&lt;CandidateProgram&gt;</code> which is the
	 * pool of <code>CandidateProgram</code>s that was last selected.
	 */
	POOL_PROGRAMS,
	
	/**
	 * Returns an <code>Integer</code> which is the number of programs that were
	 * selected to form the breeding pool.
	 */
	POOL_SIZE,

	/**
	 * Returns a <code>Long</code> which is the length of time in nanoseconds
	 * that the last pool selection took to complete.
	 */
	POOL_TIME,
	
	/**
	 * Returns an <code>Integer</code> which is the number of times the last
	 * reproduction operation was reverted.
	 */
	REP_REVERSIONS,
	
	/**
	 * Returns a <code>Long</code> which is the length of time in nanoseconds
	 * that the last reproduction operation took to complete.
	 */
	REP_TIME,
	
	/**
	 * Returns a <code>List&lt;CandidateProgram&gt;</code> which contains all 
	 * the programs that were selected for elitism.
	 */
	ELITE_PROGRAMS,
	
	/**
	 * Returns an <code>Integer</code> which is the number of programs that were
	 * selected for elitism.
	 */
	ELITE_SIZE,
	
	/**
	 * Returns a <code>double[]</code> which contains the fitnesses of all the
	 * <code>CandidateProgram</code>s selected for elitism. The fitnesses are 
	 * arranged in the same order as the <code>CandidateProgram[]</code> 
	 * returned for the <code>ELITE_PROGRAMS</code> field.
	 */
	ELITE_FITNESSES,
	
	/**
	 * Returns a <code>Double</code> which is the minimum fitness value of the 
	 * programs selected for elitism. With standardised fitness this will always
	 * be the same as the value returned from <code>GEN_FITNESS_MIN</code> from
	 * the previous generation.
	 */
	ELITE_FITNESS_MIN,
	
	/**
	 * Returns a <code>Double</code> which is the maximum fitness score of the 
	 * programs selected for elitism.
	 */
	ELITE_FITNESS_MAX,
	
	/**
	 * Returns a <code>Double</code> which is the average fitness of all the 
	 * programs selected for elitism.
	 */
	ELITE_FITNESS_AVE,
	
	/**
	 * Returns a <code>Double</code> which is the standard deviation of all the
	 * fitnesses of the programs selected for elitism.
	 */
	ELITE_FITNESS_STDEV,
	
	/**
	 * Returns a <code>Double</code> which is the median fitness score of all 
	 * the programs selected for elitism.
	 */
	ELITE_FITNESS_MEDIAN,
	
	/**
	 * Returns a <code>Double</code> which is the 95% confidence interval either
	 * side of the fitness mean for the programs selected for elitism.
	 */
	ELITE_FITNESS_CI95,
	
	/**
	 * Returns a <code>Long</code> which is the length of time in nanoseconds 
	 * that the last elitism operation took to complete.
	 */
	ELITE_TIME
}
