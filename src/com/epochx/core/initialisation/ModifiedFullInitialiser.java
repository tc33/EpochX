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

import java.util.*;
import com.epochx.core.GPModel;
import com.epochx.core.representation.*;
import com.epochx.semantics.*;

/**
 * @author Lawrence Beadle & Tom Castle
 * The modified FULL initialiser creates a starting population similar to full
 * which will not produce any programs which are not dependent on any of the terminals.
 */
public class ModifiedFullInitialiser<TYPE> implements Initialiser<TYPE> {
	
	private GPModel<TYPE> model;
	private FullInitialiser<TYPE> full;
	private SemanticModule semanticModule;
	
	/**
	 * Constructor for Modified Full Initialiser
	 * @param model The current model
	 * @param semMod The associated semantic module
	 */
	public ModifiedFullInitialiser(GPModel<TYPE> model, SemanticModule semMod) {
		this.model = model;
		this.full = new FullInitialiser<TYPE>(model);
		this.semanticModule = semMod;
	}

	/* (non-Javadoc)
	 * @see com.epochx.core.initialisation.Initialiser#getInitialPopulation()
	 */
	@Override
	public List<CandidateProgram<TYPE>> getInitialPopulation() {
		// Initialise population of candidate programs.
		int popSize = model.getPopulationSize();
		List<CandidateProgram<TYPE>> firstGen = new ArrayList<CandidateProgram<TYPE>>(popSize);
		// start the semantic module
		this.semanticModule.start();
		// Build population		
		for(int i=0; i<popSize; i++) {
			CandidateProgram<TYPE> candidate;
			Representation representation;
			do {
            	candidate = new CandidateProgram<TYPE>(full.buildFullNodeTree(model.getInitialMaxDepth()), model);
            	representation = semanticModule.codeToBehaviour(candidate);
			} while (firstGen.contains(candidate) || representation.isConstant());
			firstGen.add(candidate);
        }
		// stop the semantic module
		this.semanticModule.stop();
		
		// Return starting population.
		return firstGen;
	}

}
