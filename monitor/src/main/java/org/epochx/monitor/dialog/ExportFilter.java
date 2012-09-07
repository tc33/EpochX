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

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.epochx.monitor.MonitorUtilities;


/**
 * An <code>ExportFilter</code> extends a <code>FileFilter</code> to provides customized filter.
 */
public class ExportFilter extends FileFilter {

	private String description;
	private String[] extensions;

	/**
	 * Constructs a <code>ExportFilter</code>.
	 * 
	 * @param description the description of the filter.
	 * @param extensions the extensions which define the filter.
	 * @throws NullPointerException if a parametre is null.
	 */
	public ExportFilter(String description, String ... extensions) throws NullPointerException {
		if (description == null || extensions == null) {
			throw new NullPointerException("Null argument(s).");
		}
		this.extensions = extensions;

		StringBuffer buffer = new StringBuffer(description);
		buffer.append(" (");
		for (String ext: extensions)
			buffer.append(" *." + ext);
		buffer.append(")");

		this.description = buffer.toString();

	}

	@Override
	public boolean accept(File file) {
		if (file.isDirectory()) {
			return true;
		}

		return MonitorUtilities.isContained(MonitorUtilities.getExtension(file), extensions);
	}

	@Override
	public String getDescription() {
		return description;
	}
}
