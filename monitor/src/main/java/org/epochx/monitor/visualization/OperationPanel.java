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

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import org.epochx.monitor.MonitorUtilities;
import org.epochx.monitor.graph.GraphVertex;
import org.epochx.monitor.graph.GraphViewEvent;
import org.epochx.monitor.graph.GraphViewEvent.GraphViewProperty;
import org.epochx.monitor.graph.GraphViewListener;
import org.epochx.monitor.tree.TreeEvent;
import org.epochx.monitor.tree.TreeEvent.TreeProperty;
import org.epochx.monitor.tree.TreeListener;
import org.epochx.monitor.tree.TreeNode;
import org.epochx.refactoring.representation.TreeAble;

/**
 * A <code>OperationPanel</code>.
 */
public class OperationPanel extends JPanel implements Runnable, GraphViewListener, TreeListener {

	/**
	 * The serialVersionUID.
	 */
	private static final long serialVersionUID = -1046094773508619690L;

	private GraphVertex vertex;

	private JPanel parentPane;

	private JPanel childrenPane;
	
	private JSplitPane splitPane;

	private TreeVertexPanel parents[];

	private TreeVertexPanel children[];

	public OperationPanel() {
		super();
		this.vertex = null;
		this.parentPane = new JPanel();
		this.childrenPane = new JPanel();
		
		this.splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, parentPane, childrenPane);
		splitPane.setBackground(UIManager.getColor("TabbedPane.contentAreaColor"));

		
		parentPane.setLayout(new BoxLayout(parentPane, BoxLayout.LINE_AXIS));
		childrenPane.setLayout(new BoxLayout(childrenPane, BoxLayout.LINE_AXIS));

		parents = new TreeVertexPanel[0];
		children = new TreeVertexPanel[0];

		setName("Operation Panel");
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(UIManager.getColor("TabbedPane.contentAreaColor"));

		add(splitPane);
	}

	public void run() {

		parentPane.removeAll();
		childrenPane.removeAll();

		GraphVertex[] parentVertices = vertex.getParents();
		parents = new TreeVertexPanel[parentVertices.length];

		for (int i = 0; i < parentVertices.length; i++) {

			parents[i] = new TreeVertexPanel(parentVertices[i]);
			parents[i].setBorder(new TitledBorder(null, "Parent " + (i + 1), TitledBorder.LEADING, TitledBorder.TOP,
					null, null));

			parentPane.add(parents[i]);
		}

		GraphVertex[] siblingVertices = vertex.getSiblings();
		children = new TreeVertexPanel[siblingVertices.length];
		for (int i = 0; i < siblingVertices.length; i++) {

			children[i] = new TreeVertexPanel(siblingVertices[i]);
			children[i].setBorder(new TitledBorder(null, "Sibling " + (i + 1), TitledBorder.LEADING, TitledBorder.TOP,
					null, null));
			children[i].getTree().addTreeListener(this);

			childrenPane.add(children[i]);
		}
		colorPoints();
		splitPane.setDividerLocation(0.5);
	}

	public void colorPoints() {

		if (vertex.getOperatorEvent() != null) {
			int[] points = vertex.getOperatorEvent().getPoints();
			int n = points.length;

			for (int i = 0; i < parents.length && i < n; i++) {
				parents[i].getTree().getRoot().setSubTreeColor(Color.LIGHT_GRAY);
				TreeNode pointNode = parents[i].getTree().getRoot().get(points[i]);
				pointNode.setSubTreeColor(MonitorUtilities.COLORS[i]);
			}

			for (int i = 0; i < children.length && i < n; i++) {
				children[i].getTree().getRoot().setSubTreeColor(Color.LIGHT_GRAY);
				int r = children[i].getVertex().getRank();
				TreeNode pointNode = children[i].getTree().getRoot().get(points[r]);
				pointNode.setSubTreeColor(MonitorUtilities.COLORS[n - 1 - r]);
			}
		}
	}

	/**
	 * Returns the vertex.
	 * 
	 * @return the vertex.
	 */
	public GraphVertex getVertex() {
		return vertex;
	}

	/**
	 * Sets the vertex.
	 * 
	 * @param vertex the vertex to set.
	 * 
	 * @throws IllegalArgumentException if the individual of the given vertex is
	 *         not an instance of TreeAble.
	 */
	public void setVertex(GraphVertex vertex) throws IllegalArgumentException {
		if (vertex.getIndividual() instanceof TreeAble) {
			this.vertex = vertex;
		} else {
			throw new IllegalArgumentException("This vertex's individual is not an instance of TreeAble.");
		}
	}

	/**
	 * The <code>GraphViewListener</code> implemented method ; Recieves a
	 * <code>GraphViewEvent</code>.
	 */
	public void viewChanged(GraphViewEvent e) {
		if (e.getNewValue() instanceof GraphVertex && e.getProperty() == GraphViewProperty.SELECTED_VERTEX) {

			GraphVertex vertex = (GraphVertex) e.getNewValue();
			setVertex(vertex);
			SwingUtilities.invokeLater(this);

		}
	}

	/**
	 * The <code>TreeListener</code> implemented method ; Recieves a
	 * <code>TreeEvent</code> from siblings' tree.
	 */
	public void treeChanged(TreeEvent e) {

		if (e.getNewValue() instanceof TreeNode && e.getProperty() == TreeProperty.SELECTED_NODE) {

			TreeNode select = (TreeNode) e.getNewValue();
			if (select != null) {
				for (TreeVertexPanel parent: parents) {
					parent.getTree().getRoot().setSubTreeColor(Color.LIGHT_GRAY);
					for (TreeNode node: parent.getTree().getRoot().find(select)) {
						node.setSubTreeColor(Color.GREEN);
					}
					parent.getTree().getView().repaint();
				}
			}

		} else if (e.getNewValue() == null && e.getProperty() == TreeProperty.SELECTED_NODE) {

			colorPoints();

		}

	}

	@Override
	public String toString() {
		return getName();
	}

}
