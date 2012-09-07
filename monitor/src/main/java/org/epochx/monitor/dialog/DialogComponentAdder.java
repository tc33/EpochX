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
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import org.epochx.monitor.Monitor;
import org.epochx.monitor.MonitorUtilities;

/**
 * A <code>DialogTextFilePrinter</code> extends a <code>Dialog</code> to add a
 * componnent in the Monitor.
 * 
 * @see Dialog
 */
public class DialogComponentAdder extends Dialog {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = 7473612490133574777L;
	
	/**
	 * The row where the component must be added.
	 */
	private final int row;
	
	/**
	 * The column where the component must be added.
	 */
	private final int col;

	/**
	 * Constructs a <code>DialogComponentAdder</code>.
	 * 
	 * @param monitor the owner <code>Monitor</code>.
	 */
	public DialogComponentAdder(Monitor monitor) {
		this(monitor, 0, 0);
	}

	/**
	 * Constructs a <code>DialogComponentAdder</code>.
	 * 
	 * @param monitor the owner <code>Monitor</code>.
	 * @param r the row where the component must be added.
	 * @param c the col where the component must be added.
	 */
	public DialogComponentAdder(Monitor monitor, int r, int c) {
		super(monitor, "Component Selection");
		this.row = r;
		this.col = c;
	}

	@Override
	public void run() {
		// If no component in the list, display a message dialog.
		if (monitor.getComponents().length == 0) {
			JOptionPane.showMessageDialog(monitor, "No Components to add in the list.");
			return;
		}
		// Component Pane.
		JPanel componentPane = new JPanel();
		JLabel componentLabel = new JLabel("Component :");

		final JComboBox componantComboBox = new JComboBox();
		synchronized (monitor) {
			for (JComponent componant: monitor.getComponents())
				componantComboBox.addItem(componant);
		}
		componantComboBox.setPreferredSize(new Dimension(200, 20));

		componentPane.add(componentLabel);
		componentPane.add(componantComboBox);

		// Row & Col selection Pane.
		JLabel rowLabel = new JLabel("Row :");
		JLabel colLabel = new JLabel("Column :");
		
		final JSpinner rowSpinner = new JSpinner(new SpinnerNumberModel(1, 1, monitor.getRowCount(), 1));
		final JSpinner colSpinner = new JSpinner(new SpinnerNumberModel(1, 1, monitor.getColCount(), 1));

		if (row == 0 && col == 0) {
			componentPane.add(rowLabel);
			componentPane.add(rowSpinner);
			componentPane.add(colLabel);
			componentPane.add(colSpinner);
		} else {
			rowSpinner.getModel().setValue(new Integer(row));
			colSpinner.getModel().setValue(new Integer(col));
		}

		// Button Pane.
		JPanel buttonPane = new JPanel();
		JButton okButton = new JButton("OK");
		getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				monitor.add((JComponent) componantComboBox.getSelectedItem(), ((Integer)rowSpinner.getValue()).intValue(), ((Integer)colSpinner.getValue()).intValue());
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
		MonitorUtilities.centreRelativeToParent(this);
		setVisible(true);

	}
}
