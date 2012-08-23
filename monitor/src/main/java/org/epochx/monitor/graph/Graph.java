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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.epochx.monitor.Utilities;


/**
 * A <code>Graph</code> draw a visualization graph to monitor the evolution process.
 */
public class Graph extends JPanel implements Runnable, ActionListener {

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
	 * The <code>JScrollPane</code>.
	 */
	private JScrollPane scrollPane;

	/**
	 * The <code>GraphFooter</code>.
	 */
	private GraphFooter graphFooter;

	/**
	 * Constructs a <code>Graph</code> with a default properties.
	 * <p>
	 * Default view properties :
	 * <ul>
	 * <li>name : <code>"Graph"+noInstances</code>
	 * <li>comparator : null
	 * <li>diameter : {@link GraphViewModel#DEFAULT_DIAMETER}
	 * <li>gaps : proportionate to the diameter
	 * </ul>
	 * </p>
	 */
	public Graph() {
		this("Graph " + noInstances, null, GraphViewModel.DEFAULT_DIAMETER);
	}

	/**
	 * Constructs a <code>Graph</code> with a specified name.
	 * <p>
	 * Default view properties :
	 * <ul>
	 * <li>comparator : null
	 * <li>diameter : {@link GraphViewModel#DEFAULT_DIAMETER}
	 * <li>gaps : proportionate to the diameter
	 * </ul>
	 * </p>
	 * 
	 * @param name the name of this graph.
	 */
	public Graph(String name) {
		this(name, null, GraphViewModel.DEFAULT_DIAMETER);
	}

	/**
	 * Constructs a <code>Graph</code> with a specified comparator.
	 * <p>
	 * Default view properties :
	 * <ul>
	 * <li>name : <code>"Graph"+noInstances</code>
	 * <li>diameter : {@link GraphViewModel#DEFAULT_DIAMETER}
	 * <li>gaps : proportionate to the diameter
	 * </ul>
	 * </p>
	 * 
	 * @param comparator to sort vertices, among :
	 *        <ul>
	 *        <li>{@link FitnessComparator}
	 *        <li>{@link OperatorComparator}
	 *        <li>{@link ParentComparator}
	 *        <li>or any other implementation of
	 *        <code>Comparator<GraphVertex></code>.
	 */
	public Graph(Comparator<GraphVertex> comparator) {
		this("Graph " + noInstances, null, GraphViewModel.DEFAULT_DIAMETER);
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
		this("Graph " + noInstances, null, diameter);
	}

	/**
	 * 
	 * Constructs a <code>Graph</code> with a specified name, comparator and
	 * diameter. Gaps are proportionate to the diameter.
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
	 */
	public Graph(String name, Comparator<GraphVertex> comparator, int diameter) {
		this(name, comparator, diameter, diameter * 0.1, diameter * 3);
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
	public Graph(String name, Comparator<GraphVertex> comparator, double diameter, double hgap, double vgap) {
		super(new BorderLayout());

		this.viewModel = new GraphViewModel(comparator, (int) diameter, (int) hgap, (int) vgap);
		this.model = new GraphModel();
		this.graphHeader = new GraphHeader(viewModel);
		this.graphView = new GraphView(model, viewModel);
		this.graphRowHeader = new GraphRowHeader(viewModel);
		this.graphFooter = new GraphFooter();
		this.scrollPane = new JScrollPane();
		
		setName(name);
		
		viewModel.addGraphViewListener(graphFooter);
		viewModel.addGraphViewListener(graphRowHeader);
		
		SwingUtilities.invokeLater(this);
		
		Timer timer = new Timer(1000, this);
		timer.setInitialDelay(1000);
		timer.start();
	}
	
	/**
	 * The run method to be invoked in the EDT.
	 */
	public void run() {
		removeAll();
		
		scrollPane.setViewportView(graphView);
		scrollPane.setRowHeaderView(graphRowHeader);
		scrollPane.setPreferredSize(new Dimension(900, 600));
		
		add(graphHeader, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(graphFooter, BorderLayout.SOUTH);
	}
	

	/**
	 * The ActionListener inherited method to receive the timer's action events;
	 * Refreshs the panel, only if visible.
	 * 
	 * @param e the <code>ActionEvent</code>.
	 */
	public void actionPerformed(ActionEvent e) {
		if (Utilities.isVisible(this)) {
			graphView.resize();
		}
	}

	
	/**
	 * Returns the <code>GraphViewModel</code>.
	 * @return the <code>GraphViewModel</code>.
	 */
	public GraphViewModel getViewModel() {
		return viewModel;
	}

	
	/**
	 * Sets the <code>GraphViewModel</code>.
	 * @param viewModel the <code>GraphViewModel</code> to set.
	 */
	public void setViewModel(GraphViewModel viewModel) {
		this.viewModel = viewModel;
		SwingUtilities.invokeLater(this);
	}

	
	/**
	 * Returns the <code>GraphModel</code>.
	 * @return the <code>GraphModel</code>.
	 */
	public GraphModel getModel() {
		return model;
	}

	
	/**
	 * Sets the <code>GraphModel</code>.
	 * @param model the <code>GraphModel</code> to set.
	 */
	public void setModel(GraphModel model) {
		this.model = model;
		SwingUtilities.invokeLater(this);
	}

	
	/**
	 * Returns the <code>GraphHeader</code>.
	 * @return the <code>GraphHeader</code>.
	 */
	public GraphHeader getGraphHeader() {
		return graphHeader;
	}

	
	/**
	 * Sets the <code>GraphHeader</code>.
	 * @param graphHeader the <code>GraphHeader</code> to set.
	 */
	public void setGraphHeader(GraphHeader graphHeader) {
		this.graphHeader = graphHeader;
		SwingUtilities.invokeLater(this);
	}

	
	/**
	 * Returns the <code>GraphView</code>.
	 * @return the <code>GraphView</code>.
	 */
	public GraphView getGraphView() {
		return graphView;
	}

	
	/**
	 * Sets the <code>GraphView</code>.
	 * @param graphView the <code>GraphView</code> to set.
	 */
	public void setGraphView(GraphView graphView) {
		this.graphView = graphView;
		SwingUtilities.invokeLater(this);
	}
	
	/**
	 * Returns the <code>GraphRowHeader</code>.
	 * @return the <code>GraphRowHeader</code>.
	 */
	public GraphRowHeader getGraphRowHeader() {
		return graphRowHeader;
	}

	/**
	 * Sets the <code>GraphRowHeader</code>.
	 * @param graphRowHeader the <code>GraphRowHeader</code> to set.
	 */
	public void setGraphRowHeader(GraphRowHeader graphRowHeader) {
		this.graphRowHeader = graphRowHeader;
		SwingUtilities.invokeLater(this);
	}

	/**
	 * Returns the <code>GraphFooter</code>.
	 * @return the <code>GraphFooter</code>.
	 */
	public GraphFooter getGraphFooter() {
		return graphFooter;
	}

	/**
	 * Sets the <code>GraphFooter</code>.
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


}
