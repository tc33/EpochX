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
import com.epochx.core.representation.CandidateProgram;
import com.epochx.core.representation.Node;
import core.SemanticModule;

/**
 * 
 */
public class GrowInitialiser implements Initialiser {
	
	private GPConfig config;
	
	public GrowInitialiser(GPConfig config, SemanticModule semMod) {
		this.config = config;
	}
	
	public List<CandidateProgram> getInitialPopulation() {
		
		// initialise population of candidate programs
		int popSize = config.getPopulationSize();
		List<CandidateProgram> firstGen = new ArrayList<CandidateProgram>(popSize);
		
		// build population
		for(int i=0; i<popSize; i++) {
            CandidateProgram candidate = new CandidateProgram(buildGrowNodeTree(config.getDepth()));
            while(firstGen.contains(candidate)) {
                candidate = new CandidateProgram(buildGrowNodeTree(config.getDepth()));
            }
            firstGen.add(candidate);
        }
		
		// return starting population
		return firstGen;
	}
	
	public Node buildGrowNodeTree(int depth) {		
        // define top node
		int randomIndex = (int) Math.floor(Math.random() * config.getSyntax().size());
		Node top = null;
		try {
			top = (Node) config.getSyntax().get(randomIndex).clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // recurse down each branch to depth using Grow mechanism
        this.fillChildren(top, 1, config.getDepth());
        
        // return top node
        return top;
	}
	
	public void fillChildren(Node topNode, int currentDepth, int maxDepth) {
		int arity = topNode.getArity();
		if(arity>0) {
			if(currentDepth<maxDepth-1) {
				// fill children with functions or terminals
				for(int i = 0; i<arity; i++) {
					int randomIndex = (int) Math.floor(Math.random() * config.getSyntax().size());
					Node child = null;
					try {
						child = (Node) config.getSyntax().get(randomIndex).clone();
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					topNode.setChild(child, i);
					this.fillChildren(child, (currentDepth+1), maxDepth);
				}
			} else {
				// fill children with terminals only
				for(int i = 0; i<arity; i++) {
					int randomIndex = (int) Math.floor(Math.random() * config.getTerminals().size());
					Node child = null;
					try {
						child = (Node) config.getTerminals().get(randomIndex).clone();
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					topNode.setChild(child, i);
				}
			}
		}
	}
}
