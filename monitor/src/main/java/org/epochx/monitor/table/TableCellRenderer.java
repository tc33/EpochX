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
package org.epochx.monitor.table;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * A <code>TableCellRenderer</code> provides the default cell renderer for a
 * <code>Table</code>.
 */
public class TableCellRenderer extends DefaultTableCellRenderer {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = 7403603819246025918L;

	/**
	 * Constructs a <code>TableCellRenderer</code>.
	 */
	TableCellRenderer() {
		super();
		setHorizontalAlignment(JLabel.LEFT);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		component.setPreferredSize(new Dimension(component.getHeight(),
				component.getFontMetrics(component.getFont()).stringWidth(value.toString())));
		return component;
	}
}
