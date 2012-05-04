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
package org.epochx.gp.operator;

import static org.epochx.RandomSequence.RANDOM_SEQUENCE;

import java.util.*;

import org.epochx.*;
import org.epochx.epox.Node;
import org.epochx.event.*;
import org.epochx.gp.STGPIndividual;

/**
 * This class implements standard crossover with uniform swap points. Subtree
 * crossover works by randomly selecting a crossover point in both parent
 * programs and then swapping the subtrees at those points.
 * 
 * @see KozaCrossover
 * @see OnePointCrossover
 */
public class SubtreeCrossover implements Operator, Listener<ConfigEvent> {

	// The random number generator for controlling random behaviour.
	private RandomSequence random;

	// The probability of choosing a function node as the swap point.
	private final double functionSwapProbability;

	private double probability;

	/**
	 * Constructs a <code>SubtreeCrossover</code> using a function swap 
	 * probability of -1.0 which results in all nodes being selected from with
	 * equal probability.
	 * 
	 * @param probability the probability with which this operator should be
	 *        selected over some alternative
	 * @param random a <code>RandomSequence</code> used to lead
	 *        non-deterministic behaviour
	 */
	public SubtreeCrossover(double probability, RandomSequence random) {
		this(probability, random, -1.0);
	}

	/**
	 * Constructs a <code>SubtreeCrossover</code>.
	 * 
	 * @param probability the probability with which this operator should be
	 *        selected over some alternative
	 * @param random a <code>RandomSequence</code> used to lead
	 *        non-deterministic behaviour
	 * @param functionSwapProbability The probability of crossover operations
	 *        choosing a function node as the swap point.
	 */
	public SubtreeCrossover(double probability, RandomSequence random, double functionSwapProbability) {
		this.probability = probability;
		this.random = random;
		this.functionSwapProbability = functionSwapProbability;
	}

	/**
	 * Constructs a <code>SubtreeCrossover</code> using a function swap 
	 * probability of -1.0 which results in all nodes being selected from with
	 * equal probability. A RandomSequence is required from the Config.
	 * 
	 * @param probability the probability with which this operator should be
	 *        selected over some alternative
	 */
	public SubtreeCrossover(double probability) {
		this(probability, -1.0);
	}

	/**
	 * Constructs a <code>SubtreeCrossover</code>. A RandomSequence is required
	 * from the Config.
	 * 
	 * @param probability the probability with which this operator should be
	 *        selected over some alternative
	 * @param functionSwapProbability The probability of crossover operations
	 *        choosing a function node as the swap point.
	 */
	public SubtreeCrossover(double probability, double functionSwapProbability) {
		this.probability = probability;
		this.functionSwapProbability = functionSwapProbability;

		setup();
		EventManager.getInstance().add(ConfigEvent.class, this);
	}

	/**
	 * Crosses over the two <code>Individuals</code> provided as arguments
	 * using uniform swap points. Random crossover points are chosen at random
	 * in both programs, the genetic material at the points are then exchanged.
	 * The resulting programs are returned as new STGPIndividual objects.
	 * 
	 * @param parents the first Individual selected to undergo subtree
	 *        crossover.
	 */
	@Override
	public STGPIndividual[] apply(Individual ... parents) {
		EventManager.getInstance().fire(new OperatorEvent.StartOperator(this, parents));

		final STGPIndividual program1 = (STGPIndividual) parents[0];
		final STGPIndividual program2 = (STGPIndividual) parents[1];

		// Select first swap point.
		final int swapPoint1 = getCrossoverPoint(program1);
		final Node subtree1 = program1.getNode(swapPoint1);// .clone();

		// Find which nodes in program2 have a matching return type to subtree1.
		final Class<?> subtree1Type = subtree1.dataType();
		final List<Node> matchingNodes = new ArrayList<Node>();
		final List<Integer> matchingIndexes = new ArrayList<Integer>();
		getMatchingNodes(program2.getRoot(), subtree1Type, 0, matchingNodes, matchingIndexes);

		STGPIndividual[] children = new STGPIndividual[0];
		int[] swapPoints = new int[0];
		Node[] subtrees = new Node[0];

		if (matchingNodes.size() > 0) {
			// Select second swap point with the same data-type.
			int index = getSelectedMatch(matchingNodes);
			Node subtree2 = matchingNodes.get(index);
			int swapPoint2 = matchingIndexes.get(index);

			// Perform swap.
			program1.setNode(swapPoint1, subtree2);
			program2.setNode(swapPoint2, subtree1);

			children = new STGPIndividual[]{program1, program2};
			swapPoints = new int[]{swapPoint1, swapPoint2};
			subtrees = new Node[]{subtree1, subtree2};
		}

		// Fire end event.
		Event event = new SubtreeCrossoverEvent(this, parents, children, swapPoints, subtrees);
		EventManager.getInstance().fire(event);

		return children;
	}

	private int getMatchingNodes(final Node root, final Class<?> type, int current, final List<Node> matching,
			final List<Integer> indexes) {
		if (root.dataType() == type) {
			matching.add(root);
			indexes.add(current);
		}

		for (int i = 0; i < root.getArity(); i++) {
			current = getMatchingNodes(root.getChild(i), type, current + 1, matching, indexes);
		}

		return current;
	}

	/*
	 * Choose the crossover point for the given STGPIndividual with respect
	 * to the probabilities assigned for function and terminal node points.
	 */
	private int getCrossoverPoint(final STGPIndividual program) {
		// Calculate numbers of terminal and function nodes.
		final int length = program.length();
		final int noTerminals = program.getRoot().getNoTerminals();
		final int noFunctions = length - noTerminals;

		// Randomly decide whether to use a function or terminal node point.
		if (functionSwapProbability == -1) {
			// Randomly select a node from the program.
			return random.nextInt(length);
		} else if ((noFunctions > 0) && (random.nextDouble() < functionSwapProbability)) {
			// Randomly select a function node from the function set.
			final int f = random.nextInt(noFunctions);

			return program.getRoot().getNthFunctionNodeIndex(f);
		} else {
			// Randomly select a terminal node from the terminal set.
			final int t = random.nextInt(noTerminals);

			return program.getRoot().getNthTerminalNodeIndex(t);
		}
	}

	/*
	 * Choose the crossover point for the given STGPIndividual with respect
	 * to the probabilities assigned for function and terminal node points.
	 */
	private int getSelectedMatch(final List<Node> nodes) {
		// Randomly decide whether to use a function or terminal node point.
		if (functionSwapProbability == -1) {
			// Randomly select a node from the program.
			return random.nextInt(nodes.size());
		} else {
			final List<Integer> terminalIndexes = new ArrayList<Integer>();
			final List<Integer> functionIndexes = new ArrayList<Integer>();

			for (int i = 0; i < nodes.size(); i++) {
				if (nodes.get(i).getArity() == 0) {
					terminalIndexes.add(i);
				} else {
					functionIndexes.add(i);
				}
			}

			if ((functionIndexes.size() > 0) && (random.nextDouble() < functionSwapProbability)) {
				// Randomly select a function node from the function set.
				return functionIndexes.get(random.nextInt(functionIndexes.size()));
			} else {
				// Randomly select a terminal node from the terminal set.
				return terminalIndexes.get(random.nextInt(terminalIndexes.size()));
			}
		}
	}

	/**
	 * Sets up this operator with the appropriate configuration settings.
	 * This method is called whenever a <code>ConfigEvent</code> occurs for a
	 * change in any of the following configuration parameters:
	 * <ul>
	 * <li><code>RandomSequence.RANDOM_SEQUENCE</code>
	 * </ul>
	 */
	protected void setup() {
		random = Config.getInstance().get(RANDOM_SEQUENCE);
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
		if (event.isKindOf(RANDOM_SEQUENCE)) {
			setup();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This operator requires an input size of 2.
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
	 * Overwrites the probability of this operator being selected.
	 * 
	 * @param probability the new probability to set
	 */
	public void setProbability(double probability) {
		this.probability = probability;
	}

	/**
	 * Returns the random number generator that this crossover is using or
	 * <code>null</code> if none has been set.
	 * 
	 * @return the currently set random sequence
	 */
	public RandomSequence getRandomSequence() {
		return random;
	}

	/**
	 * Sets the random sequence to use. If this object was initially constructed
	 * using one of the constructors that does not require a RandomSequence then
	 * the value set here will be overwritten with the random sequence from
	 * the config the next time it is updated.
	 * 
	 * @param random the random number generator to set
	 */
	public void setRandomSequence(final RandomSequence random) {
		this.random = random;
	}
}
