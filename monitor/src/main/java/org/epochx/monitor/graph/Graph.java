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
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 * A <code>Graph</code> draw a visualization graph to monitor the evolution
 * process.
 */
public class Graph extends JPanel implements Runnable {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = 522739825029020356L;

	/**
	 * The <code>GraphViewModel</code>.
	 */
	private GraphViewModel viewModel;

	/**
	 * The <code>GraphModel</code>.
	 */
	private GraphModel model;

	/**
	 * The <code>GraphHeader</code>.
	 */
	private GraphHeader graphHeader;

	/**
	 * The <code>GraphView</code>.
	 */
	private GraphView view;

	/**
	 * The <code>GraphRowHeader</code>.
	 */
	private GraphRowHeader graphRowHeader;

	/**
	 * The <code>JScrollPane</code>.
	 */
	private JScrollPane scrollPane;

	////////////////////////////////////////////////////////////////////////////
	//                    C O N S T R U C T O R S                             //
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Constructs a <code>Graph</code> with a default properties.
	 * <p>
	 * Default view properties :
	 * <ul>
	 * <li>name : <code>"Graph"</code>
	 * <li>diameter : {@link GraphViewModel#DEFAULT_DIAMETER}
	 * <li>gaps : proportionate to the diameter
	 * </ul>
	 * </p>
	 */
	public Graph() {
		super(new BorderLayout());

		setName("Visualization Graph");

		this.viewModel = new GraphViewModel();
		this.model = new GraphModel();
		this.graphHeader = new GraphHeader(viewModel);
		this.view = new GraphView(viewModel, model);
		this.graphRowHeader = new GraphRowHeader(viewModel);
		this.scrollPane = new JScrollPane();

		this.view.addMouseListener(viewModel);
		this.view.addMouseMotionListener(viewModel);
		this.view.addComponentListener(graphRowHeader);

		// Creates and show the graph.
		SwingUtilities.invokeLater(this);
	}

	/**
	 * Constructs a <code>Graph</code> with a specified name.
	 * 
	 * @param name the name of this graph.
	 */
	public Graph(String name) {
		this();
		setName(name);
	}

	/**
	 * Constructs a <code>Graph</code>.
	 * 
	 */
	public Graph(String name, GraphModel model) {
		this();
		setName(name);
		setModel(model);
	}

	/**
	 * Constructs a <code>Graph</code> with specified name, model and view
	 * model.
	 * 
	 * @param name the name.
	 * @param model the model.
	 * @param viewModel the view model.
	 */
	public Graph(String name, GraphModel model, GraphViewModel viewModel) {
		this();

		setName(name);
		setModel(model);
		setViewModel(viewModel);
	}

	////////////////////////////////////////////////////////////////////////////
	//             R U N N A B L E   M E T H O D                              //
	////////////////////////////////////////////////////////////////////////////

	/**
	 * The <code>Runnable</code> implemented method. Creates and shows the
	 * panel.
	 */
	public void run() {
		removeAll();

		scrollPane.setViewportView(view);
		scrollPane.setRowHeaderView(graphRowHeader);
		scrollPane.setPreferredSize(new Dimension(900, 600));
		scrollPane.revalidate();

		if (graphHeader != null) {
			add(graphHeader, BorderLayout.NORTH);
		}
		add(scrollPane, BorderLayout.CENTER);

		revalidate();
		repaint();
	}

	////////////////////////////////////////////////////////////////////////////
	//             G E T T E R S  &  S E T T E R S                            //
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the <code>GraphViewModel</code>.
	 * 
	 * @return the <code>GraphViewModel</code>.
	 */
	public GraphViewModel getViewModel() {
		return viewModel;
	}

	/**
	 * Sets the <code>GraphViewModel</code> and updates compoments (header,
	 * view, row header).
	 * 
	 * @param viewModel the <code>GraphViewModel</code> to set.
	 */
	public void setViewModel(GraphViewModel viewModel) {

		if (graphHeader != null) {
			graphHeader.setViewModel(viewModel);
		}
		if (view != null) {
			view.setViewModel(viewModel);
		}
		if (graphRowHeader != null) {
			graphRowHeader.setViewModel(viewModel);
		}

		this.viewModel = viewModel;
		revalidate();
	}

	/**
	 * Returns the <code>GraphModel</code>.
	 * 
	 * @return the <code>GraphModel</code>.
	 */
	public GraphModel getModel() {
		return model;
	}

	/**
	 * Sets the <code>GraphModel</code> and update the view with the new model.
	 * 
	 * @param model the <code>GraphModel</code> to set.
	 */
	public void setModel(GraphModel model) {

		if (view != null) {
			view.setModel(model);
		}

		this.model = model;
	}

	/**
	 * Returns the <code>GraphHeader</code>.
	 * 
	 * @return the <code>GraphHeader</code>.
	 */
	public GraphHeader getGraphHeader() {
		return graphHeader;
	}

	/**
	 * Sets the <code>GraphHeader</code>.
	 * 
	 * @param graphHeader the <code>GraphHeader</code> to set.
	 */
	public void setGraphHeader(GraphHeader graphHeader) {
		this.graphHeader = graphHeader;
		SwingUtilities.invokeLater(this);
	}

	/**
	 * Returns the <code>GraphView</code>.
	 * 
	 * @return the <code>GraphView</code>.
	 */
	public GraphView getView() {
		return view;
	}

	/**
	 * Sets the <code>GraphView</code>. Manages the listeners update.
	 * 
	 * @param view the <code>GraphView</code> to set.
	 */
	public void setView(GraphView graphView) {

		if (this.view != null) {
			this.view.removeMouseListener(viewModel);
			this.view.removeMouseMotionListener(viewModel);
			if (graphRowHeader != null) {
				this.view.removeComponentListener(graphRowHeader);
			}
		}

		if (graphView != null) {
			graphView.addMouseListener(viewModel);
			graphView.addMouseMotionListener(viewModel);
			if (graphRowHeader != null) {
				graphView.addComponentListener(graphRowHeader);
			}
		}

		this.view = graphView;

		SwingUtilities.invokeLater(this);
	}

	/**
	 * Returns the <code>GraphRowHeader</code>.
	 * 
	 * @return the <code>GraphRowHeader</code>.
	 */
	public GraphRowHeader getGraphRowHeader() {
		return graphRowHeader;
	}

	/**
	 * Sets the <code>GraphRowHeader</code>.
	 * 
	 * @param graphRowHeader the <code>GraphRowHeader</code> to set.
	 */
	public void setGraphRowHeader(GraphRowHeader graphRowHeader) {
		this.graphRowHeader = graphRowHeader;
		SwingUtilities.invokeLater(this);
	}

	@Override
	public String toString() {
		return getName();
	}

	////////////////////////////////////////////////////////////////////////////
	//             U T I L I T I E S   M E T H O D S                          //
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Utility method. Returns the <code>GraphModel</code> stored in the
	 * specified file.
	 * 
	 * @param file the file in which themodel is to be extracted.
	 * @return the model from the specified file.
	 */
	public static GraphModel loadInputModel(String file) {
		GraphModel model = null;
		try {

			// On récupère maintenant les données !
			ObjectInputStream stream = new ObjectInputStream(new BufferedInputStream(
					new FileInputStream(new File(file))));

			model = (GraphModel) stream.readObject();

			stream.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return model;
	}

}
