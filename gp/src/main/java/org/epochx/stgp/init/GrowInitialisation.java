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
package org.epochx.stgp.init;

import static org.epochx.Population.SIZE;
import static org.epochx.RandomSequence.RANDOM_SEQUENCE;
import static org.epochx.stgp.STGPIndividual.*;

import java.math.BigInteger;
import java.util.*;

import org.epochx.*;
import org.epochx.epox.Node;
import org.epochx.event.*;
import org.epochx.stgp.STGPIndividual;

/**
 * Initialisation method which produces <tt>STGPIndividual</tt>s with program 
 * trees within a specified maximum depth. Program trees are constructed 
 * randomly from the nodes in the syntax, with each node's data-type constraints 
 * enforced.
 * 
 * <p>
 * See the {@link #setup()} method documentation for a list of configuration
 * parameters used to control this operator.
 * 
 * @see FullInitialisation
 * @see RampedHalfAndHalfInitialisation
 */
public class GrowInitialisation implements STGPInitialisation, Listener<ConfigEvent> {

	// Configuration settings
	private Node[] syntax; //TODO We don't really need to store this
	private RandomSequence random;
	private Class<?> returnType;
	private int populationSize;
	private int maxDepth;
	private boolean allowDuplicates;
	
	// The contents of the syntax split
	private List<Node> terminals;
	private List<Node> nonTerminals;

	// Lookup table of the return types valid at each depth level
	private Class<?>[][] dataTypesTable;

	/**
	 * Constructs a <tt>GrowInitialisation</tt> with control parameters 
	 * automatically loaded from the config
	 */
	public GrowInitialisation() {
		this(true);
	}
	
	/**
	 * Constructs a <tt>GrowInitialisation</tt> with control parameters 
	 * initially loaded from the config. If the <tt>autoConfig</tt> argument is 
	 * set to <tt>true</tt> then the configuration will be automatically updated
	 * when the config is modified.
	 * 
	 * @param autoConfig whether this operator should automatically update its
	 * configuration settings from the config
	 */
	public GrowInitialisation(boolean autoConfig) {
		setup();
		updateSyntax();
		
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
	 * <li>{@link Population#SIZE}
	 * <li>{@link STGPIndividual#SYNTAX}
	 * <li>{@link STGPIndividual#RETURN_TYPE}
	 * <li>{@link STGPIndividual#MAXIMUM_DEPTH}
	 * <li>{@link STGPInitialisation#MAXIMUM_INITIAL_DEPTH}
	 * <li>{@link InitialisationMethod#ALLOW_DUPLICATES} (default: <tt>true</tt>)
	 * </ul>
	 */
	protected void setup() {
		random = Config.getInstance().get(RANDOM_SEQUENCE);
		populationSize = Config.getInstance().get(SIZE);
		syntax = Config.getInstance().get(SYNTAX);
		returnType = Config.getInstance().get(RETURN_TYPE);
		allowDuplicates = Config.getInstance().get(ALLOW_DUPLICATES, true);
		
		Integer maxDepth = Config.getInstance().get(MAXIMUM_DEPTH);
		Integer maxInitialDepth = Config.getInstance().get(MAXIMUM_INITIAL_DEPTH);
		
		if (maxInitialDepth != null && (maxDepth == null || maxInitialDepth < maxDepth)) {
			this.maxDepth = maxInitialDepth;
		} else {
			this.maxDepth = (maxDepth==null) ? -1 : maxDepth;
		}
	}

	/*
	 * Splits the syntax in to terminals and nonTerminals
	 */
	private void updateSyntax() {
		terminals = new ArrayList<Node>();
		nonTerminals = new ArrayList<Node>();

		if (syntax != null) {
			for (Node n: syntax) {
				if (n.isTerminal()) {
					terminals.add(n);
				} else {
					nonTerminals.add(n);
				}
			}
		}
		
		// Lookup table will need recreating
		dataTypesTable = null;
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
		if (event.isKindOf(RANDOM_SEQUENCE, SIZE, SYNTAX, RETURN_TYPE, 
				MAXIMUM_INITIAL_DEPTH, MAXIMUM_DEPTH, ALLOW_DUPLICATES)) {
			setup();
		}

		// These will be expensive so only do them when we really have to
		if (event.isKindOf(RETURN_TYPE)) {
			dataTypesTable = null;
		}
		if (event.isKindOf(SYNTAX)) {
			updateSyntax();
		}
	}


	/**
	 * Creates a population of new <tt>STGPIndividuals</tt> from the
	 * <tt>Node</tt>s provided by the {@link STGPIndividual#SYNTAX} config
	 * parameter. Each individual is created by a call to the
	 * <tt>createIndividual</tt> method. The size of the population will be
	 * equal to the {@link Population#SIZE} config parameter. If the
	 * {@link InitialisationMethod#ALLOW_DUPLICATES} config parameter is set to
	 * <tt>false</tt> then the individuals in the population will be unique
	 * according to their <tt>equals</tt> methods. By default, duplicates are
	 * allowed.
	 * 
	 * @return a population of <code>STGPIndividual</code> objects
	 */
	@Override
	public Population createPopulation() {
		EventManager.getInstance().fire(new InitialisationEvent.StartInitialisation());
		
		Population population = new Population();
		
		for (int i = 0; i < populationSize; i++) {
			STGPIndividual individual;

			do {
				individual = createIndividual();
			} while (!allowDuplicates && population.contains(individual));

			population.add(individual);
		}
		
		EventManager.getInstance().fire(new InitialisationEvent.EndInitialisation(
				population));

		return population;
	}

	/**
	 * Constructs a new <tt>STGPIndividual</tt> instance with a grown program
	 * tree composed of nodes provided by the {@link STGPIndividual#SYNTAX}
	 * config parameter. Each node in the tree is randomly chosen from those
	 * nodes with a valid data-type. If the maximum depth has not been reached
	 * then the node is selected from all valid terminals or non-terminals and 
	 * at one less than the maximum depth only the terminals are chosen from.
	 * 
	 * @return a new individual with a full program tree
	 */
	@Override
	public STGPIndividual createIndividual() {
		return new STGPIndividual(createTree());
	}

	/**
	 * Creates a program tree within the maximum depth as specifed by the 
	 * <tt>getMaximumDepth</tt> method. The nodes in the tree are randomly 
	 * chosen from those nodes in the syntax with a data-type that matches the 
	 * requirements of their parent (or the problem for the root node).
	 * 
	 * @return the root node of the generated program tree
	 */
	public Node createTree() {
		if (random == null) {
			throw new IllegalStateException("No random number generator has been set");
		} else if (returnType == null) {
			throw new IllegalStateException("No return type has been set");
		} else if (maxDepth < 0) {
			throw new IllegalStateException("Depth must be 0 or greater");
		} else if (terminals.isEmpty()) {
			throw new IllegalStateException("Syntax must include nodes with arity of 0");
		}

		if (dataTypesTable == null) {
			updateDataTypesTable();
		}

		// TODO Validate that syntax contains node that matches return type

		return createTree(returnType, 0);
	}

	/*
	 * Helper method for the createTree method. Recursively fills the children
	 * of a node, to construct a tree down with a maximum depth
	 */
	private Node createTree(Class<?> requiredType, int currentDepth) {
		// Choose a node with correct data-type and obtainable arg types
		List<Node> validNodes = listValidNodes(maxDepth - currentDepth, requiredType);
		int randomIndex = random.nextInt(validNodes.size());
		Node root = validNodes.get(randomIndex).newInstance();
		int arity = root.getArity();

		if (arity > 0) {
			// Construct list of arg sets that produce the right return type
			// TODO Surely we can cut down the number of calls to this?!
			Class<?>[][] argTypeSets = dataTypeCombinations(arity, dataTypesTable[maxDepth - currentDepth]);
			List<Class<?>[]> validArgTypeSets = new ArrayList<Class<?>[]>();
			for (Class<?>[] argTypes: argTypeSets) {
				Class<?> type = root.dataType(argTypes);
				if ((type != null) && requiredType.isAssignableFrom(type)) {
					validArgTypeSets.add(argTypes);
				}
			}

			if (validArgTypeSets.isEmpty()) {
				throw new IllegalStateException("Syntax is unable to create full node trees of given depth.");
			}

			Class<?>[] argTypes = validArgTypeSets.get(random.nextInt(validArgTypeSets.size()));

			for (int i = 0; i < arity; i++) {
				root.setChild(i, createTree(argTypes[i], currentDepth + 1));
			}
		}

		return root;
	}

	/*
	 * Returns a list of nodes that are considered valid according to the
	 * data-types lookup table, given the available depth and required
	 * data-type.
	 * 
	 * We could make this protected to be overridden, but it only really makes
	 * sense to do so if we allow the update of the data-types table to be
	 * overridden too.
	 */
	private List<Node> listValidNodes(int remainingDepth, Class<?> requiredType) {
		List<Node> validNodes = new ArrayList<Node>();
		for (Node n: terminals) {
			if (n.dataType().isAssignableFrom(requiredType)) {
				validNodes.add(n);
			}
		}

		if (remainingDepth > 0) {
			for (Node n: nonTerminals) {
				Class<?>[][] argTypeSets = dataTypeCombinations(n.getArity(), dataTypesTable[remainingDepth - 1]);

				for (Class<?>[] argTypes: argTypeSets) {
					Class<?> type = n.dataType(argTypes);
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
	private void updateDataTypesTable() {
		dataTypesTable = new Class<?>[maxDepth + 1][];

		for (int i = 0; i <= maxDepth; i++) {
			final Set<Class<?>> types = new HashSet<Class<?>>();
			for (final Node n: terminals) {
				types.add(n.dataType());
			}

			if (i > 0) {
				// Also add any valid nonTerminals
				for (final Node n: nonTerminals) {
					final Class<?>[][] argTypeSets = dataTypeCombinations(n.getArity(), dataTypesTable[i - 1]);

					// Test each possible set of arguments
					for (final Class<?>[] argTypes: argTypeSets) {
						final Class<?> returnType = n.dataType(argTypes);
						if (returnType != null) {
							types.add(returnType);
						}
					}
				}
			}
			dataTypesTable[i] = types.toArray(new Class<?>[types.size()]);
		}
	}

	/*
	 * Generates all possible combinations of the given data-types, with arity
	 * positions
	 * 
	 * TODO We should only do this once at each depth for a particular arity
	 * TODO This is a duplicate of the same method in FullInitialisation
	 */
	private Class<?>[][] dataTypeCombinations(int arity, Class<?>[] dataTypes) {
		int noTypes = dataTypes.length;
		int noCombinations = (int) Math.pow(noTypes, arity);
		Class<?>[][] possibleTypes = new Class<?>[noCombinations][arity];

		for (int i = 0; i < arity; i++) {
			int period = (int) Math.pow(noTypes, i);

			for (int j = 0; j < noCombinations; j++) {
				int group = j / period;
				possibleTypes[j][i] = dataTypes[group % noTypes];
			}
		}

		return possibleTypes;
	}

	/**
	 * Returns a count of the number of different node trees that are possible
	 * with the available nodes and a maximum depth. This is effectively a 
	 * count of the size of the search-space.
	 * 
	 * @param remainingDepth the available depth in which the varieties are
	 * constrained
	 * @param returnType the required return type of the varieties
	 * @return a <tt>BigInteger</tt> which is a count of the number of varieties
	 */
	public BigInteger varieties(int remainingDepth, Class<?> returnType) {
		//TODO This method should probably be in a utilities class
		if (dataTypesTable == null) {
			updateDataTypesTable();
		}

		List<Class<?>> returnTypes = new ArrayList<Class<?>>();
		returnTypes.add(returnType);
		
		return varieties(remainingDepth, returnTypes);
	}

	/*
	 * Counts the number of different node trees that are possible with the 
	 * available nodes and a maximum depth. The varieties must have one of the
	 * specified return types.
	 */
	private BigInteger varieties(int remainingDepth, List<Class<?>> returnTypes) {
		// TODO This method can probably be simplified
		BigInteger varieties = BigInteger.ZERO;
		
		// Add all valid terminals
		for (Node n: terminals) {
			for (Class<?> returnType: returnTypes) {
				if (returnType.isAssignableFrom(n.dataType())) {
					varieties = varieties.add(BigInteger.ONE);
					break;
				}
			}
		}

		// Add any valid non-terminals
		if (remainingDepth > 0) {
			for (Node n: nonTerminals) {
				Class<?>[][] argTypeSets = dataTypeCombinations(n.getArity(), dataTypesTable[remainingDepth - 1]);

				// Construct a list of valid sets of argument types
				List<Class<?>[]> valid = new ArrayList<Class<?>[]>();
				for (Class<?>[] argTypes: argTypeSets) {
					Class<?> type = n.dataType(argTypes);

					if (type != null) {
						for (Class<?> returnType: returnTypes) {
							if (returnType.isAssignableFrom(type)) {
								valid.add(argTypes);
								break;
							}
						}
					}
				}

				if (valid.size() > 0) {
					BigInteger totalChildVarieties = BigInteger.ONE;
					for (int i = 0; i < n.getArity(); i++) {
						// Build list of the valid arg types for this child
						returnTypes = new ArrayList<Class<?>>();
						for (int j = 0; j < valid.size(); j++) {
							returnTypes.add(valid.get(j)[i]);
						}

						BigInteger childVarieties = varieties(remainingDepth - 1, returnTypes);

						totalChildVarieties = totalChildVarieties.multiply(childVarieties);
					}
					varieties = varieties.add(totalChildVarieties);
				}
			}
		}

		return varieties;
	}

	/**
	 * Tests whether the number of different node trees that are possible
	 * according to the <tt>varieties</tt> method is greater than or equal to
	 * a specified target. This method is more efficient than getting the 
	 * total number of varieties and doing a comparison, because it may return
	 * a result without completing the count. 
	 * 
	 * @param remainingDepth the available depth in which the varieties are
	 * constrained
	 * @param returnType the required return type of the varieties
	 * @param target the number of varieties that is considered sufficient
	 * @return <tt>true</tt> if the number of varieties is equal to or greater
	 * than <tt>target</tt> and <tt>false</tt> otherwise.
	 */
	public boolean sufficientVarieties(int remainingDepth, Class<?> returnType, BigInteger target) {
		//TODO This method should probably be in a utilities class
		if (dataTypesTable == null) {
			updateDataTypesTable();
		}

		List<Class<?>> returnTypes = new ArrayList<Class<?>>();
		returnTypes.add(returnType);
		return (varieties(remainingDepth, returnTypes, target) == null);
	}

	/*
	 * Counts the number of different node trees that are possible
	 * according to the varieties method is greater than or equal to a specified
	 * target. Returns null if the target is exceeded, otherwise it returns the
	 * number of varieties.
	 */
	private BigInteger varieties(int remainingDepth, List<Class<?>> returnTypes, BigInteger target) {
		// TODO This method can probably be simplified
		// Add all valid terminals
		BigInteger varieties = BigInteger.ZERO;
		for (Node n: terminals) {
			for (Class<?> returnType: returnTypes) {
				if (returnType.isAssignableFrom(n.dataType())) {
					varieties = varieties.add(BigInteger.ONE);
					break;
				}

				if (varieties.compareTo(target) >= 0) {
					return null;
				}
			}
		}

		// Add all valid non-terminals
		if (remainingDepth > 0) {
			for (Node n: nonTerminals) {
				Class<?>[][] argTypeSets = dataTypeCombinations(n.getArity(), dataTypesTable[remainingDepth - 1]);

				// Construct a list of valid sets of argument types
				List<Class<?>[]> valid = new ArrayList<Class<?>[]>();
				for (Class<?>[] argTypes: argTypeSets) {
					Class<?> type = n.dataType(argTypes);

					if (type != null) {
						for (Class<?> returnType: returnTypes) {
							if (returnType.isAssignableFrom(type)) {
								valid.add(argTypes);
								break;
							}
						}
					}
				}


				if (valid.size() > 0) {
					BigInteger totalChildVarieties = BigInteger.ONE;
					for (int i = 0; i < n.getArity(); i++) {
						// Build list of the valid arg types for this child
						returnTypes = new ArrayList<Class<?>>();
						for (int j = 0; j < valid.size(); j++) {
							returnTypes.add(valid.get(j)[i]);
						}

						//TODO This shouldn't be using varieties here - needs to call self
						BigInteger childVarieties = varieties(remainingDepth - 1, returnTypes, target);

						// Already exceeded limit?
						if (childVarieties == null) {
							return null;
						}
						
						totalChildVarieties = totalChildVarieties.multiply(childVarieties);
					}
					varieties = varieties.add(totalChildVarieties);
					
					if (varieties.compareTo(target) >= 0) {
						return null;
					}
				}
			}
		}

		return varieties;
	}

	/**
	 * Returns whether or not duplicates are currently allowed in generated
	 * populations
	 * 
	 * @return <tt>true</tt> if duplicates are currently allowed in populations
	 *         generated by the <tt>createPopulation</tt> method and
	 *         <tt>false</tt> otherwise
	 */
	public boolean isDuplicatesEnabled() {
		return allowDuplicates;
	}

	/**
	 * Sets whether duplicates should be allowed in populations that are
	 * generated. If automatic configuration is enabled then any value set here
	 * will be overwritten by the {@link InitialisationMethod#ALLOW_DUPLICATES}
	 * configuration setting on the next config event.
	 * 
	 * @param allowDuplicates whether duplicates should be allowed in
	 *        populations that are generated
	 */
	public void setDuplicatesEnabled(boolean allowDuplicates) {
		this.allowDuplicates = allowDuplicates;
	}

	/**
	 * Returns the array of nodes in the available syntax. Program trees are
	 * generated using the nodes in this array.
	 * 
	 * @return an array of the nodes in the syntax
	 */
	public Node[] getSyntax() {
		return syntax;
	}

	/**
	 * Sets the array of nodes to generate program trees from. If automatic
	 * configuration is enabled then any value set here will be overwritten by 
	 * the {@link STGPIndividual#SYNTAX} configuration setting on the next 
	 * config event.
	 * 
	 * @param syntax an array of nodes to generate new program trees from
	 */
	public void setSyntax(Node[] syntax) {
		this.syntax = syntax;

		updateSyntax();
	}

	/**
	 * Returns the required data-type of the return for program trees
	 * generated with this initialisation method
	 * 
	 * @return the return type of the program trees generated
	 */
	public Class<?> getReturnType() {
		return returnType;
	}

	/**
	 * Sets the required data-type of the program trees generated. If automatic
	 * configuration is enabled then any value set here will be overwritten by 
	 * the {@link STGPIndividual#RETURN_TYPE} configuration setting on the next 
	 * config event.
	 * 
	 * @param returnType the data-type of the generated programs
	 */
	public void setReturnType(final Class<?> returnType) {
		this.returnType = returnType;

		// Lookup table will need regenerating
		dataTypesTable = null;
	}

	/**
	 * Returns the number of individuals to be generated in a population created
	 * by the <tt>createPopulation</tt> method
	 * 
	 * @return the size of the populations generated
	 */
	public int getPopulationSize() {
		return populationSize;
	}

	/**
	 * Sets the number of individuals to be generated in a population created
	 * by the <tt>createPopulation</tt> method. If automatic configuration is 
	 * enabled thenAny value set here will be overwritten by the 
	 * {@link Population#SIZE} configuration setting on the next config event.
	 * 
	 * @param size the size of the populations generated
	 */
	public void setPopulationSize(int size) {
		this.populationSize = size;
	}

	/**
	 * Returns the maximum depth of the program trees generated with this 
	 * initialisation method
	 * 
	 * @return the maximum depth of the program trees constructed
	 */
	public int getMaximumDepth() {
		return maxDepth;
	}

	/**
	 * Sets the maximum depth of the program trees created by the
	 * <tt>createIndividual</tt> method. If automatic configuration is enabled 
	 * then any value set here will be overwritten by the 
	 * {@link STGPInitialisation#MAXIMUM_INITIAL_DEPTH} configuration setting on
	 * the next config event, or the {@link STGPIndividual#MAXIMUM_DEPTH} 
	 * setting if no initial maximum depth is set.
	 * 
	 * @param maxDepth the maximum depth of all program trees generated
	 */
	public void setMaximumDepth(int maxDepth) {
		if (dataTypesTable != null && maxDepth > dataTypesTable.length) {
			// Types possibilities table needs extending
			// TODO No need to regenerate the whole table, just extend it
			dataTypesTable = null;
		}

		this.maxDepth = maxDepth;
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
	 * enabled thenAny value set here will be overwritten by the 
	 * {@link RandomSequence#RANDOM_SEQUENCE} configuration setting on the next 
	 * config event.
	 * 
	 * @param random the random number generator to set
	 */
	public void setRandomSequence(RandomSequence random) {
		this.random = random;
	}
}