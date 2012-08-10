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
	 * The <code>Color</code> of the <code>GraphBond</code>.
	 */
	private Boolean selected = false;

	/**
	 * The <code>GraphNode</code> parents bounded by this <code>GraphBond</code>
	 * .
	 */
	private final ArrayList<GraphNode> parents = new ArrayList<GraphNode>();

	/**
	 * The <code>GraphNode</code> children bounded by this
	 * <code>GraphBond</code>.
	 */
	private final ArrayList<GraphNode> children = new ArrayList<GraphNode>();

	/**
	 * The <code>Operator</code> whose this <code>GraphBond</code> is the
	 * representation.
	 */
	private final Operator operator;

	/**
	 * Constructs a <code>GraphBond</code>.
	 * 
	 * @param pnlGraph the parent <code>PnlGraph</code>.
	 * @param operator the <code>Operator</code> whose this
	 *        <code>GraphBond</code> is the representation.
	 * @param bounds the bounds.
	 */
	public GraphBond(PnlGraph pnlGraph, Operator operator, Rectangle bounds) {
		super();
		this.pnlGraph = pnlGraph;
		this.operator = operator;
		setBounds(bounds);
		//setBorder(BorderFactory.createEtchedBorder());

	}

	public void setParents(GraphNode ... parents) {
		for (GraphNode node: parents) {
			node.addChildBond(this);
			this.parents.add(node);
		}

		repaint();
	}

	public void setChildren(GraphNode ... children) {
		for (GraphNode node: children) {
			node.addParentBonds(this);
			this.children.add(node);
		}
		repaint();
	}

	/**
	 * @param b the selected to set
	 */
	public synchronized void setSelected(Boolean b) {
		this.selected = b;
		/*for (GraphNode node: parents) {
			node.getModel().setRollover(b);
		}*/
		repaint();
	}
	
	/**
	 * @return the operator
	 */
	public Operator getOperator() {
		return operator;
	}

	private int getDiameter() {
		return pnlGraph.getDiameter();
	}

	private Point2D getParentCrossPoint() {
		Point2D res = null;
		if (parents.size() == 1) {
			
			res = parents.get(0).getBottom();
			
		} else if (parents.size() > 1) {
			
			double x = 0;
			for (GraphNode parent: parents) {
				x += parent.getCentre().getX();
			}
			x /= parents.size();

			Double P = 0.4;
			res = new Point2D.Double(x, (1 - P) * parents.get(0).getCentre().getY() + P
					* children.get(0).getCentre().getY());
			
		}
		return res;
	}

	private Point2D getChildrenCrossPoint() {
		Point2D res = null;
		if (children.size() == 1) {

			res = children.get(0).getTop();
					
		} else if (children.size() > 1) {

			double x = 0;
			for (GraphNode child: children) {
				x += child.getCentre().getX();
			}
			x /= children.size();

			Double P = 0.4;

			res = new Point2D.Double(x, P * parents.get(0).getCentre().getY() + (1 - P)
					* children.get(0).getCentre().getY());
			
		}
		return res;
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

		if (selected) {
			color = Color.GREEN;
			STROKE_WIDTH = 2;
		} else {
			color = Color.DARK_GRAY;
			STROKE_WIDTH = 1;
		}
		g2.setStroke(new BasicStroke(STROKE_WIDTH));
		g2.setColor(color);

		// Draws parents bonds.
		Point2D parentCross = getParentCrossPoint();
		for (GraphNode parent: parents) {
			g2.draw(createCurve(parent.getBottom(), parentCross, DElTA));
			// g2.draw(new Line2D.Double(pX, pY, pcX, pcY));
		}

		// Draws children bonds.
		Point2D childCross = getChildrenCrossPoint();
		for (GraphNode child: children) {
			g2.draw(createCurve(childCross, child.getTop(), DElTA));
		}

		// Draws middle bonds.
		g2.draw(createCurve(parentCross, childCross, 0.2));

		g2.setStroke(new BasicStroke(STROKE_WIDTH));
		g2.setColor(color);
	}
	
	/**
	 * Creates a <code>CubicCurve2D</code>.
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
