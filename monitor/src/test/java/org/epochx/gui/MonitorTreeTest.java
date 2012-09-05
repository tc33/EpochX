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
package org.epochx.gui;

import javax.swing.JScrollPane;

import org.epochx.monitor.Monitor;
import org.epochx.monitor.tree.Tree;
import org.epochx.refactoring.representation.TreeNodeAble;


/**
 * A <code>MonitorTreeTest</code>.
 */
public class MonitorTreeTest implements TreeNodeAble {

	String name;
	
	MonitorTreeTest[] children;
	
	public MonitorTreeTest(String name) {
		this.name = name;
		
		children = new MonitorTreeTest[0];
		
	}
	
	public String getName() {
		return name;
	}

	
	public TreeNodeAble[] getChildren() {
		return children;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		MonitorTreeTest root = new MonitorTreeTest("root");
		
		root.children = new MonitorTreeTest[5];
		
		root.children[0] = new MonitorTreeTest("c1");
		root.children[1] = new MonitorTreeTest("c2");
		root.children[2] = new MonitorTreeTest("c3");
		root.children[3] = new MonitorTreeTest("c4");
		root.children[4] = new MonitorTreeTest("c5");
	
		
		root.children[1].children = new MonitorTreeTest[3];
		root.children[1].children[0] = new MonitorTreeTest("c2.1");
		root.children[1].children[1] = new MonitorTreeTest("c2.2");
		root.children[1].children[2] = new MonitorTreeTest("c2.3");
		
		root.children[1].children[1].children =  new MonitorTreeTest[2];
		root.children[1].children[1].children[0] =  new MonitorTreeTest("c2.2.1");
		root.children[1].children[1].children[1] =  new MonitorTreeTest("c2.2.2");
		
		root.children[3].children = new MonitorTreeTest[3];
		root.children[3].children[0] = new MonitorTreeTest("c4.1");
		root.children[3].children[1] = new MonitorTreeTest("c4.2");
		root.children[3].children[2] = new MonitorTreeTest("c4.3");

		
		root.children[3].children[1].children =  new MonitorTreeTest[7];
		root.children[3].children[1].children[0] =  new MonitorTreeTest("c4.2.1");
		root.children[3].children[1].children[1] =  new MonitorTreeTest("c4.2.2");
		root.children[3].children[1].children[2] =  new MonitorTreeTest("c4.2.3");
		root.children[3].children[1].children[3] =  new MonitorTreeTest("c4.2.4");
		root.children[3].children[1].children[4] =  new MonitorTreeTest("c4.2.5");
		root.children[3].children[1].children[5] =  new MonitorTreeTest("c4.2.6");
		root.children[3].children[1].children[6] =  new MonitorTreeTest("c4.2.7");

		
		root.children[3].children[2].children =  new MonitorTreeTest[2];
		root.children[3].children[2].children[0] =  new MonitorTreeTest("c4.3.1");
		root.children[3].children[2].children[1] =  new MonitorTreeTest("c4.3.2");
		
		Tree t = new Tree(root);
		
		JScrollPane pane = new JScrollPane();
		pane.setViewportView(t);
		
		Monitor m = new Monitor("Monitor Tree Test");
		
		m.add(pane);
		
	}

}
