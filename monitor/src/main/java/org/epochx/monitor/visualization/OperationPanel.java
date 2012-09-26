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

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import org.epochx.monitor.MonitorUtilities;
import org.epochx.monitor.graph.GraphVertex;
import org.epochx.monitor.graph.GraphViewEvent;
import org.epochx.monitor.graph.GraphViewEvent.GraphViewProperty;
import org.epochx.monitor.graph.GraphViewListener;
import org.epochx.monitor.tree.Tree;
import org.epochx.monitor.tree.TreeEvent;
import org.epochx.monitor.tree.TreeVertex;
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
	
	private JScrollPane parentScrollPane;

	private JPanel childrenPane;
	
	private JScrollPane childrenScrollPane;

	private JSplitPane splitPane;

	private TreeVertex parents[];

	private TreeVertex children[];

	/**
	 * Constructs an <code>OperationPanel</code>.
	 * 
	 */
	public OperationPanel() {
		super();
		this.vertex = null;

		this.parentPane = new JPanel();
		parentPane.setLayout(new BoxLayout(parentPane, BoxLayout.LINE_AXIS));
		
		this.parentScrollPane = new JScrollPane(parentPane);
		parentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

		this.childrenPane = new JPanel();
		childrenPane.setLayout(new BoxLayout(childrenPane, BoxLayout.LINE_AXIS));
		
		this.childrenScrollPane = new JScrollPane(childrenPane);
		childrenScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

		this.splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, parentScrollPane, childrenScrollPane);
		splitPane.setOneTouchExpandable(true);
		splitPane.setBackground(UIManager.getColor("TabbedPane.contentAreaColor"));

		this.parents = new TreeVertex[0];
		this.children = new TreeVertex[0];

		setName("Operation Panel");
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(UIManager.getColor("TabbedPane.contentAreaColor"));

		add(splitPane);
	}
	
	/**
	 * Constructs an <code>OperationPanel</code> with a specified
	 * <code>GraphVertex</code>.
	 * 
	 * @param vertex the specified <code>GraphVertex</code>.
	 */
	public OperationPanel(GraphVertex vertex) {
		this();
		setVertex(vertex);
	}

	/**
	 * Creates the levels and computes the nodes' positions.
	 * <code>Runnable</code> implemented method.
	 * 
	 * @throws IllegalStateException if the vertex is null.
	 */
	public final synchronized void run() throws IllegalStateException {

		if (vertex == null) {
			throw new IllegalStateException("The root node is null.");
		}

		parentPane.removeAll();
		childrenPane.removeAll();

		GraphVertex[] parentVertices = vertex.getParents();
		parents = new TreeVertex[parentVertices.length];

		for (int i = 0; i < parentVertices.length; i++) {

			parents[i] = new TreeVertex(parentVertices[i]);
			parents[i].setBorder(new TitledBorder(null, "Parent " + (i + 1), TitledBorder.LEADING, TitledBorder.TOP,
					null, null));

			parentPane.add(parents[i]);
		}

		GraphVertex[] siblingVertices = vertex.getSiblings();
		children = new TreeVertex[siblingVertices.length];
		for (int i = 0; i < siblingVertices.length; i++) {

			children[i] = new TreeVertex(siblingVertices[i]);
			children[i].setBorder(new TitledBorder(null, "Sibling " + (i + 1), TitledBorder.LEADING, TitledBorder.TOP,
					null, null));
			children[i].addTreeListener(this);

			childrenPane.add(children[i]);
		}
		colorPoints();
		splitPane.setDividerLocation(0.5);
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
			if (this.vertex != null) {
				SwingUtilities.invokeLater(this);
			}
		} else {
			throw new IllegalArgumentException("This vertex's individual is not an instance of TreeAble.");
		}
	}

	/**
	 * Colors the trees according to the criticals points of the vertex.
	 */
	public void colorPoints() {

		try {
			if (vertex.getOperatorEvent() != null) {
				int[] points = vertex.getOperatorEvent().getPoints();
				int n = points.length;

				// Colors the parents.
				for (int i = 0; i < parents.length && i < n; i++) {
					parents[i].color(1, Tree.DEFAULT_COLOR, true);
					parents[i].color(points[i], MonitorUtilities.COLORS[i], true);
				}

				// Colors the siblings.
				for (int i = 0; i < children.length && i < n; i++) {
					children[i].color(1, Tree.DEFAULT_COLOR, true);
					int r = children[i].getRank();
					children[i].color(points[i], MonitorUtilities.COLORS[n - 1 - r], true);
				}
			}
		} catch (IndexOutOfBoundsException e) {
			
		}
		
	}

	/**
	 * Returns the <code>TreeVertex</code> correponding to the given
	 * <code>GraphVertex</code> in the parent array.
	 * 
	 * @param vertex the vertex whose <code>TreeVertex</code> is to be found.
	 * @return the <code>TreeVertex</code> correponding to the given
	 *         <code>GraphVertex</code> if it is present in the parent array;
	 *         Returns null otherwise.
	 */
	public TreeVertex getTreeVertex(GraphVertex vertex) {
		for (TreeVertex tv: parents) {
			if (tv.getVertex() == vertex) {
				return tv;
			}
		}
		return null;
	}

	/**
	 * The <code>GraphViewListener</code> implemented method ; Recieves a
	 * <code>GraphViewEvent</code>.
	 */
	public void viewChanged(GraphViewEvent e) {
		if (e.getNewValue() instanceof GraphVertex && e.getProperty() == GraphViewProperty.SELECTED_VERTEX) {

			GraphVertex vertex = (GraphVertex) e.getNewValue();
			setVertex(vertex);

		}
	}

	/**
	 * The <code>TreeListener</code> implemented method ; Recieves a
	 * <code>TreeEvent</code> from siblings' tree.
	 */
	public void treeChanged(TreeEvent e) {

		if (e.getSource() instanceof TreeVertex) {

			TreeVertex source = (TreeVertex) e.getSource();

			for (TreeVertex parent: parents) {
				parent.setSelected(false);
				parent.getView().repaint();
			}

			if (e.getProperty() == TreeProperty.SELECTED_NODE && e.getNewValue() instanceof TreeNode) {

				TreeNode selectedNode = (TreeNode) e.getNewValue();

				TreeVertex parent = null;

				if (source.isFromGenitor(selectedNode)) {
					parent = getTreeVertex(source.genitor());

				} else if (source.isFromProvider(selectedNode)) {
					parent = getTreeVertex(source.provider());
				}

				if (parent != null) {
					TreeNode[] finds = parent.findSubsumers(selectedNode, true);
					for(TreeNode find : finds) {
						find.setSelectedAs(selectedNode);
					}
					parent.getView().repaint();
				}
			}
		}
	}

	@Override
	public String toString() {
		return getName();
	}

}
