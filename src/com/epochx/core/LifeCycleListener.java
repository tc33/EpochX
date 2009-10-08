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

import java.util.*;

import com.epochx.representation.*;

/**
 * Implementations of LifeCycleListener can be used to listen for events 
 * throughout the life of a GE run. The provided methods will be called at the 
 * relevant times during execution, and have the opportunity to confirm the 
 * operation by returning the appropriate argument value, modify the operation 
 * by altering the argument before returning and in some cases it is possible 
 * to trigger a 'reversion' by returning null. A reversion is where the 
 * operation is discarded and rerun. In the case of reversion, the life cycle 
 * method will be recalled in the same way during the rerun, allowing the life 
 * cycle listener the ability to revert indefinitely. It is worth noting that 
 * this leaves room for the potential of an infinite loop if any of these 
 * methods were defined to return null in all possible circumstances. The 
 * number of reversions for crossover and mutation is obtainable from the 
 * crossover stats and mutation stats respectively.
 */
public interface LifeCycleListener<TYPE> {
	
	/**
	 * Called after initialisation.
	 * 
	 * @param pop the newly initialised population.
	 * @return the population of CandidatePrograms to continue with as the 
	 * newly initialised population, or null if initialisation should be rerun.
	 */
	public List<CandidateProgram<TYPE>> onInitialisation(List<CandidateProgram<TYPE>> pop);
	
	/**
	 * Called after selection of elites. If the number of elites to use is set 
	 * by the model to <=0, then this method will still be called at the 
	 * appropriate time, but with an empty list.
	 * 
	 * @param elites the selection of chosen elites.
	 * @return a List of CandidatePrograms to use as the set of elites. Note 
	 * that it is not appropriate to return a value of null and this will cause 
	 * undefined behaviour.
	 */
	public List<CandidateProgram<TYPE>> onElitism(List<CandidateProgram<TYPE>> elites);
	
	/**
	 * Called after selection of the breeding pool. If the size of the breeding
	 * pool is set in the model to <=0, then this method will still be called 
	 * at the appropriate time, but with a list containing every program in the 
	 * population. The population essentially becomes the breeding pool in this 
	 * circumstance.
	 * 
	 * @param pool the suggested breeding pool of programs.
	 * @return the breeding pool of CandidatePrograms that should actually be 
	 * used, or null if breeding pool selection should be repeated.
	 */
	public List<CandidateProgram<TYPE>> onPoolSelection(List<CandidateProgram<TYPE>> pool);
	
	//public CandidateProgram[] onCrossoverSelection(List<CandidateProgram> pop, CandidateProgram[] parents);
	
	/**
	 * Called after selection and crossover of 2 individuals.
	 * 
	 * @param parents the programs that were selected to undergo crossover.
	 * @param children the programs that were generated as a result of the 
	 * crossover operation.
	 * @return an array of CandidatePrograms to be used as the children of the 
	 * crossover operation, or null if the crossover should be reverted.
	 */
	public CandidateProgram<TYPE>[] onCrossover(CandidateProgram<TYPE>[] parents, CandidateProgram<TYPE>[] children);
	
	//public CandidateProgram[] onCrossoverAndSelection(CandidateProgram[] parents, CandidateProgram[] children);
	
	//public CandidateProgram onMutationSelection(List<CandidateProgram> pop, CandidateProgram parent);
	
	/**
	 * Called after selection and mutation of an individual program.
	 * 
	 * @param parent the program that was selected to undergo mutation.
	 * @param child the resultant program from the parent undergoing mutation.
	 * @return a CandidateProgram that should be considered the result of a 
	 * mutation operation, or null if the mutation should be reverted.
	 */
	public CandidateProgram<TYPE> onMutation(CandidateProgram<TYPE> parent, CandidateProgram<TYPE> child);
	
	//public CandidateProgram onMutationAndSelection(CandidateProgram parent, CandidateProgram child);
	
	/**
	 * Called after selection of an individual to be reproduced into the next 
	 * generation.
	 * 
	 * @param child the program that was selected to be reproduced.
	 * @return a CandidateProgram that should be used as the reproduced program
	 * and inserted into the next population.
	 */
	public CandidateProgram<TYPE> onReproduction(CandidateProgram<TYPE> child);
	
	/**
	 * Called on termination of the GE run where execution ended because of the 
	 * successful identification of a CandidateProgram with a fitness equal to 
	 * or lower than the models termination fitness parameter.
	 */
	public void onFitnessTermination();
	
	/**
	 * Called on termination of the GE run where execution ended because the 
	 * requested number of generations was completed without identification of 
	 * a CandidateProgram with a fitness equal to or lower than the models 
	 * termination fitness parameter.
	 */
	public void onGenerationTermination();
}
