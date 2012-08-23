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

import java.util.Comparator;

import javax.swing.event.EventListenerList;

import org.epochx.Config;
import org.epochx.MaximumGenerations;
import org.epochx.Population;
import org.epochx.event.Event;
import org.epochx.event.EventManager;
import org.epochx.event.GenerationEvent.StartGeneration;
import org.epochx.event.Listener;
import org.epochx.event.ParentIndividualEvent;
import org.epochx.event.RunEvent.EndRun;

/**
 * A <code>GraphModel</code> provides a graph data model and methods to be
 * interrogated by a <code>GraphView</code>.
 * <p>
 * It takes care of the management of <code>GraphModelListeners</code> and
 * generates <code>GraphModelEvents</code>.
 * </p>
 */
public class GraphModel implements Listener<Event> {

	/**
	 * The <code>EventListenerList</code>.
	 */
	private EventListenerList listenerList;

	/**
	 * The table of generations
	 */
	GraphGeneration[] generations;

	/**
	 * The current generation number.
	 */
	private int generationCount;

	/**
	 * The number of individual by generation.
	 */
	private int verticesCount;

	/**
	 * Constructs a <code>GraphModel</code>.
	 */
	public GraphModel() {
		this.listenerList = new EventListenerList();

		int n = Config.getInstance().get(MaximumGenerations.MAXIMUM_GENERATIONS) + 1;

		this.generations = new GraphGeneration[n];
		this.generationCount = 0;
		this.verticesCount = Config.getInstance().get(Population.SIZE);

		generations[0] = new GraphGeneration(0);

		EventManager.getInstance().add(StartGeneration.class, this);
		EventManager.getInstance().add(EndRun.class, this);
		EventManager.getInstance().add(ParentIndividualEvent.class, this);

	}

	/**
	 * Returns the number of generation is the model.
	 * 
	 * @return the number of generation is the model.
	 */
	public int getGenerationCount() {
		return generationCount;
	}

	/**
	 * Returns the maximum number of vertices by generation.
	 * 
	 * @return the maximum number of vertices by generation.
	 */
	public int getVerticesCount() {
		return verticesCount;
	}

	/**
	 * Returns the current size of the specified generation.
	 * 
	 * @param generation the generation whose size is to return.
	 * @return the current size of the specified generation.
	 * @throws ArrayIndexOutOfBoundsException if if generation is out of bound.
	 */
	public int getGenerationSize(int generation) throws ArrayIndexOutOfBoundsException {
		if (generation < 0 || generation > getGenerationCount()) {
			throw new IndexOutOfBoundsException(generation + "\t" + getGenerationCount());
		}
		return generations[generation].getVerticesCount();
	}

	/**
	 * Returns the <code>GraphVertex</code> of the specified generation at the
	 * specified index.
	 * 
	 * @param generation the generation of the vertex to return.
	 * @param index the index of the vertex to return.
	 * @return the <code>GraphVertex</code> of the specified generation at the
	 *         specified index.
	 * @throws ArrayIndexOutOfBoundsException if generation or index is out of
	 *         bound.
	 */
	public GraphVertex getVertex(int generation, int index) throws ArrayIndexOutOfBoundsException {

		if (generation < 0 || generation > getGenerationCount()) {
			throw new ArrayIndexOutOfBoundsException(generation + "\t" + getGenerationCount());
		}
		if (index < 0 || index >= getGenerationSize(generation)) {
			throw new ArrayIndexOutOfBoundsException(index + "\t" + getGenerationSize(generation));
		}

		return generations[generation].getGraphVertexAt(index);
	}

	/**
	 * Returns the specified <code>GraphGeneration</code>.
	 * 
	 * @param generation the generation number to return.
	 * @return the specified <code>GraphGeneration</code>.
	 * @throws ArrayIndexOutOfBoundsException if generation is out of bound.
	 */
	public GraphGeneration getGeneration(int generation) throws ArrayIndexOutOfBoundsException {
		if (generation < 0 || generation > getGenerationCount()) {
			throw new ArrayIndexOutOfBoundsException(generation + "\t" + getGenerationCount());
		}
		return generations[generation];
	}

	/**
	 * Returns the current generation.
	 * 
	 * @return the current generation.
	 */
	public GraphGeneration currentGeneration() {
		return generations[generationCount];
	}

	/**
	 * Returns the last generation.
	 * 
	 * @return the last generation.
	 */
	public GraphGeneration lastGeneration() {
		return generations[generationCount - 1];
	}

	/**
	 * The {@link Listener} implemented method ; Refreshs the model according to
	 * the given event.
	 * 
	 * @param event the <code>Event</code>.
	 * @see org.epochx.event.Listener#onEvent(org.epochx.event.Event)
	 */
	public void onEvent(Event event) {
		if (event instanceof ParentIndividualEvent) {

			ParentIndividualEvent e = (ParentIndividualEvent) event;

			GraphVertex newVertex = currentGeneration().addIndividual(e.getChild());

			GraphVertex[] parentVertices = lastGeneration().getGraphVertices(e.getParents());

			newVertex.addParents(parentVertices);
			newVertex.setOperator(e.getOperator());

		} else if (event instanceof StartGeneration) {

			StartGeneration e = (StartGeneration) event;

			currentGeneration().addPopulation(e.getPopulation());

			fireGraphGenerationInserted();

			generationCount++;
			generations[generationCount] = new GraphGeneration(e.getGeneration());

		} else if (event instanceof EndRun) {

			currentGeneration().addPopulation(((EndRun) event).getPopulation());

			fireGraphGenerationInserted();
		}

	}

	/**
	 * Sorts all generations according to the order induced by the specified
	 * comparator.
	 * 
	 * @param comparator the comparator to determine the order.
	 * @throws NullPointerException if the comparator is null.
	 */
	public void sortAllBy(Comparator<GraphVertex> comparator) throws NullPointerException {
		if (comparator == null) {
			throw new NullPointerException("The comparator cannot be null");
		}
		for (int i = 0; i < generationCount; i++) {
			generations[i].sortBy(comparator);
		}
	}

	/**
	 * Sorts the specified generation according to the order induced by the
	 * specified comparator.
	 * 
	 * @param generation the generation to sort.
	 * @param comparator the comparator to determine the order.
	 * @throws ArrayIndexOutOfBoundsException if generation is out of bound.
	 * @throws NullPointerException if the comparator is null.
	 */
	public void sortBy(int generation, Comparator<GraphVertex> comparator) throws ArrayIndexOutOfBoundsException,
			NullPointerException {
		if (generation < 0 || generation > getGenerationCount()) {
			throw new ArrayIndexOutOfBoundsException(generation + "\t" + getGenerationCount());
		}
		if (comparator == null) {
			throw new NullPointerException("The comparator cannot be null");
		}
		generations[generation].sortBy(comparator);
	}
	
// 
// Listeners management
//
	/**
	 * Adds a <code>GraphModelListener</code> to the listener list.
	 * 
	 * @param l the listener to add.
	 */
	public void addGraphModelListener(GraphModelListener l) {
		listenerList.add(GraphModelListener.class, l);
	}

	/**
	 * Removes a <code>GraphModelListener</code> to the listener list.
	 * 
	 * @param l the listener to remove.
	 */
	public void removeGraphModelListener(GraphModelListener l) {
		listenerList.remove(GraphModelListener.class, l);
	}

	/**
	 * Notifies all listeners that a new generation have been inserted.
	 */
	public void fireGraphGenerationInserted() {
		GraphModelEvent e = new GraphModelEvent(this, generationCount, GraphModelEvent.INSERT);
		fireGraphModelEvent(e);
	}

	/**
	 * Forwards the given notification event to all
	 * <code>GraphModelListener</code> that registered themselves as listeners
	 * for this graph model.
	 * 
	 * @param e the event to be forwarded
	 */
	public void fireGraphModelEvent(GraphModelEvent e) {
		GraphModelListener[] listeners = listenerList.getListeners(GraphModelListener.class);
		for (int i = 0; i < listeners.length; i++) {
			listeners[i].graphChanged(e);
		}
	}

}
