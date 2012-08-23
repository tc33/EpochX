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

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.epochx.Individual;
import org.epochx.Population;

/**
 * A <code>GraphGeneration</code> stores a collection of
 * <code>GraphVertex</code> which belong to a same generation.
 */
public class GraphGeneration {

	/**
	 * The generation number.
	 */
	private final int generation;

	/**
	 * The list of vertex.
	 */
	private final LinkedList<GraphVertex> vertices;

	/**
	 * Constructs a <code>GraphGeneration</code>.
	 * 
	 * @param generation
	 */
	public GraphGeneration(int generation) {
		this.generation = generation;
		this.vertices = new LinkedList<GraphVertex>();
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
	 * @throws <code>NoSuchElementException</code> if the <code>Individual</code>
	 *        is not in the list.
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
	 *        <code>Individuals</code> is not in the list.
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
	 *         vertices
	 *         list corresponding to the specified <code>Individual</code>.
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
	public int indexOf(Object obj) {
		Individual individual;
		if (obj instanceof GraphVertex) {
			individual = ((GraphVertex) obj).getIndividual();
		} else if (obj instanceof Individual) {
			individual = (Individual) obj;
		} else {
			throw new ClassCastException(obj.getClass().getSimpleName());
		}

		synchronized (vertices) {
			for (int i = 0; i < vertices.size(); i++) {
				if (System.identityHashCode(vertices.get(i).getIndividual()) == System.identityHashCode(individual))
					return i;
			}
		}
		return -1;
	}

	/**
	 * Sorts the vertices list according the specified comparator.
	 * 
	 * @param comparator the comparator used to sort the list ; can be null.
	 * 
	 * @return true if the sort have been done (i.e. the comparator is not
	 *         null).
	 */
	public boolean sortBy(Comparator<GraphVertex> comparator) {
		boolean b = false;
		if (comparator != null) {
			synchronized (vertices) {
				Collections.sort(vertices, comparator);
			}
			b = true;
		}
		return b;
	}

}
