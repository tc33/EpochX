/*  
 *  Copyright 2007-2010 Tom Castle & Lawrence Beadle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming software for research
 *
 *  EpochX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EpochX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 *  The latest version is available from: http:/www.epochx.org
 */
package org.epochx.gr.op.init;

import java.util.*;

import org.epochx.core.Controller;
import org.epochx.gr.model.GRModel;
import org.epochx.gr.representation.GRCandidateProgram;
import org.epochx.life.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.grammar.*;
import org.epochx.tools.random.RandomNumberGenerator;


/**
 *
 */
public class GrowInitialiser implements GRInitialiser {

	// The current controlling model.
	private GRModel model;
	
	private RandomNumberGenerator rng;
	private Grammar grammar;
	private int popSize;
	private int maxInitialProgramDepth;
	
	/**
	 * Constructs a grow initialiser.
	 * 
	 * @param model
	 */
	public GrowInitialiser(GRModel model) {
		this.model = model;
		
		// Initialise the object.
		initialise();
		
		// Re-initialise at the start of every generation.
		Controller.getLifeCycleManager().addGenerationListener(new GenerationAdapter() {
			@Override
			public void onGenerationStart() {
				initialise();
			}
		});
	}
	
	private void initialise() {
		rng = model.getRNG();
		grammar = model.getGrammar();
		popSize = model.getPopulationSize();
		maxInitialProgramDepth = model.getMaxInitialProgramDepth();
	}
	
	@Override
	public List<CandidateProgram> getInitialPopulation() {
		// Create population list to be populated.
		List<CandidateProgram> firstGen = new ArrayList<CandidateProgram>(popSize);
		
		// Create and add new programs to the population.
		for(int i=0; i<popSize; i++) {
			GRCandidateProgram candidate;
			
			do {
				// Create a new program down to the models initial max depth.
				candidate = getInitialProgram(maxInitialProgramDepth);
			} while (firstGen.contains(candidate));
			
			// Add to the new population.
			firstGen.add(candidate);
        }
		
		return firstGen;
	}
	
	public GRCandidateProgram getInitialProgram(int depth) {
		GrammarRule startRule = grammar.getStartRule();
		
		return new GRCandidateProgram(growParseTree(depth, startRule), model);
	}
	
	public NonTerminalSymbol growParseTree(int depth, GrammarRule startRule) {
		//TODO Check it is possible to create a program inside the max depth.
		
		NonTerminalSymbol parseTree = new NonTerminalSymbol(startRule);
		
		buildDerivationTree(parseTree, startRule, 0, depth);
		
		return parseTree;		
	}
	
	private void buildDerivationTree(NonTerminalSymbol parseTree, GrammarRule rule, int depth, int maxDepth) {		
		// Check if theres more than one production.
		int productionIndex = 0;
		int noProductions = rule.getNoProductions();
		if (noProductions > 1) {
			List<Integer> validProductions = getValidProductionIndexes(rule.getProductions(), maxDepth-depth-1);
			
			// Choose a production randomly.
			int chosenProduction = rng.nextInt(validProductions.size());
			productionIndex = validProductions.get(chosenProduction);
		}

		// Drop down the tree at this production.
		GrammarProduction p = rule.getProduction(productionIndex);
		
		List<GrammarNode> grammarNodes = p.getGrammarNodes();
		for (GrammarNode node: grammarNodes) {
			if (node instanceof GrammarRule) {
				GrammarRule r = (GrammarRule) node;
				
				NonTerminalSymbol nt = new NonTerminalSymbol((GrammarRule) node);
				
				buildDerivationTree(nt, r, depth+1, maxDepth);
				
				parseTree.addChild(nt);
			} else {
				// Must be a grammar literal.
				parseTree.addChild(new TerminalSymbol((GrammarLiteral) node));
			}
		}
	}
	
	private List<Integer> getValidProductionIndexes(List<GrammarProduction> grammarProductions, int maxDepth) {
		List<Integer> valid = new ArrayList<Integer>();
		
		for (int i=0; i<grammarProductions.size(); i++) {
			GrammarProduction p = grammarProductions.get(i);
			
			if (p.getMinDepth() <= maxDepth) {
				valid.add(i);
			}
		}
		
		// If there were any valid recursive productions, return them, otherwise use the others.
		return valid;
	}
	
}
