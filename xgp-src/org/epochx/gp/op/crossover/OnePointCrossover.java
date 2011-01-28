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

import java.util.*;

import org.epochx.epox.Node;
import org.epochx.gp.model.GPModel;
import org.epochx.gp.representation.GPCandidateProgram;
import org.epochx.op.ConfigOperator;
import org.epochx.representation.CandidateProgram;
import org.epochx.stats.*;
import org.epochx.stats.Stats.ExpiryEvent;
import org.epochx.tools.random.RandomNumberGenerator;

/**
 * This class implements one point crossover as described by Poli and Langdon
 * in their paper Genetic Programming with One-Point Crossover and Point
 * Mutation. It is recommended that a significant mutation rate (Poli & Langdon
 * use 30%) is used in combination with one-point crossover since it encourages
 * rapid convergence.
 * 
 * <p>
 * If a model is provided then the following parameters are loaded upon every
 * configure event:
 * 
 * <ul>
 * <li>random number generator</li>
 * </ul>
 * 
 * <p>
 * If the <code>getModel</code> method returns <code>null</code> then no model
 * is set and whatever static parameters have been set as parameters to the
 * constructor or using the standard accessor methods will be used. If any
 * compulsory parameters remain unset when the crossover is performed then an
 * <code>IllegalStateException</code> will be thrown.
 * 
 * @see KozaCrossover
 * @see SubtreeCrossover
 */
public class OnePointCrossover extends ConfigOperator<GPModel> implements GPCrossover {

	/**
	 * Requests an <code>Integer</code> which is the point chosen in the first
	 * parent for the uniform point crossover operation.
	 */
	public static final Stat XO_POINT1 = new AbstractStat(ExpiryEvent.CROSSOVER) {};

	/**
	 * Requests an <code>Integer</code> which is the point chosen in the second
	 * parent for the uniform point crossover operation.
	 */
	public static final Stat XO_POINT2 = new AbstractStat(ExpiryEvent.CROSSOVER) {};

	/**
	 * Requests a <code>Node</code> which is the subtree from the
	 * first parent program which is being exchanged into the second parent.
	 */
	public static final Stat XO_SUBTREE1 = new AbstractStat(ExpiryEvent.CROSSOVER) {};

	/**
	 * Requests a <code>Node</code> which is the subtree from the
	 * second parent program which is being exchanged into the first parent.
	 */
	public static final Stat XO_SUBTREE2 = new AbstractStat(ExpiryEvent.CROSSOVER) {};

	// The random number generator for controlling random behaviour.
	private RandomNumberGenerator rng;

	// Whether to use the strict version of one-point crossover or not.
	private boolean strict;

	/**
	 * Constructs a <code>OnePointCrossover</code> with the only necessary
	 * parameter provided. Defaults to using a non-strict implementation of
	 * one-point crossover.
	 * 
	 * @param rng a <code>RandomNumberGenerator</code> used to lead
	 *        non-deterministic behaviour.
	 */
	public OnePointCrossover(final RandomNumberGenerator rng) {
		this(rng, false);
	}

	/**
	 * Constructs a <code>OnePointCrossover</code> with the necessary
	 * parameters provided.
	 * 
	 * @param rng a <code>RandomNumberGenerator</code> used to lead
	 *        non-deterministic behaviour.
	 * @param strict whether strict one-point crossover should be used where not
	 *        only must the arity of nodes match, but the node types.
	 */
	public OnePointCrossover(final RandomNumberGenerator rng, final boolean strict) {
		this((GPModel) null, strict);

		this.rng = rng;
	}

	/**
	 * Constructs a <code>OnePointCrossover</code>. Defaults to using a
	 * non-strict implementation of one-point crossover.
	 * 
	 * @param model the current controlling model.
	 */
	public OnePointCrossover(final GPModel model) {
		this(model, false);
	}

	/**
	 * Constructs a <code>OnePointCrossover</code>.
	 * 
	 * @param model the current controlling model.
	 * @param strict whether strict one-point crossover should be used where not
	 *        only must the arity of nodes match, but the node types.
	 */
	public OnePointCrossover(final GPModel model, final boolean strict) {
		super(model);

		this.strict = strict;
	}

	/**
	 * Configures this operator with parameters from the model.
	 */
	@Override
	public void onConfigure() {
		rng = getModel().getRNG();
	}

	/**
	 * Crosses over the two <code>CandidatePrograms</code> provided as arguments
	 * using uniform swap points. Random crossover points are chosen at random
	 * in both programs, the genetic material at the points are then exchanged.
	 * The resulting programs are returned as new GPCandidateProgram objects.
	 * 
	 * @param p1 The first GPCandidateProgram selected to undergo one
	 *        point crossover.
	 * @param p2 The second GPCandidateProgram selected to undergo one
	 *        point crossover.
	 */
	@Override
	public GPCandidateProgram[] crossover(final CandidateProgram p1, final CandidateProgram p2) {
		final GPCandidateProgram program1 = (GPCandidateProgram) p1;
		final GPCandidateProgram program2 = (GPCandidateProgram) p2;

		final List<Integer> points1 = new ArrayList<Integer>();
		final List<Integer> points2 = new ArrayList<Integer>();

		traverse(program1.getRootNode(), program2.getRootNode(), points1, points2, 0, 0);

		// Select swap points.
		final int randomIndex = rng.nextInt(points1.size());
		final int swapPoint1 = points1.get(randomIndex);
		final int swapPoint2 = points2.get(randomIndex);

		// Add crossover points to the stats manager.
		Stats.get().addData(XO_POINT1, swapPoint1);
		Stats.get().addData(XO_POINT2, swapPoint2);

		// Get copies of subtrees to swap.
		final Node subtree1 = program1.getNthNode(swapPoint1);// .clone();
		final Node subtree2 = program2.getNthNode(swapPoint2);// .clone();

		// Add subtrees into the stats manager.
		Stats.get().addData(XO_SUBTREE1, subtree1);
		Stats.get().addData(XO_SUBTREE2, subtree2);

		// Perform swap.
		program1.setNthNode(swapPoint1, subtree2);
		program2.setNthNode(swapPoint2, subtree1);

		return new GPCandidateProgram[]{program1, program2};
	}

	/*
	 * Private helper method for crossover method. Traverses the two program
	 * trees and identifies the swap points that can be swapped because they're
	 * connected to a part of the tree that aligns. Supports strict or
	 * non-strict.
	 */
	private void traverse(final Node root1, final Node root2, final List<Integer> points1, final List<Integer> points2,
			int current1, int current2) {
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
				final Node child1 = root1.getChild(i);
				final Node child2 = root2.getChild(i);
				traverse(child1, child2, points1, points2, current1 + 1, current2 + 1);

				current1 += child1.getLength();
				current2 += child2.getLength();
			}
		}
	}

	/**
	 * Returns whether strict one-point crossover is being used, or not. If set
	 * to <code>true</code> then alignment of the parent programs takes into
	 * account not only the arity of the nodes, but also the node type.
	 * 
	 * @return true if strict one-point crossover is in use, and false
	 *         otherwise.
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
	 *        false otherwise.
	 */
	public void setStrict(final boolean strict) {
		this.strict = strict;
	}

	/**
	 * Returns the random number generator that this crossover is using or
	 * <code>null</code> if none has been set.
	 * 
	 * @return the rng the currently set random number generator.
	 */
	public RandomNumberGenerator getRNG() {
		return rng;
	}

	/**
	 * Sets the random number generator to use. If a model has been set then
	 * this parameter will be overwritten with the random number generator from
	 * that model on the next configure event.
	 * 
	 * @param rng the random number generator to set.
	 */
	public void setRNG(final RandomNumberGenerator rng) {
		this.rng = rng;
	}
}
