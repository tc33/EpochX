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

import info.monitorenter.gui.chart.TracePoint2D;
import info.monitorenter.gui.chart.traces.Trace2DSimple;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import org.epochx.event.Event;
import org.epochx.event.EventManager;
import org.epochx.event.Listener;
import org.epochx.event.stat.AbstractStat;
import org.epochx.monitor.MonitorUtilities;

/**
 * A <code>ChartTrace</code> traces a curve according to an X-axis and a Y-axis
 * <code>Stat</code>.
 * 
 * <p>
 * <b>Notes that if the <code>toString()</code> method of both stats does not
 * return a parsable <code>Double</code>, a <code>NumberFormatException</code>
 * will be thrown on execution.</b><br>
 * <br>
 * This class extends <code>TracePoint2D</code> from the <a
 * href="http://jchart2d.sourceforge.net/">JChart2D</a> library.<br>
 * <br>
 * Because <code>ChartTrace</code> implements <code>Listener</code>, a
 * <code>ChartTrace</code> can be submitted to the {@link EventManager} for
 * execution.<br>
 * <br>
 * In fact, the {@link #onEvent(Event)} method adds a <code>TracePoint2D</code>
 * (corresponding to the <b>Stat</b> state) to the {@link #bufferList
 * buffer} list on each <code>Event</code> of the {@link #listeners} list.<br>
 * <br>
 * Then, the buffered points are add to the trace thanks to the
 * <code>ChartTask</code> scheduled on the chart timer at a fixed rate only if
 * the parent chart is visible.
 * </p>
 * 
 * <p>
 * <h3>Construction & Settings</h3>
 * The parent Chart <i>must</i> be specified on the constructor. The others
 * optional arguments are :
 * <ul>
 * <li>A name used for the legend.
 * <li>A color (by default choosen in {@link Chart#colors}).
 * <li>A refresh rate (by default <i>100ms</i>).
 * </ul>
 * Then, the X axis <code>Stat</code> (respectively Y axis <code>Stat</code> )
 * must be setted by {@link #setXStat(Class)} (respectively
 * {@link #setYStat(Class)}) method.<br>
 * <br>
 * Finaly, a specific listeners could be added to the list of listeners by using
 * the {@link #addListener(Class)} method. This task could also be performed for
 * all traces by using the same method in the <code>Chart</code> class.<br>
 * <br>
 * Here, there is a sample code which creates a trace with a specified name, a
 * default color and a one-second refresh rate.
 * 
 * <pre>
 * ChartTrace myTrace = new ChartTrace(myParentGraph, &quot;Trace_name&quot;, null, 1000L);
 * myTrace.setXStat(GenerationNumber.class);
 * myTrace.setYStat(GenerationFitnessDiversity.class);
 * myTrace.addlistener(EndGeneration.class); // optional
 * </pre>
 * 
 * To know how to create and set a <code>Chart</code>, please see {@link Chart}.
 * </p>
 * <p>
 * <h3>Concurrency</h3>
 * This class is <b>thread-safe</b>, as it has been designed to avoid
 * concurrency issues. <br>
 * <br>
 * Methods listed below are <b>synchronized</b> by using the parent char as the
 * <i>lock</i> to avoid concurrent access (by the <i>timer thread</i> and the
 * <i>main thread</i>) to the buffer list.
 * <ul>
 * <li><code>onEvent(Event)</code>
 * <li><code>refresh()</code>
 * <li><code>Chart.export(File, String, Dimension)</code>
 * </ul>
 * 
 * </p>
 * 
 * 
 * @see Chart
 * @see AbstractStat
 * @see EventManager
 * @see TimerTask
 */
public class ChartTrace extends Trace2DSimple implements Listener<Event> {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = 166111183720897505L;

	/**
	 * The default refresh rate constant.
	 */
	private final static long DEFAULT_LATENCY = 100;

	/**
	 * The <code>TimerTask</code> which is scheduled in the timer to calls the
	 * {@link #refresh()} method a fixed rate only if the <code>Chart</code> is
	 * visible on the <code>Monitor</code>.
	 */
	private final TimerTask task;

	/**
	 * The parent <code>Chart</code>.
	 */
	private final Chart chart;

	/**
	 * The X axis stat.
	 */
	private AbstractStat<?> xStat = null;

	/**
	 * The Y axis stat.
	 */
	private AbstractStat<?> yStat = null;

	/**
	 * The buffer list of <code>TracePoint2D</code> to add to the trace.
	 */
	private final ArrayList<TracePoint2D> bufferList = new ArrayList<TracePoint2D>();

	/**
	 * The mapping of listeners registered by this <code>Table</code>.
	 */
	private final Map<Class<?>, Listener<?>> listeners = new HashMap<Class<?>, Listener<?>>();

	/**
	 * Constructs a <code>ChartTrace</code>.
	 * 
	 * @param parentChart the parent chart.
	 */
	public ChartTrace(Chart parentChart) {
		this(parentChart, null, null, DEFAULT_LATENCY);
	}

	/**
	 * Constructs a <code>ChartTrace</code>.
	 * 
	 * @param parentChart the parent chart.
	 * @param name the name of the trace.
	 */
	public ChartTrace(Chart parentChart, String name) {
		this(parentChart, name, null, DEFAULT_LATENCY);
	}

	/**
	 * Constructs a <code>ChartTrace</code>.
	 * 
	 * @param parentChart the parent chart.
	 * @param color the color of the trace.
	 */
	public ChartTrace(Chart parentChart, Color color) {
		this(parentChart, null, color, DEFAULT_LATENCY);
	}

	/**
	 * Constructs a <code>ChartTrace</code>.
	 * 
	 * @param parentChart the parent chart.
	 * @param latency the latency rate.
	 */
	public ChartTrace(Chart parentChart, long latency) {
		this(parentChart, null, null, latency);
	}

	/**
	 * Constructs a <code>ChartTrace</code>.
	 * 
	 * @param parentChart the parent chart.
	 * @param name the name of the trace.
	 * @param color the color of the trace.
	 * 
	 * @throws NullPointerException if the parent chart is null.
	 * @throws IllegalArgumentException if the latency is negative.
	 */
	public ChartTrace(Chart parentChart, String name, Color color, long latency) throws NullPointerException,
			IllegalArgumentException {

		if (parentChart == null)
			throw new NullPointerException();
		if (latency <= 0)
			throw new IllegalArgumentException("Latency must be positive.");

		if (color == null)
			color = parentChart.getColor();

		// Sets the trace.
		setColor(color);

		// Set the parent chart and add the trace to it.
		chart = parentChart;
		chart.addTrace(this);

		// Set the name.
		if (name == null)
			name = "Trace #" + chart.getTraceCount();
		setName(name);

		// Task initialization.
		task = new TimerTask() {

			public void run() {
				if (MonitorUtilities.isVisible(ChartTrace.this.chart));
					ChartTrace.this.refresh();
			}
		};

		// Schedule the task.
		chart.getTimer().scheduleAtFixedRate(task, 1000, latency);
	}

	/**
	 * Sets the X axis stat.
	 * 
	 * @param x the X axis stat.
	 */
	public <E extends Event> void setXStat(Class<? extends AbstractStat<E>> x) {

		// Adds x & y axis stats.
		AbstractStat.register(x);
		this.xStat = AbstractStat.get(x);
		setName(getName() + " - X axis : " + xStat.getClass().getSimpleName());
	}

	/**
	 * Sets the Y axis stat.
	 * 
	 * @param y the Y axis stat.
	 */
	public <E extends Event> void setYStat(Class<? extends AbstractStat<E>> y) {
		AbstractStat.register(y);
		this.yStat = AbstractStat.get(y);
		setName(getName() + " - Y axis : " + yStat.getClass().getSimpleName());
		//setName("");
	}

	/**
	 * Adds an event to the listeners list.
	 * 
	 * @param type the even added to the listeners list.
	 */
	public <E extends Event> void addListener(Class<E> type) {
		// only creates a new listener if we do not have one already
		if (!listeners.containsKey(type)) {
			EventManager.getInstance().add(type, this);
			listeners.put(type, this);
		}
	}

	/**
	 * The <code>Listener</code> inherited method which buffers a new point in
	 * the list when an event is fire.<br>
	 * <p>
	 * <b>Synchronized</b> to avoid concurrent access to the
	 * <code>bufferList</code> field.
	 * </p>
	 * 
	 * @param event the <code>Event</code> which trigger this action. Not used
	 *        here.
	 * 
	 * @see #task
	 */
	public void onEvent(Event event) throws NullPointerException {
		synchronized (chart) {
			try {
				Double x = Double.parseDouble(xStat.toString());
				Double y = Double.parseDouble(yStat.toString());
				bufferList.add(new TracePoint2D(x, y));
			} catch (NullPointerException e) {
				throw new NullPointerException("X or Y stat is null.");
			} catch (NumberFormatException e) {
				throw new NumberFormatException("X or Y stat is not a parsable Double.");
			}
		}
	}

	/**
	 * Refreshes the trace by adding the buffered points in the chart.<br>
	 * <p>
	 * <b>Synchronized</b> to avoid concurrent access to the
	 * <code>bufferList</code> field.
	 * </p>
	 */
	public void refresh() {
		synchronized (chart) {
			for (TracePoint2D point: bufferList)
				addPoint(point);
			bufferList.clear();
		}
	}
}
