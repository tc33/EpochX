/*
 * Copyright 2007-2013
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
package org.epochx.cfg;

import org.apache.commons.lang.ObjectUtils;
import org.epochx.*;
import org.epochx.Config.ConfigKey;
import org.epochx.grammar.*;

/**
 * A <code>CFGIndividual</code> is a candidate solution which is represented by a parse tree 
 * 
 * <p>
 * Note: this class has a natural ordering that may be inconsistent with
 * <code>equals</code>.
 * 
 * @since 2.0
 */
public class CFGIndividual extends AbstractIndividual {

	private static final long serialVersionUID = -2995040590619051685L;

	/**
	 * The key for setting and retrieving the maximum depth for parse trees
	 */
	public static final ConfigKey<Integer> MAXIMUM_DEPTH = new ConfigKey<Integer>();
	
	// The individual's phenotype
	private NonTerminalSymbol parseTree;

	/**
	 * Constructs a CFG individual with an initial parse tree of <code>null</code>
	 */
	public CFGIndividual() {
		this(null);
	}
	
	/**
	 * Constructs a CFG individual represented by the given parse tree
	 * 
	 * @param parseTree the root node of a parse tree
	 */
	public CFGIndividual(NonTerminalSymbol parseTree) {
		this.parseTree = parseTree;
	}

	/**
	 * Returns the root <code>NonTerminalSymbol</code> of the parse tree, if it has 
	 * been set. Otherwise <code>null</code> is returned.
	 * 
	 * @return the root of the parse tree, or <code>null</code> if it has not been set
	 */
	public NonTerminalSymbol getParseTree() {
		return parseTree;
	}
	
	/**
	 * Sets the <code>NonTerminalSymbol</code> that is the root node of the parse
	 * tree that represents this individual.
	 * 
	 * @param parseTree the root of the parse tree that represents this individual
	 */
	public void setParseTree(NonTerminalSymbol parseTree) {
		this.parseTree = parseTree;
	}

	/**
	 * Creates and returns a clone of this individual. The parse tree and fitness are
	 * copied.
	 * 
	 * @return a copy of this <code>CFGIndividual</code> instance
	 */
	@Override
	public CFGIndividual clone() {
		CFGIndividual clone = (CFGIndividual) super.clone();

		if (parseTree == null) {
			clone.parseTree = null;
		} else {
			clone.parseTree = parseTree.clone();
		}

		return clone;
	}

	/**
	 * Returns a string representation of this program. This will be a string
	 * representation of the parse tree if it has been set otherwise <code>null</code>.
	 * 
	 * @return a string representation of this individual
	 */
	@Override
	public String toString() {
		if (parseTree != null) {
			return parseTree.toString();
		}
		
		return null;
	}

	/**
	 * Compares the given object to this instance for equality. Equivalence is
	 * defined as them both being instances of <code>CFGIndividual</code> and
	 * having equal parse trees, according to <code>getParseTree().equals(obj)</code>.
	 * 
	 * @return true if the object is an equivalent individual, false otherwise
	 */
	@Override
	public boolean equals(Object o) {
		CFGIndividual individual = (CFGIndividual) o;

		return ObjectUtils.equals(parseTree, individual.parseTree);
	}
	
	/**
	 * Compares this individual to another based on their fitness. It returns a
	 * negative integer, zero, or a positive integer as this instance represents
	 * the quality of an individual that is less fit, equally fit, or more fit
	 * than the specified object. The individuals do not need to be of the same
	 * object type, but must have non-null, comparable <code>Fitness</code> instances.
	 * 
	 * @param other an individual to compare against
	 * @return a negative integer, zero, or a positive integer as this object is
	 *         less fit than, equally fit as, or fitter than the specified
	 *         object
	 */
	@Override
	public int compareTo(Individual other) {
		return getFitness().compareTo(other.getFitness());
	}
}
