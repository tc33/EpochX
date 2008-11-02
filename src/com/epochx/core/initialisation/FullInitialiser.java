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

import com.epochx.core.*;
import com.epochx.core.representation.*;
import core.*;

/**
 * 
 */
public class FullInitialiser extends Initialiser {
	
	public FullInitialiser(GPConfig config, SemanticModule semMod) {
		super(config, semMod);
	}
	
	public ArrayList<CandidateProgram> buildFirstGeneration() {
		
		// initialise population of candidate programs
		ArrayList<CandidateProgram> firstGen = new ArrayList<CandidateProgram>(super.getPopSize());
		
		// build population
		for(int i=0; i<super.getPopSize(); i++) {
            CandidateProgram candidate = new CandidateProgram(buildFullNodeTree(super.getDepth()));
            while(firstGen.contains(candidate)) {
                candidate = new CandidateProgram(buildFullNodeTree(super.getDepth()));
            }
            firstGen.add(candidate);
        }
		
		// return starting population
		return firstGen;
	}
	
	public Node buildFullNodeTree(int depth) {		
		// make full node tree
		
        // define top node form functions
        int ran = super.getRandom().nextInt(super.getNoFunctions());
        Node top = super.getFunctions().get(ran);
        
        // recurse down each branch to depth
        this.fillChildren(top, 1, super.getDepth());        
        
        // return top node
        return top;
	}
	
	public void fillChildren(Node topNode, int currentDepth, int maxDepth) {
		int arity = topNode.getArity();
		if(currentDepth<maxDepth) {
			// fill children with functions only
			for(int i = 0; i<arity; i++) {
				int ran = super.getRandom().nextInt(super.getNoFunctions());
				Node child = super.getFunctions().get(ran);
				topNode.setChild(child, i);
				this.fillChildren(child, (currentDepth+1), maxDepth);
			}
		} else {
			// fill children with terminals only
			for(int i = 0; i<arity; i++) {
				int ran = super.getRandom().nextInt(super.getNoTerminals());
				Node child = super.getTerminals().get(ran);
				topNode.setChild(child, i);
			}
		}
	}
}
