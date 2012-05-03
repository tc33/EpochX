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
import static org.epochx.gp.GPIndividual.*;

import java.util.*;

import org.epochx.*;
import org.epochx.epox.Node;
import org.epochx.event.*;
import org.epochx.gp.GPIndividual;

/**
 * This class performs a simple point mutation on a
 * <code>GPIndividual</code>.
 * 
 * <p>
 * Each node in the program tree is considered for mutation, with the
 * probability of that node being mutated given as an argument to the
 * PointMutation constructor. If the node does undergo mutation then a
 * replacement node is selected from the full syntax (function and terminal
 * sets), at random.
 * 
 * @see SubtreeMutation
 */
public class PointMutation implements Operator, Listener<ConfigEvent> {

	private Node[] syntax;

	private RandomSequence random;

	// The probability that each node has of being mutated.
	private double pointProbability;
	
	private double probability;

	/**
	 * Constructs a <code>PointMutation</code>.
	 * 
	 * @param random
	 * @param syntax
	 */
	public PointMutation(double probability, RandomSequence random, Node[] syntax, double pointProbability) {
		this.probability = probability;
		this.random = random;
		this.syntax = syntax;
		this.pointProbability = pointProbability;
	}

	/**
	 * Construct a point mutation with a default point probability of 0.01. It
	 * is generally recommended that the PointMutation(GPModel, double)
	 * constructor is used instead.
	 * 
	 * @param model The current controlling model. Parameters such as full
	 *        syntax will be obtained from this.
	 */
	public PointMutation(double probability) {
		this(probability, 0.01);
	}

	/**
	 * Construct a point mutation with user specified point probability.
	 * 
	 * @param pointProbability The probability each node in a selected program
	 *        has of undergoing a mutation. 1.0 would result in all nodes being
	 *        changed, and 0.0 would mean no nodes were changed. A typical value
	 *        would be 0.01.
	 */
	public PointMutation(double probability, double pointProbability) {
		this.probability = probability;
		this.pointProbability = pointProbability;
		
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
		syntax = Config.getInstance().get(SYNTAX);
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
	 * Perform point mutation on the given GPIndividual. Each node in the
	 * program tree is considered in turn, with each having the given
	 * probability of actually being exchanged. Given that a node is chosen
	 * then a new function or terminal node of the same arity is used to
	 * replace it.
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

		List<Integer> points = new ArrayList<Integer>();

		// Iterate over each node in the program tree.
		int length = program.getProgramLength();
		for (int i = 0; i < length; i++) {
			// Only change pointProbability of the time.
			if (random.nextDouble() < pointProbability) {
				// Get the arity of the ith node of the program.
				Node node = program.getNthNode(i);
				int arity = node.getArity();

				// Find compatible replacements.
				List<Node> replacements = getReplacements(node);
				if (!replacements.isEmpty()) {
					// Randomly choose a replacement.
					Node replacement = replacements.get(random.nextInt(replacements.size()));
					replacement = replacement.newInstance();

					// Attach the old node's children.
					for (int k = 0; k < arity; k++) {
						replacement.setChild(k, node.getChild(k));
					}
					// Then set the new node back into the program.
					child.setNthNode(i, replacement);

					// Record the index of the node that we changed.
					points.add(i);
				}
				// If no replacements then we fall out the bottom and consider
				// the next node.
			}
		}

		EventManager.getInstance().fire(new PointMutationEvent(this, program, child, points));

		return new GPIndividual[]{child};
	}

	private List<Node> getReplacements(final Node n) {
		final int arity = n.getArity();

		// Get the return type.
		// TODO Ideally this would be the parent's required argument type
		// instead.
		final Class<?> returnType = n.getReturnType();

		// Get the data-type of children.
		final Class<?>[] argTypes = new Class<?>[arity];
		for (int i = 0; i < arity; i++) {
			argTypes[i] = n.getChild(i).getClass();
		}

		// Filter the syntax down to compatible replacements.
		final List<Node> replacements = new ArrayList<Node>();
		for (final Node replacement: syntax) {
			if ((replacement.getArity() == arity) && !nodesEqual(replacement, n)) {
				final Class<?> replacementReturn = replacement.getReturnType(argTypes);
				if ((replacementReturn != null) && returnType.isAssignableFrom(replacementReturn)) {
					replacements.add(replacement);
				}
			}
		}

		return replacements;
	}

	/*
	 * Helper function to check node equivalence. We cannot just use Node's
	 * equals() method because we don't want to compare children if it's a
	 * function node.
	 */
	private boolean nodesEqual(final Node nodeA, final Node nodeB) {
		boolean equal = false;

		// Check they're the same type first.
		if (nodeA.getClass().equals(nodeB.getClass())) {
			if (nodeA.getArity() > 0) {
				// They're both the same function type.
				equal = true;
			} else {
				equal = nodeA.equals(nodeB);
			}
		}

		return equal;
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
	public void setRandomSequence(final RandomSequence random) {
		this.random = random;
	}

	/**
	 * Returns a <code>List</code> of the <code>Nodes</code> that form the
	 * syntax of new program generated with this mutation, or
	 * an empty list if none have been set.
	 * 
	 * @return the types of <code>Node</code> that should be used in
	 *         constructing new programs.
	 */
	public Node[] getSyntax() {
		return syntax;
	}

	/**
	 * Sets the <code>Nodes</code> that should be used to construct new
	 * programs. If a model has been set then this parameter will be overwritten
	 * with the syntax from that model on the next configure event.
	 * 
	 * @param syntax a <code>List</code> of the types of <code>Node</code> that
	 *        should be used in constructing new programs.
	 */
	public void setSyntax(Node[] syntax) {
		this.syntax = syntax;
	}
}