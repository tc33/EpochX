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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.DefaultButtonModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.epochx.Fitness;
import org.epochx.Individual;

/**
 * 
 */
public class GraphNode extends AbstractButton implements ChangeListener, MouseListener {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = 5961230393742298632L;

	/**
	 * The parent <code>PnlGraph</code>.
	 */
	private final PnlGraph pnlGraph;

	/**
	 * The <code>Individual</code> whose this <code>GraphNode</code> is the
	 * representation.
	 */
	private Individual ind;

	/**
	 * The generation number of this node.
	 */
	private final int genNo;

	/**
	 * The centre point of this node.
	 */
	private final Point2D centre;

	private final ArrayList<GraphNode> parents;

	private final ArrayList<GraphNode> children;

	private final ArrayList<GraphBond> childBonds;

	private final ArrayList<GraphBond> parentBonds;

	/**
	 * Constructs a <code>GraphNode</code>.
	 * 
	 * @param pnlGraph the parent <code>PnlGraph</code>.
	 * @param ind the <code>Individual</code> whose this <code>GraphNode</code>
	 *        is the representation.
	 * @param genNo the generation number.
	 * @param x the X location of the centre in the pane.
	 * @param y the Y location of the centre in the pane.
	 */
	public GraphNode(PnlGraph pnlGraph, Individual ind, int genNo, int x, int y) {
		super();
		this.pnlGraph = pnlGraph;
		this.ind = ind;
		this.centre = new Point2D.Double(x, y);
		this.genNo = genNo;

		parents = new ArrayList<GraphNode>();
		children = new ArrayList<GraphNode>();
		parentBonds = new ArrayList<GraphBond>();
		childBonds = new ArrayList<GraphBond>();

		pnlGraph.addFitness(ind.getFitness());

		addMouseListener(this);
		addChangeListener(this);
		setModel(new GraphNodeModel());
		setToolTipText(ind.getFitness().toString());
		// setBorder(BorderFactory.createEtchedBorder());

		setBounds(x - getDiameter() / 2, y - getDiameter() / 2, getDiameter(), getDiameter());
		validate();

	}

	/**
	 * @return the <code>Individual</code>.
	 */
	public Individual getIndividual() {
		return ind;
	}

	/**
	 * @return the parents list.
	 */
	public ArrayList<GraphNode> getParents() {
		return parents;
	}

	/**
	 * @return the children list.
	 */
	public ArrayList<GraphNode> getChildren() {
		return children;
	}

	/**
	 * Returns the centre point of this node.
	 * 
	 * @return the centre point of this node.
	 */
	public Point2D getCentre() {
		return centre;
	}

	/**
	 * Returns the top point of this node.
	 * 
	 * @return the top point of this node.
	 */
	public Point2D getTop() {
		return new Point2D.Double(centre.getX(), centre.getY() - getDiameter() / 2.0);
	}

	/**
	 * Returns the bottom point of this node.
	 * 
	 * @return the bottom point of this node.
	 */
	public Point2D getBottom() {
		return new Point2D.Double(centre.getX(), centre.getY() + getDiameter() / 2.0);
	}

	/**
	 * Returns the right point of this node.
	 * 
	 * @return the right point of this node.
	 */
	public Point2D getRight() {
		return new Point2D.Double(centre.getX() + getDiameter() / 2.0, centre.getY());
	}

	/**
	 * Returns the left point of this node.
	 * 
	 * @return the left point of this node.
	 */
	public Point2D getLeft() {
		return new Point2D.Double(centre.getX() - getDiameter() / 2.0, centre.getY());
	}

	/**
	 * Returns the diameter.
	 * 
	 * @return the diameter.
	 */
	public int getDiameter() {
		int diameter;
		if (getModel().isRollover()) {
			diameter = (int) (pnlGraph.getDiameter() * 1.3);
		} else {
			diameter = pnlGraph.getDiameter();
		}
		return diameter;
	}

	/**
	 * Adds a parent <code>GraphBond</code> the {@link #parentBonds} list.
	 * <p>
	 * <b>Synchronized</b> by intrinsic <i>lock</i>.
	 * </p>
	 * 
	 * @param bond the <code>GraphBond</code> to add.
	 */
	public synchronized void addParentBonds(GraphBond bond) {
		parentBonds.add(bond);
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
	 * Returns the <code>Fitness</code> of the node Individual.
	 * 
	 * @return the <code>Fitness</code> of the node Individual.
	 */
	public Fitness getFitness() {
		return ind.getFitness();
	}

	/**
	 * Returns the generation number.
	 * 
	 * @return the generation number.
	 */
	public int getGenNo() {
		return genNo;
	}

	// We don't want to update UI for this button.
	@Override
	public void updateUI() {
	}

	/**
	 * Overrides the <code>equals</code> method to return true if this node have
	 * the same <code>Individual</code> as the node argumument; false otherwise.
	 * 
	 * @param obj the reference object with which to compare.
	 * @return <code>true</code> if this node have the same
	 *         <code>Individual</code> as the object argumument; false
	 *         otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GraphNode)
			return ind.equals(((GraphNode) obj).ind);
		return false;

	}

	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// final int BORDER_WIDTH = 1;
		int diameter = getDiameter();


		Color fillColor = pnlGraph.getFitnessColor(getFitness());
		g2.setPaint(fillColor);
		
		setBounds((int) centre.getX() - diameter / 2, (int) centre.getY() - diameter / 2, diameter, diameter);
		Ellipse2D ellipse = new Ellipse2D.Double(0, 0, getBounds().getWidth(), getBounds().getHeight());
		g2.fill(ellipse);
	}

	/**
	 * The <code>StateListener</code> inherited method.
	 * 
	 * @param e the <code>ChangeEvent</code>.
	 */
	public void stateChanged(ChangeEvent e) {
		repaint();
	}

	/**
	 * A <code>MouseListener</code> inherited method.
	 * 
	 * @param e the <code>MouseEvent</code>.
	 */
	public void mouseClicked(MouseEvent e) {
		/*
		 * if ((e.getButton() == 1) && ellipse.contains(e.getX(), e.getY()) ) {
		 * //g2.translate(1, 1);
		 * System.out.println("Click in "+ind.getFitness());
		 * // JOptionPane.showMessageDialog(null,e.getX()+ "\n" + e.getY());
		 * }
		 */
		// fillColor = Color.green;
		pnlGraph.getGraph().getPnlInfo().setNode(this);
	}

	/**
	 * A <code>MouseListener</code> inherited method.
	 * 
	 * @param e the <code>MouseEvent</code>.
	 */
	public void mouseEntered(MouseEvent e) {
		getModel().setRollover(true);
	}

	/**
	 * A <code>MouseListener</code> inherited method.
	 * 
	 * @param e the <code>MouseEvent</code>.
	 */
	public void mouseExited(MouseEvent e) {
		getModel().setRollover(false);
	}

	/**
	 * A <code>MouseListener</code> inherited method.
	 * 
	 * @param e the <code>MouseEvent</code>.
	 */
	public void mousePressed(MouseEvent e) {

	}

	/**
	 * A <code>MouseListener</code> inherited method.
	 * 
	 * @param e the <code>MouseEvent</code>.
	 */
	public void mouseReleased(MouseEvent e) {

	}

	/**
	 * A <code>GraphNodeModel</code> is a customed <code>ButtonModel</code> for
	 * a <code>GraphNode</code>.
	 */
	private class GraphNodeModel extends DefaultButtonModel {

		/**
		 * Generated serial UID.
		 */
		private static final long serialVersionUID = 7388327302535855629L;

		/**
		 * Sets or clears the button's rollover state.
		 * 
		 * @param b true to turn on rollover.
		 */
		public void setRollover(boolean b) {
			super.setRollover(b);
			for (GraphNode node: parents) {
				node.getModel().setRollover(b);
			}
			for (GraphBond bond: parentBonds) {
				bond.setSelected(b);
			}
		}
	}
}
