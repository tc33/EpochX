package org.epochx.stats;

import java.util.*;

import org.epochx.life.*;
import org.epochx.representation.*;

/*
 * Listen for life cycle events and if they happen then clear appropriate old data out of the data maps.
 * 
 * When requesting a stat field check the hashmaps for it, if it exists then return it, if not then we ask 
 * each stat engine in turn if we can generate it. If so then we stash it in the hashmap for next time.
 */
public class StatsManager implements RunListener,
									 GenerationListener,
									 CrossoverListener,
									 MutationListener {
	//TODO Statistics from the controller about the whole set of runs.
	//TODO Initialisation statistics.

	private Map<String, Object> runData;
	private Map<String, Object> generationData;
	private Map<String, Object> crossoverData;
	private Map<String, Object> mutationData;
	
	private List<StatsEngine> statsEngines;
	
	public StatsManager() {
		runData = new HashMap<String, Object>();
		generationData = new HashMap<String, Object>();
		crossoverData = new HashMap<String, Object>();
		mutationData = new HashMap<String, Object>();
		
		statsEngines = new ArrayList<StatsEngine>();
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
	
	private Object generateRunStat(String field) {
		Object stat = null;
		
		for (StatsEngine engine: statsEngines) {
			stat = engine.getRunStat(field);
			
			// Stop if we've found a match.
			if (stat != null) {
				break;
			}
		}
		
		return stat;
	}
	
	private Object generateGenerationStat(String field) {
		Object stat = null;
		
		for (StatsEngine engine: statsEngines) {
			stat = engine.getGenerationStat(field);
			
			// Stop if we've found a match.
			if (stat != null) {
				break;
			}
		}
		
		return stat;
	}
	
	private Object generateCrossoverStat(String field) {
		Object stat = null;
		
		for (StatsEngine engine: statsEngines) {
			stat = engine.getCrossoverStat(field);
			
			// Stop if we've found a match.
			if (stat != null) {
				break;
			}
		}
		
		return stat;
	}
	
	private Object generateMutationStat(String field) {
		Object stat = null;
		
		for (StatsEngine engine: statsEngines) {
			stat = engine.getMutationStat(field);
			
			// Stop if we've found a match.
			if (stat != null) {
				break;
			}
		}
		
		return stat;
	}
	
	public void addStatsEngine(StatsEngine statsEngine) {
		statsEngines.add(statsEngine);
	}
	
	public void removeStatsEngine(StatsEngine statsEngine) {
		statsEngines.remove(statsEngine);
	}

	@Override
	public void onRunStart() {
		runData.clear();
	}

	@Override
	public void onGenerationStart() {
		generationData.clear();
	}

	@Override
	public CandidateProgram[] onCrossover(CandidateProgram[] parents,
			CandidateProgram[] children) {
		return null;
	}

	@Override
	public CandidateProgram onMutation(CandidateProgram parent,
			CandidateProgram child) {
		return null;
	}
}
