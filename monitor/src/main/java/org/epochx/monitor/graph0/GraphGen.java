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
package org.epochx.monitor.graph0;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.epochx.Config;
import org.epochx.Fitness;
import org.epochx.Individual;
import org.epochx.Population;

/**
 * 
 */
public class GraphGen extends JPanel implements MouseMotionListener, Runnable {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = 7492658787665441580L;

	/**
	 * The parent <code>PnlGraph</code>.
	 */
	private final PnlGraph pnlGraph;

	/**
	 * The generation number.
	 */
	private final int genenratioNo;

	/**
	 * The list of nodes.
	 */
	private final LinkedList<GraphNode> nodes;

	/**
	 * The array of constraints.
	 */
	private DefaultConstraints[] constraints;

	/**
	 * Constructs a <code>GraphGen</code>.
	 * 
	 * @param pnlGraph the parent <code>PnlGraph</code>.
	 * @param genNo the generation number.
	 */
	public GraphGen(PnlGraph pnlGraph, int genNo) {
		super(new GridBagLayout());
		this.pnlGraph = pnlGraph;
		this.genenratioNo = genNo;
		this.nodes = new LinkedList<GraphNode>();

		int popSize = Config.getInstance().get(Population.SIZE);
		this.constraints = new DefaultConstraints[popSize];
		for (int i = 0; i < constraints.length; i++) {
			constraints[i] = new DefaultConstraints(i);
		}
		
		int x = getDiameter();
		int y = genNo * (getDiameter() + getVgap());
		setLocation(x, y);
		
		int width = (int) (popSize * (getDiameter() + getHgap()));
		int height = getDiameter();
		setSize(width, height);
		
		setOpaque(true);

		// setBorder(BorderFactory.createLineBorder(Color.black));
		// setBackground(Color.BLACK);

		addMouseMotionListener(this);

		validate();
		repaint();
	}

	/**
	 * Returns the parent <code>PnlGraph</code>.
	 * 
	 * @return the parent <code>PnlGraph</code>.
	 */
	public PnlGraph getPnlGraph() {
		return pnlGraph;
	}

	/**
	 * Returns the generation number.
	 * 
	 * @return the generation number.
	 */
	public int getGenenratioNo() {
		return genenratioNo;
	}

	/**
	 * Returns the diameter.
	 * 
	 * @return the diameter.
	 */
	public int getDiameter() {
		return pnlGraph.getDiameter();
	}

	/**
	 * Returns the horizontal gap.
	 * 
	 * @return the horizontal gap.
	 */
	public int getHgap() {
		return pnlGraph.getHgap();
	}

	/**
	 * Returns the vertical gap.
	 * 
	 * @return the vertical gap.
	 */
	public int getVgap() {
		return pnlGraph.getVgap();
	}

	/**
	 * Returns the <code>GraphNode</code> at the specified position in the nodes
	 * list.
	 * 
	 * @param index index of the <code>GraphNode</code> to return.
	 * @return the <code>GraphNode</code> at the specified position.
	 */
	public GraphNode getGraphNodeAt(int index) {
		GraphNode node;
		synchronized (nodes) {
			node = nodes.get(index);
		}
		return node;
	}

	/**
	 * Returns the <code>GraphNode</code> corresponding to the specified
	 * <code>Individual</code>.
	 * 
	 * @param ind the <code>Individual</code> whose node is to be returned.
	 * @return the <code>GraphNode</code> corresponding to the specified
	 *         <code>Individual</code>.
	 */
	public GraphNode getGraphNode(Individual ind) {
		int i = indexOf(ind);
		GraphNode node;
		synchronized (nodes) {
			node = nodes.get(i);
		}
		return node;
	}

	/**
	 * Returns an array of <code>GraphNode</code> corresponding to the specified
	 * <code>Individual</code> array.
	 * 
	 * @param individuals the <code>Individual</code> array whose nodes is to be
	 *        returned.
	 * @return the <code>GraphNode</code> array.
	 */
	public GraphNode[] getGraphNodes(Individual[] individuals) {
		GraphNode[] graphNodes = new GraphNode[individuals.length];
		for (int i = 0; i < individuals.length; i++) {
			graphNodes[i] = getGraphNode(individuals[i]);
		}
		return graphNodes;
	}

	/**
	 * Adds a <code>GraphNode</code> to the nodes list.
	 * 
	 * @param graphNode the <code>GraphNode</code> to add.
	 */
	public void addGraphNode(GraphNode graphNode) {
		synchronized (nodes) {
			nodes.add(graphNode);
		}
	}

	/**
	 * Creates a <code>GraphNode</code> corresponding to the specified
	 * <code>Individual</code>, adds it to the nodes list, and returns it.
	 * 
	 * @param ind the <code>Individual</code> to add.
	 * @return the <code>GraphNode</code> corresponding to the specified
	 *         <code>Individual</code>.
	 */
	public GraphNode addIndividual(Individual ind) {
		GraphNode node = new GraphNode(this, ind);
		synchronized (nodes) {
			nodes.add(node);
		}
		return node;
	}

	/**
	 * Adds an entire population to this generation.
	 * 
	 * @param p the population to add.
	 */
	public void addPopulation(Population p) {
		for (int i = 0; i < p.size(); i++) {
			GraphNode node = new GraphNode(this, p.get(i));
			synchronized (nodes) {
				if (!nodes.contains(node)) {
					nodes.add(node);
				}
			}
		}
		try {
			SwingUtilities.invokeAndWait(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		reorderGrid(Graph.FITNESS_COMPARATOR);
	}

	/**
	 * Returns the index of the first occurrence of the given object in the
	 * nodes list ; returns -1 if not found.
	 * 
	 * <p>
	 * The given object can be an instance of <code>GraphNode</code> or
	 * <code>Individual</code>. Throws a <code>ClassCastException</code>
	 * otherwise.
	 * </p>
	 * 
	 * @param obj the object whose index in the nodes list is to be found.
	 * @return the index of the first occurrence of the given object in the ;
	 *         returns -1 if not found.
	 * @throws ClassCastException if the given object is not a
	 *         <code>GraphNode</code> or an <code>Individual</code>.
	 */
	public int indexOf(Object obj) {
		Individual individual;
		if (obj instanceof GraphNode) {
			individual = ((GraphNode) obj).getIndividual();
		} else if (obj instanceof Individual) {
			individual = (Individual) obj;
		} else {
			throw new ClassCastException(obj.getClass().getSimpleName());
		}

		synchronized (nodes) {
			for (int i = 0; i < nodes.size(); i++) {
				if (System.identityHashCode(nodes.get(i).getIndividual()) == System.identityHashCode(individual))
					return i;
			}
		}
		return -1;
	}

	/**
	 * Returns the position of the given node, on the X axis, according to the
	 * bounds.
	 * 
	 * @param node the node whose X position is to be compute.
	 * @return the position of the given node, on the X axis, according to the
	 *         bounds.
	 */
	public double getXPosition(GraphNode node) {
		int index;
		synchronized (nodes) {
			index = nodes.indexOf(node);
		}
		if (index == -1) {
			throw new IllegalArgumentException("The specified node is not part of this generation.");
		}

		return getHgap() + getDiameter() / 2.0 + index * (getDiameter() + getHgap());
	}

	/**
	 * Adds a <code>Fitness</code> in the <code>PnlGraph</code>.
	 * 
	 * @param fitness the fitness to add.
	 */
	public void addFitness(Fitness fitness) {
		pnlGraph.addFitness(fitness);
	}

	/**
	 * Computes and returns the <code>Color</code> corresponding to the rank of
	 * the specified <code>Fitness</code> in the <code>FitnessSet</code>.
	 * 
	 * @param fitness the <code>Fitness</code> whose color is to be computes
	 * @return the <code>Color</code> corresponding to the rank of the specified
	 *         <code>Fitness</code> in the <code>FitnessSet</code>
	 */
	public Color getFitnessColor(Fitness fitness) {
		return pnlGraph.getFitnessColor(fitness);
	}

	/**
	 * 
	 */
	public void run() {
		synchronized (nodes) {
			for (int i = 0; i < nodes.size(); i++) {
				GraphNode node = nodes.get(i);
				((GridBagLayout) getLayout()).setConstraints(node, constraints[i]);
				add(node, constraints[i]);
			}
		}
		validate();
		repaint();
	}

	public void reorderGrid(Comparator<GraphNode> comparator) {
		synchronized (nodes) {
			if (comparator != null) {
				Collections.sort(nodes, comparator);
			}
			for (int i = 0; i < nodes.size(); i++) {
				GraphNode node = nodes.get(i);
				((GridBagLayout) getLayout()).setConstraints(node, constraints[i]);
			}
		}
		revalidate();
		//repaint();
	}

	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}
	

	public void mouseDragged(MouseEvent arg0) {
		System.out.println(arg0.getSource());
		
	}

	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * A <code>DefaultConstraints</code> extends <code>GridBagConstraints</code>
	 * to provide a suitable constraints set for a <code>GraphGen</code> panel.
	 */
	private class DefaultConstraints extends GridBagConstraints {

		/**
		 * Generated serial UID.
		 */
		private static final long serialVersionUID = 2488348419852951608L;

		/**
		 * Constructs a <code>DefaultConstraints</code>.
		 * 
		 * @param index the X index in the grid.
		 */
		DefaultConstraints(int index) {
			super();
			gridx = index;
			gridy = 0;

			gridwidth = 1;
			gridheight = 1;

			weightx = 0;
			weighty = 0;

			insets = new Insets(0, getHgap(), 0, 0);
			ipadx = 0;
			ipady = 0;

			anchor = GridBagConstraints.WEST;
			fill = GridBagConstraints.BOTH;
		}

	}

}