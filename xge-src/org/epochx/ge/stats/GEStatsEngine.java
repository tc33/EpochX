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
package org.epochx.ge.stats;

import static org.epochx.ge.stats.GEStatField.*;
import static org.epochx.stats.StatField.GEN_POPULATION;

import java.util.List;

import org.epochx.core.Model;
import org.epochx.ge.model.GEAbstractModel;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.stats.*;

public class GEStatsEngine extends StatsEngine {

	// The controlling model.
	private GEAbstractModel model;
	
	
	public GEStatsEngine(GEAbstractModel model) {
		super(model);
		
		this.model = model;
	}

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
			} else if (field.equals(GEN_LENGTHS)) {
				value = getGenLengths();
			} else if (field.equals(GEN_DEPTH_MIN)) {
				value = getGenLengthMin();
			} else if (field.equals(GEN_DEPTH_MAX)) {
				value = getGenLengthMax();
			} else if (field.equals(GEN_DEPTH_AVE)) {
				value = getGenLengthAve();
			} else if (field.equals(GEN_DEPTH_STDEV)) {
				value = getGenLengthStdev();
			}
		}
		
		return value;
	}
	
	private Object getGenLengths() {
		int[] lengths = null;
		
		// Request the population from the stats manager.
		List<GECandidateProgram> pop = (List<GECandidateProgram>) model.getStatsManager().getGenerationStat(GEN_POPULATION);
		
		if (pop != null) {
			lengths = new int[pop.size()];
			
			for (int i=0; i<pop.size(); i++) {
				lengths[i] = pop.get(i).getNoCodons();
			}
		}
		
		return lengths;
	}
	
	private Object getGenLengthMin() {
		Integer minLength = null;
		
		// Request the population from the stats manager.
		int[] lengths = (int[]) model.getStatsManager().getGenerationStat(GEN_LENGTHS);
		
		if (lengths != null) {
			minLength = StatsUtils.min(lengths);
		}
		
		return minLength;
	}
	
	private Object getGenLengthMax() {
		Integer maxLength = null;
		
		// Request the population from the stats manager.
		int[] lengths = (int[]) model.getStatsManager().getGenerationStat(GEN_LENGTHS);
		
		if (lengths != null) {
			maxLength = StatsUtils.max(lengths);
		}
		
		return maxLength;
	}
	
	private Object getGenLengthAve() {
		Double aveLength = null;
		
		// Request the population from the stats manager.
		int[] lengths = (int[]) model.getStatsManager().getGenerationStat(GEN_LENGTHS);
		
		if (lengths != null) {
			aveLength = StatsUtils.ave(lengths);
		}
		
		return aveLength;
	}
	
	private Object getGenLengthStdev() {
		Double stdevLength = null;
		
		// Request the population from the stats manager.
		int[] lengths = (int[]) model.getStatsManager().getGenerationStat(GEN_LENGTHS);
		
		if (lengths != null) {
			stdevLength = StatsUtils.stdev(lengths);
		}
		
		return stdevLength;
	}
	
	private Object getGenDepths() {
		int[] depths = null;
		
		// Request the population from the stats manager.
		List<GECandidateProgram> pop = (List<GECandidateProgram>) model.getStatsManager().getGenerationStat(GEN_POPULATION);
		
		if (pop != null) {
			depths = new int[pop.size()];
			
			for (int i=0; i<pop.size(); i++) {
				depths[i] = pop.get(i).getParseTreeDepth();
			}
		}
		
		return depths;
	}
	
	private Object getGenDepthMin() {
		Integer minDepth = null;
		
		// Request the population from the stats manager.
		int[] depths = (int[]) model.getStatsManager().getGenerationStat(GEN_DEPTHS);
		
		if (depths != null) {
			minDepth = StatsUtils.min(depths);
		}
		
		return minDepth;
	}
	
	private Object getGenDepthMax() {
		Integer maxDepth = null;
		
		// Request the population from the stats manager.
		int[] depths = (int[]) model.getStatsManager().getGenerationStat(GEN_DEPTHS);
		
		if (depths != null) {
			maxDepth = StatsUtils.max(depths);
		}
		
		return maxDepth;
	}
	
	private Object getGenDepthAve() {
		Double aveDepth = null;
		
		// Request the population from the stats manager.
		int[] depths = (int[]) model.getStatsManager().getGenerationStat(GEN_DEPTHS);
		
		if (depths != null) {
			aveDepth = StatsUtils.ave(depths);
		}
		
		return aveDepth;
	}
	
	private Object getGenDepthStdev() {
		Double stdevDepth = null;
		
		// Request the population from the stats manager.
		int[] depths = (int[]) model.getStatsManager().getGenerationStat(GEN_DEPTHS);
		
		if (depths != null) {
			stdevDepth = StatsUtils.stdev(depths);
		}
		
		return stdevDepth;
	}

}
