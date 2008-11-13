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
 * 
 */
public abstract class GPAbstractModel<TYPE> implements GPModeld<TYPE> {

	private Initialiser<TYPE> initialiser;
	private Crossover<TYPE> crossover;
	//private Mutator mutator;
	
	private PouleSelector<TYPE> pouleSelector;
	private ParentSelector<TYPE> parentSelector;
	private int pouleSize;

	private int noRuns;
	private int noGenerations;
	private int populationSize;
	private int noElites;
	private int maxDepth;
	
	private double crossoverProbability;
	private double reproductionProbability;
	private double mutationProbability;
	
	public GPAbstractModel() {		
		// Set defaults.
		noRuns = 1;
		noGenerations = 10;
		populationSize = 100;
		maxDepth = 6;
		crossoverProbability = 1;
		reproductionProbability = 0;
		mutationProbability = 0;
		pouleSize = -1;
		noElites = 0;
		
		initialiser = new FullInitialiser<TYPE>(this);
		crossover = new UniformPointCrossover<TYPE>(this);
	}

	/**
	 * @return the pouleSize
	 */
	public int getPouleSize() {
		return pouleSize;
	}

	/**
	 * @param pouleSize the pouleSize to set
	 */
	public void setPouleSize(int pouleSize) {
		this.pouleSize = pouleSize;
	}

	/**
	 * @return the initialiser
	 */
	public Initialiser<TYPE> getInitialiser() {
		return initialiser;
	}

	/**
	 * @param initialiser the initialiser to set
	 */
	public void setInitialiser(Initialiser<TYPE> initialiser) {
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
	
	public List<Node<?>> getSyntax() {
		List<Node<?>> syntax = new ArrayList<Node<?>>(getTerminals());
		syntax.addAll(getFunctions());
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
	public Crossover<TYPE> getCrossover() {
		return crossover;
	}

	/**
	 * @param crossover the crossover to set
	 */
	public void setCrossover(Crossover<TYPE> crossover) {
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
	public void setReproductionProbability(double reproductionProbability) {
		this.reproductionProbability = reproductionProbability;
	}

	/**
	 * @return the reproductionProbability
	 */
	public double getReproductionProbability() {
		return reproductionProbability;
	}

	/**
	 * @return the mutationProbability
	 */
	public double getMutationProbability() {
		return mutationProbability;
	}

	/**
	 * @param mutationProbability the mutationProbability to set
	 */
	public void setMutationProbability(double mutationProbability) {
		this.mutationProbability = mutationProbability;
	}

	/**
	 * @return the pouleSelector
	 */
	public PouleSelector<TYPE> getPouleSelector() {
		return pouleSelector;
	}

	/**
	 * @param pouleSelector the pouleSelector to set
	 */
	public void setPouleSelector(PouleSelector<TYPE> pouleSelector) {
		this.pouleSelector = pouleSelector;
	}

	/**
	 * @return the parentSelector
	 */
	public ParentSelector<TYPE> getParentSelector() {
		return parentSelector;
	}

	/**
	 * @param parentSelector the parentSelector to set
	 */
	public void setParentSelector(ParentSelector<TYPE> parentSelector) {
		this.parentSelector = parentSelector;
	}

	/**
	 * @return the noElites
	 */
	public int getNoElites() {
		return noElites;
	}

	/**
	 * @param noElites the noElites to set
	 */
	public void setNoElites(int noElites) {
		this.noElites = noElites;
	}	
}
