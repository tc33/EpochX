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
package org.epochx.monitor.menu;

import javax.swing.JMenu;
import javax.swing.SwingUtilities;

import org.epochx.monitor.Monitor;

/**
 * An abstract <code>Monitor</code> menu.
 * <p>
 * Some Menu inherited objects have to be refresh after their initialization .
 * For those, override the {@link #run()} method, and invoke the refresh in the
 * EDT by using {@link SwingUtilities#invokeLater(Runnable)} method.
 * </p>
 */
public abstract class Menu extends JMenu implements Runnable {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = 364992162121875988L;

	/**
	 * The owner <code>Monitor</code>.
	 */
	protected final Monitor monitor;

	protected Menu(Monitor monitor) {
		this.monitor = monitor;
	}

	/**
	 * The <code>Runnable</code> inherited method to override if the Menu have
	 * to be refreshed.
	 */
	public void run() {

	}

}
