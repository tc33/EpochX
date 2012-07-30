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
package org.epochx.monitor.table;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.Enumeration;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumn;

import org.epochx.event.Event;
import org.epochx.event.EventManager;
import org.epochx.event.Listener;
import org.epochx.event.RunEvent.EndRun;
import org.epochx.event.stat.AbstractStat;

/**
 * A <code>Table</code> is a table which display several stats at several
 * events.
 */
@SuppressWarnings("serial")
public class Table extends JScrollPane {

	/**
	 * The Excel 2000 format constant.
	 */
	public static final String FORMAT_XLS = "xls";

	/**
	 * The Comma-separated Values format constant.
	 */
	public static final String FORMAT_CSV = "csv";
	
	/**
	 * The default refresh rate constant.
	 */
	private final static long DEFAULT_LATENCY = 100;

	/**
	 * The Number of created Instances.
	 */
	private static int noInstances = 0;

	/**
	 * The parent table.
	 */
	private final JTable table;

	/**
	 * The table model.
	 */
	private final TableModel model;

	/**
	 * The default cell renderer.
	 */
	private final TableCellRenderer tableCellRenderer;

	/**
	 * Constructs a <code>Table</code> with a default name.
	 * Default name : <code>"table"+noInstances</code>.
	 */
	public Table() {
		this("table" + noInstances, DEFAULT_LATENCY);
	}

	/**
	 * Constructs a <code>Table</code>.
	 * 
	 * @param name the Name given to the main component.
	 */
	public Table(String name) {
		this(name, DEFAULT_LATENCY);
	}

	/**
	 * Constructs a <code>Table</code>.
	 * 
	 * @param latency the latency rate.
	 */
	public Table(long latency) {
		this("table" + noInstances, latency);
	}

	/**
	 * Constructs a <code>Table</code>.
	 * 
	 * @param name the Name given to the main component.
	 * @param latency the latency rate.
	 */
	public Table(String name, long latency) {
		noInstances++;
		setName(name);

		// Creates the table model.
		model = new TableModel(this, latency);

		// Defines the DefaultTableCellRenderer.
		tableCellRenderer = new TableCellRenderer();

		// JTable settings.
		table = new JTable(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Uses a scrollbar.
		table.setDefaultRenderer(Object.class, new TableCellRenderer());

		// JScrollPane settings.
		setViewportView(table);
		setPreferredSize(new Dimension(20, 400));

		// Add a columnAdded Listener.
		table.getColumnModel().addColumnModelListener(new TableColumnModelListener() {

			public void columnAdded(TableColumnModelEvent e) {
				// Gets index of columns added.
				int fromIndex = e.getFromIndex();
				int toIndex = e.getToIndex();

				for (int i = fromIndex; i <= toIndex; i++) {
					TableColumn col = table.getColumnModel().getColumn(i);
					// Sets the default renderer.
					col.setCellRenderer(tableCellRenderer); 

					// Calculs & sets the preferred width.
					int colPreferredWidth = table.getTableHeader().getFontMetrics(table.getTableHeader().getFont()).stringWidth(col.getHeaderValue().toString()) + 10;
					col.setPreferredWidth(colPreferredWidth);
				}

				adjustPreferredSize();
			}

			public void columnRemoved(TableColumnModelEvent e) {
				Table.this.adjustPreferredSize();
			}

			public void columnMarginChanged(ChangeEvent arg0) {
			}

			public void columnMoved(TableColumnModelEvent arg0) {
			}

			public void columnSelectionChanged(ListSelectionEvent arg0) {
			}
		});

		// Add the a ComponentListener, when a row is added, the scroll bar
		// automatically go to the last row.
		table.addComponentListener(new ComponentAdapter() {

			public void componentResized(ComponentEvent e) {
				if (!Table.this.getVerticalScrollBar().getValueIsAdjusting())
					table.scrollRectToVisible(new Rectangle(Table.this.getHorizontalScrollBar().getValue(),
							table.getHeight(), 1, 1));
			}
		});

		// Add the EndRun event listener.
		Listener<EndRun> endRunListener = new Listener<EndRun>() {

			public void onEvent(EndRun event) {
				Table.this.adjustPreferredSize();
			}
		};
		EventManager.getInstance().add(EndRun.class, endRunListener);
	}

	/**
	 * Adds a stat to the fields list with a default name give by the simple
	 * name of the class.
	 * 
	 * @param stat the stat added to the field list.
	 */
	public <E extends Event> void addStat(Class<? extends AbstractStat<E>> stat) {
		addStat(stat, null);
	}

	/**
	 * Adds a stat to the fields list of the model.
	 * 
	 * @param stat the stat added to the field list.
	 * @param statName the name of the stat for the column header.
	 */
	public <E extends Event> void addStat(Class<? extends AbstractStat<E>> stat, String statName) {
		model.addStat(stat, statName);
	}

	/**
	 * Adds an event to the listeners list of the model.
	 * 
	 * @param type the even added to the listeners list.
	 */
	public <E extends Event> void addListener(Class<E> type) {
		model.addListener(type);
	}

	/**
	 * Adjust the preferred size.
	 */
	private void adjustPreferredSize() {
		model.refresh();
		int preferredWidth = 20;
		TableColumn col = null;
		for (Enumeration<TableColumn> e = table.getColumnModel().getColumns(); e.hasMoreElements();) {
			col = e.nextElement();

			// Calculs & sets the preferred width of the cells.
			int colPreferredWidth = col.getPreferredWidth();
			for (int i = 0; i < table.getRowCount(); i++) {
				int cellWidth = table.getTableHeader().getFontMetrics(table.getTableHeader().getFont()).stringWidth(model.getValueAt(i, col.getModelIndex()).toString()) + 5;
				if (cellWidth > colPreferredWidth)
					colPreferredWidth = cellWidth;
			}

			// Increments the table preferred width.
			col.setPreferredWidth(colPreferredWidth);

			// Increments the table preferred width.
			preferredWidth += colPreferredWidth;
		}

		setPreferredSize(new Dimension(preferredWidth, (int) getPreferredSize().getHeight()));
	}

	/**
	 * Export the table in the specified file and the specified format.
	 * 
	 * @param file
	 * @param format the format among FORMAT_XLS, FORMAT_CSV.
	 * @return true if the table export succeed.
	 * @throws IllegalArgumentException if the format is unknown.
	 * 
	 * @see #FORMAT_XLS, #FORMAT_CSV
	 */
	public void export(File file, String format) throws IllegalArgumentException {
		if (format == FORMAT_XLS)
			model.exportToXLS(file);
		else if (format == FORMAT_CSV)
			model.exportToCSV(file);
		else
			throw new IllegalArgumentException("Unknown format.");
	}

	@Override
	public String toString() {
		return getName();
	}
}
