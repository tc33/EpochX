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
package org.epochx.model;

import org.epochx.op.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.stats.StatsEngine;
import org.epochx.tools.random.RandomNumberGenerator;

/**
 * Implementations of <code>Model</code> define a configuration for a set of 
 * evolutionary runs. Instances of model provide a set of components, control
 * parameters, settings for the framework and the fitness function against 
 * which all programs will be evaluated.
 * 
 * <p>
 * It is rarely necessary to implement this interface directly, it is normally 
 * more convenient to extend <code>AbstractModel</code> or more usually a 
 * representation specific abstract model.
 * 
 * @see AbstractModel
 */
public interface Model {

	/**
	 * Specifies the component to be used to generate an initial population.
	 * 
	 * @return the <code>Initialiser</code> to be responsible for generating 
	 * the initial population of the runs.
	 */
	public Initialiser getInitialiser();
	
	/**
	 * Specifies the component to perform the crossover operation.
	 * 
	 * @return the <code>Crossover</code> to perform the exchange of genetic 
	 * material between two programs.
	 */
	public Crossover getCrossover();
	
	/**
	 * Specifies the component to perform the mutation operation.
	 * 
	 * @return the <code>Mutation</code> that will carry out
	 * the mutation of a program.
	 */
	public Mutation getMutation();
	
	/**
	 * Specifies the component to perform the selection of a breeding pool.
	 * 
	 * @return the <code>PoolSelector</code> that will select the breeding pool
	 * of programs.
	 */
	public PoolSelector getPoolSelector();
	
	/**
	 * Specifies the component to be used to select individual programs from a
	 * breeding pool to undergo operations such as crossover and mutation.
	 * 
	 * @return the <code>ProgramSelector</code> that will be used to select the
	 * programs to undergo operations.
	 */
	public ProgramSelector getProgramSelector();
	
	/**
	 * Calculates a fitness score of a program. In EpochX fitness is minimised 
	 * so a fitness score of 0.1 is better than 0.2. It is essential that this 
	 * method is implemented to provide a measure of how good the given program
	 * solution is.
	 * 
	 * @param program the candidate program to be evaluated.
	 * @return a fitness score for the program given as a parameter.
	 */
	public double getFitness(CandidateProgram program);

	/**
	 * A target fitness score. The current run will be terminated if a fitness 
	 * score equal to or less than this value is achieved. If this happens the 
	 * run will be considered a success. If the implementer doesn't wish to use
	 * a fitness termination criterion then a fitness score lower than the 
	 * lowest possible value should be used, such as <code>
	 * Double.NEGATIVE_INFINITY</code>.
	 * 
	 * @return the fitness score that will be used as the fitness termination 
	 * criterion.
	 */
	public double getTerminationFitness();
	
	/**
	 * Specifies the random number generator that to be responsible for 
	 * determining random behaviour. It is important that the random number 
	 * generator is not unnecessary re-constructed on each call to this method
	 * as that could reduce the quality of the random numbers.
	 * 
	 * <p>
	 * Evolutionary algorithms are inherently non-deterministic, so the result of 
	 * multiple calls to the <code>run</code> method with identical models will 
	 * naturally produce different results if the random number generator in use by
	 * the model is seeded differently.
	 * 
	 * @return the random number generator to be provide the random numbers.
	 */
	public RandomNumberGenerator getRNG();
	
	/**
	 * Specifies the stats engine which will provide the generation of 
	 * statistics when requested from the raw data provided through by the 
	 * stats manager. This is an optional component and null can be returned
	 * for the default stats engine to be used.
	 * 
	 * @return an instance of stats engine to provide the generation of 
	 * statistics on request.
	 */
	public StatsEngine getStatsEngine();
	
	/**
	 * Specifies the probability that when choosing how the next programs will 
	 * be generated, whether mutation will be used. The probabilities of 
	 * crossover, mutation and reproduction should add up to 1.0. If the 
	 * probabilities do not add up then crossover will be given the priority, 
	 * followed by mutation, with the remaining probability left to 
	 * reproduction.
	 * 
	 * @return the probability that the mutation operation will be carried 
	 * out.
	 */
	public double getMutationProbability();
	
	/**
	 * Specifies the probability that when choosing how the next programs will 
	 * be generated, whether crossover will be used. The probabilities of 
	 * crossover, mutation and reproduction should add up to 1.0. If the 
	 * probabilities do not add up then crossover will be given the priority, 
	 * followed by mutation, with the remaining probability left to 
	 * reproduction.
	 * 
	 * @return the probability that the crossover operation will be carried 
	 * out.
	 */
	public double getCrossoverProbability();
	
	/**
	 * Specifies the probability that when choosing how the next programs will 
	 * be generated, whether reproduction will be used. The probabilities of 
	 * crossover, mutation and reproduction should add up to 1.0. If the 
	 * probabilities do not add up then crossover will be given the priority, 
	 * followed by mutation, with the remaining probability left to 
	 * reproduction.
	 * 
	 * @return the probability that the reproduction operation will be carried 
	 * out.
	 */
	public double getReproductionProbability();
	
	/**
	 * Specifies the number of separate runs that will be carried out with this
	 * model.
	 * 
	 * @return the number of runs that will be performed.
	 */
	public int getNoRuns();
	
	/**
	 * Specifies the maximum number of generations that each run will contain. 
	 * Termination may be caused by other criteria such as fitness.
	 * 
	 * @return the maximum number of generations that will be performed in a 
	 * run before termination.
	 */
	public int getNoGenerations();
	
	/**
	 * Specifies the number of programs to maintain in the population.
	 * 
	 * @return the size of the population to be used.
	 */
	public int getPopulationSize();
	
	/**
	 * Specifies the number of elite programs to be copied directly into each
	 * generation, where elites are the very best programs in a population.
	 * 
	 * @return the number of elites to be used each generation.
	 */
	public int getNoElites();
	
	/**
	 * Specifies the size of the breeding pool to use. If a value of zero or 
	 * less is returned then the whole population will be used as the pool.
	 * 
	 * @return
	 */
	public int getPoolSize();
	
	/**
	 * Specifies whether fitness caching should be used to increase 
	 * performance. Fitness caching should be used for most problems but if the
	 * same source code can be designated different fitness scores (due to the
	 * fitness being dependent upon other properties) then fitness caching 
	 * should be disabled.
	 * 
	 * @return true if fitness caching should be used, false otherwise.
	 */
	public boolean cacheFitness();
}
