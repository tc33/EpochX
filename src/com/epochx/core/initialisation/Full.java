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
public class Full implements Initialiser {
	
	private int depth;
	private int popSize;
	private SemanticModule semMod;
	private ArrayList<Node> syntax;
	private ArrayList<Node> terminals;
	private ArrayList<Node> functions;
	private Random rGen;
	private int noFuncs;
	private int noTerms;

	/* (non-Javadoc)
	 * @see com.epochx.core.initialisation.Initialiser#initialise(java.util.ArrayList, java.util.ArrayList, java.util.ArrayList, core.SemanticModule, int)
	 */
	@Override
	public ArrayList<CandidateProgram> initialise(ArrayList<Node> syntax,
			ArrayList<Node> functions, ArrayList<Node> terminals,
			SemanticModule semMod, int popSize, int depth) {
		
		// calculate no of functions and terminals
		noFuncs = functions.size();
		noTerms = terminals.size();
		
		// set all private variables
		this.depth = depth;
		this.popSize = popSize;
		this.syntax = syntax;
		this.functions = functions;
		this.terminals = terminals;
		this.semMod = semMod;
		rGen = new Random();
		
		// initialise population of candidate programs
		ArrayList<CandidateProgram> firstGen = new ArrayList<CandidateProgram>(popSize);
		
		// build population
		for(int i=0; i<popSize; i++) {
            CandidateProgram candidate = new CandidateProgram(buildFullNodeTree(depth));
            while(firstGen.contains(candidate)) {
                candidate = new CandidateProgram(buildFullNodeTree(depth));
            }
            firstGen.add(candidate);
        }
		
		// return starting population
		return firstGen;
	}
	
	public Node buildFullNodeTree(int depth) {		
		// make full node tree
		
        // define top node form functions
        int ran = rGen.nextInt(noFuncs);
        Node top = functions.get(ran);
        
        // TODO recurse down each branch to depth
        
        // TODO check for dead alleles?        
        
        // return top node
        return top;
	}
}
