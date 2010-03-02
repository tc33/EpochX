package org.epochx.gr.stats;

import org.epochx.stats.StatField;

public final class GRStatField extends StatField {
	
	/**
	 * 
	 */
	public static final String GEN_DEPTHS = "xgr-gen-depths";
	
	/**
	 * Requests a Double which is the average depth of CandidatePrograms in 
	 * that generation.
	 */
	public static final String GEN_DEPTH_AVE = "xgr-gen-depth-ave";
	
	/**
	 * Requests a Double which is the standard deviation of depths of 
	 * CandidatePrograms in that generation.
	 */
	public static final String GEN_DEPTH_STDEV = "xgr-gen-depth-stdev";
	
	/**
	 * Requests a Double which is maximum depth of all the CandidatePrograms in 
	 * that generation. Should always be a whole number.
	 */
	public static final String GEN_DEPTH_MAX = "xgr-gen-depth-max";
	
	/**
	 * Requests a Double which is minimum depth of all the CandidatePrograms in 
	 * that generation. Should always be a whole number.
	 */
	public static final String GEN_DEPTH_MIN = "xgr-gen-depth-min";

}
