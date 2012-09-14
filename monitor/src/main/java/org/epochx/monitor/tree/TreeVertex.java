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
package org.epochx.monitor.tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.UIManager;

import org.epochx.monitor.graph.GraphVertex;
import org.epochx.monitor.graph.GraphViewEvent;
import org.epochx.monitor.graph.GraphViewEvent.GraphViewProperty;
import org.epochx.monitor.graph.GraphViewListener;
import org.epochx.monitor.visualization.InformationPanel;
import org.epochx.refactoring.representation.TreeAble;


/**
 * A <code>TreeVertex</code>.
 */
public class TreeVertex extends Tree implements GraphViewListener, Runnable, TreeListener {
	
	/**
	 * The <code>long</code>/serialVersionUID.
	 */
	private static final long serialVersionUID = 7833835797532203428L;

	private GraphVertex vertex;
	
	private InformationPanel infoPane;
	
	////////////////////////////////////////////////////////////////////////////
	//                    C O N S T R U C T O R S                             //
	////////////////////////////////////////////////////////////////////////////
	
	public TreeVertex() {
		super();
		this.vertex = null;
		this.infoPane = new InformationPanel();
		
		addTreeListener(this);
		
		infoPane.setPreferredSize(new Dimension(350, 22));
		infoPane.setBorder(null);
		add(infoPane, BorderLayout.NORTH);
		
		setName("Vertex Visualizaton");
		setBackground(UIManager.getColor("TabbedPane.contentAreaColor"));
	}
	
	public TreeVertex(GraphVertex vertex) {
		this();
		setVertex(vertex);
	}
	
	////////////////////////////////////////////////////////////////////////////
	//             G E T T E R S  &  S E T T E R S                            //
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the vertex.
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
		if (vertex != null && vertex.getIndividual() instanceof TreeAble) {
			this.vertex = vertex;
			setRoot(vertex.getIndividual());
			infoPane.setVertex(vertex);
		} else if (vertex != null) {
			throw new IllegalArgumentException("This vertex's individual is not an instance of TreeAble.");
		}
	}
	
	/**
	 * Returns the infoPane.
	 * @return the infoPane.
	 */
	public InformationPanel getInfoPane() {
		return infoPane;
	}
	
	/**
	 * Sets the infoPane.
	 * @param infoPane the infoPane to set.
	 */
	public void setInfoPane(InformationPanel informations) {
		this.infoPane = informations;
	}
	

	////////////////////////////////////////////////////////////////////////////
	//             V E R T E X   D E L E G A T E   M E T H O D S              //
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Delegate method.
	 * 
	 * @see org.epochx.monitor.graph.GraphVertex#getRank()
	 */
	public int getRank() {
		return vertex.getRank();
	}

	/**
	 * Delegate method.
	 * 
	 * @see org.epochx.monitor.graph.GraphVertex#getParents()
	 */
	public GraphVertex[] getParents() {
		return vertex.getParents();
	}

	/**
	 * Delegate method.
	 * 
	 * @see org.epochx.monitor.graph.GraphVertex#getSiblings()
	 */
	public GraphVertex[] getSiblings() {
		return vertex.getSiblings();
	}

	/**
	 * Delegate method.
	 * 
	 * @see org.epochx.monitor.graph.GraphVertex#genitorPoint()
	 */
	public int genitorPoint() {
		return vertex != null ? vertex.genitorPoint() : -1;
	}
	
	/**
	 * Delegate method.
	 * 
	 * @see org.epochx.monitor.graph.GraphVertex#providerPoint()
	 */
	public int providerPoint() {
		return vertex != null ? vertex.providerPoint() : -1;
	}

	/**
	 * Delegate method.
	 * 
	 * @see org.epochx.monitor.graph.GraphVertex#genitor()
	 */
	public GraphVertex genitor() {
		return vertex.genitor();
	}

	/**
	 * Delegate method.
	 * 
	 * @see org.epochx.monitor.graph.GraphVertex#provider()
	 */
	public GraphVertex provider() {
		return vertex.provider();
	}
	
	////////////////////////////////////////////////////////////////////////////
	//             C O N V E N I E N C E   M E T H O D S                      //
	////////////////////////////////////////////////////////////////////////////
	
	public TreeNode operationNode() {
		try {
			return get(genitorPoint());
		}catch(IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public boolean isFromGenitor(TreeNode n) {
		return !n.isDescendantOf(operationNode());
	}
	
	public boolean isFromProvider(TreeNode n) {
		return n.isDescendantOf(operationNode());
	}
	
	public void colorMutatedSubTree() {
		if(operationNode() != null){
			operationNode().setColor(Color.CYAN, true);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////
	//             G R A P H  &  T R E E   L I S T E N E R S                  //
	////////////////////////////////////////////////////////////////////////////
	
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
	 * <code>TreeEvent</code> from the tree.
	 */
	public void treeChanged(TreeEvent e) {
		
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof TreeVertex && vertex!=null) {
			TreeVertex tv = (TreeVertex) obj;
			return vertex.equals(tv.getVertex());
		}
		return super.equals(obj);
	}
	
	@Override
	public String toString() {
		return getName();
	}



	
}
