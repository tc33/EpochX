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
package com.epochx.core.initialisation;

import java.util.ArrayList;

import com.epochx.core.*;
import com.epochx.core.representation.CandidateProgram;
import com.epochx.core.representation.Node;
import core.SemanticModule;

/**
 * 
 */
public class RampedHalfAndHalfInitialiser extends Initialiser {
	
	private GrowInitialiser grow;
	private FullInitialiser full;
	private CandidateProgram candidate;
	private int depth;

	/**
	 * @param syntax A set of nodes containing all functions and terminals
	 * @param functions A set of nodes containing all the functions
	 * @param terminals A set of nodes containing all the terminals
	 * @param semMod The semantic module for this model
	 * @param popSize The population size
	 * @param depth The max depth of the program tree on initialisation
	 * @throws IllegalArgumentException for the max depth being too small to work RHH
	 */
	public RampedHalfAndHalfInitialiser(GPConfig config, SemanticModule semMod) {
		super(config, semMod);
		// set up the grow and full parts
		grow = new GrowInitialiser(config, semMod);
		full = new FullInitialiser(config, semMod);
		// modify depth for staged increase as per Koza
		if(depth>=6) {
			this.depth = depth - 4;
		} else {
			throw new IllegalArgumentException("MAX DEPTH TOO SMALL FOR RH+H");
		}
	}
	
	public ArrayList<CandidateProgram> buildFirstGeneration() {
		
		// initialise population of candidate programs
		ArrayList<CandidateProgram> firstGen = new ArrayList<CandidateProgram>(super.getPopSize());
		
		// build population
		int split = super.getPopSize() / 5;
		int marker = super.getPopSize() / 5;
		for(int i=0; i<super.getPopSize(); i++) {
			if(i>split) {
				depth++;
				split = split + marker;
			}
            candidate = new CandidateProgram(grow.buildGrowNodeTree(depth));
            while(firstGen.contains(candidate)) {
                candidate = new CandidateProgram(grow.buildGrowNodeTree(depth));
            }
            firstGen.add(candidate);
            candidate = new CandidateProgram(full.buildFullNodeTree(depth));
            while(firstGen.contains(candidate)) {
                candidate = new CandidateProgram(full.buildFullNodeTree(depth));
            }
            firstGen.add(candidate);
			
        }
		
		// return starting population
		return firstGen;
	}

}
