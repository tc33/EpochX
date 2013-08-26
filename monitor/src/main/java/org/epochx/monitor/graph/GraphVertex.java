/*
 * Copyright 2007-2013
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
 * The latest version is available from: http://www.epochx.org
 */
package org.epochx.monitor.graph;

import java.io.Serializable;
import java.util.ArrayList;

import org.epochx.Fitness;
import org.epochx.Individual;
import org.epochx.Operator;
import org.epochx.event.OperatorEvent.EndOperator;

/**
 * A <code>GraphVertex</code> encloses an <code>Individual</code> to be
 * represented in a graph.
 * 
 * <p>
 * An instance can stores extra informations about the <code>Individual</code> :
 * <ul>
 * <li>The parent <code>GraphGeneration</code>.
 * <li>The <code>Operator</code> responsible for this individual.
 * <li>The <code>EndOperator</code> event responsible for this individual.
 * <li>The rank of this individual among its siblings.
 * <li>A list of parent <code>GraphVertex</code>.
 * <li>A list of children <code>GraphVertex</code>.
 * <p>
 */
public class GraphVertex implements Comparable<Object>, Serializable {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = 654188368821480724L;

	/**
	 * The parent <code>GraphGeneration</code>.
	 */
	private GraphGeneration graphGeneration;

	/**
	 * The <code>Individual</code>.
	 */
	private Individual individual;

	/**
	 * The <code>Operator</code>.
	 */
	private Operator operator;

	/**
	 * The Operator event.
	 */
	private EndOperator operatorEvent;

	/**
	 * The rank of this individual among the siblings.
	 */
	private int rank;

	/**
	 * The list of parents.
	 */
	private ArrayList<GraphVertex> parents;

	/**
	 * The list of children.
	 */
	private transient ArrayList<GraphVertex> children;

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
		this.operator = null;
		this.operatorEvent = null;
		this.rank = -1;
		this.parents = new ArrayList<GraphVertex>();
		this.children = new ArrayList<GraphVertex>();

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
	 * Delegate method ; Returns the individual's <code>Fitness</code>.
	 * 
	 * @return the individual's <code>Fitness</code>.
	 */
	public Fitness getFitness() {
		return individual.getFitness();
	}

	/**
	 * Returns the graphGeneration.
	 * 
	 * @return the graphGeneration.
	 */
	public GraphGeneration getGraphGeneration() {
		return graphGeneration;
	}

	/**
	 * Sets the graphGeneration.
	 * 
	 * @param graphGeneration the graphGeneration to set.
	 */
	public void setGraphGeneration(GraphGeneration graphGeneration) {
		this.graphGeneration = graphGeneration;
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
	 * Returns the parent <code>Operator</code>.
	 * 
	 * @return the parent <code>Operator</code>; Can be null.
	 */
	public Operator getOperator() {
		return operator;
	}

	/**
	 * Sets the parent <code>Operator</code>.
	 * 
	 * @param operator the operator to set.
	 */
	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	/**
	 * Returns the parent <code>EndOperator</code> event.
	 * 
	 * @return the parent <code>EndOperator</code> event; Can be null.
	 */
	public EndOperator getOperatorEvent() {
		return operatorEvent;
	}

	/**
	 * Sets the parent <code>EndOperator</code> event.
	 * 
	 * @param operatorEvent the parent <code>EndOperator</code> event to set.
	 */
	public void setOperatorEvent(EndOperator operatorEvent) {
		this.operatorEvent = operatorEvent;
	}

	/**
	 * Returns the rank of this individual among the siblings.
	 * 
	 * @return the rank of this individual among the siblings.
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * Sets the rank of this individual among the siblings.
	 * 
	 * @param rank the rank to set.
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

	/**
	 * Returns the array of this vertex's parents.
	 * 
	 * @return the array of this vertex's parents.
	 */
	public GraphVertex[] getParents() {
		GraphVertex[] vertices = null;
		if (parents != null) {
			synchronized (parents) {
				vertices = new GraphVertex[parents.size()];
				parents.toArray(vertices);
			}
		}
		return vertices;
	}

	/**
	 * Adds vertices to the list of parents.
	 * 
	 * @param parentVertices vertices to add.
	 */
	public void addParents(GraphVertex ... parentVertices) {
		if (parents == null) {
			parents = new ArrayList<GraphVertex>();
		}
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
		if (parents != null) {
			synchronized (parents) {
				this.parents.clear();
			}
		} else {
			parents = new ArrayList<GraphVertex>();
		}
	}

	/**
	 * Returns the array of this vertex's children.
	 * 
	 * @return the array of this vertex's children.
	 */
	public GraphVertex[] getChildren() {
		GraphVertex[] vertices = null;
		if (children != null) {
			synchronized (children) {
				vertices = new GraphVertex[children.size()];
				children.toArray(vertices);
			}
		}
		return vertices;
	}

	/**
	 * Adds vertices to the list of children.
	 * 
	 * @param childrenVertices vertices to add.
	 */
	public void addChildren(GraphVertex ... childrenVertices) {
		if (children == null) {
			children = new ArrayList<GraphVertex>();
		}
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
		if (children != null) {
			synchronized (children) {
				this.children.clear();
			}
		} else {
			children = new ArrayList<GraphVertex>();
		}
	}

	/**
	 * Returns the siblings of this vertex among its parent generation.
	 * 
	 * @return the siblings of this vertex among its parent generation.
	 */
	public GraphVertex[] getSiblings() {
		return graphGeneration.getSiblings(this);
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
	 * Returns the genitor's vertex. The genitor is the individual from which
	 * this individual has been cloned before being modified.
	 * 
	 * @return the genitor of this vertex if it can be determined; null,
	 *         otherwise.
	 */
	public GraphVertex genitor() {
		GraphVertex res = null;

		if (operatorEvent != null && operator != null) {

			// Case 1 : only one parent.
			if (operator.inputSize() == 1 && parents.size() > 0) {
				res = parents.get(0);
			}

			// Case 2 : two parents.
			if (operator.inputSize() == 2 && 0 <= rank && rank < getParents().length) {
				res = parents.get(rank);
			}
		}
		return res;
	}

	/**
	 * Returns the index of the point from the genitor which has been
	 * mutated.The point among the points array of the <code>EndOperator</code>
	 * event corresponding to the rank of this vertex.
	 * 
	 * @return the index of the point from the genitor which has been
	 *         mutated if the <code>EndOperator</code> event has been defined;
	 *         returns -1, otherwise.
	 */
	public int genitorPoint() {
		int res = -1;
		if (operatorEvent != null && 0 <= rank && rank < operatorEvent.getPoints().length) {
			res = operatorEvent.getPoints()[rank];
		}
		return res;
	}

	/**
	 * Returns the provider's vertex. The provider is the individual which has
	 * supplied genes to create this individual.
	 * <p>
	 * For example, during a <i>Crossover</i> operation, the provider is the
	 * second parent; For a <i>Mutation</i>, there is no provider as the mutated
	 * part has been newly created.
	 * </p>
	 * 
	 * @return the provider of this vertex if it exists; null, otherwise.
	 */
	public GraphVertex provider() {
		GraphVertex res = null;

		if (operator != null) {

			// Case 2 : two parents.
			if (operator.inputSize() == 2 && 0 <= rank && rank < getParents().length) {
				res = parents.get(1 - rank);
			}
		}
		return res;
	}

	/**
	 * Returns the index of the sub-tree which is the supplied gene to create
	 * this individual among the provider tree.
	 * 
	 * @return the provider point if the <code>EndOperator</code> event has
	 *         been defined; returns -1, otherwise.
	 */
	public int providerPoint() {
		int res = -1;
		if (operatorEvent != null && 0 <= rank && rank < operatorEvent.getPoints().length) {
			res = operatorEvent.getPoints()[operatorEvent.getPoints().length - 1 - rank];
		}
		return res;
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
	 * 
	 * @throws ClassCastException if the given object is not an instance of
	 *         <code>GraphVertex</code> or <code>Individual</code>.
	 */
	public int compareTo(Object obj) throws ClassCastException {

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

		boolean res = false;

		if (obj instanceof GraphVertex) {

			res = (System.identityHashCode(individual) == System.identityHashCode(((GraphVertex) obj).getIndividual()));

		} else if (obj instanceof Individual) {

			res = (System.identityHashCode(individual) == System.identityHashCode((Individual) obj));
		}

		return res;
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

		String res = getClass().getSimpleName();

		res += "[";

		res += "Generation#" + graphGeneration.getGeneration();
		res += "@" + String.valueOf(System.identityHashCode(graphGeneration));

		res += ",";
		res += "Individual@";
		res += individual != null ? String.valueOf(System.identityHashCode(individual)) + ":"
				+ individual.toString().substring(0, Math.min(10, individual.toString().length())) + "..." : "NULL";

		res += ",";
		res += "Operator@";
		res += operator != null ? String.valueOf(System.identityHashCode(operator)) + ":"
				+ operator.getClass().getSimpleName() : "NULL";

		res += ",";
		res += "Event@";
		res += operatorEvent != null ? String.valueOf(System.identityHashCode(operatorEvent)) : "NULL";

		if (operatorEvent != null) {
			res += ",";
			res += "Points:";
			for (int i: operatorEvent.getPoints()) {
				res += i;
				res += " ";
			}
		}

		res += "]";

		return res;
	}
}
