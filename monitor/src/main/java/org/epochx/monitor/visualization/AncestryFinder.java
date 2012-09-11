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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import org.epochx.monitor.graph.GraphVertex;
import org.epochx.monitor.graph.GraphViewEvent;
import org.epochx.monitor.graph.GraphViewEvent.GraphViewProperty;
import org.epochx.monitor.graph.GraphViewListener;
import org.epochx.monitor.graph.GraphViewModel;
import org.epochx.monitor.tree.TreeEvent;
import org.epochx.monitor.tree.TreeEvent.TreeProperty;
import org.epochx.monitor.tree.TreeListener;
import org.epochx.monitor.tree.TreeNode;

/**
 * A <code>AncestryFinder</code>.
 */
public class AncestryFinder extends JPanel implements GraphViewListener, TreeListener, KeyListener {

	/**
	 * The serialVersionUID.
	 */
	private static final long serialVersionUID = 8966503575608003402L;

	private GraphViewModel viewModel;

	private TreeVertexPanel individual;

	private TreeVertexPanel ancestor;
	
	private JSplitPane splitPane;
	
	private boolean CTRL_PRESSED;

	public AncestryFinder(GraphViewModel viewModel) {
		super();
		this.viewModel = viewModel;
		this.individual = new TreeVertexPanel();
		this.ancestor = new TreeVertexPanel();
		
		this.splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, ancestor, individual);
		splitPane.setBackground(UIManager.getColor("TabbedPane.contentAreaColor"));
		
		this.CTRL_PRESSED = false;

		setName("Ancestry Finder");
		setLayout(new BorderLayout());
		setBackground(UIManager.getColor("TabbedPane.contentAreaColor"));

		individual.getTree().addTreeListener(this);

		addKeyListener(this);
		add(splitPane, BorderLayout.CENTER);
		doLayout();
	}



	/**
	 * The <code>GraphViewListener</code> implemented method ; Recieves a
	 * <code>GraphViewEvent</code>.
	 */
	public void viewChanged(GraphViewEvent e) {
		if (e.getNewValue() instanceof GraphVertex && e.getProperty() == GraphViewProperty.SELECTED_VERTEX) {

			GraphVertex vertex = (GraphVertex) e.getNewValue();
			
			if(individual.getTree() == null || individual.getTree().getSelectedNode() == null) {
				
				individual.setVertex(vertex);
				individual.run();
				individual.colorCriticalPoint();
				
			}
			else {
				ancestor.setVertex(vertex);
				ancestor.run();
			}
			splitPane.setDividerLocation(0.5);

		}
	}

	/**
	 * The <code>TreeListener</code> implemented method ; Recieves a
	 * <code>TreeEvent</code> from sibling's tree.
	 */
	public void treeChanged(TreeEvent e) {

		if (e.getNewValue() instanceof TreeNode && e.getProperty() == TreeProperty.SELECTED_NODE) {

			TreeNode select = (TreeNode) e.getNewValue();
			
			selectAncestor(select);

		} else if (e.getNewValue() == null && e.getProperty() == TreeProperty.SELECTED_NODE) {
			
			individual.colorCriticalPoint();
			
		}

	}

	public void selectAncestor(TreeNode subtree) {

		viewModel.deselectedAll();
		
		boolean b = true;
		GraphVertex vertex = individual.getVertex();
		viewModel.getVertexModel(vertex).setSelected(true);

		while (b) {

			if (vertex.getOperator() == null || vertex.getOperatorEvent() == null) {
				b = false;
			} else {

				TreeNode tree = new TreeNode(vertex);
				int rank = vertex.getRank();
				int point = vertex.getOperatorEvent().getPoints()[rank];
				int parentCount = vertex.getOperator().inputSize();

				TreeNode criticalNode = tree.get(point);

				if (criticalNode.isAncestor(subtree)) {
					viewModel.getVertexModel(vertex).setSelected(true);
					vertex = vertex.getParents()[parentCount - 1 - rank];

				} else {
					TreeNode[] finds = tree.find(subtree);
					b = false;
					for (TreeNode node: finds) {
						if (!node.isDescendant(criticalNode)) {
							b = true;
						}
					}
					if (b) {
						viewModel.getVertexModel(vertex).setSelected(true);
						vertex = vertex.getParents()[rank];
					}
				}
			}
		}
		viewModel.fireGraphViewEvent(new GraphViewEvent(viewModel, GraphViewProperty.REFRESH));

	}

	@Override
	public String toString() {
		return getName();
	}


	public void keyPressed(KeyEvent arg0) {
		CTRL_PRESSED = true;
		System.out.println(arg0);
		
	}

	public void keyReleased(KeyEvent arg0) {
		CTRL_PRESSED = false;
		
	}

	public void keyTyped(KeyEvent arg0) {
		
	}

}
