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
package org.epochx.monitor.visualization;

import javax.swing.JPanel;

import org.epochx.monitor.graph.GraphViewEvent;
import org.epochx.monitor.graph.GraphViewListener;
import org.epochx.monitor.graph.GraphViewModel;
import org.epochx.monitor.tree.Tree;
import org.epochx.monitor.tree.TreeEvent;
import org.epochx.monitor.tree.TreeListener;


/**
 * A <code>AncestryFinder</code>.
 */
public class AncestryFinder extends JPanel implements GraphViewListener, Runnable, TreeListener {
	
	/**
	 * The serialVersionUID.
	 */
	private static final long serialVersionUID = 8966503575608003402L;
	
	
	private GraphViewModel viewModel;
	
	private Tree tree;
	
	private Tree ancestor;
	
	public AncestryFinder() {
		this.viewModel = null;
		this.tree = new Tree();
		this.ancestor = new Tree();
		
		setName("Ancestry Finder");
	}
	
	
	public void run() {
		
		
	}

	/**
	 * The <code>GraphViewListener</code> implemented method ; Recieves a
	 * <code>GraphViewEvent</code>.
	 */
	public void viewChanged(GraphViewEvent e) {
		
		
	}

	/**
	 * 
	 */
	public void treeChanged(TreeEvent e) {
		
		
	}
	
	@Override
	public String toString() {
		return getName();
	}
	

}
