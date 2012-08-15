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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.JLayeredPane;
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
public class PnlGraph extends JLayeredPane implements Listener<Event>, ActionListener {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = 4849953816024039299L;

	/**
	 * Convenience object defining the Highlight color.
	 */
	public static final Color HIGHLIGHT_COLOR = Color.GREEN;
	
	/**
	 * Convenience object defining the Bond layer.
	 */
	private static final Integer BOND_LAYER = new Integer(-10);
	
	/**
	 * Convenience object defining the DEFAULT layer.
	 */
	private static final Integer DEFAULT_LAYER = new Integer(0);
	
	/**
	 * The parent <code>Graph</code>.
	 */
	private final Graph graph;

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
	 * All accesses must be <b>synchronized</b> because of concurrency with the
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

	
	/**
	 * Constructs a <code>PnlGraph</code>.
	 * 
	 * 
	 * @param graph the parent <code>Graph</code>.
	 * @param diameter the node diameter.
	 * @param hgap the horizontal gap between two column of node.
	 * @param vgap the vertical gap between two row of node.
	 */
	public PnlGraph(Graph graph, int diameter, int hgap, int vgap) {
		super();
		this.graph = graph;
		this.diameter = diameter;
		this.hgap = hgap;
		this.vgap = vgap;
		this.fitnesses = new TreeSet<Fitness>();

		setBackground(Color.white);
		currentGen = new GraphGen(this, 0);

		Timer timer = new Timer(1000, this);
		timer.start();

		EventManager.getInstance().add(StartGeneration.class, this);
		EventManager.getInstance().add(EndRun.class, this);
		EventManager.getInstance().add(ParentIndividualEvent.class, this);
	}

	/**
	 * Returns the parent <code>Graph</code>.
	 * @return the parent <code>Graph</code>.
	 */
	public Graph getGraph() {
		return graph;
	}

	/**
	 * Returns the diameter of nodes.
	 * @return the diameter of nodes.
	 */
	public int getDiameter() {
		return diameter;
	}

	/**
	 * Returns the horizontal gap.
	 * @return the horizontal gap.
	 */
	public int getHgap() {
		return hgap;
	}
	
	/**
	 * Returns the vertical gap.
	 * @return the vertical gap.
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
			int x = (int)( lastGen.getX() );
			int y = (int)( lastGen.getY()+getDiameter()/2 );
			int width = (int)( lastGen.getWidth() );
			int height = (int)( currentGen.getY()-lastGen.getY() );
			Rectangle bounds = new Rectangle(x, y, width, height);

			GraphBond bond = new GraphBond(this, e.getOperator(), bounds, newNode,
					lastGen.getGraphNodes(e.getParents()));

			add(bond, BOND_LAYER, -1);

		} else if (event instanceof StartGeneration) {
			StartGeneration e = (StartGeneration) event;

			Population p = e.getPopulation();
			p.sort();
			currentGen.addPopulation(p);
			add(currentGen, DEFAULT_LAYER);
			lastGen = currentGen;
			currentGen = new GraphGen(this, e.getGeneration());
		} else if (event instanceof EndRun) {
			currentGen.addPopulation(((EndRun) event).getPopulation());
			lastGen = currentGen;
			add(currentGen, DEFAULT_LAYER);
		}
	}

	/**
	 * The ActionListener inherited method to receive the timer's action events;
	 * Refreshs the panel, only if visible.
	 * 
	 * @param arg0 the <code>ActionEvent</code>.
	 */
	public void actionPerformed(ActionEvent arg0) {
		if (Utilities.isVisible(graph)) {
			int popSize = Config.getInstance().get(Population.SIZE);
			int width = (int) (popSize * (getDiameter() + getHgap()) + 30);
			int height = (int) ((getDiameter() + getVgap()) * (currentGen.getGenenratioNo() + 1));
			setPreferredSize(new Dimension(width, height));
			revalidate();
			repaint();
		}
	}
}
