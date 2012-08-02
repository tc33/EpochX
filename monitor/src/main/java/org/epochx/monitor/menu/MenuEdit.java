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
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.epochx.monitor.Monitor;
import org.epochx.monitor.dialog.Dialog;
import org.epochx.monitor.dialog.DialogComponentAdder;

/**
 * The edit menu.
 */
public class MenuEdit extends Menu {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = -7644685940734428304L;
	
	private final JMenuItem addComponent = new JMenuItem("Add a Componant");

	/**
	 * 
	 * Constructs a <code>MenuEdit</code>.
	 * 
	 * @param m the owner <code>Monitor</code>.
	 */
	MenuEdit(Monitor m) {
		super(m);
		setName("Edit Menu");
		setText("Edit");
		setMnemonic('E');

		// addComponent Item.
		addComponent.setMnemonic('C');
		addComponent.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		Dialog dialog;	
		if (monitor.getRowCount() > 1 || monitor.getColCount() > 1)
			dialog = new DialogComponentAdder(monitor);
		else
			dialog = new DialogComponentAdder(monitor, 1, 1);
		addComponent.addActionListener(dialog);
		add(addComponent);
	}

}