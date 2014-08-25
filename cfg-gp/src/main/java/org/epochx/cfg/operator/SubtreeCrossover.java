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
package org.epochx.cfg.operator;

import static org.epochx.Config.Template.TEMPLATE;
import static org.epochx.RandomSequence.RANDOM_SEQUENCE;

import java.util.*;

import org.epochx.AbstractOperator;
import org.epochx.Config;
import org.epochx.Individual;
import org.epochx.RandomSequence;
import org.epochx.Config.ConfigKey;
import org.epochx.cfg.CFGIndividual;
import org.epochx.event.ConfigEvent;
import org.epochx.event.EventManager;
import org.epochx.event.Listener;
import org.epochx.event.OperatorEvent;
import org.epochx.event.OperatorEvent.EndOperator;
import org.epochx.grammar.*;

/**
 * This class performs a subtree crossover on a <code>CFGIndividual</code>, as described
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
	 * 		  individuals must be instances of <code>CFGIndividual</code>.
	 * @return an array containing two <code>CFGIndividual</code>s that are the
	 *        result of the crossover
	 */
	@Override
	public CFGIndividual[] perform(EndOperator event, Individual ... parents) {
		CFGIndividual child1 = (CFGIndividual) parents[0];
		CFGIndividual child2 = (CFGIndividual) parents[1];

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
			((EndEvent) event).setCrossoverPoint1(point1);
			((EndEvent) event).setCrossoverPoint1(point2);

			// Swap the non-terminals' children.
			List<Symbol> temp = subtree1.getChildren();
			subtree1.setChildren(subtree2.getChildren());
			subtree2.setChildren(temp);

			// Add subtrees into the end event
			((EndEvent) event).setSubtree1(subtree1);
			((EndEvent) event).setSubtree2(subtree2);
		}

		return new CFGIndividual[]{child1, child2};
	}

	/**
	 * Returns a <code>SubtreeCrossoverEndEvent</code> with the operator and parents set
	 * 
	 * @param parents the parents that were operated on
	 * @return operator end event
	 */
	@Override
	protected EndEvent getEndEvent(Individual ... parents) {
		return new EndEvent(this, parents);
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
	
	/**
	 * An event fired at the end of a subtree crossover
	 * 
	 * @see SubtreeCrossover
	 * 
	 * @since 2.0
	 */
	public class EndEvent extends OperatorEvent.EndOperator {

		private NonTerminalSymbol subtree1;
		private NonTerminalSymbol subtree2;
		private int crossoverPoint1;
		private int crossoverPoint2;

		/**
		 * Constructs a <code>SubtreeCrossoverEndEvent</code> with the details of the
		 * event
		 * 
		 * @param operator the operator that performed the crossover
		 * @param parents an array of two individuals that the operator was
		 *        performed on
		 */
		public EndEvent(SubtreeCrossover operator, Individual[] parents) {
			super(operator, parents);
		}

		/**
		 * Returns an integer which is the index of the point chosen for the subtree crossover 
		 * operation. The index is from the list of all non-terminal symbols in the parse tree 
		 * of the second program, as would be returned by the <code>getNonTerminalSymbols</code> 
		 * method.
		 * 
		 * @return an integer which is the index of the crossover point
		 */
		public int getCrossoverPoint1() {
			return crossoverPoint1;
		}
		
		/**
		 * Sets the crossover position in the first parent's list of non-terminals
		 * 
		 * @param crossoverPoint1 index used as the crossover point in the first parent
		 */
		public void setCrossoverPoint1(int crossoverPoint1) {
			this.crossoverPoint1 = crossoverPoint1;
		}
		
		/**
		 * Returns an integer which is the index of the point chosen for the subtree crossover 
		 * operation. The index is from the list of all non-terminal symbols in the parse tree 
		 * of the second program, as would be returned by the <code>getNonTerminalSymbols</code> 
		 * method.
		 * 
		 * @return an integer which is the index of the crossover point
		 */
		public int getCrossoverPoint2() {
			return crossoverPoint2;
		}
		
		/**
		 * Sets the crossover position in the second parent's list of non-terminals
		 * 
		 * @param crossoverPoint2 index used as the crossover point in the second parent
		 */
		public void setCrossoverPoint2(int crossoverPoint2) {
			this.crossoverPoint2 = crossoverPoint2;
		}

		/**
		 * Returns the subtree taken from the first parent that was exchanged with the
		 * subtree from the second parent (as returned by <code>getSubtree2()</code>)
		 * 
		 * @return the root node of the first subtree
		 */
		public NonTerminalSymbol getSubtree1() {
			return subtree1;
		}

		/**
		 * Sets the subtree taken from the first parent that was exchanged with the subtree 
		 * from the second parent
		 * 
		 * @param subtree1 the root node of the first subtree
		 */
		public void setSubtree1(NonTerminalSymbol subtree1) {
			this.subtree1 = subtree1;
		}
		
		/**
		 * Returns the subtree taken from the second parent that was exchanged with the
		 * subtree from the first parent (as returned by <code>getSubtree1()</code>)
		 * 
		 * @return the root node of the second subtree
		 */
		public NonTerminalSymbol getSubtree2() {
			return subtree2;
		}

		/**
		 * Sets the subtree taken from the second parent that was exchanged with the subtree 
		 * from the first parent
		 * 
		 * @param subtree1 the root node of the second subtree
		 */
		public void setSubtree2(NonTerminalSymbol subtree2) {
			this.subtree2 = subtree2;
		}
	}
}
