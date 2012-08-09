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
package org.epochx.monitor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.epochx.monitor.Monitor;
import org.epochx.monitor.dialog.DialogTextFilePrinter;


/**
 * The help menu.
 */
public class MenuHelp extends Menu {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = -4502791710861781919L;
	
	private final JMenuItem license;
	private final JMenuItem about;

	MenuHelp(Monitor m) {
		super(m);
		setName("Help Menu");
		setText("Help");
		setMnemonic('H');

		// About Item.
		license = new JMenuItem("LICENSE");
		license.setMnemonic('L');
		license.addActionListener(new DialogTextFilePrinter(monitor, "LICENSE"));
		add(license);

		addSeparator();

		// About Item.
		about = new JMenuItem("About");
		about.setMnemonic('A');
		about.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(monitor, "Copyright 2007-2012\n"
						+ "Lawrence Beadle, Tom Castle, Fernando Otero and Loïc Vaseux\n"
						+ "Licensed under GNU Lesser General Public License");
			}
		});
		add(about);
	}
	
}