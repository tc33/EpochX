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

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.epochx.monitor.MonitorUtilities;
import org.epochx.monitor.graph.GraphVertex;
import org.epochx.monitor.graph.GraphViewEvent;
import org.epochx.monitor.graph.GraphViewEvent.GraphViewProperty;
import org.epochx.monitor.graph.GraphViewListener;

public class InformationPanel extends JPanel implements GraphViewListener, Runnable, MouseWheelListener {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = 1780839026099516116L;

	/**
	 * The <code>GraphVertex</code> whose informations are displayed.
	 */
	public GraphVertex vertex;

	/**
	 * The <code>JTextArea</code> which shows the generation number of the
	 * vertex.
	 */
	public JTextArea txtrGenNo;

	/**
	 * The <code>JTextArea</code> which shows the <code>Fitness</code> of the
	 * vertex.
	 */
	public JTextArea txtrFitness;

	/**
	 * The <code>JTextArea</code> which shows the <code>Operator</code> of the
	 * vertex.
	 */
	public JTextArea txtrOperator;

	/**
	 * The <code>JTextArea</code> which shows the <code>String</code> value of
	 * the vertex.
	 */
	public JTextArea txtrValue;
	
	/*
	 * Labels
	 */
	public JPanel pnlMisc = new JPanel(new FlowLayout(FlowLayout.LEADING));
	public JLabel lblGenNo = new JLabel("Generation :");
	public JLabel lblFitness = new JLabel("Fitness :");
	public JLabel lblOperator = new JLabel("Operator :");
	public JLabel lblValue = new JLabel("Value :");
	public JScrollPane scrollPane = new JScrollPane();

	/**
	 * Constructs a <code>InformationPanel</code>.
	 */
	public InformationPanel() {
		super();
		
		this.vertex = null;
		this.txtrGenNo = new JTextArea();
		this.txtrFitness = new JTextArea();
		this.txtrOperator = new JTextArea();
		this.txtrValue = new JTextArea();
		this.pnlMisc = new JPanel(new FlowLayout(FlowLayout.LEADING));
		this.lblGenNo = new JLabel("Generation :");
		this.lblFitness = new JLabel("Fitness :");
		this.lblOperator = new JLabel("Operator :");
		this.lblValue = new JLabel("Value :");
		this.scrollPane = new JScrollPane();
		
		txtrGenNo.setEditable(false);
		txtrFitness.setEditable(false);
		txtrOperator.setEditable(false);
		txtrValue.setEditable(false);		
		
		scrollPane.setBorder(new CompoundBorder(new EmptyBorder(0, 10, 0, 10), UIManager.getBorder("ScrollPane.border")));
		scrollPane.getViewport().addMouseWheelListener(this);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		setBorder(new TitledBorder(null, "Individual Informations", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(UIManager.getColor("TabbedPane.contentAreaColor"));
	}

	/**
	 * Creates and shows the Pannel.
	 */
	public synchronized void run() {
		removeAll();
		
		pnlMisc.setPreferredSize(new Dimension(400, 25));
		add(pnlMisc);
		
		pnlMisc.add(lblGenNo);
		pnlMisc.add(txtrGenNo);

		pnlMisc.add(Box.createHorizontalStrut(10));
		
		pnlMisc.add(lblFitness);
		pnlMisc.add(txtrFitness);

		pnlMisc.add(Box.createHorizontalStrut(10));
		
		pnlMisc.add(lblOperator);;
		pnlMisc.add(txtrOperator);

		pnlMisc.add(Box.createHorizontalStrut(10));
		
		pnlMisc.add(lblValue);
		txtrValue.setEditable(false);
		
		scrollPane.setViewportView(txtrValue);
		scrollPane.validate();
		add(scrollPane);

		MonitorUtilities.setBackground(this, UIManager.getColor("TabbedPane.contentAreaColor"));
		txtrValue.updateUI();
		scrollPane.getViewport().updateUI();

		refresh();
	}

	/**
	 * Sets the <code>GraphVertex</code>.
	 * <p>
	 * Creates and shows the panel if the <code>GraphVertex</code> was
	 * previously null, refreshs the pannel otherwise.
	 * </p>
	 * 
	 * @param vertex the <code>GraphVertex</code> to set.
	 */
	public void setVertex(GraphVertex vertex) {
		
		if(vertex == null) {
			removeAll();
		}
		else if (this.vertex == null) {
			this.vertex = vertex;
			SwingUtilities.invokeLater(this);
		}
		else {
			this.vertex = vertex;
			refresh();
		}
	}

	public void refresh() {
		if (this.vertex != null) {
			
			synchronized (this) {
				txtrGenNo.setText(String.valueOf(vertex.getGenerationNo()));
				txtrFitness.setText(vertex.getFitness().toString());
				txtrOperator.setText(vertex.getOperator() == null ? "New" : vertex.getOperator().getClass().getSimpleName());
				txtrValue.setText(vertex.getIndividual().toString());
				//txtrValue.scrollRectToVisible(new Rectangle(0, 0, 100, 1000));
				txtrValue.setCaretPosition(0);
			}
			revalidate();
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

		}

	}
	
	////////////////////////////////////////////////////////////////////////////
	//               M O U S E       L I S T E N E R S                        //
	////////////////////////////////////////////////////////////////////////////

	
	public void mouseWheelMoved(MouseWheelEvent e) {
		Point p = scrollPane.getViewport().getViewPosition();
		p.translate(e.getWheelRotation()*10, 0);
		txtrValue.repaint();
		scrollPane.getViewport().setViewPosition(p);
		
	}

}