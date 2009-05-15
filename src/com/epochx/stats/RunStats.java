/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of Epoch X - (The Genetic Programming Analysis Software)
 *
 *  Epoch X is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Epoch X is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with Epoch X.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.epochx.stats;

import java.util.*;

import com.epochx.core.*;

/**
 * 
 */
public class RunStats<TYPE> {

	// The objects listening for run stats.
	public List<RunStatListener> listeners;
	
	/**
	 * Constructor.
	 */
	public RunStats() {
		listeners = new ArrayList<RunStatListener>();
	}
	
	public void addRunStatListener(RunStatListener listener) {
		listeners.add(listener);
	}
	
	public void removeRunStatListener(RunStatListener listener) {
		listeners.remove(listener);
	}
	
	public void addRun(GPRun<TYPE> run, int runNo) {
		// Set of all the fields we need to calculate values for.
		Map<RunStatField, Object> stats = new HashMap<RunStatField, Object>();
		
		Map<RunStatListener, RunStatField[]> requestedStats = 
			new HashMap<RunStatListener, RunStatField[]>();
		
		// Add each listener's requirements to the set.
		for (RunStatListener l: listeners) {
			RunStatField[] fields = l.getRunStatFields();
			
			// The user doesn't want any fields but still wants to be informed of runs.
			if (fields == null) {
				fields = new RunStatField[0];
			}
			
			// Remember what stats fields this listener wanted.
			requestedStats.put(l, fields);
			
			// Add to Set of all stats we need to calculate.
			for (RunStatField sf: fields) {
				if (!stats.containsKey(sf))
					stats.put(sf, "");
			}
		}
		
		// Calculate all the stats that our listeners need.
		gatherStats(stats, run);
		
		// Inform each listener of their stats.
		Set<RunStatListener> ls = requestedStats.keySet();
		for (RunStatListener l: ls) {
			// Construct this listener's stats.
			RunStatField[] statFields = requestedStats.get(l);
			Object[] statResults = new Object[statFields.length];
			for (int i=0; i<statFields.length; i++) {
				statResults[i] = stats.get(statFields[i]);
			}
			
			l.runStats(runNo, statResults);
		}
	}
	
	/*
	 * Calculate, generate and gather any stats that have been requested.
	 * @param stats
	 * @param pop
	 */
	private void gatherStats(Map<RunStatField, Object>  stats, 
							 GPRun<TYPE> run) {
		if (stats.containsKey(RunStatField.BEST_PROGRAM)) {
			stats.put(RunStatField.BEST_PROGRAM, run.getBestProgram());
		}
		
		if (stats.containsKey(RunStatField.BEST_FITNESS)) {
			stats.put(RunStatField.BEST_FITNESS, run.getBestFitness());
		}
		
		if (stats.containsKey(RunStatField.RUN_TIME)) {
			stats.put(RunStatField.RUN_TIME, run.getRunTime());
		}
	}
	
	/**
	 * This enum gives all the available run statistics that can be 
	 * requested. Run statistics are those statistics that are generated 
	 * at the end of each run. Each RunStatField has a datatype related 
	 * to it which is the type which the data will be returned in.
	 * @see com.epochx.stats.RunStatListener
	 * @see com.epochx.stats.CrossoverStats.CrossoverStatField
	 * @see com.epochx.stats.MutationStats.MutationStatField
	 * @see com.epochx.stats.GenerationStats.GenStatField
	 */
	public enum RunStatField {
		/**
		 * Requests a CandidateProgram which has the 'best' fitness over a whole 
		 * run. This usually means the program with the lowest fitness score. 
		 * If elitism is not being used, the BEST_PROGRAM may not be in the final 
		 * generation. 
		 */
		BEST_PROGRAM,
		
		/**
		 * Requests a Double which is the fitness of the BEST_PROGRAM.
		 */
		BEST_FITNESS,
		
		/**
		 * Requests a Long which is the length of time in nanoseconds that the 
		 * run took to complete.
		 */
		RUN_TIME
	}
}
