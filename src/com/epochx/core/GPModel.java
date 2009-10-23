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
package com.epochx.core;

import java.util.List;

import com.epochx.op.crossover.*;
import com.epochx.op.initialisation.*;
import com.epochx.op.mutation.*;
import com.epochx.op.selection.*;
import com.epochx.representation.*;
import com.epochx.stats.*;
import com.epochx.random.RandomNumberGenerator;

/**
 * A GPModel defines all those parameters needed to control a run by GPRun. 
 * The first step - and for most problems the only step - to generate a GP  
 * evolved solution with EpochX is to provide a concrete implementation of 
 * this interface.
 * 
 * <p>For most situations, users should look to extend the abstract 
 * <code>GPAbstractModel.</code>
 *
 * @param <TYPE> The return type of CandidatePrograms being evolved.
 * @see GPAbstractModel
 */
public interface GPModel<TYPE> {
	
	/**
	 * Retrieves the Initialiser which will generate the first generation 
	 * population from which the evolution will proceed.
	 * 
	 * @return the Initialiser to create the first population.
	 */
	public Initialiser<TYPE> getInitialiser();

	/**
	 * Retrieves the implementation of Crossover to use to perform the genetic 
	 * operation of crossover between 2 parents. The 2 parents to be crossed 
	 * over will be selected using the parent selector returned by 
	 * getProgramSelector().
	 * 
	 * @return the implementation of Crossover that will perform the genetic 
	 * 		   operation of crossover.
	 * @see UniformPointCrossover
	 * @see KozaCrossover
	 */
	public Crossover<TYPE> getCrossover();

	/**
	 * Retrieves the implementation of Mutator to use to perform the genetic 
	 * operation of mutation on a CandidateProgram. The individual to be 
	 * mutated will be selected using the program selector returned by 
	 * getProgramSelector().
	 * 
	 * @return the implementation of Mutator that will perform the genetic 
	 * 		   operation of mutation.
	 */
	public Mutation<TYPE> getMutator();

	/**
	 * Retrieves the selector to use to pick parents from either a pre-selected 
	 * breeding pool (selected by the PoolSelector returned by 
	 * getPoolSelector()) or the previous population for use as input to the 
	 * genetic operators.
	 * 
	 * @return the ProgramSelector which should be used to pick parents for input 
	 * 		   to the genetic operators.
	 * @see TournamentSelector
	 */
	public ProgramSelector<TYPE> getProgramSelector();

	/**
	 * Retrieves the selector to use to construct a breeding pool from which 
	 * parents can be selected using the parent selector returned by 
	 * getProgramSelector() to undergo the genetic operators.
	 * 
	 * @return a PoolSelector which can be used to construct a breeding pool, 
	 * 		   or null if a breeding pool shouldn't be used and instead parents 
	 * 		   should be picked straight from the previous population.
	 * @see TournamentSelector
	 */
	public PoolSelector<TYPE> getPoolSelector();

	/**
	 * Retrieves the set of terminal nodes. 
	 * 
	 * @return the terminal nodes to be used during evolution.
	 */
	public List<TerminalNode<TYPE>> getTerminals();

	/**
	 * Retrieves the set of function nodes.
	 * 
	 * @return the function nodes to be used during evolution.
	 */
	public List<FunctionNode<TYPE>> getFunctions();

	/**
	 * Retrieves the full set of syntax, that is terminals AND function nodes.
	 * This is usually a combination of the calls from getTerminals() and 
	 * getFunctions().
	 * 
	 * @return the full syntax for use in building node trees.
	 */
	public List<Node<TYPE>> getSyntax();

	/**
	 * Returns the RandomNumberGenerator instance that should be used for the 
	 * generation of random numbers throughout execution.
	 *  
	 * @return the RandomNumberGenerator to use for generating randomness.
	 */
	public RandomNumberGenerator getRNG();
	
	/**
	 * Retrieves the number of runs that should be carried out using this model 
	 * as the basis. Each call to GPRun.run() will be with the same model so 
	 * this is useful when multiple runs are necessary with the same control 
	 * parameters for research purposes or otherwise in order to attain 
	 * reliable results drawn from mean averages.
	 * 
	 * @return the number of times this model should be used to control GP runs.
	 */
	public int getNoRuns();

	/**
	 * Retrieves the number of generations that each run should use before 
	 * terminating, unless prior termination occurs due to one of the other 
	 * termination criterion.
	 * 
	 * @return the number of generations that should be evolved in each run.
	 */
	public int getNoGenerations();

	/**
	 * Retrieves the number of CandidatePrograms that should make up each 
	 * generation. The population at the start and end of each generation 
	 * should always equal exactly this number.
	 * 
	 * @return the number of CandidatePrograms per generation.
	 */
	public int getPopulationSize();

	/**
	 * Retrieves the size of the breeding pool to be used for parent selection 
	 * when performing the genetic operators. If the pool size is equal to or 
	 * less than zero, or if getPoolSelector() returns null, then no pool 
	 * will be used and parent selection will take place directly from the 
	 * previous population.
	 * 
	 * @return the size of the mating pool to build with the PoolSelector 
	 * 		   returned by getPoolSelector() which will be used for parent 
	 * 		   selection.
	 */
	public int getPoolSize();

	/**
	 * Retrieves the number of elites that should be copied straight to the next 
	 * population. This number is distinct from the reproduction operator. 
	 * Elites are generally picked as the very best programs in a generation, 
	 * thus a number of elites of 1 or more will always retain the best program 
	 * found so far, through to the last generation.
	 * 
	 * @return the number of elites that should be copied through from one 
	 * 		   generation to the next.
	 */
	public int getNoElites();

	/**
	 * Retrieves the maximum depth of CandidatePrograms allowed in the 
	 * population after initialisation. The exact way in which the 
	 * implementation ensures this depth is kept to may vary.
	 * 
	 * @return the maximum depth of CandidatePrograms to be allowed in the 
	 * 		   population after initialisation.
	 */
	public int getInitialMaxDepth();
	
	/**
	 * Retrieves the maximum depth of CandidatePrograms allowed in the 
	 * population after undergoing genetic operators. The exact way in which 
	 * CandidatePrograms deeper than this limit are dealt with may vary, but 
	 * they will not be allowed to remain into the next generation unaltered.
	 * 
	 * @return the maximum depth of CandidatePrograms to be allowed in the 
	 * 		   population after genetic operators.
	 */
	public int getMaxProgramDepth();
	
	/**
	 * Retrieves a numerical value between 0.0 and 1.0 which represents the 
	 * probability of selecting the crossover genetic operator, as opposed to 
	 * mutation or reproduction. 
	 * 
	 * <p>Within one generation approximately Pc proportion of the 
	 * CandidatePrograms will have been created through crossover, Pm through 
	 * mutation and Pr through reproduction. Where Pc, Pm, Pr are the values 
	 * returned by getCrossoverProbability(), getMutationProbability() and 
	 * getReproductionProbability() respectively. The sum of the calls to 
	 * getCrossoverProbability(), getReproductionProbability() and 
	 * getMutationProbability() should total 1.0.
	 * 
	 * @return the probability of choosing the crossover genetic operator at 
	 * 		   each iteration when constructing the next population.
	 */
	public double getCrossoverProbability();

	/**
	 * Retrieves a numerical value between 0.0 and 1.0 which represents the 
	 * probability of selecting the mutation genetic operator, as opposed to 
	 * crossover or reproduction. 
	 * 
	 * <p>Within one generation approximately Pc proportion of the 
	 * CandidatePrograms will have been created through crossover, Pm through 
	 * mutation and Pr through reproduction. Where Pc, Pm, Pr are the values 
	 * returned by getCrossoverProbability(), getMutationProbability() and 
	 * getReproductionProbability() respectively. The sum of the calls to 
	 * getCrossoverProbability(), getReproductionProbability() and 
	 * getMutationProbability() should total 1.0.
	 * 
	 * @return the probability of choosing the mutation genetic operator at 
	 * 		   each iteration when constructing the next population.
	 */
	public double getMutationProbability();

	/**
	 * Retrieves a numerical value between 0.0 and 1.0 which represents the 
	 * probability of selecting the reproduction genetic operator, as opposed 
	 * to crossover or mutation. 
	 * 
	 * <p>Within one generation approximately Pc proportion of the 
	 * CandidatePrograms will have been created through crossover, Pm through 
	 * mutation and Pr through reproduction. Where Pc, Pm, Pr are the values 
	 * returned by getCrossoverProbability(), getMutationProbability() and 
	 * getReproductionProbability() respectively. The sum of the calls to 
	 * getCrossoverProbability(), getReproductionProbability() and 
	 * getMutationProbability() should total 1.0.
	 * 
	 * @return the probability of choosing the reproduction genetic operator at 
	 * 		   each iteration when constructing the next population.
	 */
	public double getReproductionProbability();

	/**
	 * Calculates and returns the fitness score of the given program. The score 
	 * returned by this method provides the underlying way in which Candidate 
	 * Programs are selected. The GP system will attempt to improve the value 
	 * returned by this method over the generations. A fitness value of 0.0 is 
	 * better than a fitness value of 1.0.
	 * 
	 * <p>There are many ways in which a fitness score can be calculated, for 
	 * example mean squared error, standard deviation or a simple count of how 
	 * many known inputs the given program provides incorrect (known) outputs.
	 * For more information, the new user should read some of the genetic 
	 * programming literature.
	 * 
	 * @param program	the CandidateProgram to evaluate and calculate a score
	 * 					for.
	 * @return a double representing the quality of the program where a small 
	 * 		   value is considered better than a larger value.
	 */
	public double getFitness(CandidateProgram<TYPE> program);
	
	/**
	 * The fitness score at which a run should be stopped. Returning a negative 
	 * value will result in no termination based upon fitness.
	 * 
	 * @return the fitness score at which a run should be terminated. A fitness 
	 * of this or less will result in termination.
	 */
	public double getTerminationFitness();

	/**
	 * This method will be called during each crossover operation before the 
	 * crossover is accepted, giving the model the opportunity to reject the 
	 * operation, in which case the operation will be attempted again until it
	 * is accepted. The number of times crossovers are rejected is retrievable 
	 * using the REVERTED_CROSSOVERS stats field. 
	 * 
	 * @param parents The programs which have been crossed over to create the 
	 * given children.
	 * @param children The children which are the result of the crossover 
	 * operation having been performed on the given parents.
	 * @return True if the crossover operation should proceed, false if it is 
	 * rejected and should be retried with new parents.
	 */
	public boolean acceptCrossover(CandidateProgram<TYPE>[] parents, 
								   CandidateProgram<TYPE>[] children);

	/**
	 * This method will be called during each mutation operation before the 
	 * mutation is accepted, giving the model the opportunity to reject the 
	 * operation, in which case the operation will be attempted again until it
	 * is accepted. The number of times mutations are rejected is retrievable 
	 * using the REVERTED_MUTATIONS stats field.
	 * 
	 * @param parent The program before the mutation operation.
	 * @param child  The program after the mutation operation has been carried 
	 * 				 out.
	 * @return True if the mutation operation should proceed, false if it is 
	 * rejected and should be retried with a new parent.
	 */
	public boolean acceptMutation(CandidateProgram<TYPE> parent, 
			   					  CandidateProgram<TYPE> child);

	/**
	 * Get a listener which will be informed of statistics about runs. The 
	 * listener will be queried for what fields are of interest, with those 
	 * statistics passed to the runStats method in the same order at the end 
	 * of each run. 
	 * 
	 * @return A RunStatListener to handle run statistics.
	 */
	public RunStatListener getRunStatListener();
	
	/**
	 * Get a listener which will be informed of statistics about generations. 
	 * The listener will be queried for what fields are of interest, with those 
	 * statistics passed to the generationStats method in the same order after 
	 * each generation. 
	 * 
	 * @return A GenerationStatListener to handle generation statistics.
	 */
	public GenerationStatListener getGenerationStatListener();
	
	/**
	 * Get a listener which will be informed of statistics about crossovers. 
	 * The listener will be queried for what fields are of interest, with those 
	 * statistics passed to the crossoverStats method in the same order after 
	 * each crossover operation. 
	 * 
	 * @return A CrossoverStatListener to handle crossover statistics.
	 */
	public CrossoverStatListener getCrossoverStatListener();
	
	/**
	 * Get a listener which will be informed of statistics about mutations. 
	 * The listener will be queried for what fields are of interest, with those 
	 * statistics passed to the mutationStats method in the same order after 
	 * each mutation operation. 
	 * 
	 * @return A MutationStatListener to handle mutation statistics.
	 */
	public MutationStatListener getMutationStatListener();
	
	/**
	 * Get a listener which will be informed of each stage of a GP run's life 
	 * cycle, and given the facility to confirm or modify each step.
	 * 
	 * @return A LifeCycleListener to confirm or modify each stage of the run's
	 * life cycle.
	 */
	public LifeCycleListener<TYPE> getLifeCycleListener();
}
