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
package com.epochx.core.crossover;

import java.util.*;
import com.epochx.core.representation.*;
import core.*;

/**
 * 
 */
public abstract class Crossover {
	
	private ArrayList<CandidateProgram> population;
	private SemanticModule semMod;
	private double pCrossover;
	private int maxDepth, popSize, poolSize, elites;
	private boolean stateChecker;
	private CandidateProgram parent1;
	private CandidateProgram parent2;
	private Random rGen;
	private ArrayList<CandidateProgram> newPopulation;
	
	public Crossover(ArrayList<CandidateProgram> population, 
			double pCrossover, SemanticModule semMod, int maxDepth,
			boolean stateChecker, int populationSize, int elites) {
		this.population = population;
		this.pCrossover = pCrossover;
		this.semMod = semMod;
		this.maxDepth = maxDepth;
		this.stateChecker = stateChecker;
		this.popSize = populationSize;
		rGen = new Random();
		newPopulation = new ArrayList<CandidateProgram>();
		poolSize = population.size();
		this.elites = elites;
		// copy through elites
		if(elites<=poolSize) {
			for(int i = 0; i<elites; i++) {
				newPopulation.add(population.get(i));
			}
		} else {
			throw new IllegalArgumentException("ELITES ARE GREATER THAN SELECTED POPULATION!!!");
		}
	}
	
	public CandidateProgram getParent1() {
		parent1 = population.get(rGen.nextInt(poolSize));
		return parent1;
	}
	
	public CandidateProgram getParent2() {
		parent1 = population.get(rGen.nextInt(poolSize));
		return parent2;
	}
	
	public SemanticModule getSemanticModule() {
		return semMod;
	}
	
	public boolean getStateChecker() {
		return stateChecker;
	}
	
	public abstract void doCrossover();

}
