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
 * This class implements standard crossover with uniform swap points. Subtree
 * crossover works by randomly selecting a crossover point in both parent
 * programs and then swapping the subtrees at those points.
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
 * @see OnePointCrossover
 */
public class SubtreeCrossover extends ConfigOperator<GPModel> implements GPCrossover {

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

	// The probability of choosing a function node as the swap point.
	private final double functionSwapProbability;

	/**
	 * Constructs a <code>SubtreeCrossover</code> with the only necessary
	 * parameter provided.
	 * 
	 * @param rng a <code>RandomNumberGenerator</code> used to lead
	 *        non-deterministic behaviour.
	 */
	public SubtreeCrossover(final RandomNumberGenerator rng) {
		this((GPModel) null);

		this.rng = rng;
	}

	/**
	 * Constructs a <code>SubtreeCrossover</code>.
	 * 
	 * @param rng a random number generator.
	 * @param functionSwapProbability The probability of crossover operations
	 *        choosing a function node as the swap point.
	 */
	public SubtreeCrossover(final RandomNumberGenerator rng, final double functionSwapProbability) {
		this((GPModel) null, functionSwapProbability);

		this.rng = rng;
	}

	/**
	 * Constructs a <code>SubtreeCrossover</code>.
	 * 
	 * @param model the current controlling model.
	 */
	public SubtreeCrossover(final GPModel model) {
		this(model, -1.0);
	}

	/**
	 * Construct an instance of Koza crossover.
	 * 
	 * @param functionSwapProbability The probability of crossover operations
	 *        choosing a function node as the swap point.
	 */
	public SubtreeCrossover(final GPModel model, final double functionSwapProbability) {
		super(model);

		this.functionSwapProbability = functionSwapProbability;
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
	 * @param p1 The first GPCandidateProgram selected to undergo subtree
	 *        crossover.
	 * @param p2 The second GPCandidateProgram selected to undergo subtree
	 *        crossover.
	 */
	@Override
	public GPCandidateProgram[] crossover(final CandidateProgram p1, final CandidateProgram p2) {
		final GPCandidateProgram program1 = (GPCandidateProgram) p1;
		final GPCandidateProgram program2 = (GPCandidateProgram) p2;

		// Select first swap point.
		final int swapPoint1 = getCrossoverPoint(program1);
		final Node subtree1 = program1.getNthNode(swapPoint1);// .clone();

		// Find which nodes in program2 have a matching return type to subtree1.
		final Class<?> subtree1Type = subtree1.getReturnType();
		final List<Node> matchingNodes = new ArrayList<Node>();
		final List<Integer> matchingIndexes = new ArrayList<Integer>();
		getMatchingNodes(program2.getRootNode(), subtree1Type, 0, matchingNodes, matchingIndexes);

		if (matchingNodes.size() > 0) {
			// Select second swap point with the same data-type.
			final int index = getSelectedMatch(matchingNodes);
			final Node subtree2 = matchingNodes.get(index);
			final int swapPoint2 = matchingIndexes.get(index);

			// Add data into the stats manager.
			Stats.get().addData(XO_POINT1, swapPoint1);
			Stats.get().addData(XO_POINT2, swapPoint2);
			Stats.get().addData(XO_SUBTREE1, subtree1);
			Stats.get().addData(XO_SUBTREE2, subtree2);

			// Perform swap.
			program1.setNthNode(swapPoint1, subtree2);
			program2.setNthNode(swapPoint2, subtree1);

			return new GPCandidateProgram[]{program1, program2};
		}

		return new GPCandidateProgram[0];
	}

	private int getMatchingNodes(final Node root, final Class<?> type, int current, final List<Node> matching,
			final List<Integer> indexes) {
		if (root.getReturnType() == type) {
			matching.add(root);
			indexes.add(current);
		}

		for (int i = 0; i < root.getArity(); i++) {
			current = getMatchingNodes(root.getChild(i), type, current + 1, matching, indexes);
		}

		return current;
	}

	/*
	 * Choose the crossover point for the given GPCandidateProgram with respect
	 * to the probabilities assigned for function and terminal node points.
	 */
	private int getCrossoverPoint(final GPCandidateProgram program) {
		// Calculate numbers of terminal and function nodes.
		final int length = program.getProgramLength();
		final int noTerminals = program.getNoTerminals();
		final int noFunctions = length - noTerminals;

		// Randomly decide whether to use a function or terminal node point.
		if (functionSwapProbability == -1) {
			// Randomly select a node from the program.
			return rng.nextInt(length);
		} else if ((noFunctions > 0) && (rng.nextDouble() < functionSwapProbability)) {
			// Randomly select a function node from the function set.
			final int f = rng.nextInt(noFunctions);

			return program.getRootNode().getNthFunctionNodeIndex(f);
		} else {
			// Randomly select a terminal node from the terminal set.
			final int t = rng.nextInt(noTerminals);

			return program.getRootNode().getNthTerminalNodeIndex(t);
		}
	}

	/*
	 * Choose the crossover point for the given GPCandidateProgram with respect
	 * to the probabilities assigned for function and terminal node points.
	 */
	private int getSelectedMatch(final List<Node> nodes) {
		// Randomly decide whether to use a function or terminal node point.
		if (functionSwapProbability == -1) {
			// Randomly select a node from the program.
			return rng.nextInt(nodes.size());
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

			if ((functionIndexes.size() > 0) && (rng.nextDouble() < functionSwapProbability)) {
				// Randomly select a function node from the function set.
				return functionIndexes.get(rng.nextInt(functionIndexes.size()));
			} else {
				// Randomly select a terminal node from the terminal set.
				return terminalIndexes.get(rng.nextInt(terminalIndexes.size()));
			}
		}
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
