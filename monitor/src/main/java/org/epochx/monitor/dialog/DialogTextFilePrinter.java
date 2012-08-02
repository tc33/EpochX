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
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import org.epochx.monitor.Monitor;

/**
 * A <code>DialogTextFilePrinter</code> extends <code>Dialog</code> to display a
 * text file.
 * 
 * @see Dialog
 */
public class DialogTextFilePrinter extends Dialog {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = 1207750960774788045L;

	/**
	 * The name of the file to display on the dialog.
	 */
	private final String fileName;

	/**
	 * Constructs a <code>MonitorDialogFileText</code>.
	 * 
	 * @param monitor the owner <code>Monitor</code>.
	 * @param fileName the name of the file to be displayed.
	 */
	public DialogTextFilePrinter(Monitor monitor, String fileName) {
		this(monitor, fileName, fileName);
	}

	/**
	 * Constructs a <code>MonitorDialogFileText</code>.
	 * 
	 * @param monitor the owner <code>Monitor</code>.
	 * @param fileName the name of the <code>File</code> to be displayed.
	 * @param frameName the name of the <code>Dialog</code> frame.
	 */
	public DialogTextFilePrinter(Monitor monitor, String fileName, String frameName) {
		super(monitor, frameName);
		this.fileName = fileName;
	}

	@Override
	public void run() {
		// Content Pane settings.
		JPanel contentPane = new JPanel(new BorderLayout());
		final int BORDERWIDTH = 3;
		contentPane.setBorder(new EmptyBorder(BORDERWIDTH, BORDERWIDTH, BORDERWIDTH, BORDERWIDTH));

		// Adds the text pane.

		JTextPane textPane = new JTextPane();
		// File reading and buffering.
		BufferedReader file = null;
		StringBuffer buffer = new StringBuffer("");
		int preferredWidth = 0;
		try {
			file = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
			String line;
			while ((line = file.readLine()) != null) {
				int lineWidth = textPane.getFontMetrics(textPane.getFont()).stringWidth(line);
				if (lineWidth > preferredWidth)
					preferredWidth = lineWidth;
				buffer.append(line + "\n");
			}
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		textPane.setText(buffer.toString());
		textPane.setEditable(false);
		textPane.setCaretPosition(0); // Make the scrollbar return to the top.
		textPane.setPreferredSize(new Dimension(preferredWidth, 500));

		contentPane.add(new JScrollPane(textPane), BorderLayout.CENTER);
		contentPane.setPreferredSize(textPane.getPreferredSize());

		// Adds OK button.

		JPanel southPane = new JPanel();
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		southPane.add(okButton);
		contentPane.add(southPane, BorderLayout.SOUTH);

		// JDialog settings

		setModal(false);
		setContentPane(contentPane);
		setPreferredSize(contentPane.getPreferredSize());
		pack();
		setVisible(true);
	}

}
