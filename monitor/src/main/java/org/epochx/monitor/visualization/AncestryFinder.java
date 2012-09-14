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
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.epochx.monitor.graph.GraphVertex;
import org.epochx.monitor.graph.GraphViewEvent;
import org.epochx.monitor.graph.GraphViewEvent.GraphViewProperty;
import org.epochx.monitor.graph.GraphViewListener;
import org.epochx.monitor.graph.GraphViewModel;
import org.epochx.monitor.tree.TreeEvent;
import org.epochx.monitor.tree.TreeEvent.TreeProperty;
import org.epochx.monitor.tree.TreeListener;
import org.epochx.monitor.tree.TreeNode;
import org.epochx.monitor.tree.TreeVertex;

/**
 * A <code>AncestryFinder</code>.
 */
public class AncestryFinder extends JPanel implements GraphViewListener, TreeListener, ListSelectionListener {

	/**
	 * The serialVersionUID.
	 */
	private static final long serialVersionUID = 8966503575608003402L;

	private FinderTask ft;
	
	private GraphViewModel viewModel;

	private TreeVertex individual;

	private JPanel ancestorPane;
	
	private VerticesTable table;

	private JSplitPane horizontalSplitPane;
	
	private JSplitPane verticalSplitPane;

	

	public AncestryFinder() {
		super();
		this.ft = new FinderTask();
		this.viewModel = null;
		this.individual = new TreeVertex();
		this.ancestorPane = new JPanel();
		ancestorPane.setLayout(new BoxLayout(ancestorPane, BoxLayout.LINE_AXIS));
		
		JScrollPane scrollPane = new JScrollPane(ancestorPane);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		
		this.table = new VerticesTable();
		
		table.setPreferredSize(new Dimension(300, 100));
		table.addListSelectionListener(this);
		
		this.horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, table, scrollPane);
		horizontalSplitPane.setOneTouchExpandable(true);

		this.verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, horizontalSplitPane, individual);
		verticalSplitPane.setBackground(UIManager.getColor("TabbedPane.contentAreaColor"));
		verticalSplitPane.setOneTouchExpandable(true);

		setName("Ancestry Finder");
		setLayout(new BorderLayout());
		setBackground(UIManager.getColor("TabbedPane.contentAreaColor"));

		individual.addTreeListener(this);

		add(verticalSplitPane, BorderLayout.CENTER);
		doLayout();
		horizontalSplitPane.setDividerLocation(0.7);
	}

	public AncestryFinder(GraphViewModel viewModel) {
		this();
		setViewModel(viewModel);
	}

	/**
	 * Returns the view model.
	 * 
	 * @return the view model.
	 */
	public GraphViewModel getViewModel() {
		return viewModel;
	}

	/**
	 * Sets the view model.
	 * 
	 * @param model the view model to set.
	 */
	public void setViewModel(GraphViewModel model) {
		if (viewModel != null) {
			viewModel.removeGraphViewListener(this);
		}
		if (model != null) {
			model.addGraphViewListener(this);
		}
		this.viewModel = model;

	}

	/**
	 * The <code>GraphViewListener</code> implemented method ; Recieves a
	 * <code>GraphViewEvent</code>.
	 */
	public void viewChanged(GraphViewEvent e) {
		if (e.getNewValue() instanceof GraphVertex && e.getProperty() == GraphViewProperty.SELECTED_VERTEX) {

			GraphVertex vertex = (GraphVertex) e.getNewValue();

			individual.setVertex(vertex);
			individual.colorMutatedSubTree();
			
			ancestorPane.removeAll();
			table.clear();

			verticalSplitPane.setDividerLocation(0.3);
			horizontalSplitPane.resetToPreferredSizes();

		}
	}
	

	/**
	 * The <code>ListSelectionListener</code> implemented method ; Recieves a
	 * <code>ListSelectionEvent</code> from <code>VerticeTable</code>.
	 */
	public void valueChanged(ListSelectionEvent e) {
		if(table.table.getRowSelectionAllowed()) {
			//System.out.println(e);
			ancestorPane.removeAll();
			for(TreeVertex tv : table.getSelectedVertex()) {
				ancestorPane.add(tv);
			}
			ancestorPane.doLayout();
			verticalSplitPane.validate();
			
		}

	}

	/**
	 * The <code>TreeListener</code> implemented method ; Recieves a
	 * <code>TreeEvent</code> from sibling's tree.
	 */
	public void treeChanged(TreeEvent e) {

		if (e.getProperty() == TreeProperty.SELECTED_NODE) {

			if (viewModel != null) {
				viewModel.deselectedAll();
				viewModel.fireGraphViewEvent(new GraphViewEvent(viewModel, GraphViewProperty.REFRESH));
			}
			ft.cancel(true);
			table.clear();

			if (e.getNewValue() instanceof TreeNode) {
				TreeNode select = (TreeNode) e.getNewValue();
				
				ft = new FinderTask(individual, select);
				ft.execute();

			}

		}

	}

	@Override
	public String toString() {
		return getName();
	}
	
	class FinderTask extends SwingWorker<Integer, TreeVertex> {

		private TreeVertex tv;
		
		private TreeNode n;
		
		/**
		 * Constructs a <code>FinderTask</code>.
		 * 
		 */
		public FinderTask() {
			super();
		}
		
		public FinderTask(TreeVertex vertex, TreeNode subTree) {
			super();
			tv = vertex;
			n = subTree;
		}
		
		@Override
		protected Integer doInBackground() throws IllegalStateException {
			
			if(tv == null || n == null) {
				throw new IllegalStateException("Null fields.");
			}
			
			table.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			table.table.setRowSelectionAllowed(false);
			table.table.setFocusable(false);
			selectAncestor(tv, n);
			return null;
		}
		
		private void selectAncestor(TreeVertex tv, TreeNode n) {
			
			if(isCancelled()) {
				return;
			}
			
			viewModel.getVertexModel(tv.getVertex()).setSelected(true);

			if (tv.isFromGenitor(n)) {

				TreeVertex parent = new TreeVertex(tv.genitor());
				TreeNode criticalPoint = null;
				try {
					criticalPoint = parent.get(tv.genitorPoint());
				} catch (IndexOutOfBoundsException e) {
					System.out.print(e.getMessage());
				}
				
				TreeNode[] finds = parent.findSubsumers(n, true);
				for(TreeNode find : finds) {
					if (!find.isDescendantOf(criticalPoint)) {
						find.setSelectedAs(n);
						table.addVertex(parent, true);
						selectAncestor(parent, find);	
					}
				}
			} else if (tv.isFromProvider(n)) {

				TreeVertex parent = new TreeVertex(tv.provider());
				TreeNode criticalPoint = null;
				try {
					criticalPoint = parent.get(tv.providerPoint());
				} catch (IndexOutOfBoundsException e) {
					System.out.print(e.getMessage());
				}
				
				TreeNode[] finds = parent.findSubsumers(n, true);
				for(TreeNode find : finds) {
					if (find.isDescendantOf(criticalPoint)) {
						find.setSelectedAs(n);
						table.addVertex(parent, false);
						selectAncestor(parent, find);
					}
				}
			}
		}
		
		protected void done() {
			table.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			table.table.setRowSelectionAllowed(true);
			table.table.setFocusable(true);
			if (viewModel != null) {
				viewModel.fireGraphViewEvent(new GraphViewEvent(viewModel, GraphViewProperty.REFRESH));
			}
			
		}

	}

}
