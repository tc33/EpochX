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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.JComponent;

import org.epochx.Fitness;
import org.epochx.Individual;

/**
 * 
 */
public class GraphNode extends JComponent implements MouseListener {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = 5961230393742298632L;
	
	private static int DEFAULT_DIAMETER;

	private static FitnessSet fitnesses = new FitnessSet();

	/**
	 * The PnlInfo shared by all GraphNode.
	 */
	private static PnlInfo pnlInfo;

	private Color borderColor;

	private int diameter = DEFAULT_DIAMETER;

	private final ArrayList<GraphNode> children = new ArrayList<GraphNode>();

	private final ArrayList<GraphNode> parents = new ArrayList<GraphNode>();

	private Individual ind;

	private final int genNo;

	private final Point2D centre;

	public GraphNode(Individual ind, int genNo , int x, int y) {
		super();
		this.ind = ind;
		this.centre = new Point2D.Double(x, y);
		this.genNo = genNo;

		// Add fitness to fitnesses set.
		synchronized (fitnesses) {
			fitnesses.add(ind.getFitness());
		}

		borderColor = Color.WHITE;

		addMouseListener(this);
		setToolTipText(ind.getFitness().toString());
		// setBorder(BorderFactory.createEtchedBorder());

		setBounds(x - diameter / 2, y - diameter / 2, diameter, diameter);
		setVisible(true);
		repaint();

	}

	public Individual getIndividual() {
		return ind;
	}

	/**
	 * @return the children
	 */
	public ArrayList<GraphNode> getChildren() {
		return children;
	}

	/**
	 * @return the parents
	 */
	public ArrayList<GraphNode> getParents() {
		return parents;
	}

	/**
	 * @return the centre
	 */
	public Point2D getCentre() {
		return centre;
	}

	/**
	 * @return the individual fitness.
	 */
	public Fitness getFitness() {
		return ind.getFitness();
	}

	/**
	 * @return the genNo
	 */
	public int getGenNo() {
		return genNo;
	}

	// We don't want to update UI for this button.
	@Override
	public void updateUI() {
	}

	/**
	 * @param diameter the diameter to set.
	 * 
	 * @throws IllegalArgumentException if diameter <5.
	 */
	public static void setDefaultDiameter(int diameter) throws IllegalArgumentException {
		if (diameter < 5) {
			throw new IllegalArgumentException("Diameter must be greater or equals than 5.");
		}
		GraphNode.DEFAULT_DIAMETER = diameter;
	}

	/**
	 * @param pnlInfo the <code>PnlInfo</code> to set.
	 */
	public static void setPnlInfo(PnlInfo pnlInfo) {
		GraphNode.pnlInfo = pnlInfo;
	}

	/**
	 * @return the fillColor
	 */
	private Color getFillColor() {
		float i;
		float n;
		synchronized (fitnesses) {
			i = fitnesses.indexOf(ind.getFitness()) * 1.f;
			n = fitnesses.size() * 1.f - 1.f;
		}
		return new Color(i / n, 0, (n - i) / n);
	}

	@Override
	public void paintComponent(Graphics g) {
		Color fillColor = getFillColor();
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();

		final int STROKE_WIDTH = 1;

		Rectangle bounds = getBounds();
		Ellipse2D ellipse = new Ellipse2D.Double(STROKE_WIDTH, STROKE_WIDTH, bounds.getWidth() - 2 * STROKE_WIDTH,
				bounds.getHeight() - 2 * STROKE_WIDTH);

		g2.setStroke(new BasicStroke(STROKE_WIDTH));

		g2.setPaint(fillColor);
		g2.fill(ellipse);
		g2.setColor(borderColor);
		g2.draw(ellipse);
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

	public void mouseClicked(MouseEvent e) {
		/*
		 * if ((e.getButton() == 1) && ellipse.contains(e.getX(), e.getY()) ) {
		 * //g2.translate(1, 1);
		 * System.out.println("Click in "+ind.getFitness());
		 * // JOptionPane.showMessageDialog(null,e.getX()+ "\n" + e.getY());
		 * }
		 */
		// fillColor = Color.green;
		pnlInfo.setNode(this);
	}

	public void mouseEntered(MouseEvent e) {
		// System.out.println("Enter "+ind.getFitness());repaint();
		borderColor = Color.GREEN;
		repaint();
	}

	public void mouseExited(MouseEvent e) {
		// System.out.println("Exit "+ind.getFitness());repaint();
		borderColor = Color.WHITE;
		repaint();
	}

	public void mousePressed(MouseEvent e) {
		// System.out.println("Press "+ind.getFitness());repaint();
		// fillColor = Color.GRAY;
		repaint();
	}

	public void mouseReleased(MouseEvent e) {
		// System.out.println("Release "+ind.getFitness());repaint();
		// fillColor = getFillColor();
		repaint();
	}

}

/**
 * A <code>FitnessSet</code> extends a <code>TreeSet</code> of
 * <code>Fitnesses</code> to overrides {@link #contains(Object)} method and
 * provides {@link #indexOf(Fitness)} method.
 * <p>
 * <b>NOT THREAD-SAFE</b>
 */
class FitnessSet extends TreeSet<Fitness> {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = -8387398262292214425L;

	/**
	 * Returns the index of the first occurrence with the same
	 * <code>String</code> value than the given argument ; returns -1 if not
	 * found.
	 * 
	 * @param fitness the Fitness whose index in this set is to be found.
	 * @return the index of the first occurrence with the same
	 *         <code>String</code> value than the given argument ; returns -1 if
	 *         not found.
	 */
	public int indexOf(Fitness fitness) {
		Fitness f;
		int i = 0;
		Iterator<Fitness> iterator = super.iterator();
		while (iterator.hasNext()) {
			f = iterator.next();
			if (f.toString().equals(fitness.toString()))
				return i;
			else
				i++;
		}
		return -1;
	}

	/**
	 * Overrides the superclass method.
	 * 
	 * @return true if the set contains a <code>Fitness</code> with the same
	 *         <code>String</code> value.
	 */
	@Override
	public boolean contains(Object o) {
		if (!(o instanceof Fitness))
			return false;

		Fitness fitness = (Fitness) o;

		Fitness f;
		for (Iterator<Fitness> iterator = super.iterator(); iterator.hasNext();) {
			f = iterator.next();
			if (f.toString().equals(fitness.toString()))
				return true;
		}
		return false;
	}
}
