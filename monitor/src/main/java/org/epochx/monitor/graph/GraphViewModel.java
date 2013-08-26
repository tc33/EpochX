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
import org.epochx.monitor.graph.GraphViewEvent.GraphViewProperty;

/**
 * A <code>GraphViewModel</code> provides a repository for a <code>Graph</code>,
 * storing all the fields related with the displaying.
 * 
 * <p>
 * <h3>Visualization Fields</h3>
 * <ul>
 * <li>The comparator which determine the vertices order.
 * <li>The diameter of the vertices.
 * <li>The gaps between vertices.
 * <li>The margins.
 * <li>The color of bonds.
 * <li>The highlight color.
 * <li>The selected vertex.
 * <li>The highlighted vertex.
 * </ul>
 * All those fields are accessible by getters and setters.
 * </p>
 * 
 * <p>
 * <h3>Repository and management of</h3>
 * <ul>
 * <li>The fitness set.
 * <li>The mapping <code>GraphVertex</code>/<code>GraphVertexModel</code>.
 * </ul>
 * </p>
 * 
 * <p>
 * <h3>Construction</h3>
 * The diameter of the vertices and gaps can be specified in constructors;
 * Otherwise, default values are applied.
 * </p>
 * 
 * <p>
 * <h3><code>GraphViewEvent</code>s / <code>GraphViewListener</code></h3>
 * Any modification of a visualization field should be reported to all
 * <code>GraphViewListener</code>.<br>
 * <br>
 * The setters automaticaly trigger a <code>GraphViewEvent</code>, indicating
 * what field has been changed, the old and the new value of this field.
 * </p>
 * 
 * <p>
 * <h3>Mouse Events Listener</h3>
 * This class implements <code>MouseListener</code> and
 * <code>MouseMotionListener</code>, to receive <code>MouseEvent</code> from the
 * <code>GraphView</code>, and cares to select and highlight vertices.
 * </p>
 * 
 * <p>
 * <h3>Concurrency</h3>
 * This class is <b>Thread-Safe</b> as the critical fields (the fitness set and
 * the map) are <b>synchronized</b> (by the set and the map respectively) and
 * <b>private</b>.
 * </p>
 * 
 * @see Graph
 * @see GraphView
 * @see GraphViewEvent
 * @see GraphViewListener
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
	 * The comparator which determine the vertices order.
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
	 * The bonds' color.
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
	private GraphVertex selectedVertex;

	/**
	 * The highlighted vertex.
	 */
	private GraphVertex highlightedVertex;

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

	////////////////////////////////////////////////////////////////////////////
	//             C O N S T R U C T O R S                                    //
	////////////////////////////////////////////////////////////////////////////

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
		this.highlightDepth = 10;
		this.fitnesses = new TreeSet<Fitness>();
		this.map = new HashMap<GraphVertex, GraphVertexModel>();
	}

	////////////////////////////////////////////////////////////////////////////
	//             G E T T E R S  &  S E T T E R S                            //
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the comparator which determines the order of the vertices.
	 * 
	 * @return the comparator which determines the order of the vertices.
	 * 
	 * @see FitnessComparator
	 * @see ParentComparator
	 * @see OperatorComparator
	 */
	public Comparator<GraphVertex> getComparator() {
		return comparator;
	}

	/**
	 * Sets the comparator which determines the order of the vertices.
	 * 
	 * @param comparator the comparator which determines the order of the
	 *        vertices.
	 */
	public void setComparator(Comparator<GraphVertex> comparator) {
		Comparator<GraphVertex> old = this.comparator;
		this.comparator = comparator;
		fireGraphViewEvent(new GraphViewEvent(this, GraphViewProperty.COMPARATOR, old, comparator));
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
	 * Sets the vertex diameter, and resets the vertices' position.
	 * 
	 * @param diameter the vertex diameter to set.
	 */
	public void setDiameter(int diameter) {
		int old = this.diameter;
		this.diameter = diameter;

		synchronized (map) {
			for (GraphVertexModel vertexModel: map.values()) {
				vertexModel.setDiameter(diameter);
				int x = margins.left + vertexModel.getIndex() * (diameter + hgap);
				int y = margins.top + vertexModel.getGeneration() * (diameter + vgap);
				vertexModel.setCentre(x, y);
			}
		}
		fireGraphViewEvent(new GraphViewEvent(this, GraphViewProperty.DIAMETER, new Integer(old), new Integer(diameter)));
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
		int old = this.hgap;
		this.hgap = hgap;
		fireGraphViewEvent(new GraphViewEvent(this, GraphViewProperty.HGAP, new Integer(old), new Integer(hgap)));
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
		int old = this.vgap;
		this.vgap = vgap;
		fireGraphViewEvent(new GraphViewEvent(this, GraphViewProperty.VGAP, new Integer(old), new Integer(vgap)));
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
		Insets old = margins;
		margins = m;
		fireGraphViewEvent(new GraphViewEvent(this, GraphViewProperty.MARGINS, old, m));
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
		boolean old = bondEnable;
		bondEnable = b;
		fireGraphViewEvent(new GraphViewEvent(this, GraphViewProperty.BOUND_ENABLE, new Boolean(old), new Boolean(b)));
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
		Color old = bondColor;
		bondColor = color;
		fireGraphViewEvent(new GraphViewEvent(this, GraphViewProperty.BOUND_COLOR, old, color));
	}

	////////////////////////////////////////////////////////////////////////////
	//             H I G H L I G H T I N G   M A N A G E M E N T              //
	////////////////////////////////////////////////////////////////////////////

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
		Color old = highlightColor;
		highlightColor = color;
		fireGraphViewEvent(new GraphViewEvent(this, GraphViewProperty.HIGHLIGHT_COLOR, old, color));
	}

	/**
	 * Returns the highlight depth which indicate how many ancestor generations
	 * are highlighted when a vertix is rollover.
	 * 
	 * @return the highlight depth.
	 */
	public int getHighlightDepth() {
		return highlightDepth;
	}

	/**
	 * Sets the highlight depth which indicate how many ancestor generations are
	 * highlighted when a vertix is rollover.
	 * 
	 * @param depth the highlight depth to set.
	 */
	public void setHighlightDepth(int depth) {
		int old = highlightDepth;
		highlightDepth = depth;
		fireGraphViewEvent(new GraphViewEvent(this, GraphViewProperty.HIGHLIGHT_DEPTH, new Integer(old), new Integer(
				depth)));
	}

	/**
	 * Returns the highlighted <code>GraphVertex</code>.
	 * 
	 * @return the highlighted <code>GraphVertex</code>.
	 */
	public GraphVertex getHighlightedVertex() {
		return highlightedVertex;
	}

	/**
	 * Sets the highlighted <code>GraphVertex</code>.
	 * 
	 * @param vertex the highlighted <code>GraphVertex</code> to set.
	 */
	public void setHighlightedVertex(GraphVertex vertex) {
		GraphVertex old = highlightedVertex;
		highlightedVertex = vertex;
		fireGraphViewEvent(new GraphViewEvent(this, GraphViewProperty.HIGHLIGHTED_VERTEX, old, highlightedVertex));
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
	 * Unhighlight all the vertices.
	 */
	public void unhighlightAll() {
		for (GraphVertexModel model: map.values()) {
			model.setHighlighted(false);
		}
		setHighlightedVertex(null);
	}

	////////////////////////////////////////////////////////////////////////////
	//             S E L E C T I N G   M A N A G E M E N T                    //
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the selected <code>GraphVertex</code>.
	 * 
	 * @return the selected <code>GraphVertex</code>.
	 */
	public GraphVertex getSelectedVertex() {
		return selectedVertex;
	}

	/**
	 * Sets the selected <code>GraphVertex</code>.
	 * 
	 * @param vertex the selected <code>GraphVertex</code> to set.
	 */
	public void setSelectedVertex(GraphVertex vertex) {
		GraphVertex old = selectedVertex;
		selectedVertex = vertex;
		fireGraphViewEvent(new GraphViewEvent(this, GraphViewProperty.SELECTED_VERTEX, old, selectedVertex));
	}

	/**
	 * Deselects all the vertices.
	 */
	public void deselectedAll() {
		for (GraphVertexModel model: map.values()) {
			model.setSelected(false);
		}
		setSelectedVertex(null);
	}

	////////////////////////////////////////////////////////////////////////////
	//             V E R T I C E S  &  M O D E L   M A P P I N G              //
	////////////////////////////////////////////////////////////////////////////

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
				// Create a new model and set it.

				vertexModel = new GraphVertexModel(vertex);

				vertexModel.setDiameter(diameter);

				addFitness(vertex.getFitness());
				vertexModel.setColor(getFitnessColor(vertex.getFitness()));

				int x = margins.left + vertexModel.getIndex() * (diameter + hgap);
				int y = margins.top + vertexModel.getGeneration() * (diameter + vgap);
				vertexModel.setCentre(x, y);

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

	////////////////////////////////////////////////////////////////////////////
	//             F I T N E S S   M A N A G E M E N T                        //
	////////////////////////////////////////////////////////////////////////////

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
			fireGraphViewEvent(new GraphViewEvent(this, GraphViewProperty.FITNESS));
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
	protected int indexOfFitness(Fitness fitness) {
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

	////////////////////////////////////////////////////////////////////////////
	//             R E - S E T T E R S                                        //
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Resets the index of all the <code>GraphVertexModel</code> according to
	 * the index of their associated vertex in their generation.
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
	 */
	public void resetIndices() {
		synchronized (map) {
			for (GraphVertexModel vertexModel: map.values()) {
				vertexModel.setIndex(vertexModel.getVertex().getIndex());
			}
		}
	}

	/**
	 * Resets the position of all the <code>GraphVertexModel</code> according to
	 * the position of their associated vertex in their generation.
	 */
	public void resetPostions() {
		synchronized (map) {
			for (GraphVertexModel vertexModel: map.values()) {
				int x = margins.left + vertexModel.getIndex() * (diameter + hgap);
				int y = margins.top + vertexModel.getGeneration() * (diameter + vgap);
				vertexModel.setCentre(x, y);
			}
		}
	}

	////////////////////////////////////////////////////////////////////////////
	//            L I S T E N E R   M A N A G E M E N T                       //
	////////////////////////////////////////////////////////////////////////////

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

	////////////////////////////////////////////////////////////////////////////
	//             M O U S E       L I S T E N E R S                          //
	////////////////////////////////////////////////////////////////////////////

	public void mouseClicked(MouseEvent e) {

		GraphVertex vertex = null;
		synchronized (map) {
			for (GraphVertexModel vertexModel: map.values()) {

				if (vertexModel.contains(e.getPoint())) {
					vertexModel.setSelected(true);
					vertex = vertexModel.getVertex();
				} else {
					vertexModel.setSelected(false);
				}
			}
		}

		setSelectedVertex(vertex);

	}

	public void mouseExited(MouseEvent e) {
		unhighlightAll();
	}

	public void mouseMoved(MouseEvent e) {

		GraphVertex vertex = null;
		synchronized (map) {
			for (GraphVertexModel vertexModel: map.values()) {

				if (vertexModel.contains(e.getPoint())) {
					vertex = vertexModel.getVertex();
				} else if (vertexModel.isHighlighted()) {
					vertexModel.setHighlighted(false);
				}
			}
		}

		if (vertex != null) {
			highlight(vertex, highlightDepth);
		}
		setHighlightedVertex(vertex);

	}

	public void mouseDragged(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

}
