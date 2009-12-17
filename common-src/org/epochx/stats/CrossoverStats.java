/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming for research
 *
 *  EpochX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EpochX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EpochX.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.epochx.stats;

import java.util.*;

import org.epochx.representation.*;


/**
 * 
 */
public class CrossoverStats {
	
	// The objects listening for crossover stats.
	public List<CrossoverStatListener> listeners;
	
	/**
	 * Constructor.
	 */
	public CrossoverStats() {
		listeners = new ArrayList<CrossoverStatListener>();
	}
	
	public void addCrossoverStatListener(CrossoverStatListener listener) {
		listeners.add(listener);
	}
	
	public void removeCrossoverStatListener(CrossoverStatListener listener) {
		listeners.remove(listener);
	}
	
	public void addCrossover(CandidateProgram[] parents, 
							 CandidateProgram[] children, 
							 long runtime,
							 int reversions) {
		// Set of all the fields we need to calculate values for.
		Map<CrossoverStatField, Object> stats = new HashMap<CrossoverStatField, Object>();
		
		Map<CrossoverStatListener, CrossoverStatField[]> requestedStats = 
			new HashMap<CrossoverStatListener, CrossoverStatField[]>();
		
		// Add each listener's requirements to the set.
		for (CrossoverStatListener l: listeners) {
			CrossoverStatField[] fields = l.getCrossoverStatFields();
			
			// The user doesn't want any fields but still wants to be informed of runs.
			if (fields == null) {
				fields = new CrossoverStatField[0];
			}
			
			// Remember what stats fields this listener wanted.
			requestedStats.put(l, fields);
			
			// Add to Set of all stats we need to calculate.
			for (CrossoverStatField sf: fields) {
				if (!stats.containsKey(sf))
					stats.put(sf, "");
			}
		}
		
		// Calculate all the stats that our listeners need.
		gatherStats(stats, parents, children, runtime, reversions);
		
		// Inform each listener of their stats.
		Set<CrossoverStatListener> ls = requestedStats.keySet();
		for (CrossoverStatListener l: ls) {
			// Construct this listener's stats.
			CrossoverStatField[] statFields = requestedStats.get(l);
			Object[] statResults = new Object[statFields.length];
			for (int i=0; i<statFields.length; i++) {
				statResults[i] = stats.get(statFields[i]);
			}
			
			l.crossoverStats(statResults);
		}
	}
	
	/*
	 * Calculate, generate and gather any stats that have been requested.
	 * @param stats
	 * @param pop
	 */
	private void gatherStats(Map<CrossoverStatField, Object>  stats, 
							 CandidateProgram[] parents, 
							 CandidateProgram[] children, 
							 long runtime,
							 int reversions) {
		if (stats.containsKey(CrossoverStatField.PARENTS)) {
			stats.put(CrossoverStatField.PARENTS, parents);
		}
		
		if (stats.containsKey(CrossoverStatField.CHILDREN)) {
			stats.put(CrossoverStatField.CHILDREN, children);
		}
		
		if (stats.containsKey(CrossoverStatField.RUN_TIME)) {
			stats.put(CrossoverStatField.RUN_TIME, runtime);
		}
		
		if (stats.containsKey(CrossoverStatField.REVERTED_CROSSOVERS)) {
			stats.put(CrossoverStatField.REVERTED_CROSSOVERS, reversions);
		}
	}
}
