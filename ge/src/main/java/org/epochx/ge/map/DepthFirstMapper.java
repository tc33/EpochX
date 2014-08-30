/*
 * Copyright 2007-2013
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

import static org.epochx.Config.Template.TEMPLATE;
import static org.epochx.ge.GEIndividual.MAXIMUM_DEPTH;
import static org.epochx.grammar.Grammar.GRAMMAR;

import org.epochx.*;
import org.epochx.event.*;
import org.epochx.ge.*;
import org.epochx.ge.map.MappingEvent.EndMapping;
import org.epochx.grammar.*;

/**
 * Mapper which converts the chromosome of a <code>GEIndividual</code> into a parse tree 
 * which is assigned to the individual. The parse tree is created by traversing the 
 * grammar in a depth-first order and using codons to choose between productions. The
 * codons are used in sequence, with the value of the codon, modulo the number of 
 * productions, determining the index of the production to use. 
 * 
 * This mapping process is described in detail in Grammatical Evolution publications.
 * 
 * @since 2.0
 */
public class DepthFirstMapper extends AbstractMapper implements Listener<ConfigEvent> {

	// Configuration settings
	private Grammar grammar;
	private Integer maxDepth;

	/**
	 * Constructs a <code>DepthFirstMapper</code> with control parameters automatically 
	 * loaded from the config
	 */
	public DepthFirstMapper() {
		this(true);
	}

	/**
	 * Constructs a <code>DepthFirstMapper</code> with control parameters
	 * initially loaded from the config. If the <code>autoConfig</code> argument is
	 * set to <code>true</code> then the configuration will be automatically updated
	 * when the config is modified.
	 * 
	 * @param autoConfig whether this operator should automatically update its
	 *        configuration settings from the config
	 */
	public DepthFirstMapper(boolean autoConfig) {
		setup();

		if (autoConfig) {
			EventManager.getInstance().add(ConfigEvent.class, this);
		}
	}

	/**
	 * Sets up this operator with the appropriate configuration settings.
	 * This method is called whenever a <code>ConfigEvent</code> occurs for a
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
	 * Receives configuration events and triggers this fitness function to 
	 * configure its parameters if the <code>ConfigEvent</code> is for one of 
	 * its required parameters.
	 * 
	 * @param event {@inheritDoc}
	 */
	@Override
	public void onEvent(ConfigEvent event) {
		if (event.isKindOf(TEMPLATE, GRAMMAR, MAXIMUM_DEPTH)) {
			setup();
		}
	}

	/**
	 * Performs the mapping operation. Mapping starts at the starting symbol of
	 * the grammar and gradually constructs a parse tree by stepping down
	 * the grammar's tree and each time there are multiple productions to
	 * choose from, the next codon from the <code>GEIndividual</code>'s
	 * chromosome is used. The codon simply undergoes modulo by the number of
	 * production choices, the result is the index of the production to be
	 * used.
	 * 
	 * @param individual the <code>GEIndividual</code> to be mapped to its source
	 * @return a parse tree equivalent to the specified <code>GEIndividual</code>'s
	 *         chromosome after mapping using the model's grammar. <code>Null</code> 
	 *         is returned if a valid parse tree could not be generated.
	 */
	@Override
	public NonTerminalSymbol map(EndMapping event, GEIndividual individual) {
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
		
		event.setNoActiveCodons(codonsUsed);

		return root;
	}

	/*
	 * Recursive helper method to resolve mapping from codons to parse tree
	 */
	private int map(GrammarRule grammarRule, NonTerminalSymbol parseNode, int depth,
			Chromosome chromosome, int currentCodon) {
		// depth+1 because the current depth does not include terminals
		if (depth + 1 > maxDepth) {
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
			
			int productionChoice = (int) Math.abs(codon.value() % noProductions);
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

	/**
	 * Returns the language grammar which defines the valid parse trees. 
	 * 
	 * @return the currently set random sequence
	 */
	public Grammar getGrammar() {
		return grammar;
	}

	/**
	 * Sets the language grammar which defines the valid parse trees. If automatic 
	 * configuration is enabled then any value set here will be overwritten by the
	 * {@link Grammar#GRAMMAR} configuration setting on the next config event.
	 * 
	 * @param grammar the language grammar to set
	 */
	public void setGrammar(Grammar grammar) {
		this.grammar = grammar;
	}

	/**
	 * Returns the maximum depth of the parse trees produced by this mapper
	 * 
	 * @return maximum depth of parse trees
	 */
	public int getMaximumDepth() {
		return maxDepth;
	}

	/**
	 * Sets the maximum depth of the parse trees this mapper produces. If automatic 
	 * configuration is enabled then any value set here will be overwritten by the
	 * {@link GEIndividual#MAXIMUM_DEPTH} configuration setting on the next config event.
	 * 
	 * @param maxDepth maximum depth to allow for parse trees
	 */
	public void setMaximumDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}
}
