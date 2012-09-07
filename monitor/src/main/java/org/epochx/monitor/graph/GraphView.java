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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.Scrollable;
import javax.swing.Timer;

import org.epochx.monitor.graph.GraphViewEvent.GraphViewProperty;

/**
 * A <code>GraphView</code>.
 */
public class GraphView extends Component implements GraphModelListener, GraphViewListener, Scrollable {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = 4461110799142363471L;

	/**
	 * The <code>GraphViewModel</code>/viewModel.
	 */
	private GraphViewModel viewModel;

	/**
	 * The <code>GraphModel</code>/model.
	 */
	private GraphModel model;

	/**
	 * The timer to refresh the view.
	 */
	private Timer timer;

	/**
	 * Constructs a <code>GraphView</code> with a null
	 * <code>GraphViewModel</code> and a null <code>GraphModel</code>.
	 * <p>
	 * Note that those models have to be set before the start of the simulation,
	 * otherwise, NullPointerException will be thrown.
	 * </p>
	 */
	public GraphView() {
		super();
		this.viewModel = null;
		this.model = null;
		this.timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resize();
			}
		});
		timer.setInitialDelay(1000);
		timer.start();
	}

	/**
	 * 
	 * 
	 * Constructs a <code>GraphView</code> with a specified view model and data
	 * model.
	 * 
	 * @param viewModel the <code>GraphViewModel</code>.
	 * @param model the <code>GraphModel</code>.
	 * 
	 * @throws IllegalArgumentException if null argument.
	 */
	public GraphView(GraphViewModel viewModel, GraphModel model) throws IllegalArgumentException {
		this();
		if (viewModel == null || model == null) {
			throw new IllegalArgumentException("Arguments cannot be null.");
		}

		this.viewModel = viewModel;
		this.model = model;

		model.addGraphModelListener(this);
		viewModel.addGraphViewListener(this);
	}

	/**
	 * Returns the <code>GraphViewModel</code>.
	 * 
	 * @return the <code>GraphViewModel</code>.
	 */
	public GraphViewModel getViewModel() {
		return viewModel;
	}

	/**
	 * Sets the <code>GraphViewModel</code>.
	 * 
	 * @param viewModel the <code>GraphViewModel</code> to set.
	 */
	public void setViewModel(GraphViewModel viewModel) {

		if (this.viewModel != null) {
			this.viewModel.removeGraphViewListener(this);
		}

		viewModel.addGraphViewListener(this);
		this.viewModel = viewModel;
	}

	/**
	 * Returns the <code>GraphModel</code>.
	 * 
	 * @return the <code>GraphModel</code>.
	 */
	public GraphModel getModel() {
		return model;
	}

	/**
	 * Sets the <code>GraphModel</code>.
	 * 
	 * @param model the <code>GraphModel</code> to set.
	 */
	public void setModel(GraphModel model) {

		if (this.model != null) {
			this.model.removeGraphModelListener(this);
		}

		model.addGraphModelListener(this);
		this.model = model;
	}



	/**
	 * The <code>GraphModelListener</code> implemented method ; Recieves a
	 * <code>GraphModelEvent</code>.
	 */
	public void graphChanged(GraphModelEvent e) {

		if (e.getType() == GraphModelEvent.INSERT) {
			int from = e.getFirstGeneration();
			int to = e.getLastGeneration();

			Comparator<GraphVertex> comparator = viewModel.getComparator();

			if (comparator != null) {
				for (int i = from; i <= to; i++) {
					model.sortBy(i, comparator);
				}
			}

			adjustPreferredSize();
			repaintGenerations(from, to);
		} else if (e.getType() == GraphModelEvent.REFRESH) {
			adjustPreferredSize();
			repaint();
		}
	}

	/**
	 * The <code>GraphViewListener</code> implemented method ; Recieves a
	 * <code>GraphViewEvent</code>.
	 */
	@SuppressWarnings("unchecked")
	public void viewChanged(GraphViewEvent e) {
		GraphViewProperty viewProperty = e.getProperty();

		switch (viewProperty) {

			case HIGHLIGHTED_VERTEX:
				GraphVertex oldVertex = (GraphVertex) e.getOldValue();
				if (oldVertex != null) {
					int genNo = oldVertex.getGenerationNo();
					repaintGenerations(genNo - viewModel.getHighlightDepth(), genNo + 1);
				}

				GraphVertex newVertex = (GraphVertex) e.getNewValue();
				if (newVertex != null) {
					int genNo = newVertex.getGenerationNo();
					repaintGenerations(genNo - viewModel.getHighlightDepth(), genNo + 1);
				}

				break;

			case DIAMETER:
			case HGAP:
			case VGAP:
			case MARGINS:
				resize();
				break;
			case COMPARATOR:
				model.sortAllBy((Comparator<GraphVertex>) e.getNewValue());
				viewModel.resetIndices();
				viewModel.resetPostions();
				repaint();
				break;
			case BOUND_ENABLE:
			case BOUND_COLOR:
			case HIGHLIGHT_COLOR:
			case FITNESS:
				repaint();
				break;

			default:
				// Do nothing
		}

	}

	/**
	 * Repaint the view in a rectangle compute to enclose the specified
	 * generation.
	 * 
	 * @param generation the generation to repaint.
	 */
	protected void repaintGeneration(int generation) {
		int x = 0;
		int y = generation * (viewModel.getDiameter() + viewModel.getVgap()) - viewModel.getVgap()
				- (int) (viewModel.getDiameter());
		int width = (int) getPreferredSize().getWidth();
		int height = 2 * viewModel.getDiameter() + viewModel.getVgap();

		repaint(x, y, width, height);
	}

	/**
	 * Repaint the view in a rectangle compute to enclose the specified
	 * generations.
	 * 
	 * @param from the first generation to repaint.
	 * @param to the last generation to repaint.
	 * 
	 * @throw IllegalArgumentException if from > to.
	 */
	protected void repaintGenerations(int from, int to) throws IllegalArgumentException {

		if (from > to) {
			throw new IllegalArgumentException("from > to");
		}

		int x = 0;
		int y = from * (viewModel.getDiameter() + viewModel.getVgap()) - viewModel.getVgap() - viewModel.getDiameter();
		int width = (int) getPreferredSize().getWidth();
		int height = (viewModel.getDiameter() + viewModel.getVgap()) * (to - from + 1) + viewModel.getDiameter();

		repaint(x, y, width, height);
	}

	/**
	 * Computes and sets the size of this view.
	 * 
	 * @return the preferred size computed.
	 */
	public Dimension adjustPreferredSize() {
		Dimension d = null;

		if (viewModel != null && model != null) {

			int width = viewModel.getMargins().left + viewModel.getMargins().right;
			width += model.getPopulationSize() * (viewModel.getDiameter() + viewModel.getHgap());

			int height = viewModel.getMargins().top + viewModel.getMargins().bottom;
			height += model.getGenerationCount() * (viewModel.getDiameter() + viewModel.getVgap());

			d = new Dimension(width, height);
		}

		setPreferredSize(d);

		return d;
	}

	/**
	 * Computes the preferred size, sets it at the real size and returns it.
	 * 
	 * @return the size.
	 */
	public Dimension resize() {

		Dimension d = adjustPreferredSize();
		if (d != null && !d.equals(getSize())) {
			setSize(d);
		}
		return d;
	}

	/**
	 * Delegate method from the view model.
	 * 
	 * @see GraphViewModel#getVertexModel(GraphVertex)
	 */
	public GraphVertexModel getVertexModel(GraphVertex vertex) {
		return viewModel.getVertexModel(vertex);
	}

	/**
	 * Delegate method from the view model.
	 * 
	 * @see GraphViewModel#addVertexModel(GraphVertex, GraphVertexModel)
	 */
	public GraphVertexModel addVertexModel(GraphVertex vertex, GraphVertexModel vertexModel) {
		return viewModel.addVertexModel(vertex, vertexModel);
	}

	/**
	 * Paints the graph. Automatically computes the generations and the vertices
	 * to paint according to the clipping region of the specified graphics
	 * context.
	 * 
	 * @param g the graphics context to use for painting.
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		if (viewModel == null || model == null) {
			return;
		}
		
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Rectangle r = g2.getClipBounds();

		// Computes the generation range.
		int ymin = (int) r.getY();
		int ymax = (int) (ymin + r.getHeight());

		int firstGeneration = getGenerationAt(ymin);
		int lastGeneration = getGenerationAt(ymax);

		// Computes the vertex range.
		int xmin = (int) r.getX();
		int xmax = (int) (xmin + r.getWidth());

		int firstVertex = getVertexAt(xmin);
		int lastVertex = getVertexAt(xmax);

		// To see the generations to paint.
//		 System.out.println("Gen : " + firstGeneration + " to : " +
//		 lastGeneration + "\tVert : " + firstVertex
//		 + " to : " + lastVertex);
//		 int c = (int) System.currentTimeMillis() % 250;
//		 Color fillColor = new Color(c, 0, c);
//		 g2.setPaint(fillColor);
//		 g2.fill(g2.getClip());

		// Buffer highlighted vertex to paint them after.
		ArrayList<GraphVertex> buffer = new ArrayList<GraphVertex>();

		for (int i = firstGeneration; i <= lastGeneration; i++) {

			for (int j = firstVertex; j < lastVertex && j < model.getGenerationSize(i); j++) {
				GraphVertex vertex = model.getVertex(i, j);
				GraphVertexModel vertexModel = getVertexModel(vertex);

				// Buffers the vertex if it is highlighted, to paint it after.
				if (vertexModel.isHighlighted()) {
					buffer.add(vertex);
				} else {
					if (viewModel.isBondEnable()) {
						paintBonds(g2, vertex);
					}
					paintVertex(g2, vertex);
				}
			}
		}

		// Paints highlighted vertex.
		for (GraphVertex vertex: buffer) {
			paintBonds(g2, vertex);
			paintVertex(g2, vertex);
		}
	}

	/**
	 * Paints the specified vertex according to the properties described by its
	 * model.
	 * 
	 * @param g the graphics context to use for painting.
	 * @param vertex the specified vertex to paint.
	 * 
	 * @see GraphVertexModel
	 */
	private void paintVertex(Graphics2D g, GraphVertex vertex) {
		GraphVertexModel vertexModel = getVertexModel(vertex);

		int x = (int) vertexModel.getLocation().getX();
		int y = (int) vertexModel.getLocation().getY();
		int diameter = vertexModel.getDiameter();

		g.setPaint(vertexModel.getColor());
		g.fillOval(x, y, diameter, diameter);
	}

	/**
	 * If the vertex has parent(s), paints the bond(s) between them.
	 * 
	 * @param g the graphics context to use for painting.
	 * @param vertex the specified vertex to paint.
	 * 
	 * @see GraphVertexModel
	 */
	private void paintBonds(Graphics2D g, GraphVertex vertex) {

		// If the vertex have no parents, do nothing.
		if (vertex.getParents() == null || vertex.getParents().length == 0) {
			return;
		}

		// Sets the graphics context's properties.
		Color boundColor;
		int STROKE_WIDTH;
		double DElTA = 0.5;
		double RATIO = 0.6;

		GraphVertexModel vertexModel = getVertexModel(vertex);
		if (vertexModel.isHighlighted()) {
			boundColor = viewModel.getHighlightColor();
			STROKE_WIDTH = 2;
		} else {
			boundColor = viewModel.getBondColor();
			STROKE_WIDTH = 1;
		}
		g.setStroke(new BasicStroke(STROKE_WIDTH));
		g.setColor(boundColor);

		// Computes the cross point.
		double x2 = 0;
		double y2 = 0;
		for (GraphVertex parent: vertex.getParents()) {

			GraphVertexModel parentVertexModel = getVertexModel(parent);

			x2 += parentVertexModel.getX();
			y2 += parentVertexModel.getY();
		}
		x2 /= vertex.getParents().length;
		y2 /= vertex.getParents().length;
		y2 = RATIO * y2 + (1 - RATIO) * vertexModel.getY();

		// Draws parents bonds.
		for (GraphVertex parent: vertex.getParents()) {

			GraphVertexModel parentVertexModel = getVertexModel(parent);

			double x1 = parentVertexModel.getX();
			double y1 = parentVertexModel.getY() + parentVertexModel.getDiameter() / 2.0;

			g.draw(createCurve(x1, y1, x2, y2, DElTA));
		}

		// Draws child bond.
		vertexModel = getVertexModel(vertex);

		double x1 = vertexModel.getX();
		double y1 = vertexModel.getY() - vertexModel.getDiameter() / 2.0;

		g.draw(createCurve(x2, y2, x1, y1, DElTA));

	}

	/**
	 * Returns the generation number corresponding to the specified coordinate.
	 * 
	 * @param y the Y axis coordonate.
	 * @return the generation number corresponding to the y coordinate.
	 */
	private int getGenerationAt(int y) {
		int top = viewModel.getMargins().top;
		int height = viewModel.getDiameter() + viewModel.getVgap();
		int res;

		if (y >= top + viewModel.getDiameter() / 2.0) {

			y = (int) (y - top + viewModel.getDiameter() / 2.0);

			res = y / height + 1;

			if (res > model.getGenerationCount()) {
				res = model.getGenerationCount();
			}
		} else {
			res = 0;
		}

		return res;
	}

	/**
	 * Returns the vertex index corresponding to the specified coordinate.
	 * 
	 * @param x the X axis coordonate.
	 * @return the vertex index corresponding to the X coordinate.
	 */
	private int getVertexAt(int x) {
		int left = viewModel.getMargins().left;
		int width = viewModel.getDiameter() + viewModel.getHgap();
		int res;

		if (x >= left + viewModel.getDiameter() / 2.0) {

			x = (int) (x - left + viewModel.getDiameter() / 2.0);

			res = x / width + 1;

		} else {
			res = 0;
		}

		return res;
	}

	//
	// Scrollable implementation
	//
	/**
	 * 
	 */
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	/**
	 * 
	 */
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return viewModel.getDiameter() + viewModel.getVgap();
	}

	/**
	 * 
	 */
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	/**
	 * 
	 */
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	/**
	 * 
	 */
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return viewModel.getDiameter() + viewModel.getVgap();
	}

	//
	// Curve drawing utility methods.
	//
	/**
	 * Creates a <code>CubicCurve2D</code>.
	 * 
	 * @param p1 the start point of the cubic curve segment.
	 * @param p2 the end point of the cubic curve segment.
	 * @param delta the delta.
	 * @return the <code>CubicCurve2D</code> binding the two points.
	 * @throws IllegalArgumentException if delta is not between 0 and 1.
	 */
	public static CubicCurve2D createCurve(Point2D p1, Point2D p2, double delta) throws IllegalArgumentException {
		if (delta < 0 || delta > 1) {
			throw new IllegalArgumentException("Must have : 0 <= delta <= 1");
		}

		return createCurve(p1.getX(), p1.getY(), p2.getX(), p2.getY(), delta);
	}

	/**
	 * Creates a <code>CubicCurve2D</code>.
	 * 
	 * @param x1 the X coordinate of the start point.
	 * @param y1 the Y coordinate of the start point.
	 * @param x2 the X coordinate of the end point.
	 * @param y2 the Y coordinate of the end point.
	 * @param delta the delta.
	 * @return the <code>CubicCurve2D</code> binding the two points.
	 * @throws IllegalArgumentException if delta is not between 0 and 1.
	 */
	public static CubicCurve2D createCurve(double x1, double y1, double x2, double y2, double delta)
			throws IllegalArgumentException {
		if (delta < 0 || delta > 1) {
			throw new IllegalArgumentException("Must have : 0 <= delta <= 1");
		}

		return new CubicCurve2D.Double(x1, y1, x1, (1 - delta) * y1 + delta * y2, x2, delta * y1 + (1 - delta) * y2,
				x2, y2);
	}

}
