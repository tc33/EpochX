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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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
public class GraphViewModel implements MouseListener, MouseMotionListener {

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
	public final static int DEFAULT_DIAMETER = 16;

	/**
	 * The default horizontal gap between two nodes.
	 */
	public final static int DEFAULT_HGAP = 1;

	/**
	 * The default vertical gap between two nodes.
	 */
	public final static int DEFAULT_VGAP = 50;

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
	
	private Insets margins;

	/**
	 * The bound color.
	 */
	private Color boundColor;

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
	 * The map of <code>GraphVertex</code> mapped with their <code>GraphVertexModel</code>.
	 * <p>
	 * All accesses must be <b>synchronized</b> because of concurrency with the
	 * EDT.
	 * </p>
	 */
	private final HashMap<GraphVertex, GraphVertexModel> map;

	/**
	 * The <code>FitnessSet</code>.
	 * <p>
	 * All accesses must be <b>synchronized</b> because of concurrency with the
	 * EDT.
	 * </p>
	 */
	
	private final TreeSet<Fitness> fitnesses;


	/**
	 * Constructs a <code>GraphViewModel</code>.
	 * 
	 */
	public GraphViewModel() {
		this(FITNESS_COMPARATOR, DEFAULT_DIAMETER, DEFAULT_HGAP, DEFAULT_VGAP);
	}

	/**
	 * Constructs a <code>GraphViewModel</code>.
	 * 
	 * @param comparator
	 * @param diameter
	 * @param hgap
	 * @param vgap
	 * 
	 * @throws IllegalArgumentException if an argument is non-suitable.
	 */
	public GraphViewModel(Comparator<GraphVertex> comparator, int diameter, int hgap, int vgap) {
		
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
		this.comparator = comparator;
		this.diameter = diameter;
		this.hgap = hgap;
		this.vgap = vgap;
		this.setMargins(new Insets(diameter, diameter, diameter, diameter));
		this.boundColor = new Color(210, 210, 210);
		this.highlightColor = Color.GREEN;
		this.highlightDepth = 5;
		this.fitnesses = new TreeSet<Fitness>();
		this.map = new HashMap<GraphVertex, GraphVertexModel>();
	}

	/**
	 * @return the comparator
	 */
	public Comparator<GraphVertex> getComparator() {
		return comparator;
	}

	/**
	 * @param comparator the comparator to set
	 */
	public void setComparator(Comparator<GraphVertex> comparator) {
		Comparator<GraphVertex> old = this.comparator;
		this.comparator = comparator;
		fireGraphViewEvent(new GraphViewEvent(this, Property.COMPARATOR, old, comparator));	
	}

	/**
	 * @return the diameter
	 */
	public int getDiameter() {
		return diameter;
	}

	/**
	 * @param diameter the diameter to set
	 */
	public void setDiameter(int diameter) {
		if (this.diameter != diameter) {
			int old = this.diameter;
			this.diameter = diameter;
			synchronized (map) {
				for ( GraphVertexModel vertexModel : map.values() ) {
					vertexModel.setDiameter(diameter);
					vertexModel.setDefaultPosition();
				}
			}
			fireGraphViewEvent(new GraphViewEvent(this, Property.DIAMETER, new Integer(old), new Integer(diameter)));
		}
	}

	/**
	 * @return the hgap
	 */
	public int getHgap() {
		return hgap;
	}

	/**
	 * @param hgap the hgap to set
	 */
	public void setHgap(int hgap) {
		if (this.hgap != hgap) {
			int old = this.hgap;
			this.hgap = hgap;
			fireGraphViewEvent(new GraphViewEvent(this, Property.HGAP, new Integer(old), new Integer(hgap)));
		}
	}

	/**
	 * @return the vgap
	 */
	public int getVgap() {
		return vgap;
	}

	/**
	 * @param vgap the vgap to set
	 */
	public void setVgap(int vgap) {
		if (this.vgap != vgap) {
			int old = this.vgap;
			this.vgap = vgap;
			fireGraphViewEvent(new GraphViewEvent(this, Property.VGAP, new Integer(old), new Integer(vgap)));
		}
	}

	/**
	 * @return the margins
	 */
	public Insets getMargins() {
		return margins;
	}

	/**
	 * @param m the margins to set
	 */
	public void setMargins(Insets m) {
		if (this.margins != m) {
			Insets old = this.margins;
			this.margins = m;
			fireGraphViewEvent(new GraphViewEvent(this, Property.MARGINS, old, m));
		}
	}

	/**
	 * @return the boundColor
	 */
	public Color getBoundColor() {
		return boundColor;
	}

	/**
	 * @param boundColor the boundColor to set
	 */
	public void setBoundColor(Color boundColor) {
		if (this.boundColor != boundColor) {
			Color old = this.boundColor;
			this.boundColor = boundColor;
			fireGraphViewEvent(new GraphViewEvent(this, Property.BOUND_COLOR, old, boundColor));
		}
	}

	/**
	 * @return the highlightColor
	 */
	public Color getHighlightColor() {
		return highlightColor;
	}

	/**
	 * @param highlightColor the highlightColor to set
	 */
	public void setHighlightColor(Color highlightColor) {
		if (this.highlightColor != highlightColor) {
			Color old = this.highlightColor;
			this.highlightColor = highlightColor;
			fireGraphViewEvent(new GraphViewEvent(this, Property.HIGHLIGHT_COLOR, old, highlightColor));
		}
	}

	/**
	 * @return the highlightDepth
	 */
	public int getHighlightDepth() {
		return highlightDepth;
	}

	/**
	 * @param highlightDepth the highlightDepth to set
	 */
	public void setHighlightDepth(int highlightDepth) {
		if (this.highlightDepth != highlightDepth) {
			int old = this.highlightDepth;
			this.highlightDepth = highlightDepth;
			fireGraphViewEvent(new GraphViewEvent(this, Property.HIGHLIGHT_DEPTH, new Integer(old), new Integer(
					highlightDepth)));
		}
	}

	/**
	 * @return the selectedGraphVertex
	 */
	public GraphVertex getSelectedGraphVertex() {
		return selectedGraphVertex;
	}

	/**
	 * @param vertex the selectedGraphVertex to set
	 */
	public void setSelectedGraphVertex(GraphVertex vertex) {
		if (selectedGraphVertex != vertex) {
			GraphVertex old = selectedGraphVertex;
			selectedGraphVertex = vertex;
			fireGraphViewEvent(new GraphViewEvent(this, Property.SELECTED_VERTEX, old, selectedGraphVertex));
		}
	}

	/**
	 * @return the highlightedGraphVertex
	 */
	public GraphVertex getHighlightedGraphVertex() {
		return highlightedGraphVertex;
	}

	/**
	 * @param vertex the highlightedGraphVertex to set
	 */
	public void setHighlightedGraphVertex(GraphVertex vertex) {
		if (highlightedGraphVertex != vertex) {
			GraphVertex old = highlightedGraphVertex;
			highlightedGraphVertex = vertex;
			fireGraphViewEvent(new GraphViewEvent(this, Property.HIGHLIGHTED_VERTEX, old, vertex));
		}
	}
	

	public void setHighlighted(GraphVertex vertex, int depth) {
		GraphVertexModel vertexModel = getVertexModel(vertex);
		vertexModel.setHighlighted(true);
		if (depth != 0) {
			for (GraphVertex parent: vertex.getParents()) {
				setHighlighted(parent, depth - 1);
			}
		}
	}
	
	public GraphVertexModel getVertexModel(GraphVertex vertex) {
		GraphVertexModel vertexModel;
		synchronized (map) {
			if (map.containsKey(vertex)) {
				vertexModel = map.get(vertex);
			}
			else {
				vertexModel = new GraphVertexModel(vertex, this);
				map.put(vertex, vertexModel);
			}
		}
		return vertexModel;
	}

	public boolean addVertexModel(GraphVertex vertex, GraphVertexModel vertexModel) {
		boolean b = false;
		synchronized (map) {
			if (!map.containsKey(vertex)) {
				map.put(vertex, vertexModel);
				b = true;
			}
		}
		return b;
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
		return new Color(i / n, 0, (n - i) / n);
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

	

	public void addGraphViewListener(GraphViewListener l) {
		listenerList.add(GraphViewListener.class, l);
	}

	public void removeGraphViewListener(GraphViewListener l) {
		listenerList.remove(GraphViewListener.class, l);
	}

	public void fireGraphViewEvent(GraphViewEvent e) {

		GraphViewListener[] listeners = listenerList.getListeners(GraphViewListener.class);

		for (int i = 0; i < listeners.length; i++) {
			listeners[i].viewChanged(e);
		}
	}
	
	public void resetPostions() {
		synchronized (map) {
			for ( GraphVertexModel vertexModel : map.values() ) {
				vertexModel.setDefaultPosition();
			}
		}
	}

//
// Mouse listener methods
//
	public void mouseClicked(MouseEvent e) {
		GraphVertex selectedVertex = null;
		synchronized (map) {
			for (GraphVertexModel vertexModel: map.values()) {

				if (vertexModel.contains(e.getPoint())) {
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

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseDragged(MouseEvent e) {
		System.out.println(e);

	}

	public void mouseMoved(MouseEvent e) {

		GraphVertex selectedVertex = null;
		synchronized (map) {
			for (GraphVertexModel vertexModel: map.values()) {

				if (vertexModel.contains(e.getPoint())) {
					selectedVertex = vertexModel.getVertex();
				} else if (vertexModel.isHighlighted()) {
					vertexModel.setHighlighted(false);
				}
			}
		}

		if (selectedVertex != null) {
			setHighlighted(selectedVertex, highlightDepth);
		}
		setHighlightedGraphVertex(selectedVertex);
	}


}
