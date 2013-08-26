/*
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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.Enumeration;
import java.util.Timer;

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
 * A <code>Table</code> is a <code>Monitor</code> component which displays
 * <code>Stats</code> on specific <code>Events</code>.
 * 
 * <p>
 * This class extends <code>JTabbedPane</code> to be displayed in the
 * <code>Monitor</code>.
 * </p>
 * 
 * <p>
 * <h3>Timer use & Refreshing rates</h3>
 * Each table has its own a {@link Timer} sheduling at fixed rate a
 * {@link TableModel#task task} which refreshs the tabular data model,
 * only if the <code>Table</code> is visible on the <code>Monitor</code>.<br>
 * <i> (Note that as we use a timer for each instance, each table refresh in a
 * separated thread.) </i>.<br>
 * </p>
 * 
 * <p>
 * <h3>Construction & Settings</h3>
 * The <i>table name</i> and the <i>refresh rate</i> can be specified on the
 * constructor as optional arguments. If not, they are initialized by default
 * (the default rate is <i>100ms</i>)<br>
 * <br>
 * 
 * Then, <code>Stats</code> are added to the stat {@link TableModel#stats list}
 * by using the {@link #addStat(Class, String)} method. The <code>String</code>
 * argument is the name to display on the header, and it is optional (the simple
 * name of the class is assigned by default).<br>
 * <br>
 * 
 * A sample code to create and set a <code>Table</code> :
 * 
 * <pre>
 * Table myTable = new Table(&quot;Table_name&quot;, 1000L);
 * myTable.addStat(GenerationNumber.class);
 * myTable.addStat(GenerationFitnessDiversity.class);
 * myTable.addListener(EndGeneration.class);
 * </pre>
 * 
 * </p>
 * 
 * <p>
 * <h3>Concurrency</h3>
 * This component is a <b>split-data model</b>, as the main fields are
 * registered in a {@link TableModel}. However, for greater convenience, the
 * <code>Table</code> handles the interface with its model in an opaque way.<br>
 * <br>
 * Because the {@link TableModel} registers all the critical fields (table date,
 * buffer list), please refer to its documentation to known how concurrency is
 * managed.
 * </p>
 * 
 * <p>
 * <h3>Export</h3>
 * A <code>Table</code> can be exported in Excel format or in Comma-separated
 * Values format (<i>*.csv</i>) by using the {@link #export(File, String)
 * export} method. <br>
 * <br>
 * <b>Note that we do not guarantee that this method is thread-safe.</b>
 * Especially, if you try to export the <code>Table</code> during the evolution
 * process as the <i>EDT</i> might refresh the <code>Table</code> during the
 * exportation. However, no conflict issues seem have occurred in our tests.
 * </p>
 * 
 * 
 * @see TableModel
 */
public class Table extends JScrollPane {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = -5683300751753421273L;

	/**
	 * The Excel 2000 format (<i>*.xls</i>) constant.
	 */
	public static final String FORMAT_XLS = "xls";

	/**
	 * The Comma-separated Values format (<i>*.csv, *.txt</i>) constant.
	 */
	public static final String FORMAT_CSV = "csv";

	/**
	 * The default refresh rate constant.
	 */
	private final static long DEFAULT_LATENCY = 100;

	/**
	 * The number of created instances.
	 */
	private static int noInstances = 0;

	/**
	 * The <code>JTable</code>.
	 */
	private final JTable table;

	/**
	 * The <code>TableModel</code>.
	 */
	private final TableModel model;

	/**
	 * The <code>TableCellRenderer</code>.
	 */
	private final TableCellRenderer tableCellRenderer;

	/**
	 * Constructs a <code>Table</code> with a default name.
	 * <p>
	 * Default name : <code>"table"+noInstances</code>.
	 * </p>
	 */
	public Table() {
		this("table" + noInstances, DEFAULT_LATENCY);
	}

	/**
	 * Constructs a <code>Table</code> with a specified name.
	 * 
	 * @param name the Name given to the main component.
	 */
	public Table(String name) {
		this(name, DEFAULT_LATENCY);
	}

	/**
	 * Constructs a <code>Table</code> with a specified latency.
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

		// Add a ColumnAddedListener.
		table.getColumnModel().addColumnModelListener(new ColumnAddedListener());

		// Add a RowAddedListener.
		table.addComponentListener(new RowAddedListener());

		// Adjusts the preferred size on the EndRun event.
		Listener<EndRun> endRunListener = new Listener<EndRun>() {

			public void onEvent(EndRun event) {
				Table.this.adjustPreferredSize();
			}
		};
		EventManager.getInstance().add(EndRun.class, endRunListener);
	}

	/**
	 * Adds a stat to the model with a default name given by the simple
	 * name of the class.
	 * 
	 * @param stat the stat added to the field list.
	 */
	public <E extends Event> void addStat(Class<? extends AbstractStat<E>> stat) {
		addStat(stat, null);
	}

	/**
	 * Adds a stat to the model.
	 * 
	 * @param stat the stat added to the field list.
	 * @param statName the name of the stat for the column header.
	 */
	public <E extends Event> void addStat(Class<? extends AbstractStat<E>> stat, String statName) {
		model.addStat(stat, statName);
	}

	/**
	 * Adds an event to the model.
	 * 
	 * @param type the even added to the listeners list.
	 */
	public <E extends Event> void addListener(Class<E> type) {
		model.addListener(type);
	}

	/**
	 * Exports a <code>Table</code>.
	 * 
	 * @param file the file in which the <code>Table</code> is exported.
	 * @param format the format among FORMAT_XLS, FORMAT_CSV.
	 * 
	 * @see Table#FORMAT_XLS
	 * @see Table#FORMAT_CSV
	 */
	public void export(File file, String format) {
		model.export(file, format);
	}

	/**
	 * Adjust the preferred size of the <code>Table</code>.
	 */
	private void adjustPreferredSize() {
		// Refresh the model
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
		// Set the preferred size.
		setPreferredSize(new Dimension(preferredWidth, (int) getPreferredSize().getHeight()));
	}

	@Override
	public String toString() {
		return getName();
	}

	/**
	 * A <code>ColumnAddedListener</code> is a
	 * <code>TableColumnModelListener</code> which resets the preferred size of
	 * each column and the table, when a column is added.
	 */
	private class ColumnAddedListener implements TableColumnModelListener {

		private ColumnAddedListener() {
		}

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

		public void columnRemoved(TableColumnModelEvent arg0) {
			Table.this.adjustPreferredSize();
		}

		public void columnMarginChanged(ChangeEvent arg0) {
		}

		public void columnMoved(TableColumnModelEvent arg0) {
		}

		public void columnSelectionChanged(ListSelectionEvent arg0) {
		}

	}

	/**
	 * A <code>RowAddedListener</code> is a <code>ComponentListener</code> which
	 * puts the vertical <code>ScrollBar</code> to the bottum, when a row is
	 * added.
	 */
	private class RowAddedListener extends ComponentAdapter {

		@Override
		public void componentResized(ComponentEvent arg0) {
			if (!Table.this.getVerticalScrollBar().getValueIsAdjusting())
				table.scrollRectToVisible(new Rectangle(Table.this.getHorizontalScrollBar().getValue(),
						table.getHeight(), 1, 1));
		}
	}
}
