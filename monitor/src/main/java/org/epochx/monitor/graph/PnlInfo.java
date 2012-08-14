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

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.UIManager;

public class PnlInfo extends JPanel {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = 1780839026099516116L;

	/**
	 * The <code>GraphNode</code> whose informations are displayed.
	 */
	private GraphNode node;

	/**
	 * The <code>JTextArea</code> which shows the generation number of the node.
	 */
	private JTextArea txtrGenNo;

	/**
	 * The <code>JTextArea</code> which shows the <code>Fitness</code> of the
	 * node.
	 */
	private JTextArea txtrFitness;

	/**
	 * The <code>JTextArea</code> which shows the <code>String</code> value of
	 * the node.
	 */
	private JTextArea txtrValue;

	/**
	 * Constructs a <code>PnlInfo</code>.
	 */
	public PnlInfo() {
		setBorder(new TitledBorder(null, "Individual Informations", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		setPreferredSize(new Dimension(350, 90));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(new JTextArea("Click on an Individual to show informations."));
	}

	/**
	 * Creates and shows the Pannel.
	 */
	private synchronized void createAndShowPanel() {
		removeAll();

		JPanel pnlMisc = new JPanel(new FlowLayout(FlowLayout.LEADING));
		pnlMisc.setPreferredSize(new Dimension(300, 25));
		add(pnlMisc);

		JLabel lblGenNo = new JLabel("Generation :");
		pnlMisc.add(lblGenNo);

		txtrGenNo = new JTextArea();
		txtrGenNo.setText(String.valueOf(node.getGenerationNo()));
		pnlMisc.add(txtrGenNo);

		pnlMisc.add(Box.createHorizontalStrut(10));

		JLabel lblFitness = new JLabel("Fitness :");
		pnlMisc.add(lblFitness);

		txtrFitness = new JTextArea();
		txtrFitness.setText(node.getFitness().toString());
		pnlMisc.add(txtrFitness);

		pnlMisc.add(Box.createHorizontalStrut(10));

		JLabel lblValue = new JLabel("Value :");
		pnlMisc.add(lblValue);

		txtrValue = new JTextArea();
		txtrValue.setEditable(false);
		txtrValue.setText(node.getIndividual().toString());

		JScrollPane scrollPane = new JScrollPane(txtrValue);
		scrollPane.setBorder(new CompoundBorder(new EmptyBorder(0, 10, 0, 10), UIManager.getBorder("ScrollPane.border")));
		scrollPane.setPreferredSize(new Dimension(250, 35));
		add(scrollPane);
	}

	/**
	 * Sets the <code>GraphNode</code>.
	 * <p>
	 * Creates and shows the panel if the <code>GraphNode</code> was previously
	 * null, refreshs the pannel otherwise.
	 * </p>
	 * 
	 * @param node the <code>GraphNode</code> to set.
	 */
	public void setNode(GraphNode node) {
		if (this.node == null) {
			this.node = node;
			createAndShowPanel();
		} else synchronized (this) {
			this.node = node;
			txtrGenNo.setText(String.valueOf(node.getGenerationNo()));
			txtrFitness.setText(node.getFitness().toString());
			txtrValue.setText(node.getIndividual().toString());
			txtrValue.scrollRectToVisible(new Rectangle(0, 0, 100, 1000));
			txtrValue.setCaretPosition(0);
		}
		updateUI();
	}

}