/*
 * Copyright 2007-2012
 * Lawrence Beadle, Tom Castle and Fernando Otero
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX
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
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.monitor.graph;

import java.util.ArrayList;

import org.epochx.Fitness;
import org.epochx.Individual;
import org.epochx.Operator;

/**
 * A <code>GraphVertex</code> encloses an <code>Individual</code> to be
 * represented in a graph.
 * 
 * <p>
 * An instance can stores extra informations about the <code>Individual</code> :
 * <ul>
 * <li>The parent <code>GraphGeneration</code>.
 * <li>The <code>Operator</code> responsible for this individual.
 * <li>A list of parent <code>GraphVertex</code>.
 * <li>a list of children <code>GraphVertex</code>.
 * <p>
 */
public class GraphVertex implements Comparable<Object> {

	/**
	 * The parent <code>GraphGeneration</code>.
	 */
	GraphGeneration graphGeneration;

	/**
	 * The <code>Individual</code>.
	 */
	Individual individual;

	/**
	 * The <code>Operator</code>.
	 */
	Operator operator;

	/**
	 * The list of parents.
	 */
	ArrayList<GraphVertex> parents;

	/**
	 * The list of children.
	 */
	ArrayList<GraphVertex> children;

	/**
	 * Constructs a <code>GraphVertex</code>.
	 * 
	 * @param graphGeneration the parent <code>GraphGeneration</code>.
	 * @param individual the <code>Individual</code> whose this
	 *        <code>GraphVertex</code> is the representation.
	 */
	public GraphVertex(GraphGeneration graphGeneration, Individual individual) {
		if (individual == null) {
			throw new NullPointerException("The individual cannot be null.");
		}

		this.individual = individual;
		this.graphGeneration = graphGeneration;
		parents = new ArrayList<GraphVertex>();
		children = new ArrayList<GraphVertex>();
	}

	/**
	 * Returns the invididual.
	 * 
	 * @return the individual.
	 */
	public Individual getIndividual() {
		return individual;
	}

	/**
	 * Sets the individual.
	 * 
	 * @param individual the individual to set
	 */
	public void setIndividual(Individual individual) {
		this.individual = individual;
	}

	/**
	 * Returns the parent <code>Operator</code> Can be null.
	 * 
	 * @return the parent <code>Operator</code>.
	 */
	public Operator getOperator() {
		return operator;
	}

	/**
	 * Sets the parent <code>Operator</code>.
	 * 
	 * @param operator the operator to set
	 */
	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	/**
	 * Returns the array of this vertex's parents.
	 * 
	 * @return the array of this vertex's parents.
	 */
	public GraphVertex[] getParents() {
		synchronized (parents) {
			GraphVertex[] vertices = new GraphVertex[parents.size()];
			parents.toArray(vertices);
			return vertices;
		}
	}

	/**
	 * Adds vertices to the list of parents.
	 * 
	 * @param parentVertices vertices to add.
	 */
	public void addParents(GraphVertex ... parentVertices) {
		synchronized (parents) {
			for (GraphVertex parentVertex: parentVertices) {
				parents.add(parentVertex);
				parentVertex.addChildren(this);
			}
		}

	}

	/**
	 * Clears the parent list.
	 */
	public void clearParents() {
		synchronized (parents) {
			this.parents.clear();
		}
	}

	/**
	 * Returns the array of this vertex's children.
	 * 
	 * @return the array of this vertex's children.
	 */
	public GraphVertex[] getChildren() {
		synchronized (children) {
			GraphVertex[] vertices = new GraphVertex[children.size()];
			children.toArray(vertices);
			return vertices;
		}
	}

	/**
	 * Adds vertices to the list of children.
	 * 
	 * @param childrenVertices vertices to add.
	 */
	public void addChildren(GraphVertex ... childrenVertices) {
		synchronized (children) {
			for (GraphVertex childVertex: childrenVertices) {
				children.add(childVertex);
			}
		}
	}

	/**
	 * Clears the children list.
	 */
	public void clearChildren() {
		synchronized (children) {
			this.children.clear();
		}
	}

	/**
	 * Delegate method ; Returns the individual's <code>Fitness</code>.
	 * 
	 * @return the individual's <code>Fitness</code>.
	 */
	public Fitness getFitness() {
		return individual.getFitness();
	}

	/**
	 * Delegate method ; Returns the generation number.
	 * 
	 * @return the generation number.
	 */
	public int getGenerationNo() {
		return graphGeneration.getGeneration();
	}

	/**
	 * Returns the index of this node in the generation list of vertices.
	 * 
	 * @return the index of this node in the generation list of vertices.
	 * @see GraphGeneration#vertices
	 * @see GraphGeneration#indexOf(Object)
	 */
	public int getIndex() {
		return graphGeneration.indexOf(this);
	}

	/**
	 * Returns the mean of indices of this vertex's parents.
	 * 
	 * @return the mean of indices of this vertex's parents.
	 */
	public double getParentMeanIndex() {
		double mean = 0;
		for (GraphVertex parent: parents) {
			mean += parent.getIndex();
		}
		mean /= parents.size();
		return mean;
	}

	/**
	 * Compares the given object with this <code>GraphVertex</code>'s
	 * <code>Fitness</code>.
	 * 
	 * <p>
	 * The given object can be an instance of <code>GraphVertex</code> or
	 * <code>Individual</code>. Throws a <code>ClassCastException</code>
	 * otherwise.
	 * </p>
	 * 
	 * @param obj the reference object with which to compare
	 * @return the value 0 if the argument's <code>Fitness</code> is equal to
	 *         this <code>GraphVertex</code>'s <code>Fitness</code>; a value
	 *         less than 0 if the argument's <code>Fitness</code> is than this
	 *         <code>GraphVertex</code>'s <code>Fitness</code>; and a value
	 *         greater than 0 if the argument's <code>Fitness</code> is less
	 *         than this <code>GraphVertex</code>'s <code>Fitness</code>.
	 */
	public int compareTo(Object obj) {

		if (obj instanceof GraphVertex) {

			return getFitness().compareTo(((GraphVertex) obj).getFitness());

		} else if (obj instanceof Individual) {

			return getFitness().compareTo(((Individual) obj).getFitness());

		} else {

			throw new ClassCastException(obj.getClass().getSimpleName());

		}
	}

	/**
	 * Overrides the <code>equals</code> method to compare the
	 * <code>Individual</code> equality.
	 * 
	 * <p>
	 * The given object can be an instance of <code>GraphVertex</code> or
	 * <code>Individual</code>; Returns false otherwise.
	 * </p>
	 * 
	 * @param obj the reference object with which to compare equality
	 * @return <code>true</code> if this node have the same
	 *         <code>Individual</code> as the object argumument; false
	 *         otherwise
	 */
	@Override
	public boolean equals(Object obj) {

		if (obj instanceof GraphVertex) {
			return individual.equals(((GraphVertex) obj).getIndividual());
		} else if (obj instanceof Individual) {
			return individual.equals((Individual) obj);
		} else {
			return false;
		}

	}

	@Override
	public GraphVertex clone() {
		try {
			GraphVertex clone = (GraphVertex) super.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}

	@Override
	public String toString() {
		return String.valueOf(System.identityHashCode(individual));
	}

}
