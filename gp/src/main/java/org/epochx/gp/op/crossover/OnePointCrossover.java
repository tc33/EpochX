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
package org.epochx.gp.op.crossover;

import static org.epochx.RandomSequence.RANDOM_SEQUENCE;

import java.util.*;

import org.epochx.*;
import org.epochx.epox.Node;
import org.epochx.event.*;
import org.epochx.gp.GPIndividual;

/**
 * This class implements one point crossover as described by Poli and Langdon
 * in their paper Genetic Programming with One-Point Crossover and Point
 * Mutation. It is recommended that a significant mutation rate (Poli & Langdon
 * use 30%) is used in combination with one-point crossover since it encourages
 * rapid convergence.
 * 
 * @see KozaCrossover
 * @see SubtreeCrossover
 */
public class OnePointCrossover implements Operator, Listener<ConfigEvent> {

	private RandomSequence random;

	// Whether to use the strict version of one-point crossover or not.
	private boolean strict;

	private double probability;

	/**
	 * Constructs a <code>OnePointCrossover</code> using a non-strict
	 * implementation of one-point crossover.
	 * 
	 * @param probability the probability with which this operator should be
	 *        selected over some alternative
	 * @param random a <code>RandomSequence</code> used to lead
	 *        non-deterministic behaviour
	 */
	public OnePointCrossover(double probability, RandomSequence random) {
		this(probability, random, false);
	}

	/**
	 * Constructs a <code>OnePointCrossover</code> with the necessary
	 * parameters provided.
	 * 
	 * @param probability the probability with which this operator should be
	 *        selected over some alternative
	 * @param random a <code>RandomSequence</code> used to lead
	 *        non-deterministic behaviour
	 * @param strict whether strict one-point crossover should be used where not
	 *        only must the arity of nodes match, but also the node types
	 */
	public OnePointCrossover(double probability, RandomSequence random, boolean strict) {
		this.probability = probability;
		this.random = random;
		this.strict = strict;
	}

	/**
	 * Constructs a <code>OnePointCrossover</code> using a non-strict
	 * implementation of one-point crossover. A RandomSequence is required from
	 * the Config.
	 * 
	 * @param probability the probability with which this operator should be
	 *        selected over some alternative
	 */
	public OnePointCrossover(double probability) {
		this(probability, false);
	}

	/**
	 * Constructs a <code>OnePointCrossover</code>.
	 * 
	 * @param probability the probability with which this operator should be
	 *        selected over some alternative
	 * @param strict whether strict one-point crossover should be used where not
	 *        only must the arity of nodes match, but also the node types
	 */
	public OnePointCrossover(double probability, boolean strict) {
		this.probability = probability;
		this.strict = strict;

		setup();
		EventManager.getInstance().add(ConfigEvent.class, this);
	}

	/**
	 * Crosses over two <code>Individuals</code> provided as arguments
	 * using uniform swap points. Random crossover points are chosen in both
	 * programs, the genetic material at the points are then exchanged.
	 * The resulting programs are returned as new GPIndividual objects.
	 * <p>
	 * An {@link OperatorEvent.StartOperator} event is fired before the
	 * operation is performed, and a {@link OnePointCrossoverEvent} event is
	 * fired after it is completed.
	 * 
	 * @param parents an array with two non-null elements which are the
	 *        GPIndividuals to undergo one point crossover.
	 */
	@Override
	public GPIndividual[] apply(Individual ... parents) {
		EventManager.getInstance().fire(OperatorEvent.StartOperator.class, new OperatorEvent.StartOperator(parents));

		GPIndividual program1 = (GPIndividual) parents[0];
		GPIndividual program2 = (GPIndividual) parents[1];

		List<Integer> points1 = new ArrayList<Integer>();
		List<Integer> points2 = new ArrayList<Integer>();

		traverse(program1.getRootNode(), program2.getRootNode(), points1, points2, 0, 0);

		// Select swap points.
		int randomIndex = random.nextInt(points1.size());
		int swapPoint1 = points1.get(randomIndex);
		int swapPoint2 = points2.get(randomIndex);

		// Get copies of subtrees to swap.
		Node subtree1 = program1.getNthNode(swapPoint1);
		Node subtree2 = program2.getNthNode(swapPoint2);

		// Swap on clones so parents and children remain distinct in event.
		GPIndividual child1 = program1.clone();
		GPIndividual child2 = program2.clone();

		// Perform swap.
		child1.setNthNode(swapPoint1, subtree2);
		child2.setNthNode(swapPoint2, subtree1);

		GPIndividual[] children = new GPIndividual[]{child1, child2};

		// Fire end event.
		OnePointCrossoverEvent event = new OnePointCrossoverEvent(parents, children, new int[]{swapPoint1, swapPoint2},
				new Node[]{subtree1, subtree2});
		EventManager.getInstance().fire(OnePointCrossoverEvent.class, event);

		return children;
	}

	/*
	 * Private helper method for crossover method. Traverses the two program
	 * trees and identifies the swap points that can be swapped because they're
	 * connected to a part of the tree that aligns. Supports strict or
	 * non-strict.
	 */
	private void traverse(Node root1, Node root2, List<Integer> points1, List<Integer> points2, int current1,
			int current2) {
		points1.add(current1);
		points2.add(current2);

		boolean valid = false;
		if (!strict) {
			valid = (root1.getArity() == root2.getArity()) && (root1.getReturnType() == root2.getReturnType());
		} else {
			valid = root1.getClass().equals(root2.getClass());
		}

		if (valid) {
			for (int i = 0; i < root1.getArity(); i++) {
				Node child1 = root1.getChild(i);
				Node child2 = root2.getChild(i);
				traverse(child1, child2, points1, points2, current1 + 1, current2 + 1);

				current1 += child1.getLength();
				current2 += child2.getLength();
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
	 * Returns whether strict one-point crossover is being used, or not. If set
	 * to <code>true</code> then alignment of the parent programs takes into
	 * account not only the arity of the nodes, but also the node type.
	 * 
	 * @return true if strict one-point crossover is in use, and false
	 *         otherwise
	 */
	public boolean isStrict() {
		return strict;
	}

	/**
	 * Sets whether strict one-point crossover should be used or not. If set to
	 * <code>true</code> then alignment of the parent programs takes into
	 * account not only the arity of the nodes, but also the node type.
	 * 
	 * @param strict true if strict one-point crossover should be used, and
	 *        false otherwise
	 */
	public void setStrict(final boolean strict) {
		this.strict = strict;
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
