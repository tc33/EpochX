package org.epochx.gp.stats;

import static org.epochx.gp.stats.GPStatField.*;
import static org.epochx.stats.StatField.*;

import java.util.*;

import org.epochx.core.*;
import org.epochx.gp.representation.*;
import org.epochx.stats.*;

public class GPStatsEngine implements StatsEngine {

	@Override
	public Object getCrossoverStat(String field) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getGenerationStat(String field) {
		Object value = null;
		
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
		}
		
		return value;
	}

	@Override
	public Object getMutationStat(String field) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getRunStat(String field) {
		// TODO Auto-generated method stub
		return null;
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

}
