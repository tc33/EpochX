package org.epochx.stats;

public final class StatField {

	/*
	 * No need to ever create an object of StatField.
	 */
	private StatField() {}

	/**
	 * 
	 */
	public static final String RUN_NUMBER = "run-number";
	
	/**
	 * 
	 */
	public static final String RUN_TIME = "run-time";
	
	/**
	 * Will be the best fitness in in the run so far.
	 */
	public static final String RUN_FITNESS_MIN = "run-fitness-min";
	
	/**
	 * Will be program with the best fitness in the run so far.
	 */
	public static final String RUN_FITTEST_PROGRAM = "run-fittest-program";
	
	/**
	 * 
	 */
	public static final String GEN_NUMBER = "gen-number";
	
	/**
	 * 
	 */
	public static final String GEN_FITNESSES = "gen-fitnesses";
	
	/**
	 * Requests a Double which is the minimum fitness of all the 
	 * CandidatePrograms in that generation. Note that if using standardised 
	 * fitness the 'best' fitness may be the minimum.
	 */
	public static final String GEN_FITNESS_MIN = "gen-fitness-min";
	
	/**
	 * Requests a Double which is the maximum fitness of all the 
	 * CandidatePrograms in that generation. Note that if using standardised 
	 * fitness the 'best' fitness may be the minimum.
	 */
	public static final String GEN_FITNESS_MAX = "gen-fitness-max";
	
	/**
	 * Requests a Double which is the average fitness of all the 
	 * CandidatePrograms in that generation.
	 */
	public static final String GEN_FITNESS_AVE = "gen-fitness-ave";
	
	/**
	 * Requests a Double which is the standard deviation of the fitnesses of 
	 * all the CandidatePrograms in that generation.
	 */
	public static final String GEN_FITNESS_STDEV = "gen-fitness-stdev";

	/**
	 * Requests a Double which is the median of all the fitnesses of all the 
	 * CandidatePrograms in that generation.
	 */
	public static final String GEN_FITNESS_MEDIAN = "gen-fitness-median";
	
	/**
	 * Requests a Double which is the confidence interval at 95% of the 
	 * fitnesses.
	 */
	public static final String GEN_FITNESS_CI95 = "gen-fitness-ci95";
	
	/**
	 * Requests a CandidateProgram which has the 'best' fitness in the 
	 * generation. This is usually the lowest fitness score.
	 */
	public static final String GEN_FITTEST_PROGRAM = "gen-fittest-program";
	
	/**
	 * Requests a List<CandidateProgram> which is the population at the end 
	 * of this generation.
	 */
	public static final String GEN_POPULATION = "gen-population";
	
	/**
	 * Requests a Long which is the length of time in nanoseconds that the
	 * generation lasted.
	 */
	public static final String GEN_TIME = "gen-time";
	
}
