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

import javax.swing.JPanel;

/**
 * 
 */
public class Graph extends JPanel {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = 522739825029020356L;

	/**
	 * The number of created instances.
	 */
	private static int noInstances = 0;

	/**
	 * The default diameter of nodes.
	 */
	private final static int DEFAULT_DIAMETER = 15;

	/**
	 * The default horizontal gap between two nodes.
	 */
	private final static int DEFAULT_HGAP = 1;

	/**
	 * The default vertical gap between two nodes.
	 */
	private final static int DEFAULT_VGAP = 50;

	/**
	 * The <code>Pnlgraph</code>.
	 */
	private final PnlGraph pnlGraph;

	/**
	 * The <code>PnlInfo</code>.
	 */
	private final PnlInfo pnlInfo;
	
	/**
	 * Constructs a <code>Graph</code> with a default name.
	 * <p>
	 * Default name : <code>"graph"+noInstances</code>.
	 * </p>
	 */
	public Graph() {
		this("Graph " + noInstances, DEFAULT_DIAMETER, DEFAULT_HGAP, DEFAULT_VGAP);
	}

	/**
	 * Constructs a <code>Graph</code> with a specified name.
	 * 
	 * @param name the Name given to the main component.
	 */
	public Graph(String name) {
		this(name, DEFAULT_DIAMETER, DEFAULT_HGAP, DEFAULT_VGAP);
	}

	/**
	 * Constructs a <code>Graph</code> with specified arguments.
	 * 
	 * @param name the Name given to the main component.
	 * @param diameter the node diameter.
	 * @param hgap the horizontal gap between two column of node.
	 * @param vgap the vertical gap between two row of node.
	 */
	public Graph(String name, int diameter, int hgap, int vgap) {

		super(new BorderLayout());
		this.pnlGraph = new PnlGraph(this, diameter, hgap, vgap);
		this.pnlInfo = new PnlInfo();

		setName(name);
		
		add(pnlGraph, BorderLayout.CENTER);
		add(pnlInfo, BorderLayout.SOUTH);
	}

	/**
	 * Returns the <code>PnlGraph</code>.
	 * 
	 * @return the <code>PnlGraph</code>.
	 */
	public PnlGraph getPnlGraph() {
		return pnlGraph;
	}

	/**
	 * Returns the <code>PnlInfo</code>.
	 * 
	 * @return the <code>PnlInfo</code>.
	 */
	public PnlInfo getPnlInfo() {
		return pnlInfo;
	}	
	
	@Override
	public String toString() {
		return getName();
	}

}
