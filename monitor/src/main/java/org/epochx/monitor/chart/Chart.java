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
package org.epochx.monitor.chart;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAxis;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

import javax.imageio.ImageIO;

import org.apache.xmlgraphics.java2d.ps.AbstractPSDocumentGraphics2D;
import org.apache.xmlgraphics.java2d.ps.EPSDocumentGraphics2D;
import org.apache.xmlgraphics.java2d.ps.PSDocumentGraphics2D;
import org.epochx.event.Event;
import org.epochx.monitor.Utilities;

/**
 * A <code>Chart</code> display several stats at several events.
 */
@SuppressWarnings("serial")
public class Chart extends Chart2D {

	/**
	 * The Excel 2000 format constant.
	 */
	public static final String FORMAT_IMAGE = "img";

	/**
	 * The Comma-separated Values format constant.
	 */
	public static final String FORMAT_VECTOR = "vect";

	/**
	 * The Number of created Instances.
	 */
	private static int noInstances = 0;

	/**
	 * The color table (can be extended).
	 */
	private final static Color[] colors = {Color.BLUE, Color.GREEN, Color.RED, Color.ORANGE, Color.CYAN, Color.MAGENTA,
			Color.GRAY, Color.PINK};

	/**
	 * The <code>Timer</code>.
	 */
	private final Timer timer;

	/**
	 * The list of <code>ChartTrace</code> `contained in the chart.
	 */
	private final ArrayList<ChartTrace> traces = new ArrayList<ChartTrace>();

	/**
	 * Constructs a <code>Chart</code>.
	 */
	public Chart() {
		this("Chart" + noInstances);
	}

	/**
	 * Constructs a <code>Chart</code>.
	 * 
	 * @param name the name of the <code>MonitorGraph</code>.
	 */
	public Chart(String name) {

		// Chart settings
		setName(name);
		setPreferredSize(new Dimension(500, 400));

		// chart.setBackground(Color.BLACK);
		// chart.setForeground(Color.WHITE);
		setGridColor(Color.LIGHT_GRAY);

		// Axis settings
		IAxis<?> axisX = this.getAxisX();
		axisX.setPaintGrid(true);
		IAxis<?> axisY = this.getAxisY();
		axisY.setPaintGrid(true);

		// Timer settings
		timer = new Timer("MONITOR-ChartTimer (" + name + ")");

		// Number of Instances incrementation.
		noInstances++;
	}

	/**
	 * @return the timer
	 */
	protected Timer getTimer() {
		return timer;
	}

	/**
	 * @return a color in the table "colors".
	 */
	protected Color getColor() {
		return colors[getTraceCount() % colors.length];
	}

	/**
	 * @return the number of traces.
	 */
	protected synchronized int getTraceCount() {
		return traces.size();
	}

	/**
	 * Adds a trace to the trace list and to the chart.
	 * 
	 * @param graphTrace the trace added to the trace list.
	 */
	protected synchronized <E extends Event> void addTrace(ChartTrace graphTrace) {
		traces.add(graphTrace);
		super.addTrace(graphTrace);
	}

	/**
	 * Clears the trace list.
	 */
	public synchronized void clear() {
		traces.clear();
	}

	/**
	 * Adds an event to the listeners list of all the traces.
	 * 
	 * @param type the even added.
	 */
	public <E extends Event> void addListener(Class<E> type) {
		if (!traces.isEmpty())
			for (ChartTrace trace: traces)
				trace.addListener(type);
	}

	/**
	 * Exports in a file in a format specified by the extension.
	 * 
	 * @param file the file in which the chart is exported.
	 * @param format the format among FORMAT_IMAGE, FORMAT_VECTOR.
	 * @param siwe the size in which the graph should be printed.
	 * @throws IllegalArgumentException if the format is unknown.
	 * 
	 * @see #FORMAT_IMAGE, #FORMAT_VECTOR
	 */
	public synchronized void export(File file, String format, Dimension size) throws IllegalArgumentException {
		String extension = Utilities.getExtension(file);
		if (format == FORMAT_IMAGE) {
			if (extension == null)
				extension = "png";
			try {
				ImageIO.write(snapShot((int) size.getWidth(), (int) size.getHeight()), extension, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (format == FORMAT_VECTOR) {
			setVisible(false);
			setSize(size);
			try {
				FileOutputStream outStream = new FileOutputStream(file);
				AbstractPSDocumentGraphics2D g2d = null;

				if (extension == "eps")
					g2d = new EPSDocumentGraphics2D(true);
				else
					g2d = new PSDocumentGraphics2D(true);

				g2d.setGraphicContext(new org.apache.xmlgraphics.java2d.GraphicContext());

				g2d.setupDocument(outStream, (int) size.getWidth(), (int) size.getHeight());
				paint(g2d);
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			setVisible(true);
		} else
			throw new IllegalArgumentException("Unknown format.");
	}

	public String toString() {
		return getName();
	}
}
