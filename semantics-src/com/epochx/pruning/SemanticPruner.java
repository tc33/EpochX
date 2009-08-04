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
package com.epochx.pruning;

import java.util.*;
import com.epochxge.core.GEModel;
import com.epochxge.representation.*;
import com.epochx.semantics.*;

/**
 * The semantic pruner class provides a method to reconstruct code in a reduced from which contains
 * no introns
 */
public class SemanticPruner {
	
	/**
	 * SemanticPruner constructor which provides all the functionality for this class
	 * @param population The GP population of candidate programs
	 * @param model The GP model in use
	 * @param semMod The semantic module in use
	 */
	public SemanticPruner(List<CandidateProgram> population, GEModel model, SemanticModule semMod) {
		
		// reduce to behaviour
		List<Representation> behaviours = new ArrayList<Representation>(model.getPopulationSize());
		for(CandidateProgram c: population) {
			behaviours.add(semMod.codeToBehaviour(c));
		}
		
		// pass through and check for constant behaviour when returning to syntax
		for(int i = 0; i<model.getPopulationSize(); i++) {
			if(behaviours.get(i).isConstant()) {
				// replace with random terminal
				// TODO generate random program (preferably terminal) 
			} else {
				// return to syntax
				population.set(i, semMod.behaviourToCode(behaviours.get(i)));
			}
		}		
	}
}
