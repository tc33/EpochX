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
	
	private final static int DEFAULT_DIAMETER = 15;
	
	private final static int DEFAULT_HGAP = 0;
	
	private final static int DEFAULT_VGAP = 10;
	
	private final PnlInfo pnlInfo = new PnlInfo();
	
	/**
	 * Constructs a <code>Graph</code> with a default name.
	 * <p>
	 * Default name : <code>"graph"+noInstances</code>.
	 * </p>
	 */
	public Graph() {
		this("graph" + noInstances, DEFAULT_DIAMETER, DEFAULT_HGAP, DEFAULT_VGAP);
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
	 * Constructs a <code>Graph</code> with a specified name.
	 * 
	 * @param name the Name given to the main component.
	 */
	public Graph(String name, int diameter, int hgap, int vgap) {
		
		// Component Settings
		super(new BorderLayout());
		setName(name);
		
		add(new PnlGraph(diameter, hgap, vgap), BorderLayout.CENTER);
		add(pnlInfo, BorderLayout.SOUTH);
		
		GraphNode.setPnlInfo(pnlInfo);

	}
	
	/*public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setBackground(Color.white);
		//g2.clearRect(0, 0, getWidth(), getHeight());
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setStroke(BASIC_STROKE);
		g2.setFont(DEFAULT_FONT);
//		synchronized(this){
//		for(GraphNode node : nodes)
//			node.paintComponent(g2);
//		}

	}*/
	
	public String toString() {
		return getName();
	}

}
