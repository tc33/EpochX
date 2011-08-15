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
package org.epochx.gp.representation;

import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.epochx.*;
import org.epochx.epox.*;

/**
 * A <code>GPIndividual</code> encapsulates an individual program within a
 * generation of a GP run.
 * 
 * <p>
 * Instances of GPIndividual can be requested to evaluate themselves,
 * which will trigger an evaluation of each <code>Node</code> and their child
 * nodes recursively down the tree. As well as the program tree itself, each
 * GPIndividual allows the retrieval of meta-data about the program.
 * 
 * <p>
 * Note: this class has a natural ordering that is inconsistent with equals.
 */
public class GPIndividual implements Individual {

	// The root node of the program tree.
	private Node rootNode;
	
	private Fitness fitness;

	public GPIndividual() {
		this(null);
	}

	/**
	 * Constructs a new program individual where <code>rootNode</code> is the
	 * top most node in the program, and which may have 0 or more child nodes.
	 * 
	 * @param rootNode the Node of the program tree which is the parent to
	 *        all other nodes.
	 * @param model the controlling model which provides the configuration
	 *        parameters for the run.
	 */
	public GPIndividual(final Node rootNode) {
		this.rootNode = rootNode;
	}

	/**
	 * Evaluates the candidate program by triggering a recursive evaluation of
	 * the node tree from the root.
	 * 
	 * @return The result of the evaluation of the program.
	 */
	public Object evaluate() {
		return rootNode.evaluate();
	}

	/**
	 * Returns the root node of the node tree held by the candidate program.
	 * 
	 * @return The root node of the node tree.
	 */
	public Node getRootNode() {
		return rootNode;
	}

	public void setRootNode(final Node rootNode) {
		this.rootNode = rootNode;
	}

	/**
	 * Returns the nth node in the <code>GPIndividual</code>. The program
	 * tree is traversed in pre-order (depth-first), indexed from 0 so that the
	 * root node is at node 0.
	 * 
	 * @param n The index of the node required. Indexes are from zero.
	 * @return The node at the specified index.
	 */
	public Node getNthNode(final int n) {
		if (n >= 0) {
			return rootNode.getNthNode(n);
		} else {
			throw new IndexOutOfBoundsException("attempt to get node at negative index");
		}
	}

	/**
	 * Replaces the node at the specified position in the GPIndividual
	 * with
	 * the specified node.
	 * 
	 * @param n index of the node to replace.
	 * @param newNode node to be set at the specified position.
	 */
	public void setNthNode(final int n, final Node newNode) {
		if (n > 0) {
			rootNode.setNthNode(n, newNode);
		} else if (n == 0) {
			setRootNode(newNode);
		} else {
			throw new IndexOutOfBoundsException("attempt to set node at negative index");
		}
	}

	/**
	 * Retrieves all the nodes in the program tree at a specified depth. If a
	 * negative depth is supplied then an <code>IndexOutOfBoundsException
	 * </code> will be thrown. However, no exception will be thrown if a depth
	 * greater than the program's maximum depth is used. In this case an empty
	 * list will be returned.
	 * 
	 * @param depth the specified depth of the nodes.
	 * @return a List of all the nodes at the specified depth.
	 */
	public List<Node> getNodesAtDepth(final int depth) {
		if (depth >= 0) {
			return rootNode.getNodesAtDepth(depth);
		} else {
			throw new IndexOutOfBoundsException("attempt to get nodes at negative depth");
		}
	}

	/**
	 * Returns a count of how many terminal nodes are in the program tree.
	 * 
	 * @return the number of terminal nodes in this program.
	 */
	public int getNoTerminals() {
		return getRootNode().getNoTerminals();
	}

	/**
	 * Returns a count of how many unique terminal nodes are in the program
	 * tree.
	 * 
	 * @return the number of unique terminal nodes in this program.
	 */
	public int getNoDistinctTerminals() {
		return getRootNode().getNoDistinctTerminals();
	}

	/**
	 * Returns a count of how many function nodes are in the program tree.
	 * 
	 * @return the number of function nodes in this program.
	 */
	public int getNoFunctions() {
		return getRootNode().getNoFunctions();
	}

	/**
	 * Returns a count of how many unique function nodes are in the program
	 * tree.
	 * 
	 * @return the number of unique function nodes in this program.
	 */
	public int getNoDistinctFunctions() {
		return getRootNode().getNoDistinctFunctions();
	}

	/**
	 * Returns the depth of deepest node in the program tree.
	 * 
	 * @return the depth of the program.
	 */
	public int getProgramDepth() {
		return getRootNode().getDepth();
	}
	
	/**
	 * Returns the number of nodes in the program tree.
	 * 
	 * @return the number of nodes in the program tree.
	 */
	public int getProgramLength() {
		return getRootNode().getLength();
	}

	/**
	 * Returns the type of output this program will produce.
	 * 
	 * @return a Class which represents the type of object this program will
	 *         output.
	 */
	public Class<?> getReturnType() {
		return getRootNode().getReturnType();
	}

	/**
	 * Creates and returns a copy of this program. The clone includes a deep
	 * copy of all the program nodes, so after calling this method none of the
	 * clones nodes will refer to the same instance.
	 * 
	 * @return a clone of this GPIndividual instance.
	 */
	@Override
	public GPIndividual clone() {
		GPIndividual clone = null;
		try {
			clone = (GPIndividual) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		// Deep copy node tree.
		if (rootNode == null) {
			clone.rootNode = null;
		} else {
			clone.rootNode = rootNode.clone();
		}

		return clone;
	}

	/**
	 * Return a string representation of the program node tree.
	 * 
	 * @return a string representation of this program instance.
	 */
	@Override
	public String toString() {
		if (rootNode == null) {
			return null;
		} else {
			return rootNode.toString();
		}
	}

	/**
	 * This equals method compares the given object to this GPIndividual
	 * to determine if they are equal. Equivalence is defined by their both
	 * being instances of GPIndividual and them having equal program node
	 * trees according to the equals methods of the root node.
	 * 
	 * @param obj an object to be compared for equivalence.
	 * @return true if this GPIndividual is the same as the obj argument;
	 *         false otherwise.
	 */
	@Override
	public boolean equals(final Object obj) {
		boolean equal = false;
		if ((obj != null) && (obj instanceof GPIndividual)) {
			final GPIndividual p = (GPIndividual) obj;
			if (ObjectUtils.equals(rootNode, p.rootNode)) {
				equal = true;
			}
		}

		return equal;
	}

	@Override
	public Fitness getFitness() {
		return fitness;
	}

	public void setFitness(Fitness fitness) {
		this.fitness = fitness;
	}
}
