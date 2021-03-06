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
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.epochx.Config;
import org.epochx.Individual;
import org.epochx.Population;

/**
 * A <code>GraphGeneration</code> stores a collection of
 * <code>GraphVertex</code> which belong to a same generation.
 */
public class GraphGeneration implements Serializable, Iterable<GraphVertex> {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = -735357460834534539L;

	/**
	 * The generation number.
	 */
	private final int generation;

	/**
	 * The list of vertex.
	 */
	private final ArrayList<GraphVertex> vertices;

	/**
	 * Constructs a <code>GraphGeneration</code>.
	 * 
	 * @param generation
	 */
	public GraphGeneration(int generation) {
		this.generation = generation;

		try {
			this.vertices = new ArrayList<GraphVertex>(Config.getInstance().get(Population.SIZE));
		} catch (NullPointerException e) {
			throw new NullPointerException("The Config seems not set.");
		}
	}

	/**
	 * Returns the generation number.
	 * 
	 * @return the generation number.
	 */
	public int getGeneration() {
		return generation;
	}

	/**
	 * Returns the vertices count.
	 * 
	 * @return the vertices count.
	 */
	public int getVerticesCount() {
		synchronized (vertices) {
			return vertices.size();
		}
	}

	/**
	 * Returns the array of this generation's vertices.
	 * 
	 * @return the array of this generation's vertices.
	 */
	public GraphVertex[] getVertices() {
		synchronized (vertices) {
			GraphVertex[] res = new GraphVertex[vertices.size()];
			vertices.toArray(res);
			return res;
		}
	}

	/**
	 * The <code>Iterable</code> implemented method ; Returns an
	 * <code>Iterator</code> of this generation's vertices.
	 * 
	 * @return the iterator of this generation's vertices.
	 */
	public Iterator<GraphVertex> iterator() {
		return vertices.iterator();
	}

	/**
	 * Returns the <code>GraphVertex</code> at the specified position in the
	 * vertices list.
	 * 
	 * @param index index of the <code>GraphVertex</code> to return.
	 * @return the <code>GraphVertex</code> at the specified position.
	 * 
	 * @throws IndexOutOfBoundsException if the index is out of range.
	 */
	public GraphVertex getGraphVertexAt(int index) throws IndexOutOfBoundsException {
		GraphVertex vertex;
		synchronized (vertices) {
			vertex = vertices.get(index);
		}
		return vertex;
	}

	/**
	 * Returns the <code>GraphVertex</code> corresponding to the specified
	 * <code>Individual</code> in the vertices list.
	 * 
	 * @param ind the <code>Individual</code> whose node is to be returned.
	 * @return the <code>GraphVertex</code> corresponding to the specified
	 *         <code>Individual</code> in the vertices list..
	 * @throws <code>NoSuchElementException</code> if the
	 *         <code>Individual</code> is not in the list.
	 */
	public GraphVertex getGraphVertex(Individual ind) throws NoSuchElementException {
		int i = indexOf(ind);
		if (i < 0) {
			throw new NoSuchElementException("No such individual in the list");
		}
		GraphVertex vertex;
		synchronized (vertices) {
			vertex = vertices.get(i);
		}
		return vertex;
	}

	/**
	 * Returns an array of <code>GraphVertex</code> corresponding to the
	 * specified <code>Individual</code> array in the vertices list.
	 * 
	 * @param individuals the <code>Individual</code> array whose vertices is to
	 *        be returned.
	 * @return the <code>GraphVertex</code> array.
	 * @throws <code>NoSuchElementException</code> if one of the
	 *         <code>Individuals</code> is not in the list.
	 */
	public GraphVertex[] getGraphVertices(Individual[] individuals) throws NoSuchElementException {
		GraphVertex[] graphVertices = new GraphVertex[individuals.length];
		try {
			for (int i = 0; i < individuals.length; i++) {
				graphVertices[i] = getGraphVertex(individuals[i]);
			}
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("One of the individual is not represented in the list");
		}
		return graphVertices;
	}

	/**
	 * Adds a <code>GraphVertex</code> to the vertices list, if it is not
	 * already present.
	 * 
	 * @param graphNode the <code>GraphVertex</code> to add.
	 * @return true if the vertex is add ; false otherwise.
	 */
	public boolean addGraphVertex(GraphVertex graphNode) {
		boolean b = false;
		synchronized (vertices) {
			if (!vertices.contains(graphNode)) {
				vertices.add(graphNode);
				b = true;
			}
		}
		return b;
	}

	/**
	 * Adds a <code>GraphVertex</code> corresponding to the specified
	 * <code>Individual</code> in the list of vertices.
	 * 
	 * @param ind the <code>Individual</code> to add.
	 * @return A new the <code>GraphVertex</code> if the specified
	 *         <code>Individual</code> is not already represented in
	 *         the vertices list ; Otherwise the <code>GraphVertex</code> of the
	 *         vertices list corresponding to the specified
	 *         <code>Individual</code>.
	 */
	public GraphVertex addIndividual(Individual ind) {
		GraphVertex vertex;
		if (!vertices.contains(ind)) {
			vertex = new GraphVertex(this, ind);
			addGraphVertex(vertex);
		} else {
			vertex = getGraphVertex(ind);
		}
		return vertex;
	}

	/**
	 * Adds an entire population to this generation.
	 * 
	 * @param p the population to add.
	 */
	public void addPopulation(Population p) {
		for (int i = 0; i < p.size(); i++) {
			addIndividual(p.get(i));
		}
	}

	/**
	 * Returns the index of the first occurrence of the given object in the
	 * vertices list ; returns -1 if not found.
	 * 
	 * <p>
	 * The given object can be an instance of <code>GraphVertex</code> or
	 * <code>Individual</code>. Throws a <code>ClassCastException</code>
	 * otherwise.
	 * </p>
	 * 
	 * @param obj the object whose index in the vertices list is to be found.
	 * @return the index of the first occurrence of the given object in the ;
	 *         returns -1 if not found.
	 * @throws ClassCastException if the given object is not a
	 *         <code>GraphVertex</code> or an <code>Individual</code>.
	 */
	public int indexOf(Object obj) throws ClassCastException {
		Individual individual;
		if (obj instanceof GraphVertex) {
			individual = ((GraphVertex) obj).getIndividual();
		} else if (obj instanceof Individual) {
			individual = (Individual) obj;
		} else {
			throw new ClassCastException(obj.getClass().getSimpleName());
		}
		int i = 0;
		synchronized (vertices) {
			for (GraphVertex vertex: vertices) {
				if (System.identityHashCode(vertex.getIndividual()) == System.identityHashCode(individual)) {
					return i;
				} else {
					i++;
				}

			}
			/*
			 * GraphVertex verticesTab[] = new GraphVertex[vertices.size()];
			 * vertices.toArray(verticesTab);
			 * 
			 * for (int i = 0; i < verticesTab.length; i++) {
			 * if (System.identityHashCode(verticesTab[i].getIndividual()) ==
			 * System.identityHashCode(individual))
			 * return i;
			 * }
			 */

		}
		return -1;
	}

	/**
	 * Returns an array containing the siblings of the specified vertex.
	 * 
	 * @return an array containing the siblings of the specified vertex, can be
	 *         an empty array.
	 */
	public GraphVertex[] getSiblings(GraphVertex vertex) {
		GraphVertex[] res;
		if(vertex.getOperator() == null || vertex.getOperatorEvent() == null) {
			res = new GraphVertex[1];
			res[0] = vertex;
		}
		else {

		LinkedList<GraphVertex> list = new LinkedList<GraphVertex>();

		synchronized (vertices) {
			for (GraphVertex v: vertices) {
				if (v.getOperatorEvent() == vertex.getOperatorEvent() || v == vertex) {
					list.add(v.getRank(), v);
				}
			}
		}

		res = new GraphVertex[list.size()];
		list.toArray(res);
		}
		
		return res;
	}

	/**
	 * Sorts the vertices list according to the order induced by the specified
	 * comparator.
	 * 
	 * @param comparator the comparator to determine the order of the list. A
	 *        null value indicates that the vertices' natural ordering should be
	 *        used.
	 */
	public void sortBy(Comparator<GraphVertex> comparator) {
		synchronized (vertices) {
			Collections.sort(vertices, comparator);
		}

	}
	
	@Override
	public String toString() {
		String res = getClass().getSimpleName();
		res+="@"+String.valueOf(System.identityHashCode(this));
		res+="["+generation;
		res+=","+getVerticesCount();
		res+="]";
		
		return res;
	}

}
