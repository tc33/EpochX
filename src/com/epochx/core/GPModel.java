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

import com.epochx.core.crossover.*;
import com.epochx.core.initialisation.*;
import com.epochx.core.representation.*;
import com.epochx.core.selection.*;

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
	 * Retrieve the size of the breeding pool to be used for parent selection 
	 * when performing the genetic operators. If the pool size is equal to or 
	 * less than zero, or if getPouleSelector() returns null, then no poule 
	 * will be used and parent selection will take place directly from the 
	 * previous population.
	 * 
	 * @return the size of the mating pool to build with the PouleSelector 
	 * 		   returned by getPouleSelector() which will be used for parent 
	 * 		   selection.
	 */
	public int getPouleSize();

	/**
	 * Retrieve the Initialiser which will generate the first generation 
	 * population from which the evolution will proceed.
	 * 
	 * @return the Initialiser to create the first population.
	 */
	public Initialiser<TYPE> getInitialiser();

	/**
	 * Retrieve the number of runs that should be carried out using this model 
	 * as the basis. Each call to GPRun.run() will be with the same model so 
	 * this is useful when multiple runs are necessary with the same control 
	 * parameters for research purposes or otherwise in order to attain 
	 * reliable results drawn from means.
	 * 
	 * @return the number of times this model should be used to control GP runs.
	 */
	public int getNoRuns();

	/**
	 * Retrieve the number of generations that each run should use before 
	 * terminating, unless prior termination occurs due to one of the other 
	 * termination criterion.
	 * 
	 * @return the number of generations that should be evolved in each run.
	 */
	public int getNoGenerations();
	
	/**
	 * Retrieve the number of CandidatePrograms that 
	 * 
	 * @return the generationSize
	 */
	public int getPopulationSize();

	/**
	 * @return the terminals
	 */
	public List<TerminalNode<?>> getTerminals();

	/**
	 * @return the functions
	 */
	public List<FunctionNode<?>> getFunctions();
	
	/**
	 * 
	 * @return
	 */
	public List<Node<?>> getSyntax();
	
	/**
	 * @return the depth
	 */
	public int getMaxDepth();	

	/**
	 * @return the crossover
	 */
	public Crossover<TYPE> getCrossover();

	/**
	 * @return the crossoverProbability
	 */
	public double getCrossoverProbability();

	/**
	 * @return the reproductionProbability
	 */
	public double getReproductionProbability();

	/**
	 * @return the mutationProbability
	 */
	public double getMutationProbability();
	
	/**
	 * @return the pouleSelector
	 */
	public PouleSelector<TYPE> getPouleSelector();

	/**
	 * @return the parentSelector
	 */
	public ParentSelector<TYPE> getParentSelector();

	/**
	 * @return the noElites
	 */
	public int getNoElites();


	/**
	 * 
	 * @param program
	 * @return
	 */
	public double getFitness(CandidateProgram<TYPE> program);
}
