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
package org.epochx.stgp.operator;

import static org.epochx.RandomSequence.RANDOM_SEQUENCE;
import static org.epochx.stgp.STGPIndividual.*;

import org.epochx.*;
import org.epochx.epox.Node;
import org.epochx.event.*;
import org.epochx.stgp.STGPIndividual;
import org.epochx.stgp.init.GrowInitialisation;

/**
 * A mutation operator for <tt>STGPIndividual</tt>s that replaces a subtree with
 * a new randomly generated subtree.
 * 
 * <p>
 * See the {@link #setup()} method documentation for a list of configuration
 * parameters used to control this operator.
 * 
 * @see PointMutation
 */
public class SubtreeMutation implements Operator, Listener<ConfigEvent> {

	private final GrowInitialisation grower;

	// Configuration settings
	private RandomSequence random;
	private int maxDepth;

	private double probability;

	/**
	 * Constructs a <tt>SubtreeMutation</tt> with control parameters
	 * automatically loaded from the config
	 */
	public SubtreeMutation() {
		this(true);
	}

	/**
	 * Constructs a <tt>SubtreeMutation</tt> with control parameters initially
	 * loaded from the config. If the <tt>autoConfig</tt> argument is set to
	 * <tt>true</tt> then the configuration will be automatically updated when
	 * the config is modified.
	 * 
	 * @param autoConfig whether this operator should automatically update its
	 *        configuration settings from the config
	 */
	public SubtreeMutation(boolean autoConfig) {
		grower = new GrowInitialisation(false);

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
	 * <li>{@link STGPIndividual#SYNTAX}
	 * <li>{@link STGPIndividual#MAXIMUM_DEPTH}
	 * </ul>
	 */
	protected void setup() {
		random = Config.getInstance().get(RANDOM_SEQUENCE);
		maxDepth = Config.getInstance().get(MAXIMUM_DEPTH);

		grower.setRandomSequence(random);
		grower.setSyntax(Config.getInstance().get(SYNTAX));
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
		if (event.isKindOf(RANDOM_SEQUENCE, SYNTAX, MAXIMUM_DEPTH)) {
			setup();
		}
	}

	/**
	 * Performs a subtree mutation on the given individual. A mutation
	 * point is randomly selected in the program tree. Then the subtree rooted
	 * at that point is replaced with a randomly generated subtree. The
	 * replacement subtree is generated using a grow initialisation method.
	 * 
	 * @param parents an array of just one individual to undergo subtree
	 *        mutation. It must be an instance of <tt>STGPIndividual</tt>.
	 * @return an array containing one <tt>STGPIndividual</tt> that was the
	 *         result of mutating the parent individual
	 */
	@Override
	public STGPIndividual[] apply(Individual ... parents) {
		EventManager.getInstance().fire(new OperatorEvent.StartOperator(this, parents));

		STGPIndividual program = (STGPIndividual) parents[0];
		STGPIndividual child = program.clone();

		// Randomly choose a mutation point
		int length = program.length();
		int mutationPoint = random.nextInt(length);

		// Calculate available depth
		int mutationPointDepth = nodeDepth(program.getRoot(), 0, mutationPoint, 0);
		int maxSubtreeDepth = maxDepth - mutationPointDepth;

		// Grow a new subtree using the GrowInitialisation
		Node originalSubtree = program.getNode(mutationPoint);
		// TODO This should be using the parent's required type not the subtree's type
		grower.setReturnType(originalSubtree.dataType());
		grower.setMaximumDepth(maxSubtreeDepth);
		Node subtree = grower.createTree();

		child.setNode(mutationPoint, subtree);

		EventManager.getInstance().fire(new SubtreeMutationEndEvent(this, program, child, mutationPoint, subtree));

		return new STGPIndividual[]{child};
	}

	/*
	 * Finds what depth a node with a given index is at. Returns -1 if the index
	 * is not found.
	 */
	private int nodeDepth(Node root, int currentIndex, int targetIndex, int currentDepth) {
		// TODO This should be in a utilities class
		if (currentIndex == targetIndex) {
			return currentDepth;
		}

		for (int i = 0; i < root.getArity(); i++) {
			Node subtree = root.getChild(i);
			int subtreeLength = subtree.length();
			if (targetIndex < subtreeLength) {
				// Target is in this subtree
				return nodeDepth(subtree, currentIndex + 1, targetIndex, currentDepth + 1);
			}
			currentIndex += subtreeLength;
		}
		return -1;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Subtree mutation operates on 1 individual.
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
		
		grower.setRandomSequence(random);
	}
	
	/**
	 * Returns the array of nodes in the available syntax. Replacement subtrees 
	 * are generated using the nodes in this array.
	 * 
	 * @return an array of the nodes in the syntax
	 */
	public Node[] getSyntax() {
		return grower.getSyntax();
	}

	/**
	 * Sets the array of nodes to generate replacement subtrees from. If 
	 * automatic configuration is enabled then any value set here will be 
	 * overwritten by the {@link STGPIndividual#SYNTAX} configuration setting on
	 * the next config event.
	 * 
	 * @param syntax an array of nodes to generate new program trees from
	 */
	public void setSyntax(Node[] syntax) {
		grower.setSyntax(syntax);
	}
}
