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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.table.AbstractTableModel;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.epochx.event.Event;
import org.epochx.event.EventManager;
import org.epochx.event.Listener;
import org.epochx.event.stat.AbstractStat;
import org.epochx.monitor.Utilities;

/**
 * The table model for the <code>Table</code> component.
 * 
 * @author loic
 * 
 */
@SuppressWarnings("serial")
public class TableModel extends AbstractTableModel {

	/**
	 * The list of <code>AbstractStat</code> to be printed.
	 */
	private final ArrayList<String> columnNames = new ArrayList<String>();

	/**
	 * The list of rows to be printed.
	 */
	private final ArrayList<String[]> rows = new ArrayList<String[]>();

	/**
	 * The list of buffered rows.
	 */
	private final ArrayList<String[]> rowsBuffer = new ArrayList<String[]>();

	/**
	 * The list of <code>AbstractStat</code> to be printed.
	 */
	private final ArrayList<AbstractStat<?>> fields = new ArrayList<AbstractStat<?>>();

	/**
	 * The mapping of listerners registered by this <code>Table</code>.
	 */
	private final Map<Class<?>, Listener<?>> listeners = new HashMap<Class<?>, Listener<?>>();

	/**
	 * The parent table. Serves as the main lock for concurrency.
	 */
	private final Table table;

	/**
	 * The timer, schedule the timer task on a specified rate.
	 */
	private final Timer timer;

	/**
	 * The timer task.
	 * If the buffer is not empty and the table is visible on the monitor, then
	 * empty the buffer list in the rows list and fire a TableRowsInserted.
	 */
	private final TimerTask task;

	/**
	 * The buffering listener which buffer a new row in the buffer list when an
	 * event is fire.
	 */
	private final Listener<Event> listener;

	/**
	 * Constructor.
	 * 
	 * @param table the parent table.
	 */
	public TableModel(Table parentTable, long latency) {
		this.table = parentTable;

		// Listener initialization.
		listener = new Listener<Event>() {

			/*
			 * On event, add a row to the buffer list.
			 */
			public void onEvent(Event event) {
				synchronized (table) {
					if (!fields.isEmpty()) {
						String[] newRow = new String[fields.size()];
						int i = 0;
						for (AbstractStat<?> stat: fields)
							newRow[i++] = stat.toString();
						rowsBuffer.add(newRow);
					}
				}
			}
		};

		// Timer and timer task initialization.
		timer = new Timer("MONITOR-TableTimer (" + table.getName() + ")");
		task = new TimerTask() {

			/*
			 * If the buffer is not empty and the table is visible on the
			 * monitor, then empty the buffer list in the rows list and fire a
			 * TableRowsInserted event.
			 */
			@Override
			public synchronized void run() {
				synchronized (table) {

					if (!rowsBuffer.isEmpty() && Utilities.isVisible(TableModel.this.table)) {
						int rowsListSize = rows.size();
						rows.addAll(rowsBuffer);
						rowsBuffer.clear();
						fireTableRowsInserted(rowsListSize, rows.size() - 1);
					}
				}
			}

		};
		// Every 20 milliseconds a new value is collected.
		timer.scheduleAtFixedRate(task, 1000, latency);
	}

	/**
	 * @return the Number of Columns.
	 */
	public int getColumnCount() {
		return columnNames.size();
	}

	/**
	 * @return the Number of Rows.
	 */
	public int getRowCount() {
		return rows.size();
	}

	/**
	 * @return the Name of the Columns col.
	 */
	public String getColumnName(int col) {
		return columnNames.get(col);
	}

	/**
	 * @return the Value at [row][col].
	 */
	public Object getValueAt(int row, int col) {
		return col < this.rows.get(row).length ? this.rows.get(row)[col] : "";
	}

	/**
	 * @return false.
	 */
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	/**
	 * Refresh the model, by emptying the buffer in the rows list.
	 */
	protected void refresh() {
		synchronized (table) {
			if (!rowsBuffer.isEmpty()) {
				int rowsListSize = rows.size();
				rows.addAll(rowsBuffer);
				rowsBuffer.clear();
				fireTableRowsInserted(rowsListSize, rows.size() - 1);
			}
		}
	}

	/**
	 * Adds a stat to the fields list.
	 * 
	 * @param stat the stat added to the field list.
	 * @param statName the name of the stat for the column header.
	 */
	protected <E extends Event> void addStat(Class<? extends AbstractStat<E>> stat, String statName) {
		AbstractStat.register(stat);
		AbstractStat<E> statRegistered = AbstractStat.get(stat);
		fields.add(statRegistered);

		if (statName == null)
			statName = statRegistered.getClass().getSimpleName();
		columnNames.add(statName);
		fireTableStructureChanged();
	}

	/**
	 * Adds an event to the listeners list.
	 * 
	 * @param type the even added to the listeners list.
	 */
	protected <E extends Event> void addListener(Class<E> type) {
		// only creates a new listener if we do not have one already
		if (!listeners.containsKey(type)) {
			EventManager.getInstance().add(type, listener);
			listeners.put(type, listener);
		}
	}

	/**
	 * Export the table in Excel 2000 format using JExel library.
	 * 
	 * @param file the File object to write to.
	 */
	protected void exportToXLS(File file) {

		try {

			WritableWorkbook workbook = Workbook.createWorkbook(file);
			WritableSheet sheet = workbook.createSheet("First Sheet", 0);

			synchronized (table) {
				int rowCount = getRowCount();
				int columnCount = getColumnCount();
				for (int i = 0; i < rowCount; i++) {
					Label column = new Label(i, 0, getColumnName(i));
					sheet.addCell(column);
				}
				for (int i = 0; i < rowCount; i++) {
					for (int j = 0; j < columnCount; j++) {
						Label row = new Label(j, i + 1, getValueAt(i, j).toString());
						sheet.addCell(row);
					}
				}
			}
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Export the table in Comma-separated Values format using JExel library.
	 * 
	 * @param file the File object to write to.
	 */
	protected void exportToCSV(File file) {

		try {

			BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(file));
			synchronized (table) {
				int rowCount = getRowCount();
				int columnCount = getColumnCount();
				for (int i = 0; i < rowCount; i++) {
					StringBuffer buffer = new StringBuffer();
					for (int j = 0; j < columnCount; j++) {
						buffer.append(",\t" + getValueAt(i, j).toString());
					}
					buffer.delete(0, 1);
					bufferWriter.append(buffer.toString());
					bufferWriter.newLine();
				}
			}
			bufferWriter.flush();
			bufferWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
