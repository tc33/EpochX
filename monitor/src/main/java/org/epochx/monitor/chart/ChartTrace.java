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
import org.epochx.monitor.Utilities;

/**
 * A <code>ChartTrace</code> defines the traces which are printed on a chart.
 */
@SuppressWarnings("serial")
public class ChartTrace extends Trace2DSimple {

	/**
	 * The default refresh rate constant.
	 */
	private final static long DEFAULT_LATENCY = 100;

	/**
	 * The <code>TimerTask</code>, used as a lock for concurency data acces.
	 */
	private final TimerTask task;

	/**
	 * The buffering listener which buffer a new point in the list when an event
	 * is fire.
	 */
	private final Listener<Event> bufferingListener;

	/**
	 * The parent chart.
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
	 * The buffer list of points to add to the chart.
	 */
	private final ArrayList<TracePoint2D> bufferList = new ArrayList<TracePoint2D>();

	/**
	 * The mapping of listerners registered by this <code>Table</code>.
	 */
	private final Map<Class<?>, Listener<?>> listeners = new HashMap<Class<?>, Listener<?>>();

	/**
	 * 
	 * Constructs a <code>ChartTrace</code>.
	 * 
	 * @param parentChart the parent chart.
	 */
	public ChartTrace(Chart parentChart) {
		this(parentChart, null, null, DEFAULT_LATENCY);
	}

	/**
	 * 
	 * Constructs a <code>ChartTrace</code>.
	 * 
	 * @param parentChart the parent chart.
	 * @param name the name of the trace.
	 */
	public ChartTrace(Chart parentChart, String name) {
		this(parentChart, name, null, DEFAULT_LATENCY);
	}

	/**
	 * 
	 * Constructs a <code>ChartTrace</code>.
	 * 
	 * @param parentChart the parent chart.
	 * @param color the color of the trace.
	 */
	public ChartTrace(Chart parentChart, Color color) {
		this(parentChart, null, color, DEFAULT_LATENCY);
	}

	/**
	 * 
	 * Constructs a <code>ChartTrace</code>.
	 * 
	 * @param parentChart the parent chart.
	 * @param latency the latency rate.
	 */
	public ChartTrace(Chart parentChart, long latency) {
		this(parentChart, null, null, latency);
	}

	/**
	 * 
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

		// Configs the trace.
		setColor(color);

		// Set the parent chart and add the trace to it.
		chart = parentChart;
		chart.addTrace(this);

		// Set the name.
		if (name == null)
			name = "Trace #" + chart.getTraceCount();
		setName(name);

		// BufferingListener initialization.
		bufferingListener = new Listener<Event>() {

			/*
			 * Buffers a new point in the buffer list when an event is fire.
			 * Synchronized because of concurency with the PrintBufferTask.
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
						e.printStackTrace();
					}
				}
			}
		};

		// Task initialization.
		task = new TimerTask() {

			/*
			 * A timer task which print the buffered points in the chart.
			 * Synchronized because of concurency with the BufferingListener.
			 */
			@Override
			public void run() {
				synchronized (chart) {
					if (Utilities.isVisible(chart)) {
						for (TracePoint2D point: bufferList) {
							addPoint(point);
						}
						bufferList.clear();
					}
				}
			}
		};

		// Schedule the task.
		chart.getTimer().scheduleAtFixedRate(task, 100, latency);
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
		// setName("");
	}

	/**
	 * Adds an event to the listeners list.
	 * 
	 * @param type the even added to the listeners list.
	 */
	public <E extends Event> void addListener(Class<E> type) {
		// only creates a new listener if we do not have one already
		if (!listeners.containsKey(type)) {
			EventManager.getInstance().add(type, bufferingListener);
			listeners.put(type, bufferingListener);
		}
	}
}
