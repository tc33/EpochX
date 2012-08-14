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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;

import org.epochx.Config;
import org.epochx.Fitness;
import org.epochx.Population;
import org.epochx.event.Event;
import org.epochx.event.EventManager;
import org.epochx.event.GenerationEvent.StartGeneration;
import org.epochx.event.Listener;
import org.epochx.event.ParentIndividualEvent;
import org.epochx.event.RunEvent.EndRun;
import org.epochx.monitor.Utilities;

/**
 * 
 */
public class PnlGraph extends JScrollPane implements Listener<Event>, ActionListener {

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
	 * <p>
	 * All accesses must be <b>synchronized</b> because of concurrency with tje
	 * EDT.
	 * </p>
	 */
	private final TreeSet<Fitness> fitnesses;

	/**
	 * The last <code>GraphGen</code>.
	 */
	private GraphGen lastGen;
	
	/**
	 * The current <code>GraphGen</code>.
	 */
	private GraphGen currentGen;

	public PnlGraph(Graph graph, int diameter, int hgap, int vgap) {
		this.graph = graph;
		this.diameter = diameter;
		this.hgap = hgap;
		this.vgap = vgap;
		this.fitnesses = new TreeSet<Fitness>();
		this.pnl = new JPanel(null, true);

		pnl.setBackground(Color.white);

		setViewportView(pnl);
		setPreferredSize(new Dimension(800, 600));
		getVerticalScrollBar().setUnitIncrement(diameter + vgap);

		currentGen = new GraphGen(this, 0);

		Timer timer = new Timer(1000, this);
		timer.start(); 

		EventManager.getInstance().add(StartGeneration.class, this);
		EventManager.getInstance().add(EndRun.class, this);
		EventManager.getInstance().add(ParentIndividualEvent.class, this);
	}

	/**
	 * Returns parent <code>Graph</code>.
	 * @return the parent <code>Graph</code>
	 */
	public Graph getGraph() {
		return graph;
	}

	/**
	 * Returns the diameter of nodes.
	 * @return the diameter of nodes
	 */
	public int getDiameter() {
		return diameter;
	}

	/**
	 * Returns the horizontal gap.
	 * @return the horizontal gap
	 */
	public int getHgap() {
		return hgap;
	}
	
	/**
	 * Returns the vertical gap.
	 * @return the vertical gap
	 */
	public int getVgap() {
		return vgap;
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
		synchronized (fitnesses) {
			fitnesses.add(fitness);
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
	 * The <code>Listener</code> inherited method.
	 * 
	 * @param event the {@link Event}.
	 */
	public void onEvent(Event event) {
		if (event instanceof ParentIndividualEvent) {
			ParentIndividualEvent e = (ParentIndividualEvent) event;

			GraphNode newNode = currentGen.addIndividual(e.getChild());

			GraphBond bond = new GraphBond(this, e.getOperator(), lastGen.getOrigin(), newNode,
					lastGen.getGraphNode(e.getParents()));

			pnl.add(bond);

		} else if (event instanceof StartGeneration) {
			StartGeneration e = (StartGeneration) event;

			Population p = e.getPopulation();
			p.sort();
			currentGen.addPopulation(p);
			pnl.add(currentGen);
			lastGen = currentGen;
			currentGen = new GraphGen(this, e.getGeneration());
		} else if (event instanceof EndRun) {
			currentGen.addPopulation(((EndRun) event).getPopulation());
			lastGen = currentGen;
			pnl.add(currentGen);
		}
	}

	/**
	 * The ActionListener inherited method to receive the timer's action events.
	 * Refreshs the panel.
	 * 
	 * @param arg0 the <code>ActionEvent</code>
	 */
	public void actionPerformed(ActionEvent arg0) {
		if (Utilities.isVisible(graph)) {
			int popSize = Config.getInstance().get(Population.SIZE);
			int width = (int) (popSize * (getDiameter() + getHgap()) + 30);
			int height = (int) (getDiameter() + getVgap()) * (currentGen.getGenenratioNo() + 1);
			pnl.setPreferredSize(new Dimension(width, height));
			pnl.revalidate();
			pnl.repaint();
		}
	}
}
