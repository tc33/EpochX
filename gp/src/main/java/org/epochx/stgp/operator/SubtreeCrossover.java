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
package org.epochx.stgp.operator;

import static org.epochx.RandomSequence.RANDOM_SEQUENCE;
import static org.epochx.stgp.STGPIndividual.MAXIMUM_DEPTH;

import java.util.*;

import org.epochx.*;
import org.epochx.Config.ConfigKey;
import org.epochx.Config.Template;
import org.epochx.epox.Node;
import org.epochx.event.*;
import org.epochx.event.OperatorEvent.EndOperator;
import org.epochx.stgp.STGPIndividual;

/**
 * A crossover operator for <tt>STGPIndividual</tt>s that exchanges subtrees 
 * in two individuals. A bias can optionally be set to influence the probability
 * that a terminal or a non-terminal is selected as the crossover points.
 * 
 * <p>
 * See the {@link #setup()} method documentation for a list of configuration
 * parameters used to control this operator.
 * 
 * @see KozaCrossover
 * @see OnePointCrossover
 * 
 * @since 2.0
 */
public class SubtreeCrossover extends AbstractOperator implements Listener<ConfigEvent> {

	/**
	 * The key for setting and retrieving the probability with which a terminal
	 * will be selected as the crossover point
	 */
	public static final ConfigKey<Double> TERMINAL_PROBABILITY = new ConfigKey<Double>();

	/**
	 * The key for setting and retrieving the probability of this operator being applied
	 */
	public static final ConfigKey<Double> PROBABILITY = new ConfigKey<Double>();
	
	// Configuration settings
	private RandomSequence random;
	private Double terminalProbability;
	private Double probability;
	private Integer maxDepth;

	/**
	 * Constructs a <tt>SubtreeCrossover</tt> with control parameters
	 * automatically loaded from the config
	 */
	public SubtreeCrossover() {
		this(true);
	}

	/**
	 * Constructs a <tt>SubtreeCrossover</tt> with control parameters initially
	 * loaded from the config. If the <tt>autoConfig</tt> argument is set to
	 * <tt>true</tt> then the configuration will be automatically updated when
	 * the config is modified.
	 * 
	 * @param autoConfig whether this operator should automatically update its
	 *        configuration settings from the config
	 */
	public SubtreeCrossover(boolean autoConfig) {
		// Default config values
		terminalProbability = -1.0;
		
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
	 * <li>{@link RandomSequence#RANDOM_SEQUENCE}
	 * <li>{@link #TERMINAL_PROBABILITY} (default: <tt>-1.0</tt>)
	 * <li>{@link #PROBABILITY}
	 * <li>{@link STGPIndividual#MAXIMUM_DEPTH}
	 * </ul>
	 */
	protected void setup() {
		random = Config.getInstance().get(RANDOM_SEQUENCE);
		terminalProbability = Config.getInstance().get(TERMINAL_PROBABILITY, terminalProbability);
		probability = Config.getInstance().get(PROBABILITY);
		maxDepth = Config.getInstance().get(MAXIMUM_DEPTH);
	}

	/**
	 * Receives configuration events and triggers this operator to configure its
	 * parameters if the <tt>ConfigEvent</tt> is for one of its required
	 * parameters.
	 * 
	 * @param event {@inheritDoc}
	 */
	@Override
	public void onEvent(ConfigEvent event) {
		if (event.isKindOf(Template.TEMPLATE, RANDOM_SEQUENCE, TERMINAL_PROBABILITY, PROBABILITY, MAXIMUM_DEPTH)) {
			setup();
		}
	}

	/**
	 * Performs a subtree crossover on the given individuals. A crossover point
	 * is randomly chosen in both programs and the subtrees at these points are
	 * exchanged.
	 * 
	 * @param parents an array of two individuals to undergo subtree crossover.
	 *        Both individuals must be instances of <tt>STGPIndividual</tt>.
	 * @return an array containing two <tt>STGPIndividual</tt>s that are the
	 *         result of the crossover
	 */
	@Override
	public STGPIndividual[] perform(EndOperator event, Individual ... parents) {
		STGPIndividual program1 = (STGPIndividual) parents[0];
		STGPIndividual program2 = (STGPIndividual) parents[1];

		// Select first swap point
		int swapPoint1 = crossoverPoint(program1);
		Node subtree1 = program1.getNode(swapPoint1);// .clone();

		// Find which nodes in program2 have a matching return type to subtree1
		Class<?> subtree1Type = subtree1.dataType();
		List<Node> matchingNodes = new ArrayList<Node>();
		List<Integer> matchingIndexes = new ArrayList<Integer>();
		nodesOfType(program2.getRoot(), subtree1Type, 0, matchingNodes, matchingIndexes);

		STGPIndividual[] children = new STGPIndividual[0];
		int[] swapPoints = new int[0];
		Node[] subtrees = new Node[0];

		if (matchingNodes.size() > 0) {
			// Select second swap point with the same data-type
			int index = selectNodeIndex(matchingNodes);
			Node subtree2 = matchingNodes.get(index);
			int swapPoint2 = matchingIndexes.get(index);

			program1.setNode(swapPoint1, subtree2);
			program2.setNode(swapPoint2, subtree1);
			
			// Check the depths are valid
			int depth1 = program1.depth();
			int depth2 = program2.depth();

			children = new STGPIndividual[2];
			
			if (depth1 <= maxDepth && depth2 <= maxDepth) {
				children = new STGPIndividual[]{program1, program2};
			} else if (depth1 <= maxDepth) {
				children = new STGPIndividual[]{program1};
			} else if (depth2 <= maxDepth) {
				children = new STGPIndividual[]{program2};
			} else {
				children = new STGPIndividual[0];
			}
			
			swapPoints = new int[]{swapPoint1, swapPoint2};
			subtrees = new Node[]{subtree1, subtree2};
		}

		((SubtreeCrossoverEndEvent) event).setCrossoverPoints(swapPoints);
		((SubtreeCrossoverEndEvent) event).setSubtrees(subtrees);

		return children;
	}

	/**
	 * Returns a <tt>SubtreeCrossoverEndEvent</tt> with the operator and 
	 * parents set
	 */
	@Override
	protected SubtreeCrossoverEndEvent getEndEvent(Individual ... parents) {
		return new SubtreeCrossoverEndEvent(this, parents);
	}
	
	/*
	 * Fills the 'matching' list argument with all the nodes in the tree rooted
	 * at 'root' that have a data-type that equals the 'type' argument. The
	 * 'indexes' list is filled with the index of each of those nodes.
	 */
	private int nodesOfType(Node root, Class<?> type, int current, List<Node> matching, List<Integer> indexes) {
		if (root.dataType() == type) {
			matching.add(root);
			indexes.add(current);
		}

		for (int i = 0; i < root.getArity(); i++) {
			current = nodesOfType(root.getChild(i), type, current + 1, matching, indexes);
		}

		return current;
	}

	/**
	 * Chooses a crossover point at random in the program tree of the given
	 * individual. The probability that a terminal is selected is equal to the
	 * result of the <tt>getTerminalProbability()</tt> method. If the terminal
	 * probability is -1.0 then all nodes will be selected from at random.
	 * 
	 * @param individual the individual to choose a crossover point in
	 * @return the index of the crossover point selected in the given
	 *         individual's program tree.
	 */
	protected int crossoverPoint(STGPIndividual individual) {
		double terminalProbability = getTerminalProbability();

		int length = individual.length();
		if (terminalProbability == -1.0) {
			return random.nextInt(length);
		}

		int noTerminals = individual.getRoot().countTerminals();
		int noNonTerminals = length - noTerminals;

		if ((noNonTerminals > 0) && (random.nextDouble() >= terminalProbability)) {
			int f = random.nextInt(noNonTerminals);
			return individual.getRoot().nthNonTerminalIndex(f);
		} else {
			int t = random.nextInt(noTerminals);
			return individual.getRoot().nthTerminalIndex(t);
		}
	}

	/**
	 * Selects a node in the list of nodes given and returns the index. The node
	 * is selected at random from the list, but the probability that a terminal
	 * will be selected is equal to the result of the
	 * <tt>getTerminalProbability()</tt> method. If the terminal probability is
	 * set to -1.0 then all nodes are selected from with equal probability.
	 * 
	 * @param nodes the list of nodes to select from
	 * @return the index of the node that was selected
	 */
	protected int selectNodeIndex(List<Node> nodes) {
		double terminalProbability = getTerminalProbability();

		if (terminalProbability == -1.0) {
			return random.nextInt(nodes.size());
		} else {
			List<Integer> terminalIndexes = new ArrayList<Integer>();
			List<Integer> nonTerminalIndexes = new ArrayList<Integer>();

			for (int i = 0; i < nodes.size(); i++) {
				if (nodes.get(i).getArity() == 0) {
					terminalIndexes.add(i);
				} else {
					nonTerminalIndexes.add(i);
				}
			}

			if ((nonTerminalIndexes.size() > 0) && (random.nextDouble() >= terminalProbability)) {
				return nonTerminalIndexes.get(random.nextInt(nonTerminalIndexes.size()));
			} else {
				return terminalIndexes.get(random.nextInt(terminalIndexes.size()));
			}
		}
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
	 * Sets the probability of this operator being selected
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
	 * Returns the probability that a terminal will be selected as the crossover
	 * point
	 * 
	 * @return the probability of a terminal being selected or -1.0 to indicate
	 *         equal probability for all nodes
	 */
	public double getTerminalProbability() {
		return terminalProbability;
	}

	/**
	 * Sets the probability that a terminal should be selected as the crossover
	 * point. The value should be between 0.0 and 1.0, or 1.0 to indicate that
	 * an equal probability should be assigned to all nodes. If automatic
	 * configuration is enabled then any value set here will be overwritten by
	 * the {@link SubtreeCrossover#TERMINAL_PROBABILITY} configuration setting
	 * on the next config event.
	 * 
	 * @param terminalProbability the probability that a terminal should be
	 *        selected
	 */
	public void setTerminalProbability(double terminalProbability) {
		this.terminalProbability = terminalProbability;
	}
	
	/**
	 * Returns the maximum depth for program trees that are returned from this
	 * operator
	 * 
	 * @return the maximum depth for program trees
	 */
	public int getMaximumDepth() {
		return maxDepth;
	}

	/**
	 * Sets the maximum depth for program trees returned from this operator. If 
	 * automatic configuration is enabled then any value set here will be overwritten 
	 * by the {@link STGPIndividual#MAXIMUM_DEPTH} configuration setting on the next 
	 * config event.
	 * 
	 * @param maxDepth the maximum depth for program trees
	 */
	public void setMaximumDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}
}
