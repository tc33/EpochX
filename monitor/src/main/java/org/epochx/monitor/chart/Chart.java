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
 * A <code>Chart</code> is a <code>Monitor</code> component which displays one
 * or more <code>ChartTrace</code>.
 * <p>
 * This class extends <code>Chart2D</code> from the <a
 * href="http://jchart2d.sourceforge.net/">JChart2D</a> library.
 * </p>
 * 
 * <p>
 * <h3>Construction</h3>
 * The chart name cans be specified in the constructor. If not, a default name
 * is given.<br>
 * A <code>Chart</code> is composed by one or more {@link ChartTrace} instances.
 * <br>
 * Each trace is automatically added to the {@link #traces} list, as the chart
 * is given in parameter of the constructor {@link ChartTrace#ChartTrace(Chart)}
 * . <br>
 * <br>
 * Here, there is a sample code which creates a <code>Chart</code>, adds a
 * trace, and adds a listener for all traces :
 * 
 * <pre>
 * Chart myGraph = new Chart(&quot;Chart_Name&quot;);
 * ChartTrace aTrace = new ChartTrace(myChart);
 * // .. Chart Trace Setting ..
 * myGraph.addListener(EndGeneration.class); // optional
 * </pre>
 * 
 * To know how to create and set a trace please, see {@link ChartTrace}.
 * </p>
 * 
 * <p>
 * <h3>Timer use & Refreshing rate</h3>
 * Each <code>Chart</code> instance has its own {@link Timer} which shedules the
 * refreshing {@link ChartTrace#task task} of each trace which could have
 * different refresh rates (<i>100ms</i> by default).<br>
 * The refresh task is not performed if the chart is not visible on the
 * <code>Monitor</code>.<br>
 * <i> (Note that as we use a timer for each instance, each chart refresh in a
 * separated thread.) </i>.
 * </p>
 * 
 * <p>
 * <h3>Concurrency</h3>
 * All the fields are <b>immutables</b>, except the private trace list.<br>
 * Some methods (
 * <code>addTrace(ChartTrace), clear(), getTraceCount(), ...</code>), which
 * access to the list, might also appear unsafe for a multiple-threads use.<br>
 * Even if this case does not seem intended, those methods are
 * <b>synchronized</b> by intrinsic <i>lock</i>.
 * </p>
 * 
 * <p>
 * <h3>Export</h3>
 * A <code>Chart</code> can be exported with a choosable size, in raster formats
 * or in vectorial formats, by using the {@link #export(File, String, Dimension)
 * export} method.<br>
 * <br>
 * <b>Note that we do not guarantee that this method is thread-safe.</b>
 * Especially, if you try to export the <code>Chart</code> during the evolution
 * process as the <i>EDT</i> might refresh the <code>Chart</code> during the
 * exportation. However, no conflict issues seem have occurred in our tests.
 * </p>
 * 
 * 
 * @see ChartTrace
 * @see Timer
 */
public class Chart extends Chart2D {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = -6969229302568666457L;

	/**
	 * The raster format (<i>*.png, *.jpg, *.gif, *.bmp</i>) constant.
	 */
	public static final String FORMAT_RASTER = "raster";

	/**
	 * The vector format (<i>*.ps, *.eps</i>) constant.
	 */
	public static final String FORMAT_VECTOR = "vector";

	/**
	 * The number of created instances.
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
	 * The list of <code>ChartTrace</code> contained in the chart.
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
	 * Returns the <code>Timer</code>.
	 * 
	 * @return the <code>Timer</code>.
	 */
	protected Timer getTimer() {
		return timer;
	}

	/**
	 * Returns a <code>Color</code> among the table of colors.
	 * 
	 * @return a <code>Color</code> among the table of colors.
	 */
	protected Color getColor() {
		return colors[getTraceCount() % colors.length];
	}

	/**
	 * Returns the number of traces.
	 * 
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
	public synchronized <E extends Event> void addListener(Class<E> type) {
		if (!traces.isEmpty())
			for (ChartTrace trace: traces)
				trace.addListener(type);
	}

	/**
	 * Exports a <code>Chart</code>.
	 * 
	 * @param file the file in which the <code>Chart</code> is exported.
	 * @param format the format among FORMAT_RASTER, FORMAT_VECTOR.
	 * @param size the size in which the graph should be printed.
	 * @throws IllegalArgumentException if the format is unknown.
	 * 
	 * @see #FORMAT_RASTER
	 * @see #FORMAT_VECTOR
	 */
	public synchronized void export(File file, String format, Dimension size) throws IllegalArgumentException {
		String extension = Utilities.getExtension(file);
		if (format == FORMAT_RASTER) {
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
