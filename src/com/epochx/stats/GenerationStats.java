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
	
	public void addGen(List<CandidateProgram<TYPE>> pop, 
						int gen, 
						long runtime, 
						int revertedCrossovers, 
						int revertedMutations) {
		// Set of all the fields we need to calculate values for.
		Map<GenerationStatField, Object> stats = new HashMap<GenerationStatField, Object>();
		
		Map<GenerationStatListener, GenerationStatField[]> requestedStats = 
			new HashMap<GenerationStatListener, GenerationStatField[]>();
		
		// Add each listener's requirements to the set.
		for (GenerationStatListener l: listeners) {
			GenerationStatField[] fields = l.getGenStatFields();
			
			// The user doesn't want any fields but still wants to be informed of generations.
			if (fields == null) {
				fields = new GenerationStatField[0];
			}
			
			// Remember what stats fields this listener wanted.
			requestedStats.put(l, fields);
			
			// Add to Set of all stats we need to calculate.
			for (GenerationStatField sf: fields) {
				if (!stats.containsKey(sf))
					stats.put(sf, "");
			}
		}
		
		// Calculate all the stats that our listeners need.
		gatherStats(stats, pop, runtime, revertedCrossovers, revertedMutations);
		
		// Inform each listener of their stats.
		Set<GenerationStatListener> ls = requestedStats.keySet();
		for (GenerationStatListener l: ls) {
			// Construct this listener's stats.
			GenerationStatField[] statFields = requestedStats.get(l);
			Object[] statResults = new Object[statFields.length];
			for (int i=0; i<statFields.length; i++) {
				statResults[i] = stats.get(statFields[i]);
			}
			
			l.generationStats(gen, statResults);
		}
	}
	
	/*
	 * Calculate, generate and gather any stats that have been requested.
	 * @param stats
	 * @param pop
	 */
	private void gatherStats(Map<GenerationStatField, Object>  stats, 
							 List<CandidateProgram<TYPE>> pop,
							 long runtime,
							 int revertedCrossovers,
							 int revertedMutations) {
		gatherDepthStats(stats, pop);
		gatherLengthStats(stats, pop);
		gatherTerminalStats(stats, pop);
		gatherDistinctTerminalStats(stats, pop);
		gatherFunctionStats(stats, pop);
		gatherDistinctFunctionStats(stats, pop);
		gatherFitnessStats(stats, pop);
		
		if (stats.containsKey(GenerationStatField.POPULATION)) {
			stats.put(GenerationStatField.POPULATION, pop);
		}
		
		if (stats.containsKey(GenerationStatField.RUN_TIME)) {
			stats.put(GenerationStatField.RUN_TIME, runtime);
		}
		
		if (stats.containsKey(GenerationStatField.REVERTED_CROSSOVERS)) {
			stats.put(GenerationStatField.REVERTED_CROSSOVERS, revertedCrossovers);
		}
		
		if (stats.containsKey(GenerationStatField.REVERTED_MUTATIONS)) {
			stats.put(GenerationStatField.REVERTED_MUTATIONS, revertedMutations);
		}
	}
	
	/*
	 * Calculate, generate and gather any stats related to depth that have 
	 * been requested.
	 * @param stats
	 * @param pop
	 * TODO Am having to iterate over the depths a lot of times, theres room for 
	 * performance improvements here.
	 */
	private void gatherDepthStats(Map<GenerationStatField, Object> stats, List<CandidateProgram<TYPE>> pop) {
		if (stats.containsKey(GenerationStatField.DEPTH_AVE)
				|| stats.containsKey(GenerationStatField.DEPTH_STDEV)
				|| stats.containsKey(GenerationStatField.DEPTH_MAX)
				|| stats.containsKey(GenerationStatField.DEPTH_MIN)
				|| stats.containsKey(GenerationStatField.AVE_NODES_PER_DEPTH)) {
			// If any stats about depth are needed it's more efficient to get them all at once.
			double[] depths = new double[pop.size()];
			for (int i=0; i<pop.size(); i++) {
				depths[i] = GPProgramAnalyser.getProgramDepth(pop.get(i));
			}
			
			// Average depth. StDev needs the average so calc it here to avoid duplication.
			if (stats.containsKey(GenerationStatField.DEPTH_AVE)) {
				stats.put(GenerationStatField.DEPTH_AVE, Double.toString(ave(depths)));
			}
			
			// Standard deviation of depth.
			if (stats.containsKey(GenerationStatField.DEPTH_STDEV)) {
				stats.put(GenerationStatField.DEPTH_STDEV, Double.toString(stdev(depths)));
			}
			
			// Maximum depth.
			if (stats.containsKey(GenerationStatField.DEPTH_MAX)) {
				stats.put(GenerationStatField.DEPTH_MAX, Double.toString(max(depths)));
			}
			
			// Minimum depth.
			if (stats.containsKey(GenerationStatField.DEPTH_MIN)) {
				stats.put(GenerationStatField.DEPTH_MIN, Double.toString(min(depths)));
			}
			
			// Average number of nodes per depth.
			if (stats.containsKey(GenerationStatField.AVE_NODES_PER_DEPTH)) {
				stats.put(GenerationStatField.AVE_NODES_PER_DEPTH, getAveNodesPerDepth(pop));
			}
		}
	}
	
	private double[] getAveNodesPerDepth(List<CandidateProgram<TYPE>> pop) {
		// Get maximum depth of the population.
		int maxDepth = 0;
		for (CandidateProgram<TYPE> program: pop) {
			int depth = GPProgramAnalyser.getProgramDepth(program);
			if (depth > maxDepth) {
				maxDepth = depth;
			}
		}
		
		// Array to fill with average number of nodes at each depth.
		double[] aveNodes = new double[maxDepth];
		
		// For each depth.
		for (int d=0; d<maxDepth; d++) {
			// Get number of nodes for each program.
			double[] noNodes = new double[pop.size()];
			for (int j=0; j<pop.size(); j++) {
				noNodes[j] = pop.get(j).getNodesAtDepth(d).size();
			}
			aveNodes[d] = ave(noNodes);
		}
		
		return aveNodes;
	}
	
	private void gatherLengthStats(Map<GenerationStatField, Object> stats, List<CandidateProgram<TYPE>> pop) {
		if (stats.containsKey(GenerationStatField.LENGTH_AVE)
				|| stats.containsKey(GenerationStatField.LENGTH_STDEV)
				|| stats.containsKey(GenerationStatField.LENGTH_MAX)
				|| stats.containsKey(GenerationStatField.LENGTH_MIN)) {
			// If any stats about length are needed it's more efficient to get them all at once.
			double[] lengths = new double[pop.size()];
			for (int i=0; i<pop.size(); i++) {
				lengths[i] = GPProgramAnalyser.getProgramLength(pop.get(i));
			}
			
			// Average length.
			if (stats.containsKey(GenerationStatField.LENGTH_AVE)) {
				stats.put(GenerationStatField.LENGTH_AVE, Double.toString(ave(lengths)));
			}
			
			// Standard deviation of length.
			if (stats.containsKey(GenerationStatField.LENGTH_STDEV)) {
				stats.put(GenerationStatField.LENGTH_STDEV, Double.toString(stdev(lengths)));
			}
			
			// Maximum length.
			if (stats.containsKey(GenerationStatField.LENGTH_MAX)) {
				stats.put(GenerationStatField.LENGTH_MAX, Double.toString(max(lengths)));
			}
			
			// Minimum length.
			if (stats.containsKey(GenerationStatField.LENGTH_MIN)) {
				stats.put(GenerationStatField.LENGTH_MIN, Double.toString(min(lengths)));
			}
		}
	}
	
	private void gatherTerminalStats(Map<GenerationStatField, Object> stats, List<CandidateProgram<TYPE>> pop) {
		if (stats.containsKey(GenerationStatField.NO_TERMINALS_AVE)
				|| stats.containsKey(GenerationStatField.NO_TERMINALS_STDEV)
				|| stats.containsKey(GenerationStatField.NO_TERMINALS_MAX)
				|| stats.containsKey(GenerationStatField.NO_TERMINALS_MIN)) {
			// If any stats about length are needed it's more efficient to get them all at once.
			double[] noTerminals = new double[pop.size()];
			for (int i=0; i<pop.size(); i++) {
				noTerminals[i] = GPProgramAnalyser.getNoTerminals(pop.get(i));
			}
			
			// Average no terminals.
			if (stats.containsKey(GenerationStatField.NO_TERMINALS_AVE)) {
				stats.put(GenerationStatField.NO_TERMINALS_AVE, Double.toString(ave(noTerminals)));
			}
			
			// Standard deviation of no terminals.
			if (stats.containsKey(GenerationStatField.NO_TERMINALS_STDEV)) {
				stats.put(GenerationStatField.NO_TERMINALS_STDEV, Double.toString(stdev(noTerminals)));
			}
			
			// Maximum no terminals.
			if (stats.containsKey(GenerationStatField.NO_TERMINALS_MAX)) {
				stats.put(GenerationStatField.NO_TERMINALS_MAX, Double.toString(max(noTerminals)));
			}
			
			// Minimum no terminals.
			if (stats.containsKey(GenerationStatField.NO_TERMINALS_MIN)) {
				stats.put(GenerationStatField.NO_TERMINALS_MIN, Double.toString(min(noTerminals)));
			}
		}
	}
	
	private void gatherDistinctTerminalStats(Map<GenerationStatField, Object> stats, List<CandidateProgram<TYPE>> pop) {
		if (stats.containsKey(GenerationStatField.NO_DISTINCT_TERMINALS_AVE)
				|| stats.containsKey(GenerationStatField.NO_DISTINCT_TERMINALS_STDEV)
				|| stats.containsKey(GenerationStatField.NO_DISTINCT_TERMINALS_MAX)
				|| stats.containsKey(GenerationStatField.NO_DISTINCT_TERMINALS_MIN)) {
			// If any stats about length are needed it's more efficient to get them all at once.
			double[] noDTerminals = new double[pop.size()];
			for (int i=0; i<pop.size(); i++) {
				noDTerminals[i] = GPProgramAnalyser.getNoDistinctTerminals(pop.get(i));
			}
			
			// Average no distinct terminals.
			if (stats.containsKey(GenerationStatField.NO_DISTINCT_TERMINALS_AVE)) {
				stats.put(GenerationStatField.NO_DISTINCT_TERMINALS_AVE, Double.toString(ave(noDTerminals)));
			}
			
			// Standard deviation of no distinct terminals.
			if (stats.containsKey(GenerationStatField.NO_DISTINCT_TERMINALS_STDEV)) {
				stats.put(GenerationStatField.NO_DISTINCT_TERMINALS_STDEV, Double.toString(stdev(noDTerminals)));
			}
			
			// Maximum no distinct terminals.
			if (stats.containsKey(GenerationStatField.NO_DISTINCT_TERMINALS_MAX)) {
				stats.put(GenerationStatField.NO_DISTINCT_TERMINALS_MAX, Double.toString(max(noDTerminals)));
			}
			
			// Minimum no distinct terminals.
			if (stats.containsKey(GenerationStatField.NO_DISTINCT_TERMINALS_MIN)) {
				stats.put(GenerationStatField.NO_DISTINCT_TERMINALS_MIN, Double.toString(min(noDTerminals)));
			}
		}
	}
	
	private void gatherFunctionStats(Map<GenerationStatField, Object> stats, List<CandidateProgram<TYPE>> pop) {
		if (stats.containsKey(GenerationStatField.NO_FUNCTIONS_AVE)
				|| stats.containsKey(GenerationStatField.NO_FUNCTIONS_STDEV)
				|| stats.containsKey(GenerationStatField.NO_FUNCTIONS_MAX)
				|| stats.containsKey(GenerationStatField.NO_FUNCTIONS_MIN)) {
			// If any stats about length are needed it's more efficient to get them all at once.
			double[] noFunctions = new double[pop.size()];
			for (int i=0; i<pop.size(); i++) {
				noFunctions[i] = GPProgramAnalyser.getNoFunctions(pop.get(i));
			}
			
			// Average no functions.
			if (stats.containsKey(GenerationStatField.NO_FUNCTIONS_AVE)) {
				stats.put(GenerationStatField.NO_FUNCTIONS_AVE, Double.toString(ave(noFunctions)));
			}
			
			// Standard deviation of no functions.
			if (stats.containsKey(GenerationStatField.NO_FUNCTIONS_STDEV)) {
				stats.put(GenerationStatField.NO_FUNCTIONS_STDEV, Double.toString(stdev(noFunctions)));
			}
			
			// Maximum no functions.
			if (stats.containsKey(GenerationStatField.NO_FUNCTIONS_MAX)) {
				stats.put(GenerationStatField.NO_FUNCTIONS_MAX, Double.toString(max(noFunctions)));
			}
			
			// Minimum no functions.
			if (stats.containsKey(GenerationStatField.NO_FUNCTIONS_MIN)) {
				stats.put(GenerationStatField.NO_FUNCTIONS_MIN, Double.toString(min(noFunctions)));
			}
		}
	}
	
	private void gatherDistinctFunctionStats(Map<GenerationStatField, Object> stats, List<CandidateProgram<TYPE>> pop) {
		if (stats.containsKey(GenerationStatField.NO_DISTINCT_FUNCTIONS_AVE)
				|| stats.containsKey(GenerationStatField.NO_DISTINCT_FUNCTIONS_STDEV)
				|| stats.containsKey(GenerationStatField.NO_DISTINCT_FUNCTIONS_MAX)
				|| stats.containsKey(GenerationStatField.NO_DISTINCT_FUNCTIONS_MIN)) {
			// If any stats about length are needed it's more efficient to get them all at once.
			double[] noDFunctions = new double[pop.size()];
			for (int i=0; i<pop.size(); i++) {
				noDFunctions[i] = GPProgramAnalyser.getNoDistinctFunctions(pop.get(i));
			}
			
			// Average no distinct functions.
			if (stats.containsKey(GenerationStatField.NO_DISTINCT_FUNCTIONS_AVE)) {
				stats.put(GenerationStatField.NO_DISTINCT_FUNCTIONS_AVE, Double.toString(ave(noDFunctions)));
			}
			
			// Standard deviation of no distinct functions.
			if (stats.containsKey(GenerationStatField.NO_DISTINCT_FUNCTIONS_STDEV)) {
				stats.put(GenerationStatField.NO_DISTINCT_FUNCTIONS_STDEV, Double.toString(stdev(noDFunctions)));
			}
			
			// Maximum no distinct functions.
			if (stats.containsKey(GenerationStatField.NO_DISTINCT_FUNCTIONS_MAX)) {
				stats.put(GenerationStatField.NO_DISTINCT_FUNCTIONS_MAX, Double.toString(max(noDFunctions)));
			}
			
			// Minimum no distinct functions.
			if (stats.containsKey(GenerationStatField.NO_DISTINCT_FUNCTIONS_MIN)) {
				stats.put(GenerationStatField.NO_DISTINCT_FUNCTIONS_MIN, Double.toString(min(noDFunctions)));
			}
		}
	}
	
	private void gatherFitnessStats(Map<GenerationStatField, Object> stats, List<CandidateProgram<TYPE>> pop) {
		if (stats.containsKey(GenerationStatField.FITNESS_AVE)
				|| stats.containsKey(GenerationStatField.FITNESS_STDEV)
				|| stats.containsKey(GenerationStatField.FITNESS_MAX)
				|| stats.containsKey(GenerationStatField.FITNESS_MIN)
				|| stats.containsKey(GenerationStatField.FITNESS_MEDIAN)
				|| stats.containsKey(GenerationStatField.BEST_PROGRAM)
				|| stats.containsKey(GenerationStatField.FITNESS_CI_95)) {
			// If any stats about length are needed it's more efficient to get them all at once.
			double[] fitnesses = new double[pop.size()];
			for (int i=0; i<pop.size(); i++) {
				fitnesses[i] = pop.get(i).getFitness();
			}
			
			int maxIndex = -1;
			int minIndex = -1;
			double stdev = -1;
			
			// Average fitness.
			if (stats.containsKey(GenerationStatField.FITNESS_AVE)) {
				stats.put(GenerationStatField.FITNESS_AVE, Double.toString(ave(fitnesses)));
			}
			
			// Standard deviation of fitness.
			if (stats.containsKey(GenerationStatField.FITNESS_STDEV)) {
				stdev = stdev(fitnesses);
				stats.put(GenerationStatField.FITNESS_STDEV, Double.toString(stdev));
			}
			
			// Maximum fitness.
			if (stats.containsKey(GenerationStatField.FITNESS_MAX)) {
				maxIndex = maxIndex(fitnesses);
				stats.put(GenerationStatField.FITNESS_MAX, Double.toString(fitnesses[maxIndex]));
			}
			
			// Minimum fitness.
			if (stats.containsKey(GenerationStatField.FITNESS_MIN)) {
				stats.put(GenerationStatField.FITNESS_MIN, Double.toString(min(fitnesses)));
			}
			
			// Median fitness.
			if (stats.containsKey(GenerationStatField.FITNESS_MEDIAN)) {
				stats.put(GenerationStatField.FITNESS_MEDIAN, Double.toString(median(fitnesses)));
			}
			
			// Best program.
			if (stats.containsKey(GenerationStatField.BEST_PROGRAM)) {
				// If we haven't already found maxIndex find it now.
				if (minIndex == -1) {
					minIndex = minIndex(fitnesses);
				}
				stats.put(GenerationStatField.BEST_PROGRAM, pop.get(minIndex));
			}
			
			// Confidence Interval at 95%
			if (stats.containsKey(GenerationStatField.FITNESS_CI_95)) {
				if (stdev == -1) {
					stdev = stdev(fitnesses);
				}
				double ci = 1.96 * (stdev/Math.sqrt(fitnesses.length));
				
				stats.put(GenerationStatField.FITNESS_CI_95, Double.toString(ci));
			}
		}
	}
	
	private double ave(double[] values) {
		double sum = 0;
		for (int i=0; i<values.length; i++) {
			sum += values[i];
		}
		return sum/values.length;
	}
	
	private double stdev(double[] values) {
		return stdev(values, ave(values));
	}
	
	private double stdev(double[] values, double ave) {
		// Sum the squared differences.
		double sqDiff = 0;
		for (int i=0; i<values.length; i++) {
			sqDiff += Math.pow(values[i] - ave, 2);
		}
		
		// Take the square root of the average.
		return Math.sqrt(sqDiff / values.length);
	}
	
	private double max(double[] values) {
		double max = 0;
		for (int i=0; i<values.length; i++) {
			max = (values[i] > max) ? values[i] : max;
		}
		return max;
	}
	
	private int maxIndex(double[] values) {
		double max = 0;
		int maxIndex = -1;
		for (int i=0; i<values.length; i++) {
			if (values[i] > max) {
				max = values[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}
	
	private double min(double[] values) {
		double min = Double.MAX_VALUE;
		for (int i=0; i<values.length; i++) {
			min = (values[i] < min) ? values[i] : min;
		}
		return min;
	}
	
	private int minIndex(double[] values) {
		double min = Double.MAX_VALUE;
		int minIndex = -1;
		for (int i=0; i<values.length; i++) {
			if (values[i] < min) {
				min = values[i];
				minIndex = i;
			}
		}
		return minIndex;
	}
	
	private double median(double[] values) {
		// Sort the array.
		Arrays.sort(values);
		
		// Pick out the middle value.
		int medianIndex = (int) Math.floor(values.length / 2);
		double median = values[medianIndex-1];
		
		// There might have been an even number - use average of 2 medians.
		if ((values.length % 2) == 0) {
			median += values[medianIndex];
			median = median/2;
		}
		
		return median;
	}
}
