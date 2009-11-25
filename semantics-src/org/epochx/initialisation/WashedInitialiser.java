/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming for research
 *
 *  EpochX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EpochX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EpochX.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.epochx.initialisation;

import java.util.ArrayList;
import java.util.List;

import org.epochx.core.GPModel;
import org.epochx.op.initialisation.*;
import org.epochx.representation.*;
import org.epochx.semantics.*;


/**
 * The washed initialised creates programs using the modified full method and then reduces
 * the programs by translating them to a representation and back again. This produces 
 * smaller more effective node trees.
 */
public class WashedInitialiser<TYPE> implements Initialiser<TYPE> {
	
	private GPModel<TYPE> model;
	private FullInitialiser<TYPE> full;
	private SemanticModule<TYPE> semanticModule;
	
	/**
	 * Constructor for Washed Initialiser
	 * @param model The current model
	 * @param semMod The associated semantic module
	 */
	public WashedInitialiser(GPModel<TYPE> model, SemanticModule<TYPE> semMod) {
		this.model = model;
		this.full = new FullInitialiser<TYPE>(model);
		this.semanticModule = semMod;
	}

	/* (non-Javadoc)
	 * @see org.epochx.core.initialisation.Initialiser#getInitialPopulation()
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
            	representation = semanticModule.codeToBehaviour(candidate.getRootNode());
			} while (firstGen.contains(candidate) || representation.isConstant());
			firstGen.add(candidate);
        }
		// reduce population
		for(int i=0; i<popSize; i++) {
			CandidateProgram<TYPE> candidate;
			Representation representation;
			Node<TYPE> reducedCandidate;
			candidate = firstGen.get(i);
			representation = semanticModule.codeToBehaviour(candidate.getRootNode());
			reducedCandidate = semanticModule.behaviourToCode(representation);
			firstGen.set(i, new CandidateProgram<TYPE>(reducedCandidate, model));
        }
		
		// stop the semantic module
		this.semanticModule.stop();
		
		// Return starting population.
		return firstGen;
	}

}
