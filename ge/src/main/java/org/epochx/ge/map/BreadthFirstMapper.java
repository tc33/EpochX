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

import static org.epochx.ge.GEIndividual.MAXIMUM_DEPTH;
import static org.epochx.grammar.Grammar.GRAMMAR;

import java.util.*;

import org.epochx.*;
import org.epochx.event.*;
import org.epochx.ge.*;
import org.epochx.grammar.*;

/**
 * 
 * 
 * @see BreadthFirstMapper
 */
public class BreadthFirstMapper implements Mapper, Listener<ConfigEvent> {

	private Grammar grammar;
	private int maxDepth;

	public BreadthFirstMapper() {
		this(true);
	}

	public BreadthFirstMapper(boolean autoConfig) {
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
	 * </ul>
	 */
	protected void setup() {
		grammar = Config.getInstance().get(GRAMMAR);
		maxDepth = Config.getInstance().get(MAXIMUM_DEPTH);
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
	 * Perform the mapping operation.
	 * 
	 * @param chromosome the GEIndividual to be mapped to its source.
	 * @return a source string equivalent to the specified GEIndividual's
	 *         chromosome after mapping using the model's grammar. Null is
	 *         returned if
	 *         a valid string could not be generated.
	 */
	public String mapToString(Chromosome chromosome) {
		List<GrammarNode> symbols = new ArrayList<GrammarNode>();

		symbols.add(grammar.getStartRule());

		int i = 0;
		int currentCodon = 0;
		while (containsNonTerminals(symbols)) {
			if (i > maxDepth) {
				return null;
			} else {
				currentCodon = map(chromosome, currentCodon, symbols);
				if (currentCodon == -1) {
					return null;
				}
				i++;
			}
		}

		StringBuilder buffer = new StringBuilder();
		for (GrammarNode s: symbols) {
			GrammarLiteral t = (GrammarLiteral) s;
			buffer.append(t.getValue());
		}

		return buffer.toString();
	}

	/*
	 * Helper method which checks whether a list of symbols contains any non
	 * terminal symbols.
	 */
	private boolean containsNonTerminals(List<GrammarNode> symbolList) {
		for (GrammarNode s: symbolList) {
			if (s instanceof GrammarRule) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Non-recursive helper method that performs one pass of mapping.
	 */
	private int map(Chromosome chromosome, int currentCodon, List<GrammarNode> toProcess) {
		List<GrammarNode> nextToProcess = new ArrayList<GrammarNode>();
		for (GrammarNode grammarNode: toProcess) {
			if (grammarNode instanceof GrammarRule) {
				GrammarRule grammarRule = (GrammarRule) grammarNode;
				
				// Pick a production to replace it with
				int noProductions = grammarRule.getNoProductions();
				GrammarProduction p;
				if (noProductions == 1) {
					p = grammarRule.getProduction(0);
				} else {
					Codon codon = chromosome.getCodon(currentCodon++);
					if (codon == null) {
						// Must have run out of codons, so mapping fails
						return -1;
					}
					
					int productionChoice = (int) (codon.value() % noProductions);
					p = grammarRule.getProduction(productionChoice);
				}
				for (GrammarNode nextS: p.getGrammarNodes()) {
					nextToProcess.add(nextS);
				}
			} else {
				nextToProcess.add(grammarNode);
			}
		}
		toProcess.clear();
		toProcess.addAll(nextToProcess);
		return currentCodon;
	}
}
