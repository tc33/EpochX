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
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.epochx.Fitness;
import org.epochx.Individual;
import org.epochx.Population;
import org.epochx.event.Event;
import org.epochx.event.EventManager;
import org.epochx.event.GenerationEvent.EndGeneration;
import org.epochx.event.Listener;
import org.epochx.event.OperatorEvent.EndOperator;

/**
 * 
 */
public class PnlGraph extends JScrollPane implements Listener<Event> {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = 4849953816024039299L;
	
	/**
	 * The parent <code>Graph</code>.
	 */
	private final Graph graph;

	/**
	 * The view panel.
	 */
	private final JPanel pnl;

	/**
	 * The diameter of nodes.
	 */
	private final int diameter;

	/**
	 * The the horizontal gap between two column of node.
	 */
	private final int hgap;

	/**
	 * The vertical gap between two row of node.
	 */
	private final int vgap;

	/**
	 * The <code>FitnessSet</code>.
	 */
	private final FitnessSet fitnesses;

	/**
	 * The buffer of <code>EndOperator</code> event.
	 */
	private final ArrayList<EndOperator> buffer = new ArrayList<EndOperator>();

	private ArrayList<GraphNode> children = new ArrayList<GraphNode>();

	private ArrayList<GraphNode> parents = new ArrayList<GraphNode>();

	public PnlGraph(Graph graph, int diameter, int hgap, int vgap) {
		this.graph = graph;
		this.diameter = diameter;
		this.hgap = hgap;
		this.vgap = vgap;
		this.fitnesses = new FitnessSet();
		this.pnl = new JPanel(null, true);
		
		pnl.setBackground(Color.white);

		setViewportView(pnl);
		setPreferredSize(new Dimension(800, 600));
		getVerticalScrollBar().setUnitIncrement(diameter+vgap);

		EventManager.getInstance().add(EndGeneration.class, this);
		EventManager.getInstance().add(EndOperator.class, this);
	}
	
	/**
	 * @return the graph
	 */
	public Graph getGraph() {
		return graph;
	}

	/**
	 * @return the diameter
	 */
	public int getDiameter() {
		return diameter;
	}

	/**
	 * Adds a <code>Fitness</code> in the <code>FitnessSet</code>.
	 * 
	 * @param fitness the <code>Fitness</code> to be added.
	 */
	public void addFitness(Fitness fitness) {
		synchronized (fitnesses) {
			fitnesses.add(fitness);
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
			i = fitnesses.indexOf(fitness) * 1.f;
			n = fitnesses.size() * 1.f - 1.f;
		}
		return new Color(i / n, 0, (n - i) / n);
	}
	
	/**
	 * The <code>Listener</code> inherited method.
	 * @param event the {@link Event}.
	 */
	public void onEvent(Event event) {
		if (event instanceof EndOperator) {
			System.out.print(2);
			buffer.add((EndOperator) event);
		} else if (event instanceof EndGeneration) {
			refresh((EndGeneration) event);
		}
	}

	/**
	 * Refreshs the graph on an <code>EndGeneration</code> event.
	 * @param eg the <code>EndGeneration</code> event.
	 */
	@SuppressWarnings("unchecked")
	public void refresh(EndGeneration eg) {
		Population p = eg.getPopulation();
		p.sort();
		int genNo = eg.getGeneration();

		children = new ArrayList<GraphNode>();

		int xPos = hgap + diameter;
		for (int i = 0; i < p.size(); i++) {
			GraphNode node = new GraphNode(this, p.get(i), genNo, xPos, genNo * (diameter + vgap));
			pnl.add(node);
			children.add(node);
			xPos += diameter + hgap;
		}
		

		Rectangle bounds = new Rectangle(xPos, (genNo + 1) * (diameter + vgap));
		//pnl.setBounds(bounds);
		pnl.setPreferredSize(bounds.getSize());

		// System.out.print(buffer.size());
		if (!parents.isEmpty()) {

			GraphBond arrow1 = new GraphBond(this, null, bounds);
			arrow1.setParents(parents.get(0), parents.get(10));
			arrow1.setChildren(children.get(2), children.get(5));
			pnl.add(arrow1);
			
			GraphBond arrow2 = new GraphBond(this, null, bounds);
			arrow2.setParents(parents.get(15));
			arrow2.setChildren(children.get(20));
			pnl.add(arrow2);

			GraphBond arrow3 = new GraphBond(this, null, bounds);
			arrow3.setParents(parents.get(10));
			arrow3.setParents(parents.get(15));
			arrow3.setChildren(children.get(10));
			pnl.add(arrow3);

			for (EndOperator eo: buffer) {

				for (Individual father: eo.getParents()) {
					System.out.print(parents.contains(father));
				}

			}

		}

		parents = (ArrayList<GraphNode>) children.clone();
		buffer.clear();

		pnl.repaint();
		// setPreferredSize(bound.getSize());
		revalidate();
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
	 * @param fitness the <code>Fitness</code> whose index in this set is to be
	 *        found.
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
