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
package org.epochx.monitor.dialog;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JFileChooser;

import org.epochx.monitor.Monitor;
import org.epochx.monitor.table.Table;


/**
 * 
 */
@SuppressWarnings("serial")
public class DialogExportTable extends Dialog {

	private final Table table;
	protected final JFileChooser fileChooser;

	private final ExportFilter xlsFilter = new ExportFilter("Excel 2000 format", "xls");
	private final ExportFilter csvFilter = new ExportFilter("Comma-separated Values", "csv", "txt");

	public DialogExportTable(Monitor monitor, Table t) {
		super(monitor);
		this.table = t;
		fileChooser = new JFileChooser();
	}

	@Override
	public void run() {
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(xlsFilter);
		fileChooser.setFileFilter(xlsFilter);
		fileChooser.addChoosableFileFilter(csvFilter);

		fileChooser.setSelectedFile(new File(table.getName() + ".xls"));

		fileChooser.addPropertyChangeListener(JFileChooser.FILE_FILTER_CHANGED_PROPERTY, new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent arg0) {
				if (fileChooser.getFileFilter() == xlsFilter)
					fileChooser.setSelectedFile(new File(table.getName() + ".xls"));
				else if (fileChooser.getFileFilter() == csvFilter)
					fileChooser.setSelectedFile(new File(table.getName() + ".csv"));
			}
		});
		
		if (fileChooser.showSaveDialog(monitor) == JFileChooser.APPROVE_OPTION) {
			if (fileChooser.getFileFilter() == xlsFilter)
				table.export(fileChooser.getSelectedFile(), Table.FORMAT_XLS);
			else if (fileChooser.getFileFilter() == csvFilter)
				table.export(fileChooser.getSelectedFile(), Table.FORMAT_CSV);

		}
	}

}
