/* 
 * Copyright 2007-2010 Tom Castle & Lawrence Beadle
 * Licensed under GNU General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.gr.op.init;

import java.util.*;

import org.epochx.gr.model.GRAbstractModel;
import org.epochx.gr.representation.GRCandidateProgram;
import org.epochx.life.ConfigAdapter;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.grammar.*;
import org.epochx.tools.random.RandomNumberGenerator;


/**
 * Note: full initialisation currently only works for depth first mapping.
 *
 */
public class FullInitialiser implements GRInitialiser {
	
	// The controlling model.
	private GRAbstractModel model;
	
	private RandomNumberGenerator rng;
	private Grammar grammar;
	private int popSize;
	private int maxInitialProgramDepth;
	
	/**
	 * Constructs a full initialiser.
	 * 
	 * @param model
	 */
	public FullInitialiser(GRAbstractModel model) {
		this.model = model;
		
		// Configure parameters from the model.
		model.getLifeCycleManager().addConfigListener(new ConfigAdapter() {
			@Override
			public void onConfigure() {
				configure();
			}
		});
	}
	
	/*
	 * Configure component with parameters from the model.
	 */
	private void configure() {
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
				// Create a new program at the models initial max depth.
				candidate = getInitialProgram(maxInitialProgramDepth);
			} while (firstGen.contains(candidate));
			
			// Add to the new population.
			firstGen.add(candidate);
        }
		
		return firstGen;
	}

	public GRCandidateProgram getInitialProgram(int depth) {
		GrammarRule startRule = grammar.getStartRule();
		
		//TODO Check it is possible to create a program inside the max depth.
		
		NonTerminalSymbol parseTree = new NonTerminalSymbol(startRule);
		
		buildDerivationTree(parseTree, startRule, 0, depth);

		return new GRCandidateProgram(parseTree, model);
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
		List<Integer> validRecursive = new ArrayList<Integer>();
		List<Integer> validAll = new ArrayList<Integer>();
		
		for (int i=0; i<grammarProductions.size(); i++) {
			GrammarProduction p = grammarProductions.get(i);
			
			if (p.getMinDepth() <= maxDepth) {
				validAll.add(i);
				
				if (p.isRecursive()) {
					validRecursive.add(i);
				}
			}
		}
		
		// If there were any valid recursive productions, return them, otherwise use the others.
		return validRecursive.isEmpty() ? validAll : validRecursive;
	}
}
