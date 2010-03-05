/*  
 *  Copyright 2007-2010 Tom Castle & Lawrence Beadle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming software for research
 *
 *  EpochX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EpochX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 *  The latest version is available from: http:/www.epochx.org
 */
package org.epochx.gp.stats;

import static org.epochx.gp.stats.GPStatField.*;
import static org.epochx.stats.StatField.GEN_POPULATION;

import java.util.List;

import org.epochx.core.Controller;
import org.epochx.gp.representation.GPCandidateProgram;
import org.epochx.stats.*;

public class GPStatsEngine extends StatsEngine {

	@Override
	public Object getGenerationStat(String field) {
		Object value = super.getGenerationStat(field);
		
		if (value == null) {
			if (field.equals(GEN_DEPTHS)) {
				value = getGenDepths();
			} else if (field.equals(GEN_DEPTH_MIN)) {
				value = getGenDepthMin();
			} else if (field.equals(GEN_DEPTH_MAX)) {
				value = getGenDepthMax();
			} else if (field.equals(GEN_DEPTH_AVE)) {
				value = getGenDepthAve();
			} else if (field.equals(GEN_DEPTH_STDEV)) {
				value = getGenDepthStdev();
			} else if (field.equals(GEN_AVE_NODES_PER_DEPTH)) {
				value = getGenAveNodesPerDepth();
			} else if (field.equals(GEN_LENGTHS)) {
				value = getGenLengths();
			} else if (field.equals(GEN_LENGTH_MIN)) {
				value = getGenLengthMin();
			} else if (field.equals(GEN_LENGTH_MAX)) {
				value = getGenLengthMax();
			} else if (field.equals(GEN_LENGTH_AVE)) {
				value = getGenLengthAve();
			} else if (field.equals(GEN_LENGTH_STDEV)) {
				value = getGenLengthStdev();
			} else if (field.equals(GEN_NO_TERMINALS)) {
				value = getGenNoTerminals();
			} else if (field.equals(GEN_NO_TERMINALS_MIN)) {
				value = getGenNoTerminalsMin();
			} else if (field.equals(GEN_NO_TERMINALS_MAX)) {
				value = getGenNoTerminalsMax();
			} else if (field.equals(GEN_NO_TERMINALS_AVE)) {
				value = getGenNoTerminalsAve();
			} else if (field.equals(GEN_NO_TERMINALS_STDEV)) {
				value = getGenNoTerminalsStdev();
			} else if (field.equals(GEN_NO_DISTINCT_TERMINALS)) {
				value = getGenNoDistinctTerminals();
			} else if (field.equals(GEN_NO_DISTINCT_TERMINALS_MIN)) {
				value = getGenNoDistinctTerminalsMin();
			} else if (field.equals(GEN_NO_DISTINCT_TERMINALS_MAX)) {
				value = getGenNoDistinctTerminalsMax();
			} else if (field.equals(GEN_NO_DISTINCT_TERMINALS_AVE)) {
				value = getGenNoDistinctTerminalsAve();
			} else if (field.equals(GEN_NO_DISTINCT_TERMINALS_STDEV)) {
				value = getGenNoDistinctTerminalsStdev();
			} else if (field.equals(GEN_NO_FUNCTIONS)) {
				value = getGenNoFunctions();
			} else if (field.equals(GEN_NO_FUNCTIONS_MIN)) {
				value = getGenNoFunctionsMin();
			} else if (field.equals(GEN_NO_FUNCTIONS_MAX)) {
				value = getGenNoFunctionsMax();
			} else if (field.equals(GEN_NO_FUNCTIONS_AVE)) {
				value = getGenNoFunctionsAve();
			} else if (field.equals(GEN_NO_FUNCTIONS_STDEV)) {
				value = getGenNoFunctionsStdev();
			} else if (field.equals(GEN_NO_DISTINCT_FUNCTIONS)) {
				value = getGenNoDistinctFunctions();
			} else if (field.equals(GEN_NO_DISTINCT_FUNCTIONS_MIN)) {
				value = getGenNoDistinctFunctionsMin();
			} else if (field.equals(GEN_NO_DISTINCT_FUNCTIONS_MAX)) {
				value = getGenNoDistinctFunctionsMax();
			} else if (field.equals(GEN_NO_DISTINCT_FUNCTIONS_AVE)) {
				value = getGenNoDistinctFunctionsAve();
			} else if (field.equals(GEN_NO_DISTINCT_FUNCTIONS_STDEV)) {
				value = getGenNoDistinctFunctionsStdev();
			}
		}
		
		return value;
	}

	private Object getGenDepths() {
		int[] depths = null;
		
		// Request the population from the stats manager.
		List<GPCandidateProgram> pop = (List<GPCandidateProgram>) Controller.getStatsManager().getGenerationStat(GEN_POPULATION);
		
		if (pop != null) {
			depths = new int[pop.size()];
			
			for (int i=0; i<pop.size(); i++) {
				depths[i] = pop.get(i).getProgramDepth();
			}
		}
		
		return depths;
	}
	
	private Object getGenDepthMin() {
		Integer minDepth = null;
		
		// Request the population from the stats manager.
		int[] depths = (int[]) Controller.getStatsManager().getGenerationStat(GEN_DEPTHS);
		
		if (depths != null) {
			minDepth = StatsUtils.min(depths);
		}
		
		return minDepth;
	}
	
	private Object getGenDepthMax() {
		Integer maxDepth = null;
		
		// Request the population from the stats manager.
		int[] depths = (int[]) Controller.getStatsManager().getGenerationStat(GEN_DEPTHS);
		
		if (depths != null) {
			maxDepth = StatsUtils.max(depths);
		}
		
		return maxDepth;
	}
	
	private Object getGenDepthAve() {
		Double aveDepth = null;
		
		// Request the population from the stats manager.
		int[] depths = (int[]) Controller.getStatsManager().getGenerationStat(GEN_DEPTHS);
		
		if (depths != null) {
			aveDepth = StatsUtils.ave(depths);
		}
		
		return aveDepth;
	}
	
	private Object getGenDepthStdev() {
		Double stdevDepth = null;
		
		// Request the population from the stats manager.
		int[] depths = (int[]) Controller.getStatsManager().getGenerationStat(GEN_DEPTHS);
		
		if (depths != null) {
			stdevDepth = StatsUtils.stdev(depths);
		}
		
		return stdevDepth;
	}
	
	private Object getGenAveNodesPerDepth() {
		double[] aveNodes = null;
		
		// Request the population from the stats manager.
		int[] depths = (int[]) Controller.getStatsManager().getGenerationStat(GEN_DEPTHS);
		int maxDepth = (Integer) Controller.getStatsManager().getGenerationStat(GEN_DEPTH_MAX);
		List<GPCandidateProgram> pop = (List<GPCandidateProgram>) Controller.getStatsManager().getGenerationStat(GEN_POPULATION);
		
		if (depths != null) {
			// Array to fill with average number of nodes at each depth.
			aveNodes = new double[maxDepth];
			
			// For each depth.
			for (int d=0; d<maxDepth; d++) {
				// Get number of nodes for each program.
				double[] noNodes = new double[pop.size()];
				for (int j=0; j<pop.size(); j++) {
					noNodes[j] = pop.get(j).getNodesAtDepth(d).size();
				}
				aveNodes[d] = StatsUtils.ave(noNodes);
			}
		}
		
		return aveNodes;
	}
	
	private Object getGenLengths() {
		int[] lengths = null;
		
		// Request the population from the stats manager.
		List<GPCandidateProgram> pop = (List<GPCandidateProgram>) Controller.getStatsManager().getGenerationStat(GEN_POPULATION);
		
		if (pop != null) {
			lengths = new int[pop.size()];
			
			for (int i=0; i<pop.size(); i++) {
				lengths[i] = pop.get(i).getProgramLength();
			}
		}
		
		return lengths;
	}
	
	private Object getGenLengthMin() {
		Integer minLength = null;
		
		// Request the population from the stats manager.
		int[] lengths = (int[]) Controller.getStatsManager().getGenerationStat(GEN_LENGTHS);
		
		if (lengths != null) {
			minLength = StatsUtils.min(lengths);
		}
		
		return minLength;
	}
	
	private Object getGenLengthMax() {
		Integer maxLength = null;
		
		// Request the population from the stats manager.
		int[] lengths = (int[]) Controller.getStatsManager().getGenerationStat(GEN_LENGTHS);
		
		if (lengths != null) {
			maxLength = StatsUtils.max(lengths);
		}
		
		return maxLength;
	}
	
	private Object getGenLengthAve() {
		Double aveLength = null;
		
		// Request the population from the stats manager.
		int[] lengths = (int[]) Controller.getStatsManager().getGenerationStat(GEN_LENGTHS);
		
		if (lengths != null) {
			aveLength = StatsUtils.ave(lengths);
		}
		
		return aveLength;
	}
	
	private Object getGenLengthStdev() {
		Double stdevLength = null;
		
		// Request the population from the stats manager.
		int[] lengths = (int[]) Controller.getStatsManager().getGenerationStat(GEN_LENGTHS);
		
		if (lengths != null) {
			stdevLength = StatsUtils.stdev(lengths);
		}
		
		return stdevLength;
	}
	
	private Object getGenNoTerminals() {
		int[] noTerminals = null;
		
		// Request the population from the stats manager.
		List<GPCandidateProgram> pop = (List<GPCandidateProgram>) Controller.getStatsManager().getGenerationStat(GEN_POPULATION);
		
		if (pop != null) {
			noTerminals = new int[pop.size()];
			
			for (int i=0; i<pop.size(); i++) {
				noTerminals[i] = pop.get(i).getNoTerminals();
			}
		}
		
		return noTerminals;
	}
	
	private Object getGenNoTerminalsMin() {
		Integer minNoTerminals = null;
		
		// Request the population from the stats manager.
		int[] noTerminals = (int[]) Controller.getStatsManager().getGenerationStat(GEN_NO_TERMINALS);
		
		if (noTerminals != null) {
			minNoTerminals = StatsUtils.min(noTerminals);
		}
		
		return minNoTerminals;
	}
	
	private Object getGenNoTerminalsMax() {
		Integer maxNoTerminals = null;
		
		// Request the population from the stats manager.
		int[] noTerminals = (int[]) Controller.getStatsManager().getGenerationStat(GEN_NO_TERMINALS);
		
		if (noTerminals != null) {
			maxNoTerminals = StatsUtils.max(noTerminals);
		}
		
		return maxNoTerminals;
	}
	
	private Object getGenNoTerminalsAve() {
		Double aveNoTerminals = null;
		
		// Request the population from the stats manager.
		int[] noTerminals = (int[]) Controller.getStatsManager().getGenerationStat(GEN_NO_TERMINALS);
		
		if (noTerminals != null) {
			aveNoTerminals = StatsUtils.ave(noTerminals);
		}
		
		return aveNoTerminals;
	}
	
	private Object getGenNoTerminalsStdev() {
		Double stdevNoTerminals = null;
		
		// Request the population from the stats manager.
		int[] noTerminals = (int[]) Controller.getStatsManager().getGenerationStat(GEN_NO_TERMINALS);
		
		if (noTerminals != null) {
			stdevNoTerminals = StatsUtils.stdev(noTerminals);
		}
		
		return stdevNoTerminals;
	}
	
	private Object getGenNoDistinctTerminals() {
		int[] noDistinctTerminals = null;
		
		// Request the population from the stats manager.
		List<GPCandidateProgram> pop = (List<GPCandidateProgram>) Controller.getStatsManager().getGenerationStat(GEN_POPULATION);
		
		if (pop != null) {
			noDistinctTerminals = new int[pop.size()];
			
			for (int i=0; i<pop.size(); i++) {
				noDistinctTerminals[i] = pop.get(i).getNoDistinctTerminals();
			}
		}
		
		return noDistinctTerminals;
	}

	private Object getGenNoDistinctTerminalsMin() {
		Integer minNoDistinctTerminals = null;
		
		// Request the population from the stats manager.
		int[] noTerminals = (int[]) Controller.getStatsManager().getGenerationStat(GEN_NO_DISTINCT_TERMINALS);
		
		if (noTerminals != null) {
			minNoDistinctTerminals = StatsUtils.min(noTerminals);
		}
		
		return minNoDistinctTerminals;
	}
	
	private Object getGenNoDistinctTerminalsMax() {
		Integer maxNoDistinctTerminals = null;
		
		// Request the population from the stats manager.
		int[] noTerminals = (int[]) Controller.getStatsManager().getGenerationStat(GEN_NO_DISTINCT_TERMINALS);
		
		if (noTerminals != null) {
			maxNoDistinctTerminals = StatsUtils.max(noTerminals);
		}
		
		return maxNoDistinctTerminals;
	}
	
	private Object getGenNoDistinctTerminalsAve() {
		Double aveNoDistinctTerminals = null;
		
		// Request the population from the stats manager.
		int[] noTerminals = (int[]) Controller.getStatsManager().getGenerationStat(GEN_NO_DISTINCT_TERMINALS);
		
		if (noTerminals != null) {
			aveNoDistinctTerminals = StatsUtils.ave(noTerminals);
		}
		
		return aveNoDistinctTerminals;
	}
	
	private Object getGenNoDistinctTerminalsStdev() {
		Double stdevNoDistinctTerminals = null;
		
		// Request the population from the stats manager.
		int[] noTerminals = (int[]) Controller.getStatsManager().getGenerationStat(GEN_NO_DISTINCT_TERMINALS);
		
		if (noTerminals != null) {
			stdevNoDistinctTerminals = StatsUtils.stdev(noTerminals);
		}
		
		return stdevNoDistinctTerminals;
	}
	
	private Object getGenNoFunctions() {
		int[] noFunctions = null;
		
		// Request the population from the stats manager.
		List<GPCandidateProgram> pop = (List<GPCandidateProgram>) Controller.getStatsManager().getGenerationStat(GEN_POPULATION);
		
		if (pop != null) {
			noFunctions = new int[pop.size()];
			
			for (int i=0; i<pop.size(); i++) {
				noFunctions[i] = pop.get(i).getNoFunctions();
			}
		}
		
		return noFunctions;
	}
	
	private Object getGenNoFunctionsMin() {
		Integer minNoFunctions = null;
		
		// Request the population from the stats manager.
		int[] noFunctions = (int[]) Controller.getStatsManager().getGenerationStat(GEN_NO_FUNCTIONS);
		
		if (noFunctions != null) {
			minNoFunctions = StatsUtils.min(noFunctions);
		}
		
		return minNoFunctions;
	}
	
	private Object getGenNoFunctionsMax() {
		Integer maxNoFunctions = null;
		
		// Request the population from the stats manager.
		int[] noFunctions = (int[]) Controller.getStatsManager().getGenerationStat(GEN_NO_FUNCTIONS);
		
		if (noFunctions != null) {
			maxNoFunctions = StatsUtils.max(noFunctions);
		}
		
		return maxNoFunctions;
	}
	
	private Object getGenNoFunctionsAve() {
		Double aveNoFunctions = null;
		
		// Request the population from the stats manager.
		int[] noFunctions = (int[]) Controller.getStatsManager().getGenerationStat(GEN_NO_FUNCTIONS);
		
		if (noFunctions != null) {
			aveNoFunctions = StatsUtils.ave(noFunctions);
		}
		
		return aveNoFunctions;
	}
	
	private Object getGenNoFunctionsStdev() {
		Double stdevNoFunctions = null;
		
		// Request the population from the stats manager.
		int[] noFunctions = (int[]) Controller.getStatsManager().getGenerationStat(GEN_NO_FUNCTIONS);
		
		if (noFunctions != null) {
			stdevNoFunctions = StatsUtils.stdev(noFunctions);
		}
		
		return stdevNoFunctions;
	}
	
	private Object getGenNoDistinctFunctions() {
		int[] noDistinctFunctions = null;
		
		// Request the population from the stats manager.
		List<GPCandidateProgram> pop = (List<GPCandidateProgram>) Controller.getStatsManager().getGenerationStat(GEN_POPULATION);
		
		if (pop != null) {
			noDistinctFunctions = new int[pop.size()];
			
			for (int i=0; i<pop.size(); i++) {
				noDistinctFunctions[i] = pop.get(i).getNoDistinctFunctions();
			}
		}
		
		return noDistinctFunctions;
	}

	private Object getGenNoDistinctFunctionsMin() {
		Integer minNoDistinctFunctions = null;
		
		// Request the population from the stats manager.
		int[] noFunctions = (int[]) Controller.getStatsManager().getGenerationStat(GEN_NO_DISTINCT_FUNCTIONS);
		
		if (noFunctions != null) {
			minNoDistinctFunctions = StatsUtils.min(noFunctions);
		}
		
		return minNoDistinctFunctions;
	}
	
	private Object getGenNoDistinctFunctionsMax() {
		Integer maxNoDistinctFunctions = null;
		
		// Request the population from the stats manager.
		int[] noFunctions = (int[]) Controller.getStatsManager().getGenerationStat(GEN_NO_DISTINCT_FUNCTIONS);
		
		if (noFunctions != null) {
			maxNoDistinctFunctions = StatsUtils.max(noFunctions);
		}
		
		return maxNoDistinctFunctions;
	}
	
	private Object getGenNoDistinctFunctionsAve() {
		Double aveNoDistinctFunctions = null;
		
		// Request the population from the stats manager.
		int[] noFunctions = (int[]) Controller.getStatsManager().getGenerationStat(GEN_NO_DISTINCT_FUNCTIONS);
		
		if (noFunctions != null) {
			aveNoDistinctFunctions = StatsUtils.ave(noFunctions);
		}
		
		return aveNoDistinctFunctions;
	}
	
	private Object getGenNoDistinctFunctionsStdev() {
		Double stdevNoDistinctFunctions = null;
		
		// Request the population from the stats manager.
		int[] noFunctions = (int[]) Controller.getStatsManager().getGenerationStat(GEN_NO_DISTINCT_FUNCTIONS);
		
		if (noFunctions != null) {
			stdevNoDistinctFunctions = StatsUtils.stdev(noFunctions);
		}
		
		return stdevNoDistinctFunctions;
	}
}
