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
import com.epochx.core.representation.*;

/**
 * 
 */
public class GenerationStats<TYPE> {

	// The objects listening for generation stats.
	public List<GenerationStatListener> listeners;
	
	/**
	 * Constructor.
	 */
	public GenerationStats() {
		listeners = new ArrayList<GenerationStatListener>();
	}
	
	public void addGenerationStatListener(GenerationStatListener listener) {
		listeners.add(listener);
	}
	
	public void removeGenerationStatListener(GenerationStatListener listener) {
		listeners.remove(listener);
	}
	
	public void addGen(List<CandidateProgram<TYPE>> pop) {
		// Set of all the fields we need to calculate values for.
		Map<GenStatField, String> stats = new HashMap<GenStatField, String>();
		
		Map<GenerationStatListener, GenStatField[]> requestedStats = 
			new HashMap<GenerationStatListener, GenStatField[]>();
		
		// Add each listener's requirements to the set.
		for (GenerationStatListener l: listeners) {
			GenStatField[] fields = l.getStatFields();
			
			// Remember what stats fields this listener wanted.
			requestedStats.put(l, fields);
			
			// Add to Set of all stats we need to calculate.
			for (GenStatField sf: fields) {
				if (!stats.containsKey(sf))
					stats.put(sf, "");
			}
		}
		
		// Calculate all the stats that our listeners need.
		gatherStats(stats, pop);
		
		// Inform each listener of their stats.
		Set<GenerationStatListener> ls = requestedStats.keySet();
		for (GenerationStatListener l: ls) {
			// Construct this listener's stats.
			GenStatField[] statFields = requestedStats.get(l);
			String[] statResults = new String[statFields.length];
			for (int i=0; i<statFields.length; i++) {
				statResults[i] = stats.get(statFields[i]);
			}
			
			l.generationStats(statResults);
		}
	}
	
	/*
	 * Calculate, generate and gather any stats that have been requested.
	 * @param stats
	 * @param pop
	 */
	private void gatherStats(Map<GenStatField, String>  stats, 
							 List<CandidateProgram<TYPE>> pop) {
		gatherDepthStats(stats, pop);
		gatherLengthStats(stats, pop);
		gatherTerminalStats(stats, pop);
	}
	
	/*
	 * Calculate, generate and gather any stats related to depth that have 
	 * been requested.
	 * @param stats
	 * @param pop
	 * TODO Am having to iterate over the depths a lot of times, theres room for 
	 * performance improvements here.
	 */
	private void gatherDepthStats(Map<GenStatField, String> stats, List<CandidateProgram<TYPE>> pop) {
		if (stats.containsKey(GenStatField.DEPTH_AVE)
				|| stats.containsKey(GenStatField.DEPTH_STDEV)
				|| stats.containsKey(GenStatField.DEPTH_MAX)
				|| stats.containsKey(GenStatField.DEPTH_MIN)) {
			// If any stats about depth are needed it's more efficient to get them all at once.
			int[] depths = new int[pop.size()];
			for (int i=0; i<pop.size(); i++) {
				depths[i] = GPProgramAnalyser.getProgramDepth(pop.get(i));
			}
			
			// Average depth. StDev needs the average so calc it here to avoid duplication.
			if (stats.containsKey(GenStatField.DEPTH_AVE)) {
				stats.put(GenStatField.DEPTH_AVE, Double.toString(ave(depths)));
			}
			
			// Standard deviation.
			if (stats.containsKey(GenStatField.DEPTH_STDEV)) {
				stats.put(GenStatField.DEPTH_STDEV, Double.toString(stdev(depths)));
			}
			
			// Maximum depth.
			if (stats.containsKey(GenStatField.DEPTH_MAX)) {
				stats.put(GenStatField.DEPTH_MAX, Double.toString(max(depths)));
			}
			
			// Minimum depth.
			if (stats.containsKey(GenStatField.DEPTH_MIN)) {
				stats.put(GenStatField.DEPTH_MIN, Double.toString(min(depths)));
			}
		}
	}
	
	private void gatherLengthStats(Map<GenStatField, String> stats, List<CandidateProgram<TYPE>> pop) {
		if (stats.containsKey(GenStatField.LENGTH_AVE)
				|| stats.containsKey(GenStatField.LENGTH_STDEV)
				|| stats.containsKey(GenStatField.LENGTH_MAX)
				|| stats.containsKey(GenStatField.LENGTH_MIN)) {
			// If any stats about length are needed it's more efficient to get them all at once.
			int[] lengths = new int[pop.size()];
			for (int i=0; i<pop.size(); i++) {
				lengths[i] = GPProgramAnalyser.getProgramLength(pop.get(i));
			}
			
			// Average depth.
			if (stats.containsKey(GenStatField.LENGTH_AVE)) {
				stats.put(GenStatField.LENGTH_AVE, Double.toString(ave(lengths)));
			}
			
			// Standard deviation.
			if (stats.containsKey(GenStatField.LENGTH_STDEV)) {
				stats.put(GenStatField.LENGTH_STDEV, Double.toString(stdev(lengths)));
			}
			
			// Maximum depth.
			if (stats.containsKey(GenStatField.LENGTH_MAX)) {
				stats.put(GenStatField.LENGTH_MAX, Integer.toString(max(lengths)));
			}
			
			// Minimum depth.
			if (stats.containsKey(GenStatField.LENGTH_MIN)) {
				stats.put(GenStatField.LENGTH_MIN, Integer.toString(min(lengths)));
			}
		}
	}
	
	private void gatherTerminalStats(Map<GenStatField, String> stats, List<CandidateProgram<TYPE>> pop) {
		if (stats.containsKey(GenStatField.NO_TERMINALS_AVE)
				|| stats.containsKey(GenStatField.NO_TERMINALS_STDEV)
				|| stats.containsKey(GenStatField.NO_TERMINALS_MAX)
				|| stats.containsKey(GenStatField.NO_TERMINALS_MIN)) {
			// If any stats about length are needed it's more efficient to get them all at once.
			int[] noTerminals = new int[pop.size()];
			for (int i=0; i<pop.size(); i++) {
				noTerminals[i] = GPProgramAnalyser.getNoTerminals(pop.get(i));
			}
			
			// Average depth.
			if (stats.containsKey(GenStatField.NO_TERMINALS_AVE)) {
				stats.put(GenStatField.NO_TERMINALS_AVE, Double.toString(ave(noTerminals)));
			}
			
			// Standard deviation.
			if (stats.containsKey(GenStatField.NO_TERMINALS_STDEV)) {
				stats.put(GenStatField.NO_TERMINALS_STDEV, Double.toString(stdev(noTerminals)));
			}
			
			// Maximum depth.
			if (stats.containsKey(GenStatField.NO_TERMINALS_MAX)) {
				stats.put(GenStatField.NO_TERMINALS_MAX, Integer.toString(max(noTerminals)));
			}
			
			// Minimum depth.
			if (stats.containsKey(GenStatField.NO_TERMINALS_MIN)) {
				stats.put(GenStatField.NO_TERMINALS_MIN, Integer.toString(min(noTerminals)));
			}
		}
	}
	
	private double ave(int[] values) {
		double sum = 0;
		for (int i=0; i<values.length; i++) {
			sum += values[i];
		}
		return sum/values.length;
	}
	
	private double stdev(int[] values) {
		return stdev(values, ave(values));
	}
	
	private double stdev(int[] values, double ave) {
		// Sum the squared differences.
		double sqDiff = 0;
		for (int i=0; i<values.length; i++) {
			sqDiff += Math.pow(values[i] - ave, 2);
		}
		
		// Take the square root of the average.
		return Math.sqrt(sqDiff / values.length);
	}
	
	private int max(int[] values) {
		int max = 0;
		for (int i=0; i<values.length; i++) {
			max = (values[i] > max) ? values[i] : max;
		}
		return max;
	}
	
	private int min(int[] values) {
		int min = 0;
		for (int i=0; i<values.length; i++) {
			min = (values[i] > min) ? values[i] : min;
		}
		return min;
	}
	
	public enum GenStatField {
		DEPTH_AVE,
		DEPTH_STDEV,
		DEPTH_MAX,
		DEPTH_MIN,
		LENGTH_AVE,
		LENGTH_STDEV,
		LENGTH_MAX,
		LENGTH_MIN,
		NO_TERMINALS_AVE,
		NO_TERMINALS_STDEV,
		NO_TERMINALS_MAX,
		NO_TERMINALS_MIN
	}
}
