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
package org.epochx.monitor;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

/**
 * A collection of utility methods.
 * <i>Cannot be instancied.</i>
 */
public final class Utilities {

	private Utilities() {
	}

	/**
	 * Returns <code>true</code> if the <code>JComponent</code> is visible
	 * on the monitor (e.g. his parent tab is selected).
	 * 
	 * @param comp the <code>JComponent</code> to check if selected.
	 * @return <code>true</code> if <code>JComponent</code> is visible on the
	 *         monitor (e.g. his tab is selected).
	 */
	public static boolean isVisible(JComponent comp) {
		Container parent = comp.getParent();
		if (parent == null || !(parent instanceof JTabbedPane))
			return false;

		JTabbedPane tabbedPane = (JTabbedPane) parent;
		if (tabbedPane.getSelectedComponent() == comp)
			return true;
		else
			return false;
	}

	/**
	 * Places the <code>Component</code>  at the center of the screen.
	 * 
	 * @param comp the <code>Component</code> to centre.
	 */
	public static void centre(Component comp) {
		Rectangle r = comp.getGraphicsConfiguration().getBounds();
		Dimension d = comp.getSize();
		int x = (int) (r.getX() + (r.getWidth() - d.getWidth()) / 2);
		int y = (int) (r.getY() + (r.getHeight() - d.getHeight()) / 2);
		comp.setLocation(x, y);
	}

	/**
	 * Places the <code>Component</code>  at the center of if parent.
	 * 
	 * @param comp the <code>Component</code>  to centre.
	 * @throws IllegalArgumentException if the component has no parent.
	 */
	public static void centreRelativeToParent(Component comp) throws IllegalArgumentException {
		try {
			Rectangle r = comp.getParent().getBounds();
			Dimension d = comp.getSize();
			int x = (int) (r.getX() + (r.getWidth() - d.getWidth()) / 2);
			int y = (int) (r.getY() + (r.getHeight() - d.getHeight()) / 2);
			comp.setLocation(x, y);
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("The component has no parent.");
		}
	}

	/**
	 * Returns the last extension (in low case) of a file.
	 * 
	 * @param file the <code>File</code> to get extension.
	 * @return the last extension or null.
	 */
	public static String getExtension(File file) {
		String extension = null;
		String s = file.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			extension = s.substring(i + 1).toLowerCase();
		}

		return extension;
	}

	/**
	 * Returns true if  <i>str</i> is contained in <i>tab</i>.
	 * @param str the <code>String</code> whose presence in the <i>tab</i> is to be tested..
	 * @param tab the <code>String</code> table.
	 * @return true if <i>str</i> in contained in the <i>tab</i>.
	 */
	public static boolean isContained(String str, String ... tab) {
		if (str == null)
			return false;

		for (String s: tab)
			if (str.equals(s))
				return true;
		return false;
	}
}
