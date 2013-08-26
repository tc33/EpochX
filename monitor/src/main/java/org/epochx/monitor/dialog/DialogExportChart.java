/*
 * Copyright 2007-2013
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
 * The latest version is available from: http://www.epochx.org
 */
package org.epochx.monitor.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.epochx.monitor.Monitor;
import org.epochx.monitor.MonitorUtilities;
import org.epochx.monitor.chart.Chart;

/**
 * A <code>DialogExportChart</code> extends a <code>Dialog</code> to export a
 * <code>Chart</code> in a choosable size to a <code>File</code> selected by a
 * <code>JFileChooser</code>.
 * 
 * @see Dialog
 */
public class DialogExportChart extends Dialog {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = 2539570526477026677L;

	/**
	 * The <code>Chart</code> to export.
	 */
	private final Chart chart;

	/**
	 * The <code>JFileChooser</code> to choose the file in which the
	 * <code>Chart</code> must be exported.
	 */
	protected final JFileChooser fileChooser;

	/**
	 * An <code>ExportFilter</code> for raster format.
	 */
	private final ExportFilter imgFilter = new ExportFilter("Classic image format", "bmp", "gif", "jpg", "png");

	/**
	 * An <code>ExportFilter</code> for vectorial format.
	 */
	private final ExportFilter vectorFilter = new ExportFilter("Vectorial format", "eps", "ps");

	/**
	 * Constructs a <code>DialogExportChart</code>.
	 * 
	 * @param monitor the parent <code>Monitor</code>.
	 * @param c the <code>Chart</code> to export.
	 */
	public DialogExportChart(Monitor monitor, Chart c) {
		super(monitor, "Select Size");
		this.chart = c;
		fileChooser = new JFileChooser();
	}

	@Override
	public void run() {

		final Dimension size = chart.getSize();

		// Label
		JLabel label = new JLabel("Enter a size (Min 200x100, Max 1600x1400) :");

		// Width & Height selection Pane.
		JLabel widthLabel = new JLabel("Width :");
		final JTextField widthField = new JTextField(String.valueOf((int) size.getWidth()));
		widthField.setPreferredSize(new Dimension(50, 20));

		JLabel heightLabel = new JLabel("Height :");
		final JTextField heightField = new JTextField(String.valueOf((int) size.getHeight()));
		heightField.setPreferredSize(new Dimension(50, 20));

		JPanel mainPane = new JPanel();
		mainPane.add(widthLabel);
		mainPane.add(widthField);
		mainPane.add(heightLabel);
		mainPane.add(heightField);

		// Button Pane.
		JButton okButton = new JButton("OK");
		getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				try {
					int width = Integer.parseInt(widthField.getText());
					int height = Integer.parseInt(heightField.getText());

					if (width < 200 || width > 1600 || height < 100 || height > 1400)
						throw new NumberFormatException();

					size.setSize(width, height);

					fileChooser.setAcceptAllFileFilterUsed(false);
					fileChooser.addChoosableFileFilter(imgFilter);
					fileChooser.setFileFilter(imgFilter);
					fileChooser.addChoosableFileFilter(vectorFilter);

					fileChooser.setSelectedFile(new File(chart.getName() + ".png"));

					fileChooser.addPropertyChangeListener(JFileChooser.FILE_FILTER_CHANGED_PROPERTY, new PropertyChangeListener() {

						public void propertyChange(PropertyChangeEvent arg0) {
							if (fileChooser.getFileFilter() == imgFilter)
								fileChooser.setSelectedFile(new File(chart.getName() + ".png"));
							else if (fileChooser.getFileFilter() == vectorFilter)
								fileChooser.setSelectedFile(new File(chart.getName() + ".ps"));
						}
					});

					if (fileChooser.showSaveDialog(monitor) == JFileChooser.APPROVE_OPTION) {
						if (fileChooser.getFileFilter() == imgFilter) {
							chart.export(fileChooser.getSelectedFile(), Chart.FORMAT_RASTER, size);
						} else if (fileChooser.getFileFilter() == vectorFilter) {
							chart.export(fileChooser.getSelectedFile(), Chart.FORMAT_VECTOR, size);
						}

					}

				} catch (NumberFormatException e) {
					setVisible(false);
					JOptionPane.showMessageDialog(monitor, "Please enter a correct size.", "Wrong size error", JOptionPane.ERROR_MESSAGE);
					setVisible(true);
				}
				setVisible(false);
			}
		});

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});

		JPanel buttonPane = new JPanel();
		buttonPane.add(okButton);
		buttonPane.add(cancelButton);

		// Content Pane settings.
		JPanel contentPane = new JPanel(new BorderLayout());
		final int BORDERWIDTH = 3;
		contentPane.setBorder(new EmptyBorder(BORDERWIDTH, BORDERWIDTH, BORDERWIDTH, BORDERWIDTH));

		contentPane.add(label, BorderLayout.NORTH);
		contentPane.add(mainPane, BorderLayout.CENTER);
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
