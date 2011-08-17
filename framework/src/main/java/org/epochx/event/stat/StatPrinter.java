/*
 * Copyright 2007-2011
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

package org.epochx.event.stat;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.epochx.event.Event;
import org.epochx.event.EventManager;
import org.epochx.event.Listener;

/**
 * 
 */
public class StatPrinter {

	/**
	 * The default separator string.
	 */
	public static final String SEPARATOR = "\t";

	/**
	 * The list of <code>AbstractStat</code> to be printed.
	 */
	private ArrayList<AbstractStat<?>> fields = new ArrayList<AbstractStat<?>>();

	/**
	 * The mapping of listerners registered by this <code>StatPrinter</code>.
	 */
	private Map<Class<?>, Listener<?>> listeners = new HashMap<Class<?>, Listener<?>>();

	/**
	 * The current separator.
	 */
	private String separator;

	/**
	 * The output stream.
	 */
	private PrintStream out;

	/**
	 * Constructs a <code>StatPrinter</code>.
	 */
	public StatPrinter() {
		this(System.out, SEPARATOR);
	}

	/**
	 * Constructs a <code>StatPrinter</code>.
	 * 
	 * @param out the output stream.
	 */
	public StatPrinter(PrintStream out) {
		this(out, SEPARATOR);
	}

	/**
	 * Constructs a <code>StatPrinter</code>.
	 * 
	 * @param separator the delimiter string.
	 */
	public StatPrinter(String separator) {
		this(System.out, separator);
	}

	/**
	 * Constructs a <code>StatPrinter</code>.
	 * 
	 * @param out the output stream.
	 * @param delimiter the delimiter string.
	 */
	public StatPrinter(PrintStream out, String separator) {
		this.out = out;
		this.separator = separator;

	}

	public <E extends Event> void add(Class<? extends AbstractStat<E>> type) {
		AbstractStat.register(type);
		fields.add(AbstractStat.get(type));
	}

	public void clear() {
		fields.clear();
	}

	public void print() {
		if (!fields.isEmpty()) {
			StringBuffer buffer = new StringBuffer();

			for (AbstractStat<?> stat: fields) {
				buffer.append(separator);
				buffer.append(stat);
			}

			buffer.delete(0, separator.length());
			out.println(buffer.toString());
		}
	}

	public <E extends Event> void printOnEvent(Class<E> type) {
		// only creates a new listener if we do not have one already
		if (!listeners.containsKey(type)) {
			Listener<E> listener = new Listener<E>() {

				public void onEvent(E event) {
					StatPrinter.this.print();
				}
			};

			EventManager.getInstance().add(type, listener);
			listeners.put(type, listener);
		}
	}

}