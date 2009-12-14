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
public class MutationStats<TYPE> {

	// The objects listening for mutation stats.
	public List<MutationStatListener> listeners;
	
	public MutationStats() {
		listeners = new ArrayList<MutationStatListener>();
	}
	
	public void addMutationStatListener(MutationStatListener listener) {
		listeners.add(listener);
	}
	
	public void removeMutationStatListener(MutationStatListener listener) {
		listeners.remove(listener);
	}
	
	public void addMutation(GPCandidateProgram<TYPE> parent, 
							GPCandidateProgram<TYPE> child, 
							long runtime,
							int reversions) {
		// Set of all the fields we need to calculate values for.
		Map<MutationStatField, Object> stats = new HashMap<MutationStatField, Object>();
		
		Map<MutationStatListener, MutationStatField[]> requestedStats = 
			new HashMap<MutationStatListener, MutationStatField[]>();
		
		// Add each listener's requirements to the set.
		for (MutationStatListener l: listeners) {
			MutationStatField[] fields = l.getMutationStatFields();
			
			// The user doesn't want any fields but still wants to be informed of runs.
			if (fields == null) {
				fields = new MutationStatField[0];
			}
			
			// Remember what stats fields this listener wanted.
			requestedStats.put(l, fields);
			
			// Add to Set of all stats we need to calculate.
			for (MutationStatField sf: fields) {
				if (!stats.containsKey(sf))
					stats.put(sf, "");
			}
		}
		
		// Calculate all the stats that our listeners need.
		gatherStats(stats, parent, child, runtime, reversions);
		
		// Inform each listener of their stats.
		Set<MutationStatListener> ls = requestedStats.keySet();
		for (MutationStatListener l: ls) {
			// Construct this listener's stats.
			MutationStatField[] statFields = requestedStats.get(l);
			Object[] statResults = new Object[statFields.length];
			for (int i=0; i<statFields.length; i++) {
				statResults[i] = stats.get(statFields[i]);
			}
			
			l.mutationStats(statResults);
		}
	}
	
	/*
	 * Calculate, generate and gather any stats that have been requested.
	 * @param stats
	 * @param pop
	 */
	private void gatherStats(Map<MutationStatField, Object>  stats, 
							 GPCandidateProgram<TYPE> parent, 
							 GPCandidateProgram<TYPE> child, 
							 long runtime,
							 int reversions) {
		if (stats.containsKey(MutationStatField.PROGRAM_BEFORE)) {
			stats.put(MutationStatField.PROGRAM_BEFORE, parent);
		}
		
		if (stats.containsKey(MutationStatField.PROGRAM_AFTER)) {
			stats.put(MutationStatField.PROGRAM_AFTER, child);
		}
		
		if (stats.containsKey(MutationStatField.RUN_TIME)) {
			stats.put(MutationStatField.RUN_TIME, runtime);
		}
		
		if (stats.containsKey(MutationStatField.REVERTED_MUTATIONS)) {
			stats.put(MutationStatField.REVERTED_MUTATIONS, reversions);
		}
	}
}
