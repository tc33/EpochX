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

import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;

import org.epochx.monitor.Monitor;

/**
 * The menu bar.
 */
public class MenuBar extends JMenuBar implements Runnable {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = -6577329669637640329L;

	/**
	 * Constructs a <code>MenuBar</code>.
	 * 
	 * @param monitor the owner <code>Monitor</code>.
	 */
	public MenuBar(Monitor monitor) {

		add(new MenuFile(monitor));
		add(new MenuEdit(monitor));
		add(new MenuView(monitor));
		add(new MenuHelp(monitor));

	}

	/**
	 * The <code>Runnable</code> inherited method, invoked in the EDT by the
	 * {@link SwingUtilities#invokeLater(Runnable)} method, when menus have to
	 * be refreshed.
	 */
	public void run() {

		for (int i = 0; i < getMenuCount(); i++) {
			((Menu) getMenu(i)).run();
		}

	}
}
