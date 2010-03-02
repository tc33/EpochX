package org.epochx.stats;

public interface StatsEngine {

	public Object getRunStat(String field);
	
	public Object getGenerationStat(String field);
	
	public Object getCrossoverStat(String field);
	
	public Object getMutationStat(String field);
	
}
