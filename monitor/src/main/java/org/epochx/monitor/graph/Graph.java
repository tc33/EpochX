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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 * A <code>Graph</code> draw a visualization graph to monitor the evolution
 * process.
 */
public class Graph extends JPanel implements Runnable {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = 522739825029020356L;

	/**
	 * The number of created instances.
	 */
	private static int noInstances = 0;

	/**
	 * The <code>GraphViewModel</code>.
	 */
	private GraphViewModel viewModel;

	/**
	 * The <code>GraphModel</code>.
	 */
	private GraphModel model;

	/**
	 * The <code>GraphMouseListener</code>.
	 */
	private GraphMouseListener mouseListener;

	/**
	 * The <code>GraphHeader</code>.
	 */
	private GraphHeader graphHeader;

	/**
	 * The <code>GraphView</code>.
	 */
	private GraphView graphView;

	/**
	 * The <code>GraphRowHeader</code>.
	 */
	private GraphRowHeader graphRowHeader;

	/**
	 * The <code>GraphFooter</code>.
	 */
	private GraphFooter graphFooter;

	/**
	 * The <code>JScrollPane</code>.
	 */
	private JScrollPane scrollPane;

	/**
	 * Constructs a <code>Graph</code> with a default properties.
	 * <p>
	 * Default view properties :
	 * <ul>
	 * <li>name : <code>"Graph"+noInstances</code>
	 * <li>diameter : {@link GraphViewModel#DEFAULT_DIAMETER}
	 * <li>gaps : proportionate to the diameter
	 * </ul>
	 * </p>
	 */
	public Graph() {
		
		this("Graph " + noInstances, GraphViewModel.DEFAULT_DIAMETER);
	}

	/**
	 * Constructs a <code>Graph</code> with a specified name.
	 * <p>
	 * Default view properties :
	 * <ul>
	 * <li>diameter : {@link GraphViewModel#DEFAULT_DIAMETER}
	 * <li>gaps : proportionate to the diameter
	 * </ul>
	 * </p>
	 * 
	 * @param name the name of this graph.
	 */
	public Graph(String name) {
		this(name, GraphViewModel.DEFAULT_DIAMETER);
	}

	/**
	 * 
	 * Constructs a <code>Graph</code> with a specified diameter.
	 * <p>
	 * Default view properties :
	 * <ul>
	 * <li>name : <code>"Graph"+noInstances</code>
	 * <li>comparator : null
	 * <li>gaps : proportionate to the diameter
	 * </ul>
	 * </p>
	 * 
	 * @param diameter the diameter of the vertices. Must be an <b>even</b>
	 *        number !
	 */
	public Graph(int diameter) {
		this("Graph " + noInstances, diameter);
	}

	/**
	 * 
	 * Constructs a <code>Graph</code> with a specified name, comparator and
	 * diameter. Gaps are proportionate to the diameter.
	 * 
	 * @param name the name of this graph.
	 * @param diameter the diameter of the vertices. Must be an <b>even</b>
	 *        number !
	 */
	public Graph(String name, int diameter) {
		this(name, diameter, diameter * 0.1, diameter * 3);
	}

	/**
	 * Constructs a <code>Graph</code> with a specified diameter size, and
	 * specified gaps.
	 * 
	 * @param name the name of this graph.
	 * @param diameter the diameter of the vertices. Must be an <b>even</b>
	 *        number !
	 * @param hgap the horizontal gap.
	 * @param vgap the vertical gap.
	 */
	public Graph(String name, double diameter, double hgap, double vgap) {
		this(name, new GraphModel(), new GraphViewModel((int) diameter, (int) hgap, (int) vgap));
	}

	/**
	 * Constructs a <code>Graph</code>.
	 * 
	 */
	public Graph(String name, GraphModel model) {
		this(name, model, new GraphViewModel());
	}

	/**
	 * Constructs a <code>Graph</code>.
	 * 
	 * @param name the name of this graph.
	 * @param comparator to sort vertices, among :
	 *        <ul>
	 *        <li>{@link FitnessComparator}
	 *        <li>{@link OperatorComparator}
	 *        <li>{@link ParentComparator}
	 *        <li>or any other implementation of
	 *        <code>Comparator<GraphVertex></code>.
	 * @param diameter the diameter of the vertices. Must be an <b>even</b>
	 *        number !
	 * @param hgap the horizontal gap.
	 * @param vgap the vertical gap.
	 */
	public Graph(String name, GraphModel model, GraphViewModel viewModel) {
		super(new BorderLayout());

		setName(name);

		this.viewModel = viewModel;
		this.model = model;
		this.mouseListener = new GraphMouseListener();
		this.graphHeader = new GraphHeader(viewModel);
		this.graphView = new GraphView(viewModel, model);
		this.graphRowHeader = new GraphRowHeader(viewModel, model);
		this.graphFooter = new GraphFooter();
		this.scrollPane = new JScrollPane();

		this.viewModel.addGraphViewListener(graphFooter);

		this.graphView.addMouseListener(mouseListener);
		this.graphView.addMouseMotionListener(mouseListener);
		this.graphView.addComponentListener(graphRowHeader);

		// Create and show the graph.
		SwingUtilities.invokeLater(this);
	}

	/**
	 * The run method to be invoked in the EDT.
	 */
	public void run() {
		removeAll();

		scrollPane.setViewportView(graphView);
		scrollPane.setRowHeaderView(graphRowHeader);
		scrollPane.setPreferredSize(new Dimension(900, 600));
		scrollPane.revalidate();

		add(graphHeader, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(graphFooter, BorderLayout.SOUTH);

		revalidate();
		repaint();
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
		this.viewModel = viewModel;
		SwingUtilities.invokeLater(this);
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
		this.model = model;
		SwingUtilities.invokeLater(this);
	}

	/**
	 * Returns the <code>GraphMouseListener</code>.
	 * 
	 * @return the <code>GraphMouseListener</code>.
	 */
	public GraphMouseListener getMouseListener() {
		return mouseListener;
	}

	/**
	 * Sets the <code>GraphMouseListener</code>.
	 * 
	 * @param mouseListener the <code>GraphMouseListener</code> to set.
	 */
	public void setMouseListener(GraphMouseListener mouseListener) {
		this.mouseListener = mouseListener;
	}

	/**
	 * Returns the <code>GraphHeader</code>.
	 * 
	 * @return the <code>GraphHeader</code>.
	 */
	public GraphHeader getGraphHeader() {
		return graphHeader;
	}

	/**
	 * Sets the <code>GraphHeader</code>.
	 * 
	 * @param graphHeader the <code>GraphHeader</code> to set.
	 */
	public void setGraphHeader(GraphHeader graphHeader) {
		this.graphHeader = graphHeader;
		SwingUtilities.invokeLater(this);
	}

	/**
	 * Returns the <code>GraphView</code>.
	 * 
	 * @return the <code>GraphView</code>.
	 */
	public GraphView getGraphView() {
		return graphView;
	}

	/**
	 * Sets the <code>GraphView</code>.
	 * 
	 * @param graphView the <code>GraphView</code> to set.
	 */
	public void setGraphView(GraphView graphView) {
		this.graphView = graphView;
		SwingUtilities.invokeLater(this);
	}

	/**
	 * Returns the <code>GraphRowHeader</code>.
	 * 
	 * @return the <code>GraphRowHeader</code>.
	 */
	public GraphRowHeader getGraphRowHeader() {
		return graphRowHeader;
	}

	/**
	 * Sets the <code>GraphRowHeader</code>.
	 * 
	 * @param graphRowHeader the <code>GraphRowHeader</code> to set.
	 */
	public void setGraphRowHeader(GraphRowHeader graphRowHeader) {
		this.graphRowHeader = graphRowHeader;
		SwingUtilities.invokeLater(this);
	}

	/**
	 * Returns the <code>GraphFooter</code>.
	 * 
	 * @return the <code>GraphFooter</code>.
	 */
	public GraphFooter getGraphFooter() {
		return graphFooter;
	}

	/**
	 * Sets the <code>GraphFooter</code>.
	 * 
	 * @param graphFooter the <code>GraphFooter</code> to set.
	 */
	public void setGraphFooter(GraphFooter graphFooter) {
		this.graphFooter = graphFooter;
		SwingUtilities.invokeLater(this);
	}

	@Override
	public String toString() {
		return getName();
	}

	/**
	 * Utility method. Returns the <code>GraphModel</code> stored in the
	 * specified file.
	 * 
	 * @param file the file in which themodel is to be extracted.
	 * @return the model from the specified file.
	 */
	public static GraphModel loadInputModel(String file) {
		GraphModel model = null;
		try {

			// On récupère maintenant les données !
			ObjectInputStream stream = new ObjectInputStream(new BufferedInputStream(
					new FileInputStream(new File(file))));

			model = (GraphModel) stream.readObject();

			stream.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return model;
	}

}
