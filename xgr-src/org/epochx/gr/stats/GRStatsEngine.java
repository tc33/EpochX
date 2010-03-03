package org.epochx.gr.stats;

import static org.epochx.gr.stats.GRStatField.*;
import static org.epochx.stats.StatField.GEN_POPULATION;

import java.util.List;

import org.epochx.core.Controller;
import org.epochx.gr.representation.GRCandidateProgram;
import org.epochx.stats.*;

public class GRStatsEngine extends StatsEngine {

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
			}
		}
		
		return value;
	}

	private Object getGenDepths() {
		int[] depths = null;
		
		// Request the population from the stats manager.
		List<GRCandidateProgram> pop = (List<GRCandidateProgram>) Controller.getStatsManager().getGenerationStat(GEN_POPULATION);
		
		if (pop != null) {
			depths = new int[pop.size()];
			
			for (int i=0; i<pop.size(); i++) {
				depths[i] = pop.get(i).getDepth();
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

}
