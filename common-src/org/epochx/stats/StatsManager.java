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
package org.epochx.stats;

import java.util.*;

import org.epochx.core.Model;
import org.epochx.life.*;

/*
 * Listen for life cycle events and if they happen then clear appropriate old data out of the data maps.
 * 
 * When requesting a stat field check the hashmaps for it, if it exists then return it, if not then we ask 
 * each stat engine in turn if we can generate it. If so then we stash it in the hashmap for next time.
 */
public class StatsManager {
	//TODO Statistics from the controller about the whole set of runs.
	//TODO Initialisation statistics.

	// The controlling model.
	private Model model;
	
	private Map<String, Object> runData;
	private Map<String, Object> generationData;
	private Map<String, Object> crossoverData;
	private Map<String, Object> mutationData;
	
	private StatsEngine statsEngine;
	
	public StatsManager(Model model) {
		this(model, new StatsEngine(model));
	}
	
	public StatsManager(Model model, StatsEngine statsEngine) {
		this.model = model;
		this.statsEngine = statsEngine;
		
		runData = new HashMap<String, Object>();
		generationData = new HashMap<String, Object>();
		crossoverData = new HashMap<String, Object>();
		mutationData = new HashMap<String, Object>();
		
		// Setup the listeners to clear the data stores when appropriate.
		setupListeners();
	}
	
	public void addRunData(String field, Object value) {
		runData.put(field, value);
	}
	
	public void addGenerationData(String field, Object value) {
		generationData.put(field, value);
	}
	
	public void addCrossoverData(String field, Object value) {
		crossoverData.put(field, value);
	}
	
	public void addMutationData(String field, Object value) {
		mutationData.put(field, value);
	}
	
	/**
	 * If the stat field does not exist or is otherwise unavailable then null
	 * will be returned.
	 * @param field
	 * @return
	 */
	public Object getRunStat(String field) {
		Object stat = runData.get(field);
		
		if (stat == null) {
			stat = generateRunStat(field);
		}
		
		return stat;
	}
	
	public Object getGenerationStat(String field) {
		Object stat = generationData.get(field);
		
		if (stat == null) {
			stat = generateGenerationStat(field);
		}
		
		return stat;
	}
	
	public Object getCrossoverStat(String field) {
		Object stat = crossoverData.get(field);
		
		if (stat == null) {
			stat = generateCrossoverStat(field);
		}
		
		return stat;
	}
	
	public Object getMutationStat(String field) {
		Object stat = mutationData.get(field);
		
		if (stat == null) {
			stat = generateMutationStat(field);
		}
		
		return stat;
	}
	
	public Object[] getRunStats(String[] fields) {
		Object[] stats = new Object[fields.length];
		for (int i=0; i<fields.length; i++) {
			stats[i] = getRunStat(fields[i]);
		}
		return stats;
	}
	
	public Object[] getGenerationStats(String[] fields) {
		Object[] stats = new Object[fields.length];
		for (int i=0; i<fields.length; i++) {
			stats[i] = getGenerationStat(fields[i]);
		}
		return stats;
	}
	
	public Object[] getCrossoverStats(String[] fields) {
		Object[] stats = new Object[fields.length];
		for (int i=0; i<fields.length; i++) {
			stats[i] = getCrossoverStat(fields[i]);
		}
		return stats;
	}
	
	public Object[] getMutationStats(String[] fields) {
		Object[] stats = new Object[fields.length];
		for (int i=0; i<fields.length; i++) {
			stats[i] = getMutationStat(fields[i]);
		}
		return stats;
	}
	
	public void printRunStats(String[] fields) {
		printRunStats(fields, "\t");
	}
	
	public void printRunStats(String[] fields, String separator) {
		Object[] stats = getRunStats(fields);
		
		printStats(stats, separator);
	}
	
	public void printGenerationStats(String[] fields) {
		printGenerationStats(fields, "\t");
	}
	
	public void printGenerationStats(String[] fields, String separator) {
		Object[] stats = getGenerationStats(fields);
		
		printStats(stats, separator);
	}
	
	public void printCrossoverStats(String[] fields) {
		printCrossoverStats(fields, "\t");
	}
	
	public void printCrossoverStats(String[] fields, String separator) {
		Object[] stats = getCrossoverStats(fields);
		
		printStats(stats, separator);
	}
	
	public void printMutationStats(String[] fields) {
		printMutationStats(fields, "\t");
	}
	
	public void printMutationStats(String[] fields, String separator) {
		Object[] stats = getMutationStats(fields);
		
		printStats(stats, separator);
	}
	
	private void printStats(Object[] stats, String separator) {
		for (int i=0; i<stats.length; i++) {
			if (i != 0) {
				System.out.print(separator);
			}
			System.out.print(stats[i]);
		}
		System.out.println();
	}
	
	private Object generateRunStat(String field) {		
		return statsEngine.getRunStat(field);
	}
	
	private Object generateGenerationStat(String field) {
		return statsEngine.getGenerationStat(field);
	}
	
	private Object generateCrossoverStat(String field) {
		return statsEngine.getCrossoverStat(field);
	}
	
	private Object generateMutationStat(String field) {
		return statsEngine.getMutationStat(field);
	}
	
	public void setStatsEngine(StatsEngine statsEngine) {
		if (statsEngine == null) {
			statsEngine = new StatsEngine(model);
		}
		
		this.statsEngine = statsEngine;
	}

	private void setupListeners() {
		// Clear the run data.
		model.getLifeCycleManager().addRunListener(new RunAdapter(){
			@Override
			public void onRunStart() {
				runData.clear();
			}
		});
		
		// Clear the run data.
		model.getLifeCycleManager().addGenerationListener(new GenerationAdapter(){
			@Override
			public void onGenerationStart() {
				generationData.clear();
			}
		});
		
		// Clear the run data.
		model.getLifeCycleManager().addCrossoverListener(new CrossoverAdapter(){
			@Override
			public void onCrossoverStart() {
				crossoverData.clear();
			}
		});
		
		// Clear the run data.
		model.getLifeCycleManager().addMutationListener(new MutationAdapter(){
			@Override
			public void onMutationStart() {
				mutationData.clear();
			}
		});
	}

}
