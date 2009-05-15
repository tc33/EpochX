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

import com.epochx.core.representation.*;

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
	
	public void addMutation(CandidateProgram<TYPE> parent, 
							CandidateProgram<TYPE> child, 
							long runtime) {
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
		gatherStats(stats, parent, child, runtime);
		
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
							 CandidateProgram<TYPE> parent, 
							 CandidateProgram<TYPE> child, 
							 long runtime) {
		if (stats.containsKey(MutationStatField.PROGRAM_BEFORE)) {
			stats.put(MutationStatField.PROGRAM_BEFORE, parent);
		}
		
		if (stats.containsKey(MutationStatField.PROGRAM_AFTER)) {
			stats.put(MutationStatField.PROGRAM_AFTER, child);
		}
		
		if (stats.containsKey(MutationStatField.RUN_TIME)) {
			stats.put(MutationStatField.RUN_TIME, runtime);
		}
	}
	
	/**
	 * This enum gives all the available mutation statistics that can be 
	 * requested. Mutation statistics are those statistics that are generated 
	 * per mtuation operation. Each MutationStatField has a datatype related 
	 * to it which is the type which the data will be returned in.
	 * @see com.epochx.stats.MutationStatListener
	 * @see com.epochx.stats.GenerationStats.GenStatField
	 * @see com.epochx.stats.CrossoverStats.CrossoverStatField
	 * @see com.epochx.stats.RunStats.RunStatField
	 */
	public enum MutationStatField {
		
		/**
		 * Requests a CandidateProgram which is a clone of the program as it 
		 * was <b>before</b> the mutation operation was applied.
		 */
		PROGRAM_BEFORE,
		
		/**
		 * Requests a CandidateProgram which is the program as it exists 
		 * <b>after</b> the mutation operation was applied.
		 */
		PROGRAM_AFTER,
		
		/**
		 * Requests a Long which is the length of time in nanoseconds that the 
		 * mutation operation took to complete.
		 */
		RUN_TIME
	}
	
}
