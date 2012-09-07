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
import org.epochx.monitor.MonitorUtilities;

/**
 * A <code>TableModel</code> provides both a tabular data model and a fields
 * storage for a <code>Table</code> component.
 * 
 * <p>
 * This class extends <code>AbstractTableModel</code> which provides some
 * conveniences for generating <code>TableModelEvents</code> and dispatching
 * them to the listeners.<br>
 * <br>
 * Because <code>TableModel</code> implements <code>Listener</code>, a
 * <code>TableModel</code> can be submitted to the {@link EventManager} for
 * execution.<br>
 * <br>
 * In fact, the {@link #onEvent(Event)} method adds a new row to the
 * {@link #rowsBuffer buffer} list corresponding to the state of each
 * <code>Stat</code> in the stats list at the <code>Event</code>.
 * </p>
 * 
 * <p>
 * <h3>Concurrency</h3>
 * For greater convenience, the <code>TableModel</code> class registers the
 * {@link Timer} and the {@link #task} which refreshs the tabular data model at
 * a fixed rate specified in the constructor.<br>
 * <br>
 * Because a <code>TableModel</code> store all the critical fields (the tabular
 * data, and the buffer list) which could be accessed from both the <i>timer
 * thread</i> and the <i>main thread</i>, its implementation has to take care of
 * concurrency issues to ensure the safety of the <code>Table</code> component.<br>
 * <br>
 * Methods listed below are <b>synchronized</b> by using a intrinsic <i>lock</i>
 * to avoid concurrent access (by the <i>timer thread</i> and the <i>main
 * thread</i>) to the buffer list.
 * <ul>
 * <li><code>onEvent(Event)</code>
 * <li><code>refresh()</code>
 * <li><code>export(File, String)</code>
 * </ul>
 * 
 * </p>
 * 
 * 
 * @see Table
 * @see AbstractStat
 * @see EventManager
 */
public class TableModel extends AbstractTableModel implements Listener<Event> {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = -2599888443084646185L;

	/**
	 * The list of column names.
	 */
	private final ArrayList<String> columnNames = new ArrayList<String>();

	/**
	 * The rowsData list of the data model.
	 */
	private final ArrayList<String[]> rowsData = new ArrayList<String[]>();

	/**
	 * The list of buffered rows.
	 */
	private final ArrayList<String[]> rowsBuffer = new ArrayList<String[]>();

	/**
	 * The list of <code>Stats</code> to be printed.
	 */
	private final ArrayList<AbstractStat<?>> stats = new ArrayList<AbstractStat<?>>();

	/**
	 * The mapping of listerners registered by this <code>Table</code>.
	 */
	private final Map<Class<?>, Listener<?>> listeners = new HashMap<Class<?>, Listener<?>>();

	/**
	 * The parent <code>Table</code>.
	 */
	private final Table table;

	/**
	 * The <code>Timer</code> schedules the timer task on a specified rate.
	 */
	private final Timer timer;

	/**
	 * The <code>TimerTask</code> which is scheduled in the timer to calls the
	 * {@link #refresh()} method a fixed rate only if the <code>Table</code> is
	 * visible on the <code>Monitor</code>.
	 */
	private final TimerTask task;

	/**
	 * Constructs a <code>TableModel</code>.
	 * 
	 * @param parentTable the parent table.
	 * @param latency the refresh rate.
	 */
	public TableModel(Table parentTable, long latency) {
		this.table = parentTable;

		// Timer and timer task initialization.
		timer = new Timer("MONITOR-TableTimer (" + table.getName() + ")");
		task = new TimerTask() {

			public void run() {
				if (MonitorUtilities.isVisible(TableModel.this.table))
					TableModel.this.refresh();
			}
		};
		// Every 20 milliseconds a new value is collected.
		timer.scheduleAtFixedRate(task, 1000, latency);
	}

	public int getColumnCount() {
		return columnNames.size();
	}

	public int getRowCount() {
		return rowsData.size();
	}

	public String getColumnName(int col) {
		return columnNames.get(col);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return columnIndex < this.rowsData.get(rowIndex).length ? this.rowsData.get(rowIndex)[columnIndex] : "";
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	/**
	 * Adds a stat to the stats list.
	 * 
	 * @param stat the stat added to the field list.
	 * @param statName the name of the stat for the column header.
	 */
	protected <E extends Event> void addStat(Class<? extends AbstractStat<E>> stat, String statName) {
		AbstractStat.register(stat);
		AbstractStat<E> statRegistered = AbstractStat.get(stat);
		stats.add(statRegistered);

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
			EventManager.getInstance().add(type, this);
			listeners.put(type, this);
		}
	}

	/**
	 * The method inherited from <code>Listener</code> interface, which adds a
	 * new row to the {@link #rowsBuffer buffer} list corresponding to the state
	 * of each <code>Stat</code> in the stats list at the <code>Event</code>.
	 * 
	 * <p>
	 * <b>Synchronized</b> to avoid concurent access to the
	 * <code>rowsBuffer</code> field.
	 * </p>
	 * 
	 * @param event the <code>Event</code> which trigger this action. Not used
	 *        here.
	 */
	public synchronized void onEvent(Event event) {
		if (!stats.isEmpty()) {
			String[] newRow = new String[stats.size()];
			int i = 0;
			for (AbstractStat<?> stat: stats)
				newRow[i++] = stat.toString();
			rowsBuffer.add(newRow);
		}
	}

	/**
	 * Refreshs the table data by adding the buffered rows in the data list, and
	 * firing a <code>TableRowsInserted</code> event.
	 * 
	 * <p>
	 * <b>Synchronized</b> to avoid concurent access to the
	 * <code>rowsBuffer</code> field.
	 * </p>
	 * 
	 */
	public synchronized void refresh() {
		if (!rowsBuffer.isEmpty()) {
			int rowsListSize = rowsData.size();
			rowsData.addAll(rowsBuffer);
			rowsBuffer.clear();
			fireTableRowsInserted(rowsListSize, rowsData.size() - 1);
		}
	}

	/**
	 * Exports a <code>Table</code>.
	 * 
	 * @param file the file in which the <code>Table</code> is exported.
	 * @param format the format among FORMAT_XLS, FORMAT_CSV.
	 * 
	 * @throws IllegalArgumentException if the format is unknown.
	 * 
	 * @see Table#FORMAT_XLS
	 * @see Table#FORMAT_CSV
	 */
	protected void export(File file, String format) throws IllegalArgumentException {
		refresh();
		if (format == Table.FORMAT_XLS) {
			try {
				WritableWorkbook workbook = Workbook.createWorkbook(file);
				WritableSheet sheet = workbook.createSheet("First Sheet", 0);
				synchronized (this) {
					int rowCount = getRowCount();
					int columnCount = getColumnCount();
					System.out.println(rowCount + " " + columnCount);
					for (int i = 0; i < columnCount; i++) {
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
		} else if (format == Table.FORMAT_CSV) {
			try {

				BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(file));
				synchronized (this) {
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
		} else
			throw new IllegalArgumentException("Unknown format.");
	}
}
