/*
 * Copyright 2007-2010 Tom Castle & Lawrence Beadle
 * Licensed under GNU General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.gp.op.init;

import java.util.*;

import org.epochx.gp.model.GPModel;
import org.epochx.gp.representation.*;
import org.epochx.life.ConfigAdapter;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.random.RandomNumberGenerator;

/**
 * Initialisation implementation which randomly grows program trees down to a
 * maximum depth.
 * 
 * <p>
 * If a model is provided then the following parameters are loaded upon every
 * configure event:
 * </p>
 * 
 * <ul>
 * <li>population size</li>
 * <li>maximum initial program depth</li>
 * <li>syntax</li>
 * <li>random number generator</li>
 * </ul>
 * 
 * @see FullInitialiser
 * @see RampedHalfAndHalfInitialiser
 */
public class GrowInitialiser implements GPInitialiser {

	// The controlling model.
	private final GPModel model;

	private RandomNumberGenerator rng;

	// The language to construct the trees from.
	private List<Node> syntax;
	private final List<Node> terminals;
	private final List<Node> functions;

	// The size of the populations to construct.
	private int popSize;

	// The maximum depth of each program tree to generate.
	private int initialMaxDepth;

	// Whether programs must be unique in generated populations.
	private boolean acceptDuplicates;

	/**
	 * Constructs a <code>GrowInitialiser</code> with the necessary parameters
	 * loaded from the given model. The parameters are reloaded on configure
	 * events. Duplicate programs are allowed in the populations that are
	 * constructed.
	 * 
	 * @param model the <code>Model</code> instance from which the necessary
	 *        parameters should be loaded.
	 */
	public GrowInitialiser(final GPModel model) {
		this(model, true);
	}

	/**
	 * Constructs a <code>GrowInitialiser</code> with the necessary parameters
	 * loaded from the given model. The parameters are reloaded on configure
	 * events.
	 * 
	 * @param model the <code>Model</code> instance from which the necessary
	 *        parameters should be loaded.
	 * @param acceptDuplicates whether duplicates should be allowed in the
	 *        populations that are generated.
	 */
	public GrowInitialiser(final GPModel model, final boolean acceptDuplicates) {
		this.model = model;
		this.acceptDuplicates = acceptDuplicates;

		terminals = new ArrayList<Node>();
		functions = new ArrayList<Node>();

		// Configure parameters from the model.
		model.getLifeCycleManager().addConfigListener(new ConfigAdapter() {

			@Override
			public void onConfigure() {
				configure();
			}
		});
	}

	/*
	 * Configure component with parameters from the model.
	 */
	private void configure() {
		rng = model.getRNG();
		popSize = model.getPopulationSize();
		initialMaxDepth = model.getMaxInitialDepth();

		// Perhaps we could check whether the syntax has changed first?
		terminals.clear();
		functions.clear();
		syntax = model.getSyntax();

		for (final Node n: syntax) {
			if (n.getArity() == 0) {
				terminals.add(n);
			} else {
				functions.add(n);
			}
		}
	}

	/**
	 * Generates a population of new <code>CandidatePrograms</code> constructed
	 * from the <code>Nodes</code> in the syntax attribute. The size of the
	 * population will be equal to the population size attribute. All programs
	 * in the population are only guarenteed to be unique (as defined by the
	 * <code>equals</code> method on <code>GPCandidateProgram</code>) if the
	 * <code>isDuplicatesEnabled</code> method returns <code>true</code>.
	 * Each program will have a node tree with a depth at most equal to the
	 * value of the maximum depth attribute.
	 * 
	 * @return A <code>List</code> of newly generated
	 *         <code>CandidatePrograms</code>.
	 */
	@Override
	public List<CandidateProgram> getInitialPopulation() {
		// Create population list to be populated.
		final List<CandidateProgram> firstGen = new ArrayList<CandidateProgram>(
				popSize);

		// Create and add new programs to the population.
		for (int i = 0; i < popSize; i++) {
			GPCandidateProgram candidate;

			do {
				// Grow a new node tree.
				final Node nodeTree = buildGrowNodeTree(initialMaxDepth);

				// Create a program around the node tree.
				candidate = new GPCandidateProgram(nodeTree, model);
			} while (!acceptDuplicates && firstGen.contains(candidate));

			// Must be unique - add to the new population.
			firstGen.add(candidate);
		}

		return firstGen;
	}

	/**
	 * Builds a grown node tree with a maximum depth as given. The nodes that 
	 * form the tree will be randomly selected from the nodes provided as the 
	 * syntax attribute.
	 * 
	 * @param initialMaxDepth The maximum depth of the node tree to be grown, where
	 *        the depth is the number of nodes from the root.
	 * @return The root node of a randomly generated node tree.
	 */
	public Node buildGrowNodeTree(final int maxDepth) {
		// Randomly choose a root node.
		final int randomIndex = rng.nextInt(syntax.size());
		final Node root = syntax.get(randomIndex).clone();

		// Populate the root node with grown children with maximum depth-1.
		this.fillChildren(root, 0, maxDepth);

		return root;
	}

	/*
	 * Helper method for the buildFullNodeTree method. Recursively fills the 
	 * children of a node, to construct a grown tree down to at most a depth of 
	 * initialMaxDepth.
	 */
	private void fillChildren(final Node currentNode, final int currentDepth,
			final int maxDepth) {
		final int arity = currentNode.getArity();
		if (arity > 0) {
			if (currentDepth < maxDepth - 1) {
				// Not near the maximum depth yet, use functions OR terminals.
				for (int i = 0; i < arity; i++) {
					final int randomIndex = rng.nextInt(syntax.size());
					final Node child = syntax.get(randomIndex).clone();

					currentNode.setChild(i, child);
					this.fillChildren(child, (currentDepth + 1), maxDepth);
				}
			} else {
				// At maximum depth-1, fill children with terminals.
				for (int i = 0; i < arity; i++) {
					final int randomIndex = rng.nextInt(terminals.size());
					final Node child = terminals.get(randomIndex).clone();

					currentNode.setChild(i, child);
				}
			}
		}
	}

	/**
	 * Returns whether or not duplicates are currently accepted or rejected from
	 * generated populations.
	 * 
	 * @return <code>true</code> if duplicates are currently accepted in any
	 *         populations generated by the <code>getInitialPopulation</code>
	 *         method and <code>false</code> otherwise
	 */
	public boolean isDuplicatesEnabled() {
		return acceptDuplicates;
	}

	/**
	 * Sets whether duplicates should be allowed in the populations that are
	 * generated, or if they should be discarded.
	 * 
	 * @param acceptDuplicates whether duplicates should be accepted in the
	 *        populations that are constructed.
	 */
	public void setDuplicatesEnabled(boolean acceptDuplicates) {
		this.acceptDuplicates = acceptDuplicates;
	}
}
