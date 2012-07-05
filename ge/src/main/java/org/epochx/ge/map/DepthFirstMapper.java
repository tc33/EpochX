/*
 * Copyright 2007-2011 Tom Castle & Lawrence Beadle
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http://www.epochx.org
 */
package org.epochx.ge.map;

import static org.epochx.ge.GEIndividual.MAXIMUM_DEPTH;
import static org.epochx.grammar.Grammar.GRAMMAR;

import org.epochx.*;
import org.epochx.event.*;
import org.epochx.ge.*;
import org.epochx.grammar.*;

/**
 * 
 * 
 * @see BreadthFirstMapper
 */
public class DepthFirstMapper implements Mapper, Listener<ConfigEvent> {

	private Grammar grammar;
	private int maxDepth;

	public DepthFirstMapper() {
		this(true);
	}

	public DepthFirstMapper(boolean autoConfig) {
		setup();

		if (autoConfig) {
			EventManager.getInstance().add(ConfigEvent.class, this);
		}
	}

	/**
	 * Sets up this operator with the appropriate configuration settings.
	 * This method is called whenever a <tt>ConfigEvent</tt> occurs for a
	 * change in any of the following configuration parameters:
	 * <ul>
	 * <li>{@link Grammar#GRAMMAR}
	 * <li>{@link GEIndividual#MAXIMUM_DEPTH}
	 * </ul>
	 */
	protected void setup() {
		grammar = Config.getInstance().get(GRAMMAR);
		maxDepth = Config.getInstance().get(MAXIMUM_DEPTH); //TODO This shouldn't be compulsory
	}
	
	/**
	 * Receives configuration events and triggers this operator to reconfigure
	 * if the <tt>ConfigEvent</tt> is for one of its required parameters
	 * 
	 * @param event {@inheritDoc}
	 */
	@Override
	public void onEvent(ConfigEvent event) {
		if (event.isKindOf(GRAMMAR, MAXIMUM_DEPTH)) {
			setup();
		}
	}
	
	@Override
	public Population process(Population population) {
		for (Individual individual: population) {
			if (individual instanceof GEIndividual) {
				GEIndividual geIndividual = (GEIndividual) individual;
				geIndividual.setParseTree(map(geIndividual));
			}
		}
		
		return population;
	}

	/**
	 * Perform the mapping operation. Mapping starts at the starting symbol of
	 * the grammar and gradually constructs a program source by stepping down
	 * the grammar's tree and each time there are multiple productions to
	 * choose from, the next codon from the <code>GEIndividual's</code>
	 * chromosome is used. The codon simply undergoes modulo by the number of
	 * production choices, the result is the index of the production to be
	 * used.
	 * 
	 * @param individual the GEIndividual to be mapped to its source.
	 * @return a source string equivalent to the specified GEIndividual's
	 *         chromosome after mapping using the model's grammar. Null is
	 *         returned if
	 *         a valid string could not be generated.
	 */
	@Override
	public NonTerminalSymbol map(GEIndividual individual) {
		if (grammar == null) {
			throw new IllegalStateException("grammar not set");
		}

		GrammarRule grammarRoot = grammar.getStartRule();
		
		// The root of the parse tree
		NonTerminalSymbol root = new NonTerminalSymbol(grammarRoot);

		int codonsUsed = map(grammarRoot, root, 0, individual.getChromosome(), 0);

		if (codonsUsed == -1) {
			return null;
		}

		return root;
	}

	/*
	 * Recursive helper method to resolve mapping from codons to parse tree
	 */
	private int map(GrammarRule grammarRule, NonTerminalSymbol parseNode, int depth,
			Chromosome chromosome, int currentCodon) {
		// depth+1 because the current depth does not include terminals
		if (depth + 1 > maxDepth - 1) {
			return -1;
		}

		// Assume it must be a non-terminal symbol
		int noProductions = grammarRule.getNoProductions();

		// If only one choice, no need to use codon.
		GrammarProduction production;
		if (noProductions == 1) {
			production = grammarRule.getProduction(0);
		} else {
			// Pick a production using the next codon.
			Codon codon = chromosome.getCodon(currentCodon++);
			if (codon == null) {
				// Must have run out of codons, so mapping fails
				return -1;
			}
			
			int productionChoice = (int) (codon.value() % noProductions);
			production = grammarRule.getProduction(productionChoice);
		}

		// Map each symbol of the production
		for (GrammarNode s: production.getGrammarNodes()) {
			if (s instanceof GrammarLiteral) {
				parseNode.addChild(new TerminalSymbol((GrammarLiteral) s));
			} else {
				GrammarRule nextGrammarRule = (GrammarRule) s;
				NonTerminalSymbol nextParseNode = new NonTerminalSymbol(nextGrammarRule);
				parseNode.addChild(nextParseNode);

				currentCodon = map(nextGrammarRule, nextParseNode, depth + 1, chromosome, currentCodon);
				if (currentCodon == -1) {
					return -1;
				}
			}
		}

		return currentCodon;
	}

	public Grammar getGrammar() {
		return grammar;
	}

	public void setGrammar(Grammar grammar) {
		this.grammar = grammar;
	}

	public int getMaximumDepth() {
		return maxDepth;
	}

	public void setMaximumDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}
}
