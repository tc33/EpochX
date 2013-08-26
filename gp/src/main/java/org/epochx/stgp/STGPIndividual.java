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
package org.epochx.stgp;

import org.apache.commons.lang.ObjectUtils;
import org.epochx.*;
import org.epochx.Config.ConfigKey;
import org.epochx.epox.*;

/**
 * An <tt>STGPIndividual</tt> is a candidate solution which uses a strongly
 * typed tree representation to represent a computer program. This class
 * provides several convenient methods for obtaining information about the
 * program tree (such as {@link #length()} and {@link #depth()}), but more
 * information is available directly from the tree. Use {@link #getRoot()} to
 * get access to the tree.
 * 
 * <p>
 * Note: this class has a natural ordering that may be inconsistent with
 * <tt>equals</tt>.
 */
public class STGPIndividual implements Individual {

	private static final long serialVersionUID = -1428100428151029601L;

	/**
	 * The key for setting and retrieving the set of nodes that individuals are
	 * constructed from
	 */
	public static final ConfigKey<Node[]> SYNTAX = new ConfigKey<Node[]>();

	/**
	 * The key for setting and retrieving the required data-type for the root
	 * node
	 */
	public static final ConfigKey<Class<?>> RETURN_TYPE = new ConfigKey<Class<?>>();

	/**
	 * The key for setting and retrieving the maximum depth setting for program
	 * trees
	 */
	public static final ConfigKey<Integer> MAXIMUM_DEPTH = new ConfigKey<Integer>();

	private Fitness fitness;
	private Node root;

	/**
	 * Constructs an individual represented by a strongly typed tree, with
	 * a <tt>null</tt> root node
	 */
	public STGPIndividual() {
		this(null);
	}

	/**
	 * Constructs an individual represented by a strongly typed tree, where
	 * <tt>root</tt> is the root node of the tree
	 * 
	 * @param root the <tt>Node</tt> to set as the root
	 */
	public STGPIndividual(Node root) {
		this.root = root;
	}

	/**
	 * Evaluates the strongly typed program tree this individual represents and
	 * returns the value returned from the root
	 * 
	 * @return the result of evaluating the program tree
	 */
	public Object evaluate() {
		return root.evaluate();
	}

	/**
	 * Returns the <tt>Node</tt> that is set as the root of the program tree
	 * 
	 * @return the root node of the program tree.
	 */
	public Node getRoot() {
		return root;
	}

	/**
	 * Replaces the <tt>Node</tt> that is set as the root of the program tree
	 * 
	 * @param root the <tt>Node</tt> to set as the root
	 */
	public void setRoot(Node root) {
		this.root = root;
	}

	/**
	 * Returns the <i>n</i>th node in the program tree. The tree is traversed in
	 * pre-order (depth-first), indexed from 0 so that the root node is at
	 * index 0.
	 * 
	 * @param index index of the node to return
	 * @return the node at the specified index
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0
	 *         || index >= getLength())
	 */
	public Node getNode(int index) {
		if (index >= 0) {
			return root.getNode(index);
		} else {
			throw new IndexOutOfBoundsException("attempt to get node at negative index");
		}
	}

	/**
	 * Replaces the node at the specified position in the program tree with the
	 * specified node.
	 * 
	 * @param index index of the node to be replaced
	 * @param node node to be set at the specified position
	 * @return the node previously at this position
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0
	 *         || index >= getLength())
	 */
	public Node setNode(int index, Node node) {
		if (index > 0) {
			return root.setNode(index, node);
		} else if (index == 0) {
			Node old = getRoot();
			setRoot(node);
			return old;
		} else {
			// We rely on Node to throw exception if index >= length. It's too
			// expensive to check here.
			throw new IndexOutOfBoundsException("attempt to set node at negative index");
		}
	}

	/**
	 * Returns the maximum depth of the program tree. The depth of a tree is
	 * defined as the length of the path from the root to the deepest node in
	 * the tree. For a tree with just one node (the root), the depth is 0.
	 * 
	 * @return the maximum depth of the program tree
	 */
	public int depth() {
		return getRoot().depth();
	}

	/**
	 * Returns the total number of nodes in the program tree
	 * 
	 * @return the number of nodes in the program tree.
	 */
	public int length() {
		return getRoot().length();
	}

	/**
	 * Returns the data-type of the values returned by the program tree
	 * 
	 * @return the object <tt>Class</tt> of the values returned
	 */
	public Class<?> dataType() {
		return getRoot().dataType();
	}

	/**
	 * Creates and returns a copy of this program. The copied individual has a
	 * deep clone of the program tree.
	 * 
	 * @return a clone of this <tt>STGPIndividual</tt> instance
	 */
	@Override
	public STGPIndividual clone() {
		STGPIndividual clone = null;
		try {
			clone = (STGPIndividual) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		// Deep copy node tree
		if (root == null) {
			clone.root = null;
		} else {
			clone.root = root.clone();
		}

		return clone;
	}

	/**
	 * Returns a string representation of this individual
	 * 
	 * @return a string representation of this individual
	 */
	@Override
	public String toString() {
		if (root == null) {
			return null;
		} else {
			return root.toString();
		}
	}

	/**
	 * Compares the given object to this instance for equality. Equivalence is
	 * defined as them both being instances of <tt>STGPIndividual</tt> and
	 * having equal program trees according to <tt>getRoot().equals(obj)</tt>.
	 * 
	 * @param obj an object to be compared for equivalence.
	 * @return true if this individual is equivalent to the specified object and
	 *         false otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		boolean equal = false;
		if ((obj != null) && (obj instanceof STGPIndividual)) {
			STGPIndividual p = (STGPIndividual) obj;
			if (ObjectUtils.equals(root, p.root)) {
				equal = true;
			}
		}

		return equal;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Fitness getFitness() {
		return fitness;
	}

	/**
	 * Sets the fitness of this individual. The fitness is used as the basis of
	 * comparison between individuals.
	 * 
	 * @param fitness the fitness value to set
	 * @see #compareTo(Individual)
	 */
	public void setFitness(Fitness fitness) {
		this.fitness = fitness;
	}

	/**
	 * Compares this individual to another based on their fitness. It returns a
	 * negative integer, zero, or a positive integer as this instance represents
	 * the quality of an individual that is less fit, equally fit, or more fit
	 * than the specified object. The individuals do not need to be of the same
	 * object type, but must have comparable <tt>Fitness</tt> instances.
	 * 
	 * @param other an individual to compare against
	 * @return a negative integer, zero, or a positive integer as this object is
	 *         less fit than, equally fit as, or fitter than the specified
	 *         object
	 */
	@Override
	public int compareTo(Individual other) {
		return fitness.compareTo(other.getFitness());
	}
}
