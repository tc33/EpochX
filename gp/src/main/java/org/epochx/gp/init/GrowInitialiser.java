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
package org.epochx.gp.init;

import static org.epochx.Population.SIZE;
import static org.epochx.RandomSequence.RANDOM_SEQUENCE;
import static org.epochx.gp.GPIndividual.*;
import java.math.BigInteger;
import java.util.*;

import org.epochx.*;
import org.epochx.epox.Node;
import org.epochx.event.*;
import org.epochx.gp.GPIndividual;

/**
 * Initialisation implementation which randomly grows program trees down to a
 * maximum depth.
 * 
 * @see FullInitialiser
 * @see RampedHalfAndHalfInitialiser
 */
public class GrowInitialiser extends GPInitialiser implements Listener<ConfigEvent> {

	private RandomSequence random;

	// The language to construct the trees from.
	private Node[] syntax;
	private List<Node> terminals;
	private List<Node> functions;

	// Each generated program's return type.
	private Class<?> returnType;

	// The size of the populations to construct.
	private int popSize;

	// The maximum depth of each program tree to generate.
	private int maxDepth;

	// Whether programs must be unique in generated populations.
	private boolean acceptDuplicates;

	// Return types that are valid at each depth.
	private Class<?>[][] validDepthTypes;

	/**
	 * Constructs a <code>GrowInitialiser</code> with all the necessary
	 * parameters given.
	 */
	public GrowInitialiser(final RandomSequence rng, final Node[] syntax, final Class<?> returnType,
			final int popSize, final int maxDepth, final boolean acceptDuplicates) {
		this.random = rng;
		this.syntax = syntax;
		this.returnType = returnType;
		this.popSize = popSize;
		this.maxDepth = maxDepth;

		updateSyntax();
	}

	/**
	 * Constructs a <code>GrowInitialiser</code> with the necessary parameters
	 * loaded from the given model. The parameters are reloaded on configure
	 * events. Duplicate programs are allowed in the populations that are
	 * constructed.
	 * 
	 * @param model the <code>GPModel</code> instance from which the necessary
	 *        parameters should be loaded.
	 */
	public GrowInitialiser() {
		this(true);
	}

	/**
	 * Constructs a <code>GrowInitialiser</code> with the necessary parameters
	 * loaded from the given model. The parameters are reloaded on configure
	 * events.
	 * 
	 * @param acceptDuplicates whether duplicates should be allowed in the
	 *        populations that are generated.
	 */
	public GrowInitialiser(boolean acceptDuplicates) {
		this.acceptDuplicates = acceptDuplicates;
		
		terminals = new ArrayList<Node>();
		functions = new ArrayList<Node>();

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
		super.setup();
		random = Config.getInstance().get(RANDOM_SEQUENCE);
		popSize = Config.getInstance().get(SIZE);
		syntax = Config.getInstance().get(SYNTAX);
		returnType = Config.getInstance().get(RETURN_TYPE);
		
		maxDepth = Config.getInstance().get(MAXIMUM_DEPTH);
		int maxInitialDepth = Config.getInstance().get(MAXIMUM_INITIAL_DEPTH);
		
		if (maxInitialDepth < maxDepth || maxDepth == -1) {
			maxDepth = maxInitialDepth;
		}
	}

	/*
	 * Updates the terminals and functions lists from the syntax.
	 */
	private void updateSyntax() {
		terminals = new ArrayList<Node>();
		functions = new ArrayList<Node>();

		if (syntax != null) {
			for (final Node n: syntax) {
				if (n.isTerminal()) {
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
	 * Generates a population of new <code>GPCandidatePrograms</code>
	 * constructed
	 * from the <code>Nodes</code> in the syntax attribute. The size of the
	 * population will be equal to the population size attribute. All programs
	 * in the population are only guarenteed to be unique (as defined by the
	 * <code>equals</code> method on <code>GPIndividual</code>) if the
	 * <code>isDuplicatesEnabled</code> method returns <code>false</code>.
	 * Each program will have a node tree with a depth at most equal to the
	 * value of the maximum depth attribute.
	 * 
	 * @return A <code>List</code> of newly generated
	 *         <code>GPCandidatePrograms</code>.
	 */
	@Override
	public Population process(Population population) {
		EventManager.getInstance().fire(new InitialisationEvent.StartInitialisation(
				population));
		
		// Create and add new programs to the population.
		for (int i = 0; i < popSize; i++) {
			GPIndividual candidate;

			do {
				// Grow a new node tree.
				candidate = create();
			} while (!acceptDuplicates && population.contains(candidate));

			// Must be unique - add to the new population.
			population.add(candidate);
		}
		
		EventManager.getInstance().fire(new InitialisationEvent.EndInitialisation(
				population));

		return population;
	}

	/**
	 * Grows a new node tree and returns it within a
	 * <code>GPIndividual</code>. The nodes that form the tree will be
	 * randomly selected from the nodes provided as the syntax attribute.
	 * 
	 * @return a new <code>GPIndividual</code> instance.
	 */
	public GPIndividual create() {
		final Node root = getGrownNodeTree(maxDepth);

		return new GPIndividual(root);
	}

	/**
	 * Builds a grown node tree with a maximum depth as given. The nodes that
	 * form the tree will be randomly selected from the nodes provided as the
	 * syntax attribute.
	 * 
	 * @param maxDepth The maximum depth of the node tree to be grown, where
	 *        the depth is the number of nodes from the root.
	 * @return The root node of a randomly generated node tree.
	 */
	public Node getGrownNodeTree(final int maxDepth) {
		if (random == null) {
			throw new IllegalStateException("No random number generator has been set");
		} else if (maxDepth < 0) {
			throw new IllegalStateException("Depth must be 0 or greater");
		} else if (terminals.isEmpty()) {
			throw new IllegalStateException("Syntax must include nodes with arity of 0");
		}

		// Update the types possibilities table if needed.
		if (validDepthTypes == null) {
			updateValidTypes();
		}

		// TODO Add validation that syntax contains node that matches return
		// type.

		return getNodeTree(returnType, 0);
	}

	/*
	 * Helper method for the getGrownNodeTree method. Recursively fills the
	 * children of a node, to construct a full tree down to a depth of
	 * maxDepth.
	 */
	private Node getNodeTree(final Class<?> requiredType, final int currentDepth) {
		// Choose a node with correct type and obtainable arg types.
		final List<Node> validNodes = getValidNodes(maxDepth - currentDepth, requiredType);
		final int randomIndex = random.nextInt(validNodes.size());
		final Node root = validNodes.get(randomIndex).newInstance();
		final int arity = root.getArity();

		if (arity > 0) {
			// Construct a list of the arg sets that produce the right return
			// type.
			// TODO We can surely cut down the amount of times we're calling
			// this?!
			final Class<?>[][] argTypeSets = getPossibleArgTypes(arity, validDepthTypes[maxDepth - currentDepth]);
			final List<Class<?>[]> validArgTypeSets = new ArrayList<Class<?>[]>();
			for (final Class<?>[] argTypes: argTypeSets) {
				final Class<?> type = root.getReturnType(argTypes);
				if ((type != null) && requiredType.isAssignableFrom(type)) {
					validArgTypeSets.add(argTypes);
				}
			}

			if (validArgTypeSets.isEmpty()) {
				throw new IllegalStateException("Syntax is unable to create full node trees of given depth.");
			}

			// Randomly select from the valid arg sets.
			final Class<?>[] argTypes = validArgTypeSets.get(random.nextInt(validArgTypeSets.size()));

			for (int i = 0; i < arity; i++) {
				root.setChild(i, getNodeTree(argTypes[i], currentDepth + 1));
			}
		}

		return root;
	}

	private List<Node> getValidNodes(final int remainingDepth, final Class<?> requiredType) {
		final List<Node> validNodes = new ArrayList<Node>();
		for (final Node n: terminals) {
			if (n.getReturnType().isAssignableFrom(requiredType)) {
				validNodes.add(n);
			}
		}

		if (remainingDepth > 0) {
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
		validDepthTypes = new Class<?>[maxDepth + 1][];

		for (int i = 0; i <= maxDepth; i++) {
			final Set<Class<?>> types = new HashSet<Class<?>>();
			for (final Node n: terminals) {
				types.add(n.getReturnType());
			}

			if (i > 0) {
				// Also add any valid functions.
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
			}
			validDepthTypes[i] = types.toArray(new Class<?>[types.size()]);
		}
	}

	/*
	 * TODO This is a duplicate of the same method in FullInitialiser.
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

	public BigInteger noVarieties(final int remainingDepth, final Class<?> returnType) {
		// Update the types possibilities table if needed.
		if (validDepthTypes == null) {
			updateValidTypes();
		}

		final List<Class<?>> returnTypes = new ArrayList<Class<?>>();
		returnTypes.add(returnType);
		return noVarieties(remainingDepth, returnTypes);
	}

	/**
	 * Counts and returns the number of varieties of node tree that are possible
	 * with a set of nodes to a given depth. The quantity of varieties is
	 * effectively the size of the search space. The result will grow
	 * exponentially with depth and out grow a long very quickly, so a
	 * BigInteger is used here.
	 * 
	 * @param syntax the available nodes.
	 * @param remainingDepth the depth of node trees allowable.
	 * @return an int which is the number of varieties of node trees possible.
	 */
	private BigInteger noVarieties(final int remainingDepth, List<Class<?>> returnTypes) {
		BigInteger varieties = BigInteger.ZERO;
		for (final Node n: terminals) {
			for (final Class<?> returnType: returnTypes) {
				if (returnType.isAssignableFrom(n.getReturnType())) {
					varieties = varieties.add(BigInteger.ONE);
					break;
				}
			}
		}

		if (remainingDepth > 0) {
			// Also add any valid functions.
			for (final Node n: functions) {
				final Class<?>[][] argTypeSets = getPossibleArgTypes(n.getArity(), validDepthTypes[remainingDepth - 1]);

				// Valid sets of argument types.
				final List<Class<?>[]> valid = new ArrayList<Class<?>[]>();

				// Test each possible set of arguments.
				for (final Class<?>[] argTypes: argTypeSets) {
					final Class<?> type = n.getReturnType(argTypes);

					if (type != null) {
						// If the return type is a subtype of one that is
						// needed.
						for (final Class<?> returnType: returnTypes) {
							if (returnType.isAssignableFrom(type)) {
								valid.add(argTypes);
								break;
							}
						}
					}
				}

				// If there are any possible valid arguments.
				if (valid.size() > 0) {
					BigInteger totalChildVarieties = BigInteger.ONE;
					for (int i = 0; i < n.getArity(); i++) {
						// Construct a list of the valid argument types for this
						// child.
						returnTypes = new ArrayList<Class<?>>();
						for (int j = 0; j < valid.size(); j++) {
							returnTypes.add(valid.get(j)[i]);
						}

						// Find the number of varieties for the subtree rooted
						// at this child.
						final BigInteger childVarieties = noVarieties(remainingDepth - 1, returnTypes);

						// Multiply to get the number of varieties amongst the
						// children.
						totalChildVarieties = totalChildVarieties.multiply(childVarieties);
					}
					varieties = varieties.add(totalChildVarieties);
				}
			}
		}

		return varieties;
	}

	public boolean isSufficientVarieties(final int remainingDepth, final Class<?> returnType, final BigInteger target) {
		// Update the types possibilities table if needed.
		if (validDepthTypes == null) {
			updateValidTypes();
		}

		final List<Class<?>> returnTypes = new ArrayList<Class<?>>();
		returnTypes.add(returnType);
		return isSufficientVarieties(remainingDepth, returnTypes, target);
	}

	/**
	 * Returns true if the number of node tree varieties is equal to or greater
	 * than the target parameter. Otherwise, false is returned. The noVarieties
	 * method can be used to determine the number of varieties, but often the
	 * caller is only interested in whether there are sufficient varieties so
	 * there is no need to calculate the full number.
	 * 
	 * @param syntax the available nodes.
	 * @param depth the depth of node trees allowable.
	 * @param target the number of varieties that is considered sufficient.
	 * @return true if the number of node tree varieties is equal to or greater
	 *         than the target parameter, false otherwise.
	 */
	private boolean isSufficientVarieties(final int remainingDepth, List<Class<?>> returnTypes, final BigInteger target) {
		// TODO This and the related noVarieties methods can be simplified
		// massively.
		BigInteger varieties = BigInteger.ZERO;
		for (final Node n: terminals) {
			for (final Class<?> returnType: returnTypes) {
				if (returnType.isAssignableFrom(n.getReturnType())) {
					varieties = varieties.add(BigInteger.ONE);
					break;
				}

				if (varieties.compareTo(target) >= 0) {
					return true;
				}
			}
		}

		if (remainingDepth > 0) {
			// Also add any valid functions.
			for (final Node n: functions) {
				final Class<?>[][] argTypeSets = getPossibleArgTypes(n.getArity(), validDepthTypes[remainingDepth - 1]);

				// Valid sets of argument types.
				final List<Class<?>[]> valid = new ArrayList<Class<?>[]>();

				// Test each possible set of arguments.
				for (final Class<?>[] argTypes: argTypeSets) {
					final Class<?> type = n.getReturnType(argTypes);

					if (type != null) {
						// If the return type is a subtype of one that is
						// needed.
						for (final Class<?> returnType: returnTypes) {
							if (returnType.isAssignableFrom(type)) {
								valid.add(argTypes);
								break;
							}
						}
					}
				}

				// If there are any possible valid arguments.
				if (valid.size() > 0) {
					BigInteger totalChildVarieties = BigInteger.ONE;
					for (int i = 0; i < n.getArity(); i++) {
						// Construct a list of the valid argument types for this
						// child.
						returnTypes = new ArrayList<Class<?>>();
						for (int j = 0; j < valid.size(); j++) {
							returnTypes.add(valid.get(j)[i]);
						}

						// Find the number of varieties for the subtree rooted
						// at this child.
						final BigInteger childVarieties = noVarieties(remainingDepth - 1, returnTypes);

						// Multiply to get the number of varieties amongst the
						// children.
						totalChildVarieties = totalChildVarieties.multiply(childVarieties);
					}
					varieties = varieties.add(totalChildVarieties);
				}

				if (varieties.compareTo(target) >= 0) {
					return true;
				}
			}
		}

		return false;
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
		if (event.isKindOf(RANDOM_SEQUENCE, SIZE, SYNTAX, RETURN_TYPE, MAXIMUM_INITIAL_DEPTH, MAXIMUM_DEPTH)) {
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
	public void setSyntax(final Node[] syntax) {
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
	 * @return the maximum depth the program trees constructed should be.
	 */
	public int getMaxDepth() {
		return maxDepth;
	}

	/**
	 * Sets the depth that the program trees this initialiser constructs should
	 * be.
	 * 
	 * @param maxDepth the maximum depth of all new program trees.
	 */
	public void setMaxDepth(final int maxDepth) {
		if (maxDepth > this.maxDepth) {
			// Types possibilities table needs updating.
			// TODO Actually the whole table doesn't need updating, just
			// extending to new depth.
			validDepthTypes = null;
		}

		this.maxDepth = maxDepth;
	}
}
