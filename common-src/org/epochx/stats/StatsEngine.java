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
package org.epochx.stats;

import static org.epochx.stats.StatField.*;

import java.util.*;

import org.epochx.core.*;
import org.epochx.representation.*;

public class StatsEngine {

	public Object getRunStat(String field) {
		return null;
	}
	
	public Object getGenerationStat(String field) {
		Object value = null;
		
		if (field.equals(GEN_FITNESSES)) {
			value = getGenFitnesses();
		} else if (field.equals(GEN_FITNESS_MIN)) {
			value = getGenFitnessMin();
		} else if (field.equals(GEN_FITNESS_MAX)) {
			value = getGenFitnessMax();
		} else if (field.equals(GEN_FITNESS_AVE)) {
			value = getGenFitnessAve();
		} else if (field.equals(GEN_FITNESS_STDEV)) {
			value = getGenFitnessStdev();
		} else if (field.equals(GEN_FITNESS_MEDIAN)) {
			value = getGenFitnessMedian();
		} else if (field.equals(GEN_FITNESS_CI95)) {
			value = getGenFitnessCI95();
		} else if (field.equals(GEN_FITTEST_PROGRAM)) {
			value = getGenBestProgram();
		}
		
		return value;
	}
	
	public Object getCrossoverStat(String field) {
		return null;
	}

	public Object getMutationStat(String field) {
		return null;
	}
	
	private Object getGenFitnesses() {
		double[] fitnesses = null;
		
		// Request the population from the stats manager.
		List<CandidateProgram> pop = (List<CandidateProgram>) Controller.getStatsManager().getGenerationStat(GEN_POPULATION);
		
		if (pop != null) {
			fitnesses = new double[pop.size()];
			
			for (int i=0; i<pop.size(); i++) {
				fitnesses[i] = pop.get(i).getFitness();
			}
		}
		
		return fitnesses;
	}

	private Object getGenFitnessMin() {
		Double minFitness = null;
		
		// Request the population from the stats manager.
		double[] fitnesses = (double[]) Controller.getStatsManager().getGenerationStat(GEN_FITNESSES);
		
		// 
		if (fitnesses != null) {
			minFitness = StatsUtils.min(fitnesses);
		}
		
		return minFitness;
	}
	
	private Object getGenFitnessMax() {
		Double maxFitness = null;
		
		// Request the population from the stats manager.
		double[] fitnesses = (double[]) Controller.getStatsManager().getGenerationStat(GEN_FITNESSES);
		
		// 
		if (fitnesses != null) {
			maxFitness = StatsUtils.max(fitnesses);
		}
		
		return maxFitness;
	}

	private Object getGenFitnessAve() {
		Double aveFitness = null;
		
		// Request the population from the stats manager.
		double[] fitnesses = (double[]) Controller.getStatsManager().getGenerationStat(GEN_FITNESSES);
		
		// 
		if (fitnesses != null) {
			aveFitness = StatsUtils.ave(fitnesses);
		}
		
		return aveFitness;
	}
	
	private Object getGenFitnessStdev() {
		Double stdevFitness = null;
		
		// Request the population from the stats manager.
		double[] fitnesses = (double[]) Controller.getStatsManager().getGenerationStat(GEN_FITNESSES);
		double averageFitness = (Double) Controller.getStatsManager().getGenerationStat(GEN_FITNESS_AVE);
		
		// 
		if (fitnesses != null) {
			stdevFitness = StatsUtils.stdev(fitnesses, averageFitness);
		}
		
		return stdevFitness;
	}
	
	private Object getGenFitnessMedian() {
		Double medianFitness = null;
		
		// Request the population from the stats manager.
		double[] fitnesses = (double[]) Controller.getStatsManager().getGenerationStat(GEN_FITNESSES);
		
		// 
		if (fitnesses != null) {
			medianFitness = StatsUtils.median(fitnesses);
		}
		
		return medianFitness;
	}
	
	private Object getGenFitnessCI95() {
		Double ci95Fitness = null;
		
		// Request the population from the stats manager.
		double[] fitnesses = (double[]) Controller.getStatsManager().getGenerationStat(GEN_FITNESSES);
		double stdev = (Double) Controller.getStatsManager().getGenerationStat(GEN_FITNESS_STDEV);
		
		// 
		if (fitnesses != null) {
			ci95Fitness = StatsUtils.ci95(fitnesses, stdev);
		}
		
		return ci95Fitness;
	}
	
	private Object getGenBestProgram() {
		CandidateProgram bestProgram = null;
		
		double[] fitnesses = (double[]) Controller.getStatsManager().getGenerationStat(GEN_FITNESSES);
		List<CandidateProgram> pop = (List<CandidateProgram>) Controller.getStatsManager().getGenerationStat(GEN_POPULATION);
		
		if (fitnesses != null) {
			int bestProgramIndex = StatsUtils.minIndex(fitnesses);
			
			bestProgram = pop.get(bestProgramIndex);
		}
		
		return bestProgram;
	}
}
