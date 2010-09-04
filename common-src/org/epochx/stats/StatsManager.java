/*
 * Copyright 2007-2010 Tom Castle & Lawrence Beadle
 * Licensed under GNU General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.stats;

import java.util.*;

import org.epochx.core.Model;

/**
 * Gathers data and statistics about events that occur during execution of the
 * given <code>Model</code> and makes them available for use. Any component may
 * add data into the <code>StatsManager</code> by using the <code>addXxxData
 * </code> methods. The <code>StatsManager</code> will also use its internal
 * <code>CommonStatsEngine</code> instance to generate statistics upon request, and
 * stash the results internally. The data that is stored will only be held for
 * the current/previous incident of that event that took place. All data for an
 * event will be cleared upon the start of the next incident.
 */
public class StatsManager {
	
	private final Map<Object, Object> data;

	private List<StatsEngine> statsEngines;

	/**
	 * Constructs a <code>StatsManager</code> for the given <code>Model</code>,
	 * with a new <code>CommonStatsEngine</code>.
	 * 
	 * @param model the Model statistics will be about.
	 */
	public StatsManager(final Model model) {
		// Setup the stats manager.
		data = new HashMap<Object, Object>();
		statsEngines = new ArrayList<StatsEngine>();
		
		addStatsEngine(new CommonStatsEngine(model));
	}

	/**
	 * Sets the <code>StatsEngine</code> responsible for generating new
	 * statistics upon request.
	 * 
	 * @param statsEngine a <code>StatsEngine</code> that can generate further
	 *        statistics upon request.
	 */
	public void addStatsEngine(final StatsEngine statsEngine) {
		statsEngines.add(statsEngine);
	}
	
	public void addStatsEngine(int index, StatsEngine statsEngine) {
		statsEngines.add(index, statsEngine);
	}
	
	public StatsEngine removeStatsEngine(int index) {
		return statsEngines.remove(index);
	}
	
	public void removeStatsEngine(StatsEngine statsEngine) {
		statsEngines.remove(statsEngine);
	}

	/**
	 * Inserts an item of data about a mutation into the stats manager
	 * associated
	 * with the given field key. If data is already stored against the given
	 * field then it will be overwritten.
	 * 
	 * @param field the key to associate with the given data value.
	 * @param value the statistics/data to be stored.
	 */
	public void addData(final Object field, final Object value) {
		data.put(field, value);
	}

	/**
	 * Retrieves the statistic data about a mutation associated with the
	 * provided field. If the stat field does not exist or is otherwise
	 * unavailable then null will be returned.
	 * 
	 * <p>
	 * This object will start by checking its internal storage for the field, if
	 * it holds an entry for that field then the data will be returned. If no
	 * entry is stored, then the <code>CommonStatsEngine</code> instance will be asked
	 * if it is able to generate it. If it can be generated then it will then be
	 * stored for future requests and then returned. If the
	 * <code>CommonStatsEngine</code> is unable to create the statistic then <code>
	 * null</code> will be returned from this method.
	 * 
	 * <p>
	 * The object type of the instance that is returned will be dependent upon
	 * the field requested. The object type is normally specified in the
	 * documentation for the field.
	 * 
	 * @param field the name of the statistics field to retrieve.
	 * @return an object which represents the statistics data requested or
	 *         <code>null</code> if the field does not exist or the data is
	 *         otherwise unavailable.
	 */
	public Object getStat(final Object field) {
		Object stat = data.get(field);

		// If stat not stored then ask the stats engines if they can generate it.
		if (stat == null) {
			for (StatsEngine s: statsEngines) {
				stat = s.getStat(field);
				
				if (stat != null) {
					break;
				}
			}
		}

		return stat;
	}

	/**
	 * Retrieves a sequence of mutation statistics associated with the given
	 * fields. The returned array will be of the same length as the number of
	 * fields provided and each element will represent each field in order.
	 * 
	 * <p>
	 * Each statistic field requested will be obtained according to the contract
	 * specified by the <code>getMutationStat(String)</code> method.
	 * 
	 * @param fields the names of the statistics fields to retrieve.
	 * @return an array of Objects where each element is the statistic field
	 *         generated for the requested field at that array index. If a field
	 *         is
	 *         for data that does not exist or is otherwise unavailable then
	 *         that array
	 *         element will be <code>null</code>.
	 */
	public Object[] getStats(final Object ... fields) {
		final Object[] stats = new Object[fields.length];
		for (int i = 0; i < fields.length; i++) {
			stats[i] = getStat(fields[i]);
		}
		return stats;
	}

	/**
	 * Retrieve and print to the standard output the sequence of mutation
	 * statistics referenced by the given fields. The statistics will be
	 * obtained according to the contract specified by the
	 * <code>getRunStats(String[])</code> method. The result of calling the
	 * <code>toString()</code> method on each statistic object returned will be
	 * printed separated by a '\t' tab character, and the line terminated with a
	 * '\n' newline character.
	 * 
	 * @param fields the names of the statistics fields to print out.
	 */
	public void printStats(final Object ... fields) {
		printStats(fields, "\t");
	}

	/**
	 * Retrieve and print to the standard output the sequence of mutation
	 * statistics referenced by the given fields. The statistics will be
	 * obtained according to the contract specified by the
	 * <code>getRunStats(String[])</code> method. The result of calling the
	 * <code>toString()</code> method on each statistic object returned will be
	 * printed separated by a the <code>separator</code> <code>String</code>
	 * parameter provided, and the line terminated with a '\n' newline
	 * character.
	 * 
	 * @param fields the names of the statistics fields to print out.
	 * @param separator the String to be printed between each statistic.
	 */
	public void printStats(final Object[] fields, final String separator) {
		final Object[] stats = getStats(fields);

		printArray(stats, separator);
	}

	/*
	 * Print each element of the given array separated by the provided separator
	 * character followed by a new line character.
	 */
	private static void printArray(final Object[] array, final String separator) {
		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				System.out.print(separator);
			}
			System.out.print(array[i]);
		}
		System.out.println();
	}
}
