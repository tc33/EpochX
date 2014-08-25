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

import static org.epochx.RandomSequence.RANDOM_SEQUENCE;
import static org.epochx.grammar.Grammar.GRAMMAR;

import java.util.List;

import org.epochx.AbstractOperator;
import org.epochx.Config;
import org.epochx.Individual;
import org.epochx.RandomSequence;
import org.epochx.Config.ConfigKey;
import org.epochx.Config.Template;
import org.epochx.cfg.CFGIndividual;
import org.epochx.cfg.init.Grow;
import org.epochx.event.ConfigEvent;
import org.epochx.event.EventManager;
import org.epochx.event.Listener;
import org.epochx.event.OperatorEvent;
import org.epochx.event.OperatorEvent.EndOperator;
import org.epochx.grammar.*;

/**
 * This class performs a subtree mutation on a <code>CFGIndividual</code>, as described
 * by Whigham in his paper "Grammatically-based genetic programming".
 * 
 * <p>
 * A <code>NonTerminalSymbol</code> is randomly selected as the crossover point
 * in each individual's parse tree and the two subtrees rooted at those nodes are
 * exchanged.
 * 
 * @since 2.0
 */
public class SubtreeMutation extends AbstractOperator implements Listener<ConfigEvent> {

	/**
	 * The key for setting and retrieving the probability of this operator being applied
	 */
	public static final ConfigKey<Double> PROBABILITY = new ConfigKey<Double>();
	
	private final Grow grower;
	
	// Configuration settings
	private RandomSequence random;
	private Double probability;
	private Grammar grammar;

	/**
	 * Constructs a <code>SubtreeMutation</code> with control parameters
	 * automatically loaded from the config
	 */
	public SubtreeMutation() {
		this(true);
	}

	/**
	 * Constructs a <code>SubtreeMutation</code> with control parameters initially
	 * loaded from the config. If the <code>autoConfig</code> argument is set to
	 * <code>true</code> then the configuration will be automatically updated when
	 * the config is modified.
	 * 
	 * @param autoConfig whether this operator should automatically update its
	 *        configuration settings from the config
	 */
	public SubtreeMutation(boolean autoConfig) {
		grower = new Grow(false);
		
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
	 * <li>{@link Grammar#GRAMMAR}
	 * </ul>
	 */
	protected void setup() {
		random = Config.getInstance().get(RANDOM_SEQUENCE);
		probability = Config.getInstance().get(PROBABILITY);

		grower.setRandomSequence(random);
		grower.setGrammar(grammar);
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
		if (event.isKindOf(Template.TEMPLATE, RANDOM_SEQUENCE, PROBABILITY, GRAMMAR)) {
			setup();
		}
	}

	/**
	 * Performs a subtree mutation operation on the specified parent individual.
	 * 
	 * <p>
	 * A <code>NonTerminalSymbol</code> is randomly selected as the mutation point
	 * in the individual's parse tree and the subtree rooted at that point is exchanged
	 * with a new randomly generated subtree.
	 * 
	 * @param event the <code>EndOperator</code> event to be filled with information
	 *        about this operation
	 * @param parents an array containing one individual to undergo mutation. The 
	 * 		  individual must be an instance of <code>CFGIndividual</code>.
	 * @return an array containing one <code>CFGIndividual</code>s that is the
	 *         result of the mutation
	 */
	@Override
	public CFGIndividual[] perform(EndOperator event, Individual ... parents) {
		CFGIndividual child = (CFGIndividual) parents[0];

		NonTerminalSymbol parseTree = child.getParseTree();

		// This is v.inefficient because we have to fly up and down the tree lots of times.
		List<Integer> nonTerminals = parseTree.getNonTerminalIndexes();

		// Choose a node to change.
		int point = nonTerminals.get(random.nextInt(nonTerminals.size()));
		NonTerminalSymbol original = (NonTerminalSymbol) parseTree.getNthSymbol(point);
		
		int originalDepth = original.getDepth();

		// Add mutation into the end event
		((EndEvent) event).setMutationPoint(point);

		// Construct a new subtree from that node's grammar rule
		//TODO Should allow any depth down to the maximum rather than the original subtrees depth
		GrammarRule rule = original.getGrammarRule();
		NonTerminalSymbol subtree = grower.growParseTree(originalDepth, rule);

		// Add subtree into the end event
		((EndEvent) event).setSubtree(subtree);

		// Replace subtree
		if (point == 0) {
			child.setParseTree(subtree);
		} else {
			parseTree.setNthSymbol(point, subtree);
		}

		return new CFGIndividual[]{child};
	}

	/**
	 * Returns a <code>SubtreeMutationEndEvent</code> with the operator and 
	 * parent set
	 * 
	 * @param parent the individual that was operated on
	 * @return operator end event
	 */
	@Override
	protected EndEvent getEndEvent(Individual ... parent) {
		return new EndEvent(this, parent);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Subtree mutation operates on one individual.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public int inputSize() {
		return 1;
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
		
		grower.setRandomSequence(random);
	}
	
	/**
	 * Returns the grammar that any mutated <code>CFGIndividual</code>s will satisfy with
	 * full parse trees
	 * 
	 * @return the currently set grammar
	 */
	public Grammar getGrammar() {
		return grammar;
	}

	/**
	 * Sets the grammar to be satisfied by the parse trees of the mutated <code>CFGIndividual</code>s. 
	 * If automatic configuration is enabled then any value set here will be overwritten by the 
	 * {@link Grammar#GRAMMAR} configuration setting on the next config event.
	 * 
	 * @param grammar the grammar to set
	 */
	public void setGrammar(Grammar grammar) {
		this.grammar = grammar;
		
		grower.setGrammar(grammar);
	}
	
	/**
	 * An event fired at the end of a subtree mutation
	 * 
	 * @see SubtreeMutation
	 * 
	 * @since 2.0
	 */
	public class EndEvent extends OperatorEvent.EndOperator {

		private NonTerminalSymbol subtree;
		private int mutationPoint;

		/**
		 * Constructs a <code>SubtreeMutationEndEvent</code> with the details of the event
		 * 
		 * @param operator the operator that performed the crossover
		 * @param parents an array containing one individual that the operator was performed on
		 */
		public EndEvent(SubtreeMutation operator, Individual[] parents) {
			super(operator, parents);
		}

		/**
		 * Returns an integer which is the index of the point chosen for the subtree mutation 
		 * operation. The index is the position in the whole parse tree as would be returned 
		 * by calling <code>getNthSymbol</code> on the root node.
		 * 
		 * @return an integer which is the index of the mutation point
		 */
		public int getMutationPoint() {
			return mutationPoint;
		}
		
		/**
		 * Sets the index of the symbol in the parse tree that was mutated
		 * 
		 * @param mutationPoint index used as the mutation point
		 */
		public void setMutationPoint(int mutationPoint) {
			this.mutationPoint = mutationPoint;
		}

		/**
		 * Returns the new subtree that was generated as a replacement for the subtree originally
		 * at the mutation point
		 * 
		 * @return the root node of the new subtree
		 */
		public NonTerminalSymbol getSubtree() {
			return subtree;
		}

		/**
		 * Sets the new subtree that was generated as a replacement for the subtree originally
		 * at the mutation point
		 * 
		 * @param subtree the root node of the first subtree
		 */
		public void setSubtree(NonTerminalSymbol subtree) {
			this.subtree = subtree;
		}
	}
}
