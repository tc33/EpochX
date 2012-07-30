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
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.epochx.monitor.Monitor;
import org.epochx.monitor.chart.Chart;
import org.epochx.monitor.dialog.DialogExportChart;
import org.epochx.monitor.dialog.DialogExportTable;
import org.epochx.monitor.table.Table;

/**
 * 
 */
@SuppressWarnings("serial")
public class MenuFile extends Menu {

	private final JMenu exportMenu;
	private final JMenuItem exit, close;

	MenuFile(Monitor m) {
		super(m);
		setName("File Menu");
		setText("File");
		setMnemonic('F');

		// Export Menu.
		exportMenu = new JMenu("Export");
		exportMenu.setMnemonic('E');
		add(exportMenu);
		run();

		addSeparator();

		// Close Item.
		close = new JMenuItem("Close");
		close.setMnemonic('C');
		close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		close.setToolTipText("Close only the window");
		close.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				monitor.setVisible(false);
			}
		});
		add(close);

		// Exit Item.
		exit = new JMenuItem("Exit");
		exit.setMnemonic('X');
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		exit.setToolTipText("Exit the program");
		exit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		add(exit);

	}

	public void run() {
		// Synchronized block because of concurent acces to the component list.
		synchronized (monitor) {
			// Reset the export menu.
			exportMenu.removeAll();
			for (JComponent component: monitor.getComponentList())

				if (component instanceof Chart) {
					final Chart chart = (Chart) component;
					JMenuItem item = new JMenuItem(chart.getName());
					item.addActionListener(new DialogExportChart(monitor, chart));
					exportMenu.add(item);
				}
			
			exportMenu.addSeparator();
			
			for (JComponent component: monitor.getComponentList())

				if (component instanceof Table) {

					final Table table = (Table) component;
					JMenuItem item = new JMenuItem(table.getName());
					item.addActionListener(new DialogExportTable(monitor, table));
					exportMenu.add(item);
				}
		}
	}
}
