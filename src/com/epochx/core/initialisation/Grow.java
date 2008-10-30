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
import java.util.Random;
import com.epochx.core.representation.CandidateProgram;
import com.epochx.core.representation.Node;
import core.SemanticModule;

/**
 * 
 */
public class Grow extends Initialiser {
	
	public Grow(ArrayList<Node> syntax, ArrayList<Node> functions, 
			ArrayList<Node> terminals, SemanticModule semMod, 
			int popSize, int depth) {
		super(syntax, functions, terminals, semMod, popSize, depth);
	}
	
	public ArrayList<CandidateProgram> buildFirstGeneration() {
		
		// initialise population of candidate programs
		ArrayList<CandidateProgram> firstGen = new ArrayList<CandidateProgram>(super.getPopSize());
		
		// build population
		for(int i=0; i<super.getPopSize(); i++) {
            CandidateProgram candidate = new CandidateProgram(buildGrowNodeTree(super.getDepth()));
            while(firstGen.contains(candidate)) {
                candidate = new CandidateProgram(buildGrowNodeTree(super.getDepth()));
            }
            firstGen.add(candidate);
        }
		
		// return starting population
		return firstGen;
	}
	
	public Node buildGrowNodeTree(int depth) {		
		// make full node tree
		
        // define top node form functions
        int ran = super.getRandom().nextInt(super.getNoFunctions());
        Node top = super.getFunctions().get(ran);
        
        // TODO recurse down each branch to depth     
        
        // return top node
        return top;
	}
}
