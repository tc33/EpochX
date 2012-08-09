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
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
public class PnlGraph extends JScrollPane implements Listener<Event>{

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = 4849953816024039299L;
	
	private final JPanel pnl;
	
	private final int diameter;

	private final int hgap;
	
	private final int vgap;
	
	private final ArrayList<EndOperator> buffer = new ArrayList<EndOperator>();
	
	private ArrayList<GraphNode> children = new ArrayList<GraphNode>();
	
	private ArrayList<GraphNode> parents = new ArrayList<GraphNode>();
	
	private int genNo = 0;
	
	public PnlGraph(int diameter, int hgap, int vgap) {
		this.diameter = diameter;
		this.hgap = hgap;
		this.vgap = vgap;
				
		pnl = new JPanel(null, true);
		pnl.setBackground(Color.white);
		
		setViewportView(pnl);
		setPreferredSize(new Dimension(800, 600));
		
		GraphNode.setDefaultDiameter(diameter);
		
		EventManager.getInstance().add(EndGeneration.class, this);
		EventManager.getInstance().add(EndOperator.class, this);
	}
	
	public void onEvent(Event event) {
		if (event instanceof EndOperator) {
			System.out.print(2);
			buffer.add((EndOperator) event);
		} else if (event instanceof EndGeneration) {
			refresh((EndGeneration) event);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void refresh(EndGeneration eg) {
		Population p = eg.getPopulation();
		p.sort();
		genNo = eg.getGeneration();
			
		children = new ArrayList<GraphNode>();
		
		int xPos = hgap+diameter/2;
		for (int i = 0; i < p.size(); i++) {
			GraphNode node = new GraphNode(p.get(i), genNo , xPos, genNo*(diameter+vgap));
			pnl.add(node);
			children.add(node);
			xPos+=diameter+hgap;
		}
		//System.out.print(buffer.size());
		if(!parents.isEmpty()) {
			
			for(EndOperator eo : buffer) {
				
				for(Individual father : eo.getParents()) {
					System.out.print(parents.contains(father));
				}
				
				
			}
			
		}
		
		parents = (ArrayList<GraphNode>)children.clone();
		buffer.clear();
		
		pnl.setPreferredSize(new Dimension(xPos, (genNo+1)*(diameter+vgap)));
		pnl.repaint();
		setPreferredSize(new Dimension(xPos+2*diameter, (genNo+1)*(diameter+vgap)));
		revalidate();
		updateUI();
	}

}
