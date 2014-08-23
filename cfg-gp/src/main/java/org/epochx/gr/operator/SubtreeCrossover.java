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
package org.epochx.gr.operator;

import static org.epochx.Config.Template.TEMPLATE;
import static org.epochx.RandomSequence.RANDOM_SEQUENCE;

import java.util.*;

import org.epochx.AbstractOperator;
import org.epochx.Config;
import org.epochx.Individual;
import org.epochx.RandomSequence;
import org.epochx.Config.ConfigKey;
import org.epochx.event.ConfigEvent;
import org.epochx.event.EventManager;
import org.epochx.event.Listener;
import org.epochx.event.OperatorEvent.EndOperator;
import org.epochx.gr.GRIndividual;
import org.epochx.grammar.*;

/**
 * This class performs a subtree crossover on a <code>GRIndividual</code>, as described
 * by Whigham in his paper "Grammatically-based genetic programming".
 * 
 * <p>
 * A <code>NonTerminalSymbol</code> is randomly selected as the crossover point
 * in each individual's parse tree and the two subtrees rooted at those nodes are
 * exchanged.
 * 
 * @since 2.0
 */
public class SubtreeCrossover extends AbstractOperator implements Listener<ConfigEvent> {

	/**
	 * The key for setting and retrieving the probability of this operator being applied
	 */
	public static final ConfigKey<Double> PROBABILITY = new ConfigKey<Double>();

	// Configuration settings
	private RandomSequence random;
	private Double probability;

	/**
	 * Constructs a <code>SubtreeCrossover</code> with control parameters
	 * automatically loaded from the config
	 */
	public SubtreeCrossover() {
		this(true);
	}

	/**
	 * Constructs a <code>SubtreeCrossover</code> with control parameters initially
	 * loaded from the config. If the <code>autoConfig</code> argument is set to
	 * <code>true</code> then the configuration will be automatically updated when
	 * the config is modified.
	 * 
	 * @param autoConfig whether this operator should automatically update its
	 *        configuration settings from the config
	 */
	public SubtreeCrossover(boolean autoConfig) {
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
	 * <li>{@link RandomSequence#RANDOM_SEQUENCE}
	 * <li>{@link #PROBABILITY}
	 * </ul>
	 */
	protected void setup() {
		random = Config.getInstance().get(RANDOM_SEQUENCE);
		probability = Config.getInstance().get(PROBABILITY);
	}

	/**
	 * Receives configuration events and triggers this operator to configure its
	 * parameters if the <code>ConfigEvent</code> is for one of its required
	 * parameters.
	 * 
	 * @param event {@inheritDoc}
	 */
	@Override
	public void onEvent(ConfigEvent event) {
		if (event.isKindOf(TEMPLATE, RANDOM_SEQUENCE, PROBABILITY)) {
			setup();
		}
	}

	/**
	 * Performs a subtree crossover operation on the specified parent individuals.
	 * 
	 * <p>
	 * A <code>NonTerminalSymbol</code> is randomly selected as the crossover point
	 * in each individual's parse tree and the two subtrees rooted at those nodes are
	 * exchanged.
	 * 
	 * @param event the <code>EndOperator</code> event to be filled with information
	 *        about this operation
	 * @param parents an array of two individuals to undergo subtree crossover. Both 
	 * 		  individuals must be instances of <code>GRIndividual</code>.
	 * @return an array containing two <code>GRIndividual</code>s that are the
	 *        result of the crossover
	 */
	@Override
	public GRIndividual[] perform(EndOperator event, Individual ... parents) {
		GRIndividual child1 = (GRIndividual) parents[0];
		GRIndividual child2 = (GRIndividual) parents[1];

		NonTerminalSymbol parseTree1 = child1.getParseTree();
		NonTerminalSymbol parseTree2 = child2.getParseTree();

		List<NonTerminalSymbol> nonTerminals1 = parseTree1.getNonTerminalSymbols();
		List<NonTerminalSymbol> nonTerminals2 = parseTree2.getNonTerminalSymbols();

		// Choose a non-terminal at random
		int point1 = random.nextInt(nonTerminals1.size());
		NonTerminalSymbol subtree1 = nonTerminals1.get(point1);

		// Generate a list of matching non-terminals from the second program.
		List<NonTerminalSymbol> matchingNonTerminals = new ArrayList<NonTerminalSymbol>();
		for (NonTerminalSymbol nt: nonTerminals2) {
			if (nt.getGrammarRule().equals(subtree1.getGrammarRule())) {
				matchingNonTerminals.add(nt);
			}
		}

		if (matchingNonTerminals.isEmpty()) {
			// No valid points in second program, cancel crossover.
			return null;
		} else {
			// Randomly choose a second point out of the matching non-terminals.
			int point2 = random.nextInt(matchingNonTerminals.size());
			NonTerminalSymbol subtree2 = matchingNonTerminals.get(point2);

			// Add crossover points to the end event
			((SubtreeCrossoverEndEvent) event).setCrossoverPoint1(point1);
			((SubtreeCrossoverEndEvent) event).setCrossoverPoint1(point2);

			// Swap the non-terminals' children.
			List<Symbol> temp = subtree1.getChildren();
			subtree1.setChildren(subtree2.getChildren());
			subtree2.setChildren(temp);

			// Add subtrees into the end event
			((SubtreeCrossoverEndEvent) event).setSubtree1(subtree1);
			((SubtreeCrossoverEndEvent) event).setSubtree2(subtree2);
		}

		return new GRIndividual[]{child1, child2};
	}

	/**
	 * Returns a <code>SubtreeCrossoverEndEvent</code> with the operator and parents set
	 * 
	 * @param parents the parents that were operated on
	 * @return operator end event
	 */
	@Override
	protected SubtreeCrossoverEndEvent getEndEvent(Individual ... parents) {
		return new SubtreeCrossoverEndEvent(this, parents);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Subtree crossover operates on 2 individuals.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public int inputSize() {
		return 2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double probability() {
		return probability;
	}

	/**
	 * Sets the probability of this operator being selected. If automatic configuration is
	 * enabled then any value set here will be overwritten by the {@link #PROBABILITY} 
	 * configuration setting on the next config event.
	 * 
	 * @param probability the new probability to set
	 */
	public void setProbability(double probability) {
		this.probability = probability;
	}
	
	/**
	 * Returns the random number sequence in use
	 * 
	 * @return the currently set random sequence
	 */
	public RandomSequence getRandomSequence() {
		return random;
	}

	/**
	 * Sets the random number sequence to use. If automatic configuration is
	 * enabled then any value set here will be overwritten by the
	 * {@link RandomSequence#RANDOM_SEQUENCE} configuration setting on the next
	 * config event.
	 * 
	 * @param random the random number generator to set
	 */
	public void setRandomSequence(RandomSequence random) {
		this.random = random;
	}
}
