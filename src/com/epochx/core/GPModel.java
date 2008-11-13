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

public interface GPModel<TYPE> {
	
	/**
	 * @return the pouleSize
	 */
	public int getPouleSize();

	/**
	 * @return the initialiser
	 */
	public Initialiser<TYPE> getInitialiser();

	/**
	 * @return the noRuns
	 */
	public int getNoRuns();

	/**
	 * @return the noGenerations
	 */
	public int getNoGenerations();
	
	/**
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


	public double getFitness(CandidateProgram<TYPE> program);
}
