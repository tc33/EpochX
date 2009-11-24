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
package org.epochx.pruning;

import java.util.*;

import org.epochx.core.GPModel;
import org.epochx.representation.*;
import org.epochx.semantics.*;


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
	public SemanticPruner(List<Node> population, GPModel model, SemanticModule semMod) {
		
		// reduce to behaviour
		List<Representation> behaviours = new ArrayList<Representation>(model.getPopulationSize());
		for(Node c: population) {
			behaviours.add(semMod.codeToBehaviour(c));
		}
		
		// pass through and check for constant behaviour when returning to syntax
		for(int i = 0; i<model.getPopulationSize(); i++) {
			if(behaviours.get(i).isConstant()) {
				// replace with random terminal
				Random rGen = new Random();
				int n = model.getTerminals().size();
				Node terminal = (Node) model.getTerminals().get(rGen.nextInt(n));
				population.set(i, terminal);
			} else {
				// return to syntax
				population.set(i, semMod.behaviourToCode(behaviours.get(i)));
			}
		}		
	}
}
