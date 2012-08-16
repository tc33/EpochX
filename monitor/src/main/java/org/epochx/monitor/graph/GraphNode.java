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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.DefaultButtonModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.epochx.Fitness;
import org.epochx.Individual;
import org.epochx.Operator;

/**
 * 
 */
public class GraphNode extends AbstractButton implements Comparable<Object>, ChangeListener, MouseListener {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = 5961230393742298632L;

	/**
	 * The recursion depth of hightlight when a node is rollover.
	 */
	private static final int HIGHTLIGHT_DEPTH = 5;

	/**
	 * The parent <code>GraphGen</code>.
	 */
	private final GraphGen graphGen;

	/**
	 * The <code>Individual</code> whose this <code>GraphNode</code> is the
	 * representation.
	 */
	private Individual ind;

	/**
	 * The parent <code>GraphBond</code>.
	 */
	private GraphBond parentBond = null;

	/**
	 * The list of child <code>GraphBond</code>.
	 */
	private final ArrayList<GraphBond> childBonds;

	/**
	 * Constructs a <code>GraphNode</code>.
	 * 
	 * @param graphGen the parent <code>GraphGen</code>
	 * @param ind the <code>Individual</code> whose this <code>GraphNode</code>
	 *        is the representation
	 */
	public GraphNode(GraphGen graphGen, Individual ind) {
		super();
		this.graphGen = graphGen;
		this.ind = ind;

		childBonds = new ArrayList<GraphBond>();

		setModel(new DefaultButtonModel());

		graphGen.addFitness(ind.getFitness());

		addMouseListener(this);
		addChangeListener(this);
		addMouseMotionListener(graphGen);

		setPreferredSize(new Dimension(getDiameter(), getDiameter()));

		repaint();
		validate();

	}

	/**
	 * Return the <code>Individual</code>.
	 * 
	 * @return the <code>Individual</code>
	 */
	public Individual getIndividual() {
		return ind;
	}

	/**
	 * Returns the <code>Fitness</code> of the node's <code>Individual</code>.
	 * 
	 * @return the <code>Fitness</code> of the node's <code>Individual</code>
	 */
	public Fitness getFitness() {
		return ind.getFitness();
	}

	/**
	 * Returns the generation number.
	 * 
	 * @return the generation number
	 */
	public int getGenerationNo() {
		return graphGen.getGenenratioNo();
	}

	/**
	 * Returns the index of this <code>GraphNode</code> in the parent
	 * <code>GraphGen</code>'s set.
	 * 
	 * @return the index of this <code>GraphNode</code> in the parent
	 *         <code>GraphGen</code>'s set
	 */
	public int getIndex() {
		return graphGen.indexOf(this);
	}

	/**
	 * Returns the position of this node, on the X axis, according to the bounds
	 * of the parent <code>GraphGen</code>.
	 * 
	 * @return the position of this node, on the X axis, according to the
	 *         bounds of the parent <code>GraphGen</code>.
	 */
	public double getXPosition() {
		return graphGen.getXPosition(this);
	}

	/**
	 * Returns the diameter.
	 * 
	 * @return the diameter
	 */
	public int getDiameter() {
		return graphGen.getDiameter();
	}

	/**
	 * Returns the parent <code>GraphBond</code>.
	 * 
	 * @return the parent <code>GraphBond</code>
	 */
	public GraphBond getParentBond() {
		return parentBond;
	}

	/**
	 * Sets the parent <code>GraphBond</code>.
	 * 
	 * @param parentBond the <code>GraphBond</code> to set
	 */
	public void setParentBond(GraphBond parentBond) {
		this.parentBond = parentBond;
	}

	/**
	 * @return the mean of X position of this node's parents.
	 */
	public double getParentMeanX() {
		if (parentBond == null)
			return -1;
		else
			return parentBond.getParentMeanX();
	}

	/**
	 * Returns the Operator which gave this node's individual. Can be null.
	 * 
	 * @return the Operator which gave this node's individual
	 */
	public Operator getParentOperator() {
		if (parentBond == null)
			return null;
		else
			return parentBond.getOperator();
	}

	/**
	 * Adds a child <code>GraphBond</code> the {@link #childBonds} list.
	 * <p>
	 * <b>Synchronized</b> by intrinsic <i>lock</i>.
	 * </p>
	 * 
	 * @param bond the <code>GraphBond</code> to add.
	 */
	public synchronized void addChildBond(GraphBond bond) {
		childBonds.add(bond);
	}

	/**
	 * Sets or clears the button's hightlight state.
	 * 
	 * @param b true to turn on hightlight.
	 */
	public void setHighlighted(boolean b, int depth) {
		getModel().setRollover(b);
		if (depth > 0 && parentBond != null) {
			parentBond.setHighlighted(b, depth);
		}

		if (depth == HIGHTLIGHT_DEPTH) {
			for (GraphBond bond: childBonds) {
				bond.setHighlighted(b, 0);
				bond.repaint();
			}
		}
	}

	/**
	 * Compares the given object with this <code>GraphNode</code>'s
	 * <code>Fitness</code>.
	 * 
	 * <p>
	 * The given object can be an instance of <code>GraphNode</code> or
	 * <code>Individual</code>. Throws a <code>ClassCastException</code>
	 * otherwise.
	 * </p>
	 * 
	 * @param obj the reference object with which to compare
	 * @return the value 0 if the argument's <code>Fitness</code> is equal to
	 *         this <code>GraphNode</code>'s <code>Fitness</code>; a value less
	 *         than 0 if the argument's <code>Fitness</code> is than this
	 *         <code>GraphNode</code>'s <code>Fitness</code>; and a value
	 *         greater than 0 if the argument's <code>Fitness</code> is less
	 *         than this <code>GraphNode</code>'s <code>Fitness</code>.
	 */
	public int compareTo(Object obj) {
		if (obj instanceof GraphNode) {
			return getFitness().compareTo(((GraphNode) obj).getFitness());
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
	 * The given object can be an instance of <code>GraphNode</code> or
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
		if (obj instanceof GraphNode) {
			return ind.equals(((GraphNode) obj).ind);
		} else if (obj instanceof Individual) {
			return ind.equals((Individual) obj);
		} else {
			return false;
		}
	}

	// We don't want to update UI for this button.
	@Override
	public void updateUI() {
	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// final int BORDER_WIDTH = 1;
		if (getModel().isRollover()) {
			Rectangle2D rectangle = new Rectangle2D.Double(0, 0, getDiameter(), getDiameter());
			g2.setPaint(PnlGraph.HIGHLIGHT_COLOR);
			g2.fill(rectangle);
		}
		Color fillColor = graphGen.getFitnessColor(getFitness());
		g2.setPaint(fillColor);

		Ellipse2D ellipse = new Ellipse2D.Double(0, 0, getDiameter(), getDiameter());
		g2.fill(ellipse);
	}

	/**
	 * The <code>StateListener</code> inherited method.
	 * 
	 * @param e the <code>ChangeEvent</code>
	 */
	public void stateChanged(ChangeEvent e) {
		repaint();
	}

	public void mouseClicked(MouseEvent e) {
		graphGen.getPnlGraph().getGraph().getPnlInfo().setNode(this);
		// setRollover(!getModel().isRollover(), HIGHTLIGHT_DEPTH);
	}

	public void mouseEntered(MouseEvent e) {
		setHighlighted(true, HIGHTLIGHT_DEPTH);
		setToolTipText(ind.getFitness().toString());
		// graphGen.getPnlGraph().getGraph().getPnlInfo().setNode(this);
	}

	public void mouseExited(MouseEvent e) {
		setHighlighted(false, HIGHTLIGHT_DEPTH);
	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

}
