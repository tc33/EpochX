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
import static org.epochx.gp.GPIndividual.SYNTAX;

import org.epochx.*;
import org.epochx.epox.Node;
import org.epochx.event.*;
import org.epochx.gp.GPIndividual;
import org.epochx.gp.init.GrowInitialiser;

/**
 * This class performs a subtree mutation on a <code>GPIndividual</code>.
 * 
 * <p>
 * A mutation point is randomly selected anywhere in the program tree. Then the
 * node at that point is replaced with a newly generated program tree, which is
 * created using a grow strategy.
 * 
 * <p>
 * If a model is provided then the following parameters are loaded upon every
 * configure event:
 * 
 * <ul>
 * <li>random number generator</li>
 * <li>syntax</li>
 * </ul>
 * 
 * <p>
 * If the <code>getModel</code> method returns <code>null</code> then no model
 * is set and whatever static parameters have been set as parameters to the
 * constructor or using the standard accessor methods will be used. If any
 * compulsory parameters remain unset when the mutation is performed then an
 * <code>IllegalStateException</code> will be thrown.
 */
public class SubtreeMutation implements Operator, Listener<ConfigEvent> {

	// Grow initialiser to build our replacement subtrees.
	private final GrowInitialiser grower;

	private RandomSequence random;

	// The maximum depth of the new subtree.
	private int maxSubtreeDepth;

	private double probability;

	public SubtreeMutation(RandomSequence random, Node[] syntax, int maxSubtreeDepth) {
		this.random = random;
		this.maxSubtreeDepth = maxSubtreeDepth;

		// Don't let this configure itself because it will use the wrong depth.
		grower = new GrowInitialiser(null, null, null, -1, maxSubtreeDepth, false);

		grower.setRandomSequence(random);
		grower.setSyntax(syntax);
	}

	/**
	 * Simple constructor for subtree mutation using a default maximum depth
	 * of 4 for new subtrees.
	 * 
	 * @param model The controlling model which provides any configuration
	 *        parameters for the run.
	 */
	public SubtreeMutation() {
		// 4 is a slightly arbitrary choice but we had to choose something.
		this(4);
	}

	/**
	 * Subtree mutation constructor with control for the maximum depth of new
	 * subtrees.
	 * 
	 * @param model The controlling model which provides any configuration
	 *        parameters for the run.
	 * @param maxSubtreeDepth The maximum depth of the inserted subtree.
	 */
	public SubtreeMutation(int maxSubtreeDepth) {
		this.maxSubtreeDepth = maxSubtreeDepth;

		// Don't let this configure itself because it will use the wrong depth.
		grower = new GrowInitialiser(null, null, null, -1, maxSubtreeDepth, false);

		setup();
		EventManager.getInstance().add(ConfigEvent.class, this);
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

		grower.setRandomSequence(random);
		grower.setSyntax(Config.getInstance().get(SYNTAX));
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
		if (event.isKindOf(RANDOM_SEQUENCE, SYNTAX)) {
			setup();
		}
	}

	/**
	 * Perform subtree mutation on the given GPIndividual. A mutation
	 * point
	 * is randomly selected anywhere in the program tree. Then the node at that
	 * point is replaced with a newly generated program tree, which is created
	 * using a grow strategy.
	 * 
	 * @param p The GPIndividual selected to undergo this mutation
	 *        operation.
	 * @return A GPIndividual that was the result of a point mutation on
	 *         the provided GPIndividual.
	 */
	@Override
	public GPIndividual[] apply(Individual ... parents) {
		EventManager.getInstance().fire(new OperatorEvent.StartOperator(this, parents));

		GPIndividual program = (GPIndividual) parents[0];
		GPIndividual child = program.clone();

		// Randomly choose a mutation point.
		int length = program.getProgramLength();
		int mutationPoint = random.nextInt(length);

		// Update grower to use the right data-type.
		final Node originalSubtree = program.getNthNode(mutationPoint);
		grower.setReturnType(originalSubtree.getReturnType());

		// Grow a new subtree using the GrowInitialiser.
		final Node subtree = grower.getGrownNodeTree(maxSubtreeDepth);

		// Set the new subtree.
		child.setNthNode(mutationPoint, subtree);

		EventManager.getInstance().fire(new SubtreeMutationEvent(this, program, child,
				mutationPoint, subtree));

		return new GPIndividual[]{child};
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
	 * Overwrites the probability of this operator being selected.
	 * 
	 * @param probability the new probability to set
	 */
	public void setProbability(double probability) {
		this.probability = probability;
	}

	/**
	 * Returns the random number generator that this mutation is using or
	 * <code>null</code> if none has been set.
	 * 
	 * @return the random the currently set random number generator.
	 */
	public RandomSequence getRandomSequence() {
		return random;
	}

	/**
	 * Sets the random number generator to use. If a model has been set then
	 * this parameter will be overwritten with the random number generator from
	 * that model on the next configure event.
	 * 
	 * @param random the random number generator to set.
	 */
	public void setRandomSequence(RandomSequence random) {
		this.random = random;

		grower.setRandomSequence(random);
	}

	/**
	 * Returns the maximum depth that the new subtrees being inserted into the
	 * program may be.
	 * 
	 * @return an int which is the maximum subtree depth that will be generated.
	 */
	public int getMaxSubtreeDepth() {
		return maxSubtreeDepth;
	}

	/**
	 * Sets the maximum depth to use for new subtrees being inserted into
	 * programs by this mutation.
	 * 
	 * @param maxSubtreeDepth the maximum subtree depth to use for new subtrees.
	 */
	public void setMaxSubtreeDepth(final int maxSubtreeDepth) {
		this.maxSubtreeDepth = maxSubtreeDepth;

		grower.setMaxDepth(maxSubtreeDepth);
	}
}
