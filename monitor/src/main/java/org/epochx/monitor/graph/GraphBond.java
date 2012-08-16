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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JComponent;

import org.epochx.Operator;

/**
 * 
 */
public class GraphBond extends JComponent {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = 306446085423074662L;

	/**
	 * The related <code>PnlGraph</code>.
	 */
	private final PnlGraph pnlGraph;

	/**
	 * The boolean which defines is this bond is highlighted.
	 */
	private boolean highlighted;

	/**
	 * The <code>GraphNode</code> parents bounded by this <code>GraphBond</code>
	 * .
	 */
	private final ArrayList<GraphNode> parents;

	/**
	 * The <code>GraphNode</code> children bounded by this
	 * <code>GraphBond</code>.
	 */
	private final ArrayList<GraphNode> children;

	/**
	 * The <code>Operator</code> whose this <code>GraphBond</code> is the
	 * representation.
	 */
	private final Operator operator;

	/**
	 * Constructs a <code>GraphBond</code>.
	 * 
	 * @param pnlGraph the parent <code>PnlGraph</code>
	 * @param operator the <code>Operator</code> whose this
	 *        <code>GraphBond</code> is the representation
	 * @param bounds the bounds
	 * @param offspring the offspring <code>GraphNode</code>
	 * @param parents the parents <code>GraphNode</code>
	 */
	public GraphBond(PnlGraph pnlGraph, Operator operator, Rectangle bounds, GraphNode offspring, GraphNode ... parents) {
		super();
		this.pnlGraph = pnlGraph;
		this.operator = operator;
		this.highlighted = false;
		this.parents = new ArrayList<GraphNode>();
		this.children = new ArrayList<GraphNode>();
		
		children.add(offspring);
		offspring.setParentBond(this);
		
		for (GraphNode node: parents) {
			node.addChildBond(this);
			this.parents.add(node);
		}
		
		setBounds(bounds);
		setOpaque(false);
		
		//setBorder(BorderFactory.createLineBorder(Color.black));
	}

	public void setParents(GraphNode ... parents) {
		this.parents.clear();
		for (GraphNode node: parents) {
			node.addChildBond(this);
			this.parents.add(node);
		}
		repaint();
	}

	/**
	 * @param b the selected to set
	 */
	public synchronized void setHighlighted(Boolean b, int depth) {
		this.highlighted = b;
		if (b) {
			pnlGraph.moveToFront(this);
		} else {
			pnlGraph.moveToBack(this);
		}
		repaint();
		if(depth > 0) {
			for (GraphNode node: parents) {
				node.setHighlighted(b, depth-1);
			}
		}
	}
	
	/**
	 * Returns the operator.
	 * @return the operator
	 */
	public Operator getOperator() {
		return operator;
	}

	/**
	 * Returns the diameter.
	 * @return the diameter
	 */
	private int getDiameter() {
		return pnlGraph.getDiameter();
	}
	
	/**
	 * Returns the horizontal gap.
	 * @return the horizontal gap
	 */
	public int getHgap() {
		return pnlGraph.getHgap();
	}
	
	/**
	 * Returns the vertical gap.
	 * @return the vertical gap
	 */
	public int getVgap() {
		return pnlGraph.getVgap();
	}
	
	/**
	 * Return the parent mean index.
	 * @return the parent mean index
	 */
	public int getParentMeanIndex() {
		int index = 0;
		for (GraphNode parent: parents) {
			index += parent.getIndex();
		}
		index /= parents.size();
		return index;
	}

	/**
	 * Return the parent mean X location.
	 * @return the parent mean X location
	 */
	public double getParentMeanX() {
		double mean = 0;
		for (GraphNode parent: parents) {
			double x = parent.getXPosition();
			mean += x;
		}
		mean /= parents.size();
		return mean;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		// if parents or children list is empty, do nothing.
		if (parents.isEmpty() || children.isEmpty())
			return;
		
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Color color;
		int STROKE_WIDTH;
		double DElTA = 0.5;
		double RATIO = 0.4;

		if (highlighted) {
			color = PnlGraph.HIGHLIGHT_COLOR;
			STROKE_WIDTH = 2;
		} else {
			int c = 210;
			color = new Color(c, c, c);
			STROKE_WIDTH = 1;
		}
		g2.setStroke(new BasicStroke(STROKE_WIDTH));
		g2.setColor(color);

		// Draws parents bonds.
		for (GraphNode parent: parents) {
			
			double x1 = getHgap() + 1.0*getDiameter() / 2.0 + parent.getIndex() * (getDiameter() + getHgap());
			double y1 = getDiameter()/2.0;
			double x2 = getParentMeanX();
			double y2 = getBounds().getHeight()*RATIO;
			
			g2.draw(createCurve(x1, y1, x2, y2, DElTA));
		}

		// Draws middle bonds.
		double x1 = getParentMeanX();
		double y1 = getBounds().getHeight()*RATIO;
		double x2 = (int) (getHgap() + getDiameter() / 2.0 + children.get(0).getIndex() * (1.0*getDiameter() + getHgap()));
		double y2 = getBounds().getHeight() - getDiameter()/2.0;
		g2.draw(createCurve(x1, y1, x2, y2, DElTA));

		g2.setStroke(new BasicStroke(STROKE_WIDTH));
		g2.setColor(color);
	}
	
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
		if (delta < 0 || delta > 1 ) {
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
	public static CubicCurve2D createCurve(double x1, double y1, double x2, double y2, double delta) throws IllegalArgumentException {
		if (delta < 0 || delta > 1 ) {
			throw new IllegalArgumentException("Must have : 0 <= delta <= 1");
		}

		return new CubicCurve2D.Double(x1, y1, x1, (1 - delta) * y1 + delta * y2, x2, delta * y1 + (1 - delta) * y2,
				x2, y2);
	}

}
