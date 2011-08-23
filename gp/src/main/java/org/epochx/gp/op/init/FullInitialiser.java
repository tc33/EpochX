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
package org.epochx.gp.op.init;

import static org.epochx.Population.SIZE;
import static org.epochx.RandomSequence.RANDOM_SEQUENCE;
import static org.epochx.gp.GPIndividual.*;
import static org.epochx.gp.op.init.GPInitialiser.MAXIMUM_INITIAL_DEPTH;

import java.util.*;

import org.epochx.*;
import org.epochx.epox.Node;
import org.epochx.event.*;
import org.epochx.gp.GPIndividual;
import org.epochx.tools.util.TypeUtils;

/**
 * Initialisation implementation which produces full program trees down to a
 * specified depth.
 * 
 * @see GrowInitialiser
 * @see RampedHalfAndHalfInitialiser
 */
public class FullInitialiser implements Initialiser, Listener<ConfigEvent> {

	private RandomSequence random;

	// The language to construct the trees from.
	private final List<Node> terminals;
	private final List<Node> functions;
	private Node[] syntax;

	// Each generated program's return type.
	private Class<?> returnType;

	// The size of the populations to construct.
	private int popSize;

	// The depth of every program tree to generate.
	private int depth;

	// Whether programs must be unique in generated populations.
	private boolean acceptDuplicates;

	// Return types that are valid at each depth.
	private Class<?>[][] validDepthTypes;

	/**
	 * Constructs a <code>FullInitialiser</code> with all the necessary
	 * parameters given.
	 */
	public FullInitialiser(RandomSequence random, Node[] syntax, Class<?> returnType,
			int popSize, int depth, boolean acceptDuplicates) {
		this(acceptDuplicates);

		this.random = random;
		this.syntax = syntax;
		this.returnType = returnType;
		this.popSize = popSize;
		this.depth = depth;

		updateSyntax();
	}

	/**
	 * Constructs a <code>FullInitialiser</code> with the necessary parameters
	 * loaded from the given model. The parameters are reloaded on configure
	 * events. Duplicate programs are allowed in the populations that are
	 * constructed.
	 * 
	 * @param model the <code>GPModel</code> instance from which the necessary
	 *        parameters should be loaded.
	 */
	public FullInitialiser() {
		this(true);
	}

	/**
	 * Constructs a <code>FullInitialiser</code> with the necessary parameters
	 * loaded from the given model. The parameters are reloaded on configure
	 * events.
	 * 
	 * @param model the <code>GPModel</code> instance from which the necessary
	 *        parameters should be loaded.
	 * @param acceptDuplicates whether duplicates should be allowed in the
	 *        populations that are generated.
	 */
	public FullInitialiser(boolean acceptDuplicates) {
		terminals = new ArrayList<Node>();
		functions = new ArrayList<Node>();

		this.acceptDuplicates = acceptDuplicates;
		
		setup();
		EventManager.getInstance().add(ConfigEvent.class, this);
	}

	/**
	 * Generates a population of new <code>CandidatePrograms</code> constructed
	 * from the <code>Nodes</code> in the syntax attribute. The size of the
	 * population will be equal to the population size attribute. All programs
	 * in the population are only guarenteed to be unique (as defined by the
	 * <code>equals</code> method on <code>GPIndividual</code>) if the
	 * <code>isDuplicatesEnabled</code> method returns <code>true</code>. Each
	 * program will have a full node tree with a depth equal to the
	 * depth attribute.
	 * 
	 * @return A <code>List</code> of newly generated
	 *         <code>GPIndividual</code> instances with full node trees.
	 */
	@Override
	public Population process(Population population) {
		// Create population list to be populated.
		Population firstGen = new Population();

		// Create and add new programs to the population.
		for (int i = 0; i < popSize; i++) {
			GPIndividual candidate;

			do {
				// Build a new full node tree.
				candidate = create();
			} while (!acceptDuplicates && firstGen.contains(candidate));

			// Must be unique - add to the new population.
			firstGen.add(candidate);
		}

		return firstGen;
	}

	/**
	 * Constructs a new full node tree and returns it within a
	 * <code>GPIndividual</code>. The nodes that form the tree will be
	 * randomly selected from the nodes provided as the syntax attribute.
	 * 
	 * @return a new <code>GPIndividual</code> instance.
	 */
	@Override
	public GPIndividual create() {
		final Node root = getFullNodeTree();

		return new GPIndividual(root);
	}

	/**
	 * Builds a full node tree down to the given depth. As the node tree will be
	 * full the maximum and minimum depths of the returned node tree should be
	 * equal to the depth argument. The nodes that form the tree will be
	 * randomly selected from the nodes provided as the syntax attribute.
	 * 
	 * @return The root node of a randomly generated full node tree of the
	 *         requested depth.
	 */
	public Node getFullNodeTree() {
		if (random == null) {
			throw new IllegalStateException("No random number generator has been set");
		} else if (returnType == null) {
			throw new IllegalStateException("No return type has been set");
		} else if (depth < 0) {
			throw new IllegalStateException("Depth must be 0 or greater");
		} else if (terminals.isEmpty()) {
			throw new IllegalStateException("Syntax must include nodes with arity of 0");
		} else if ((depth > 0) && functions.isEmpty()) {
			throw new IllegalStateException("Syntax must include nodes with arity of >=1 if a depth >0 is used");
		}

		// Update the types possibilities table if needed.
		if (validDepthTypes == null) {
			updateValidTypes();
		}

		if (!TypeUtils.containsSub(validDepthTypes[depth], returnType)) {
			throw new IllegalStateException("Syntax is not able to produce full trees with the given return type");
		}

		return getNodeTree(returnType, 0);
	}

	/*
	 * Helper method for the getFullNodeTree method. Recursively fills the
	 * children of a node, to construct a full tree down to a depth of
	 * maxDepth.
	 */
	private Node getNodeTree(final Class<?> requiredType, final int currentDepth) {
		// Choose a node with correct type and obtainable arg types.
		final List<Node> validNodes = getValidNodes(depth - currentDepth, requiredType);

		if (validNodes.isEmpty()) {
			throw new IllegalStateException("Syntax is unable to create full node trees of given depth.");
		}
		final int randomIndex = random.nextInt(validNodes.size());
		final Node root = validNodes.get(randomIndex).newInstance();
		final int arity = root.getArity();

		if (arity > 0) {
			// Construct a list of the arg sets that produce the right return
			// type.
			// TODO We can surely cut down the amount of times we're calling
			// this?!
			final Class<?>[][] argTypeSets = getPossibleArgTypes(arity, validDepthTypes[depth - currentDepth - 1]);
			final List<Class<?>[]> validArgTypeSets = new ArrayList<Class<?>[]>();
			for (final Class<?>[] argTypes: argTypeSets) {
				final Class<?> type = root.getReturnType(argTypes);
				if ((type != null) && requiredType.isAssignableFrom(type)) {
					validArgTypeSets.add(argTypes);
				}
			}

			// Randomly select from the valid arg sets.
			if (validArgTypeSets.isEmpty()) {
				throw new IllegalStateException("Syntax is unable to create full node trees of given depth.");
			}
			final Class<?>[] argTypes = validArgTypeSets.get(random.nextInt(validArgTypeSets.size()));

			for (int i = 0; i < arity; i++) {
				root.setChild(i, getNodeTree(argTypes[i], currentDepth + 1));
			}
		}

		return root;
	}

	private List<Node> getValidNodes(final int remainingDepth, final Class<?> requiredType) {
		final List<Node> validNodes = new ArrayList<Node>();
		if (remainingDepth == 0) {
			for (final Node n: terminals) {
				if (n.getReturnType().isAssignableFrom(requiredType)) {
					validNodes.add(n);
				}
			}
		} else {
			for (final Node n: functions) {
				final Class<?>[][] argTypeSets = getPossibleArgTypes(n.getArity(), validDepthTypes[remainingDepth - 1]);

				for (final Class<?>[] argTypes: argTypeSets) {
					final Class<?> type = n.getReturnType(argTypes);
					if ((type != null) && requiredType.isAssignableFrom(type)) {
						validNodes.add(n);
						break;
					}
				}
			}
		}
		return validNodes;
	}

	/*
	 * Generates the "type possibilities table" from the syntax and return
	 * type, as described by Montana.
	 */
	private void updateValidTypes() {
		validDepthTypes = new Class<?>[depth + 1][];

		// Trees of depth 0 must be single terminal element.
		Set<Class<?>> types = new HashSet<Class<?>>();
		for (final Node n: terminals) {
			types.add(n.getReturnType());
		}
		validDepthTypes[0] = types.toArray(new Class<?>[types.size()]);

		// Handle depths above 1.
		for (int i = 1; i <= depth; i++) {
			types = new HashSet<Class<?>>();
			for (final Node n: functions) {
				final Class<?>[][] argTypeSets = getPossibleArgTypes(n.getArity(), validDepthTypes[i - 1]);

				// Test each possible set of arguments.
				for (final Class<?>[] argTypes: argTypeSets) {
					final Class<?> returnType = n.getReturnType(argTypes);
					if (returnType != null) {
						types.add(returnType);
					}
				}
			}
			validDepthTypes[i] = types.toArray(new Class<?>[types.size()]);
		}
	}

	/*
	 * TODO We actually only need to do this once at each depth for a particular
	 * arity.
	 */
	private Class<?>[][] getPossibleArgTypes(final int arity, final Class<?>[] availableTypes) {
		final int noTypes = availableTypes.length;
		final int noCombinations = (int) Math.pow(noTypes, arity);
		final Class<?>[][] possibleTypes = new Class<?>[noCombinations][arity];

		for (int i = 0; i < arity; i++) {
			final int period = (int) Math.pow(noTypes, i);

			for (int j = 0; j < noCombinations; j++) {
				final int group = j / period;
				possibleTypes[j][i] = availableTypes[group % noTypes];
			}
		}

		return possibleTypes;
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
	public void setDuplicatesEnabled(final boolean acceptDuplicates) {
		this.acceptDuplicates = acceptDuplicates;
	}

	/**
	 * Returns a <code>List</code> of the <code>Nodes</code> that form the
	 * syntax of new program generated with this initialiser, or
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

		updateSyntax();
	}

	/**
	 * Gets the return type of the program trees that are generated with this
	 * initialiser. For a program to have a specific return type means that its
	 * root node should return an instance of the given type.
	 * 
	 * @return the return type of the programs being generated.
	 */
	public Class<?> getReturnType() {
		return returnType;
	}

	/**
	 * Sets the return type of the program trees that are generated with this
	 * initialiser. For a program to have a specific return type means that is
	 * root node will return an instance of the given type.
	 * 
	 * @param returnType the return type that generated programs should have.
	 */
	public void setReturnType(final Class<?> returnType) {
		this.returnType = returnType;

		// Types possibilities table needs updating.
		validDepthTypes = null;
	}

	/**
	 * Returns the size of the populations that this initialiser constructs or
	 * <code>-1</code> if none has been set.
	 * 
	 * @return the size of the populations that this initialiser will generate.
	 */
	public int getPopSize() {
		return popSize;
	}

	/**
	 * Sets the size of the populations that this initialiser should construct
	 * on calls to the <code>getInitialPopulation</code> method.
	 * 
	 * @param popSize the size of the populations that should be created by this
	 *        initialiser.
	 */
	public void setPopSize(final int popSize) {
		this.popSize = popSize;
	}

	/**
	 * Returns the depth that every program generated by this initialiser should
	 * have.
	 * 
	 * @return the depth of the program trees constructed.
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * Sets the depth that the program trees this initialiser constructs should
	 * be.
	 * 
	 * @param depth the depth of all new program trees.
	 */
	public void setDepth(final int depth) {
		if (depth > this.depth) {
			// Types possibilities table needs updating.
			// TODO Actually the whole table doesn't need updating, just
			// extending to new depth.
			validDepthTypes = null;
		}

		this.depth = depth;
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
		popSize = Config.getInstance().get(SIZE);
		syntax = Config.getInstance().get(SYNTAX);
		returnType = Config.getInstance().get(RETURN_TYPE);
		
		int maxDepth = Config.getInstance().get(MAXIMUM_DEPTH);
		int maxInitialDepth = Config.getInstance().get(MAXIMUM_INITIAL_DEPTH);
		
		if (maxInitialDepth < maxDepth || maxDepth == -1) {
			depth = maxInitialDepth;
		} else {
			depth = maxDepth;
		}
	}
	
	/*
	 * Updates the terminals and functions lists from the syntax.
	 */
	private void updateSyntax() {
		terminals.clear();
		functions.clear();

		if (syntax != null) {
			for (final Node n: syntax) {
				if (n.getArity() == 0) {
					terminals.add(n);
				} else {
					functions.add(n);
				}
			}
		}
		
		// Types possibilities table needs updating.
		validDepthTypes = null;
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
		if (event.isKindOf(RANDOM_SEQUENCE, SIZE, SYNTAX, RETURN_TYPE, MAXIMUM_INITIAL_DEPTH)) {
			setup();
		}
		if (event.isKindOf(SYNTAX)) {
			// This is a little expensive, so only do it when needed.
			updateSyntax();
		}
		if (event.isKindOf(RETURN_TYPE)) {
			// This will be expensive too.
			validDepthTypes = null;
		}
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
