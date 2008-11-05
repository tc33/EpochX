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

/**
 * 
 */
public class GPConfig {

	private Initialiser initialiser;
	private Crossover crossover;
	//private Mutator mutator;
	
	private int noRuns;
	private int noGenerations;
	private int populationSize;
	private int maxDepth;
	private double crossoverProbability;
	private int reproductionProbability;
	
	private List<TerminalNode<?>> terminals;
	private List<FunctionNode<?>> functions;
	
	public GPConfig() {
		// Set defaults.
		crossoverProbability = 1;
	}
	
	/**
	 * @return the initialiser
	 */
	public Initialiser getInitialiser() {
		return initialiser;
	}

	/**
	 * @param initialiser the initialiser to set
	 */
	public void setInitialiser(Initialiser initialiser) {
		this.initialiser = initialiser;
	}

	/**
	 * @return the noRuns
	 */
	public int getNoRuns() {
		return noRuns;
	}

	/**
	 * @param noRuns the noRuns to set
	 */
	public void setNoRuns(int noRuns) {
		this.noRuns = noRuns;
	}

	/**
	 * @return the noGenerations
	 */
	public int getNoGenerations() {
		return noGenerations;
	}

	/**
	 * @param noGenerations the noGenerations to set
	 */
	public void setNoGenerations(int noGenerations) {
		this.noGenerations = noGenerations;
	}
	
	/**
	 * @return the generationSize
	 */
	public int getPopulationSize() {
		return populationSize;
	}

	/**
	 * @param populationSize the generationSize to set
	 */
	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	/**
	 * @return the terminals
	 */
	public List<TerminalNode<?>> getTerminals() {
		return terminals;
	}

	/**
	 * @param terminals the terminals to set
	 */
	public void setTerminals(List<TerminalNode<?>> terminals) {
		this.terminals = terminals;
	}

	/**
	 * @return the functions
	 */
	public List<FunctionNode<?>> getFunctions() {
		return functions;
	}

	/**
	 * @param functions the functions to set
	 */
	public void setFunctions(List<FunctionNode<?>> functions) {
		this.functions = functions;
	}
	
	public List<Node<?>> getSyntax() {
		List<Node<?>> syntax = new ArrayList<Node<?>>(terminals);
		syntax.addAll(functions);
		return syntax;
	}
	
	/**
	 * @return the depth
	 */
	public int getMaxDepth() {
		return maxDepth;
	}

	/**
	 * @param maxDepth the depth to set
	 */
	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}
	

	/**
	 * @return the crossover
	 */
	public Crossover getCrossover() {
		return crossover;
	}

	/**
	 * @param crossover the crossover to set
	 */
	public void setCrossover(Crossover crossover) {
		this.crossover = crossover;
	}

	/**
	 * @param crossoverProbability the crossoverProbability to set
	 */
	public void setCrossoverProbability(double crossoverProbability) {
		this.crossoverProbability = crossoverProbability;
	}

	/**
	 * @return the crossoverProbability
	 */
	public double getCrossoverProbability() {
		return crossoverProbability;
	}

	/**
	 * @param reproductionProbability the reproductionProbability to set
	 */
	public void setReproductionProbability(int reproductionProbability) {
		this.reproductionProbability = reproductionProbability;
	}

	/**
	 * @return the reproductionProbability
	 */
	public int getReproductionProbability() {
		return reproductionProbability;
	}

}
