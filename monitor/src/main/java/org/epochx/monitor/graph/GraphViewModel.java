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

import java.awt.Color;
import java.awt.Insets;
import java.awt.Point;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.event.EventListenerList;

import org.epochx.Fitness;
import org.epochx.monitor.graph.GraphViewEvent.Property;

/**
 * 
 */
public class GraphViewModel {

	/**
	 * The <code>EventListenerList</code>.
	 */
	private final EventListenerList listenerList;

	/**
	 * Convenience object defining a <code>FitnessComparator</code>.
	 */
	public static final Comparator<GraphVertex> FITNESS_COMPARATOR = new FitnessComparator();

	/**
	 * Convenience object defining a <code>ParentComparator</code>.
	 */
	public static final Comparator<GraphVertex> PARENT_COMPARATOR = new ParentComparator();

	/**
	 * Convenience object defining an <code>OperatorComparator</code>.
	 */
	public static final Comparator<GraphVertex> OPERATOR_COMPARATOR = new OperatorComparator();

	/**
	 * The default diameter of nodes.
	 */
	public final static int DEFAULT_DIAMETER = 12;

	/**
	 * The default horizontal gap between two nodes.
	 */
	public final static int DEFAULT_HGAP = 1;

	/**
	 * The default vertical gap between two nodes.
	 */
	public final static int DEFAULT_VGAP = 36;

	/**
	 * The comparator.
	 */
	private Comparator<GraphVertex> comparator;

	/**
	 * The diameter of nodes.
	 */
	private int diameter;

	/**
	 * The the horizontal gap between two column of node.
	 */
	private int hgap;

	/**
	 * The vertical gap between two row of node.
	 */
	private int vgap;

	/**
	 * The margins.
	 */
	private Insets margins;

	/**
	 * True if bonds are to be print.
	 */
	private boolean bondEnable;

	/**
	 * The bond color.
	 */
	private Color bondColor;

	/**
	 * The highlight color.
	 */
	private Color highlightColor;

	/**
	 * The highlight depth.
	 */
	private int highlightDepth;

	/**
	 * The selected vertex.
	 */
	private GraphVertex selectedGraphVertex;

	/**
	 * The highlighted vertex.
	 */
	private GraphVertex highlightedGraphVertex;

	/**
	 * The vertices mapped with their <code>GraphVertexModel</code>.
	 * <p>
	 * All accesses must be <b>synchronized</b> because of concurrency with the
	 * EDT.
	 * </p>
	 */
	private final HashMap<GraphVertex, GraphVertexModel> map;

	/**
	 * The set of fitnesses.
	 * <p>
	 * All accesses must be <b>synchronized</b> because of concurrency with the
	 * EDT.
	 * </p>
	 */
	private final TreeSet<Fitness> fitnesses;

	/**
	 * Constructs a <code>GraphViewModel</code> with default properties.
	 * <p>
	 * Default view properties :
	 * <ul>
	 * <li>comparator : null
	 * <li>diameter : {@link GraphViewModel#DEFAULT_DIAMETER}
	 * <li>gaps : {@link GraphViewModel#DEFAULT_HGAP} &
	 * {@link GraphViewModel#DEFAULT_VGAP}
	 * <li>margins : equal to the diameter.
	 * </ul>
	 * </p>
	 */
	public GraphViewModel() {
		this(DEFAULT_DIAMETER, DEFAULT_HGAP, DEFAULT_VGAP);
	}

	/**
	 * Constructs a <code>GraphViewModel</code> with specified diameter and
	 * proportionate gaps.
	 * 
	 * @param diameter the diameter of the vertices. Must be an <b>even</b>
	 *        number !
	 * 
	 * @throws IllegalArgumentException if the diameter is not an even number.
	 */
	public GraphViewModel(int diameter) {
		this(diameter, 3 * diameter, diameter / 8);
	}

	/**
	 * Constructs a <code>GraphViewModel</code> with specified diameter and
	 * gaps.
	 * 
	 * @param diameter the diameter of the vertices. Must be an <b>even</b>
	 *        number !
	 * @param hgap the horizontal gap.
	 * @param vgap the vertical gap.
	 * 
	 * @throws IllegalArgumentException if an argument is non-suitable.
	 */
	public GraphViewModel(int diameter, int hgap, int vgap) {

		if (diameter % 2 != 0) {
			throw new IllegalArgumentException("The diameter must be an even number.");
		}
		if (hgap < 0) {
			throw new IllegalArgumentException("The horizontal gap must be positive.");
		}
		if (vgap < 0) {
			throw new IllegalArgumentException("The vertical gap must be positive.");
		}

		this.listenerList = new EventListenerList();
		this.comparator = null;
		this.diameter = diameter;
		this.hgap = hgap;
		this.vgap = vgap;
		this.margins = new Insets(25, 25, 25, 25);
		this.bondEnable = false;
		this.bondColor = new Color(210, 210, 210);
		this.highlightColor = Color.GREEN;
		this.highlightDepth = 5;
		this.fitnesses = new TreeSet<Fitness>();
		this.map = new HashMap<GraphVertex, GraphVertexModel>();
	}

	/**
	 * Returns the comparator who determine the order of the vertices.
	 * 
	 * @return the comparator who determine the order of the vertices.
	 * 
	 * @see FitnessComparator
	 * @see ParentComparator
	 * @see OperatorComparator
	 */
	public Comparator<GraphVertex> getComparator() {
		return comparator;
	}

	/**
	 * Sets the comparator who determine the order of the vertices.
	 * 
	 * @param comparator the comparator who determine the order of the vertices.
	 */
	public void setComparator(Comparator<GraphVertex> comparator) {
		Comparator<GraphVertex> old = this.comparator;
		this.comparator = comparator;
		fireGraphViewEvent(new GraphViewEvent(this, Property.COMPARATOR, old, comparator));
	}

	/**
	 * Returns the vertex diameter.
	 * 
	 * @return the vertex diameter.
	 */
	public int getDiameter() {
		return diameter;
	}

	/**
	 * Sets the vertex diameter.
	 * 
	 * @param diameter the vertex diameter to set.
	 */
	public void setDiameter(int diameter) {
		if (this.diameter != diameter) {
			int old = this.diameter;
			this.diameter = diameter;

			synchronized (map) {
				for (GraphVertexModel vertexModel: map.values()) {
					vertexModel.setDiameter(diameter);
					vertexModel.resetDefaultPosition();
				}
			}
			fireGraphViewEvent(new GraphViewEvent(this, Property.DIAMETER, new Integer(old), new Integer(diameter)));
		}
	}

	/**
	 * Returns the horizontal gap between two vertices.
	 * 
	 * @return the horizontal hgap.
	 */
	public int getHgap() {
		return hgap;
	}

	/**
	 * Sets the horizontal gap between two vertices.
	 * 
	 * @param hgap the horizontal gap to set.
	 */
	public void setHgap(int hgap) {
		if (this.hgap != hgap) {
			int old = this.hgap;
			this.hgap = hgap;
			fireGraphViewEvent(new GraphViewEvent(this, Property.HGAP, new Integer(old), new Integer(hgap)));
		}
	}

	/**
	 * Returns the vertical gap between two vertices.
	 * 
	 * @return the vertical gap.
	 */
	public int getVgap() {
		return vgap;
	}

	/**
	 * Sets the vertical gap between two vertices.
	 * 
	 * @param vgap the vertical gap to set.
	 */
	public void setVgap(int vgap) {
		if (this.vgap != vgap) {
			int old = this.vgap;
			this.vgap = vgap;
			fireGraphViewEvent(new GraphViewEvent(this, Property.VGAP, new Integer(old), new Integer(vgap)));
		}
	}

	/**
	 * Returns the margins which surround the view.
	 * 
	 * @return the margins.
	 */
	public Insets getMargins() {
		return margins;
	}

	/**
	 * Sets the margins which surround the view.
	 * 
	 * @param margins the margins to set.
	 */
	public void setMargins(Insets m) {
		if (margins != m) {
			Insets old = margins;
			margins = m;
			fireGraphViewEvent(new GraphViewEvent(this, Property.MARGINS, old, m));
		}
	}

	/**
	 * Returns true if the bonds have to be print.
	 * 
	 * @return true if the bonds have to be print.
	 */
	public boolean isBondEnable() {
		return bondEnable;
	}

	/**
	 * Sets if the bond have to be printed.
	 * 
	 * @param b true if the bond have to be printed, otherwise false.
	 */
	public void setBondEnable(boolean b) {
		if (bondEnable != b) {
			boolean old = bondEnable;
			bondEnable = b;
			fireGraphViewEvent(new GraphViewEvent(this, Property.BOUND_ENABLE, new Boolean(old), new Boolean(b)));
		}
	}

	/**
	 * Returns the bond color.
	 * 
	 * @return the bond color.
	 */
	public Color getBondColor() {
		return bondColor;
	}

	/**
	 * Sets the bond color.
	 * 
	 * @param color the bond color to set.
	 */
	public void setBondColor(Color color) {
		if (bondColor != color) {
			Color old = bondColor;
			bondColor = color;
			fireGraphViewEvent(new GraphViewEvent(this, Property.BOUND_COLOR, old, color));
		}
	}

	/**
	 * Returns the highlight color.
	 * 
	 * @return the highlight color.
	 */
	public Color getHighlightColor() {
		return highlightColor;
	}

	/**
	 * Sets the highlight color.
	 * 
	 * @param color the highlight color to set.
	 */
	public void setHighlightColor(Color color) {
		if (highlightColor != color) {
			Color old = highlightColor;
			highlightColor = color;
			fireGraphViewEvent(new GraphViewEvent(this, Property.HIGHLIGHT_COLOR, old, color));
		}
	}

	/**
	 * Returns the highlight depth which indicate how many ancestor generation
	 * are highlighted when a vertix is rollover.
	 * 
	 * @return the highlight depth.
	 */
	public int getHighlightDepth() {
		return highlightDepth;
	}

	/**
	 * Sets the highlight depth which indicate how many ancestor generation are
	 * highlighted when a vertix is rollover.
	 * 
	 * @param depth the highlight depth to set.
	 */
	public void setHighlightDepth(int depth) {
		if (highlightDepth != depth) {
			int old = highlightDepth;
			highlightDepth = depth;
			fireGraphViewEvent(new GraphViewEvent(this, Property.HIGHLIGHT_DEPTH, new Integer(old), new Integer(depth)));
		}
	}

	/**
	 * Returns the selected <code>GraphVertex</code>.
	 * 
	 * @return the selected <code>GraphVertex</code>.
	 */
	public GraphVertex getSelectedGraphVertex() {
		return selectedGraphVertex;
	}

	/**
	 * Sets the selected <code>GraphVertex</code>.
	 * 
	 * @param vertex the selected <code>GraphVertex</code> to set.
	 */
	public void setSelectedGraphVertex(GraphVertex vertex) {
		if (selectedGraphVertex != vertex) {
			GraphVertex old = selectedGraphVertex;
			selectedGraphVertex = vertex;
			fireGraphViewEvent(new GraphViewEvent(this, Property.SELECTED_VERTEX, old, selectedGraphVertex));
		}
	}

	/**
	 * Returns the highlighted <code>GraphVertex</code>.
	 * 
	 * @return the highlighted <code>GraphVertex</code>.
	 */
	public GraphVertex getHighlightedGraphVertex() {
		return highlightedGraphVertex;
	}

	/**
	 * Sets the highlighted <code>GraphVertex</code>.
	 * 
	 * @param vertex the highlighted <code>GraphVertex</code> to set.
	 */
	public void setHighlightedGraphVertex(GraphVertex vertex) {
		if (highlightedGraphVertex != vertex) {
			GraphVertex old = highlightedGraphVertex;
			highlightedGraphVertex = vertex;
			fireGraphViewEvent(new GraphViewEvent(this, Property.HIGHLIGHTED_VERTEX, old, vertex));
		}
	}

	/**
	 * Highlight a <code>GraphVertex</code> and its ancestors according to the
	 * specified recursion depth.
	 * 
	 * @param vertex the vertex to highlight.
	 * @param depth the depth.
	 */
	public void highlight(GraphVertex vertex, int depth) {
		GraphVertexModel vertexModel = getVertexModel(vertex);
		vertexModel.setHighlighted(true);
		if (depth != 0 && vertex.getParents() != null) {
			for (GraphVertex parent: vertex.getParents()) {
				highlight(parent, depth - 1);
			}
		}
	}

	/**
	 * Returns the set of fitnesses.
	 * 
	 * @return the set of fitnesses.
	 */
	public TreeSet<Fitness> getFitnesses() {
		return fitnesses;
	}

	/**
	 * Returns the <code>GraphVertexModel</code> to which the specified
	 * <code>GraphVertex</code> is mapped, if it is contained. Creates, adds and
	 * returns a new model otherwise.
	 * 
	 * @param vertex the vertex whose model is to be found.
	 * @return the <code>GraphVertexModel</code> mapped with the specified
	 *         <code>GraphVertex</code>.
	 */
	public GraphVertexModel getVertexModel(GraphVertex vertex) {
		GraphVertexModel vertexModel;
		synchronized (map) {
			if (map.containsKey(vertex)) {
				vertexModel = map.get(vertex);
			} else {
				vertexModel = new GraphVertexModel(vertex, this);
				map.put(vertex, vertexModel);
			}
		}
		return vertexModel;
	}

	/**
	 * Associates the specified <code>GraphVertexModel</code> with the specified
	 * <code>GraphVertex</code> in this map. If the map previously contained a
	 * mapping for the vertex, the old model is replaced.
	 * 
	 * @param vertex the vertex key.
	 * @param vertexModel the vertex model to mapped with the vertex key.
	 * @return the previous value associated with key, or null if there was no
	 *         mapping for key.
	 */
	public GraphVertexModel addVertexModel(GraphVertex vertex, GraphVertexModel vertexModel) {
		synchronized (map) {
			return map.put(vertex, vertexModel);
		}
	}

	/**
	 * Computes and returns the <code>Color</code> corresponding to the rank of
	 * the specified <code>Fitness</code> in the <code>FitnessSet</code>.
	 * 
	 * @param fitness the <code>Fitness</code> whose color is to be computes.
	 * @return the <code>Color</code> corresponding to the rank of the specified
	 *         <code>Fitness</code> in the <code>FitnessSet</code>.
	 */
	public Color getFitnessColor(Fitness fitness) {
		float i;
		float n;
		synchronized (fitnesses) {
			i = indexOfFitness(fitness) * 1.f;
			n = fitnesses.size() * 1.f - 1.f;
		}
		float r = i / n;
		float b = 1 - r;
		return new Color(r, 0, b);
	}

	/**
	 * Adds a <code>Fitness</code> in the <code>FitnessSet</code>.
	 * 
	 * @param fitness the <code>Fitness</code> to be added.
	 */
	public void addFitness(Fitness fitness) {
		boolean added;
		synchronized (fitnesses) {
			added = fitnesses.add(fitness);
		}
		if (added) {
			resetColors();
			fireGraphViewEvent(new GraphViewEvent(this, Property.FITNESS));
		}
	}

	/**
	 * Returns the index of the first occurrence with the same
	 * <code>String</code> value than the given argument ; returns -1 if not
	 * found.
	 * 
	 * @param fitness the <code>Fitness</code> whose index in the fitness set is
	 *        to be found.
	 * @return the index of the first occurrence with the same
	 *         <code>String</code> value than the given argument ; returns -1 if
	 *         not found.
	 */
	public int indexOfFitness(Fitness fitness) {
		Fitness f;
		int i = 0;
		synchronized (fitnesses) {
			Iterator<Fitness> iterator = fitnesses.iterator();
			while (iterator.hasNext()) {
				f = iterator.next();
				if (f.toString().equals(fitness.toString()))
					return i;
				else
					i++;
			}
		}
		return -1;
	}

	/**
	 * Resets the index of all the <code>GraphVertexModel</code> according to
	 * the index of their associated vertex in their generation.
	 * 
	 * @see GraphVertexModel#resetDefaultIndex()
	 */
	public void resetColors() {
		synchronized (map) {
			for (GraphVertexModel vertexModel: map.values()) {
				vertexModel.setColor(getFitnessColor(vertexModel.getVertex().getFitness()));
			}
		}
	}

	/**
	 * Resets the index of all the <code>GraphVertexModel</code> according to
	 * the index of their associated vertex in their generation.
	 * 
	 * @see GraphVertexModel#resetDefaultIndex()
	 */
	public void resetIndices() {
		synchronized (map) {
			for (GraphVertexModel vertexModel: map.values()) {
				vertexModel.resetDefaultIndex();
			}
		}
	}

	/**
	 * Resets the position of all the <code>GraphVertexModel</code> according to
	 * the position of their associated vertex in their generation.
	 * 
	 * @see GraphVertexModel#resetDefaultPosition()
	 */
	public void resetPostions() {
		synchronized (map) {
			for (GraphVertexModel vertexModel: map.values()) {
				vertexModel.resetDefaultPosition();
			}
		}
	}

	/**
	 * Select the vertex at the specified point (only if it points on a
	 * vertex), all the other vertices will be non selected.
	 * <p>
	 * A <code>GraphViewEvent</code> might be fire if the selected vertex is
	 * different than the previous one.
	 * </p>
	 * 
	 * @param point the point.
	 */
	public void select(Point point) {
		GraphVertex selectedVertex = null;
		synchronized (map) {
			for (GraphVertexModel vertexModel: map.values()) {

				if (vertexModel.contains(point)) {
					selectedVertex = vertexModel.getVertex();
				} else if (vertexModel.isHighlighted()) {
					vertexModel.setHighlighted(false);
				}
			}
		}

		if (selectedVertex != null) {

		}
		setSelectedGraphVertex(selectedVertex);
	}

	/**
	 * Highlight the vertex at the specified point (only if it points on a
	 * vertex), all the other vertices will be non highlighted.
	 * <p>
	 * A <code>GraphViewEvent</code> might be fire if the highlighted vertex is
	 * different than the previous one.
	 * </p>
	 * 
	 * @param point the point.
	 */
	public void highlight(Point point) {
		GraphVertex selectedVertex = null;
		synchronized (map) {
			for (GraphVertexModel vertexModel: map.values()) {

				if (vertexModel.contains(point)) {
					selectedVertex = vertexModel.getVertex();
				} else if (vertexModel.isHighlighted()) {
					vertexModel.setHighlighted(false);
				}
			}
		}

		if (selectedVertex != null) {
			highlight(selectedVertex, highlightDepth);
		}
		setHighlightedGraphVertex(selectedVertex);
	}

	//
	// Listeners management //
	//

	/**
	 * Adds a <code>GraphViewListener</code> to the listener list.
	 * 
	 * @param l the listener to add.
	 */
	public void addGraphViewListener(GraphViewListener l) {
		listenerList.add(GraphViewListener.class, l);
	}

	/**
	 * Removes the specified <code>GraphViewListener</code> from the listener
	 * list.
	 * 
	 * @param l the listener to remove.
	 */
	public void removeGraphViewListener(GraphViewListener l) {
		listenerList.remove(GraphViewListener.class, l);
	}

	/**
	 * Forwards the given notification event to all
	 * <code>GraphViewListener</code> that registered themselves as listeners
	 * for this graph model.
	 * 
	 * @param e the event to be forwarded
	 */
	public void fireGraphViewEvent(GraphViewEvent e) {

		GraphViewListener[] listeners = listenerList.getListeners(GraphViewListener.class);

		for (int i = 0; i < listeners.length; i++) {
			listeners[i].viewChanged(e);
		}
	}

}
