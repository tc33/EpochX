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
package org.epochx.monitor.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.epochx.monitor.Monitor;
import org.epochx.monitor.Utilities;

/**
 * Creates and shows a <code>Dialog</code> to add a componnent in the Monitor.
 * Component 
 */
@SuppressWarnings("serial")
public class DialogComponentAdder extends Dialog {

	/**
	 * The row and column indicating where the component should be added.
	 */
	private final int row, col;

	/**
	 * Constructs a <code>DialogComponentAdder</code>.
	 * 
	 * @param monitor the owner monitor.
	 */
	public DialogComponentAdder(Monitor monitor) {
		this(monitor, 0, 0);
	}

	/**
	 * 
	 * Constructs a <code>DialogComponentAdder</code>.
	 * 
	 * @param monitor the owner monitor.
	 * @param r the row where the component should be added.
	 * @param c the col where the component should be added.
	 */
	public DialogComponentAdder(Monitor monitor, int r, int c) {
		super(monitor, "Component Selection");
		this.row = r;
		this.col = c;
	}

	@Override
	public void run() {
		// If no component in the list, display a message dialog.
		if (monitor.getComponentList().isEmpty()) {
			JOptionPane.showMessageDialog(monitor, "No Components to add in the list.");
			return;
		}
		// Component Pane.
		JPanel componentPane = new JPanel();
		JLabel componentLabel = new JLabel("Component :");

		final JComboBox componantComboBox = new JComboBox();
		synchronized(monitor){
			for (JComponent componant: monitor.getComponentList())
				componantComboBox.addItem(componant);
		}
		componantComboBox.setPreferredSize(new Dimension(200, 20));

		componentPane.add(componentLabel);
		componentPane.add(componantComboBox);

		// Row & Col selection Pane.
		JLabel rowLabel = new JLabel("Row :");
		final JComboBox rowComboBox = new JComboBox();
		for (int i = 1; i <= monitor.getRowCount(); i++)
			rowComboBox.addItem(new Integer(i));

		JLabel colLabel = new JLabel("Column :");
		final JComboBox colComboBox = new JComboBox();
		for (int i = 1; i <= monitor.getColCount(); i++)
			colComboBox.addItem(new Integer(i));

		if (row == 0 && col == 0) {
			componentPane.add(rowLabel);
			componentPane.add(rowComboBox);
			componentPane.add(colLabel);
			componentPane.add(colComboBox);
		} else {
			rowComboBox.setSelectedItem(new Integer(row));
			colComboBox.setSelectedItem(new Integer(col));
		}

		// Button Pane.
		JPanel buttonPane = new JPanel();
		JButton okButton = new JButton("OK");
		getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				monitor.add((JComponent) componantComboBox.getSelectedItem(), ((Integer) rowComboBox.getSelectedItem()).intValue(), ((Integer) colComboBox.getSelectedItem()).intValue());
				setVisible(false);
			}
		});
		buttonPane.add(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		buttonPane.add(cancelButton);

		// Content Pane settings.
		JPanel contentPane = new JPanel(new BorderLayout());
		final int BORDERWIDTH = 3;
		contentPane.setBorder(new EmptyBorder(BORDERWIDTH, BORDERWIDTH, BORDERWIDTH, BORDERWIDTH));

		contentPane.add(componentPane, BorderLayout.NORTH);
		contentPane.add(buttonPane, BorderLayout.SOUTH);

		// JDialog settings.
		setModal(true);
		setResizable(false);
		setContentPane(contentPane);
		setPreferredSize(contentPane.getPreferredSize());
		pack();
		Utilities.centreRelativeToParent(this);
		setVisible(true);

	}
}
