package org.epochx.ge.stats;

import org.epochx.stats.StatField;

public final class GEStatField extends StatField {

	/*
	 * No need to ever create an object of GEStatField.
	 */
	private GEStatField() {}
	
	/**
	 * 
	 */
	public static final String GEN_LENGTHS = "xge-gen-lengths";
	
	/**
	 * Requests a Double which is minimum length (that is - number of nodes) 
	 * of all the CandidatePrograms in that generation. Should always be a 
	 * whole number.
	 */
	public static final String GEN_LENGTH_MIN = "xge-gen-length-min";
	
	/**
	 * Requests a Double which is maximum length (that is - number of nodes) 
	 * of all the CandidatePrograms in that generation. Should always be a 
	 * whole number.
	 */
	public static final String GEN_LENGTH_MAX = "xge-gen-length-max";
	
	/**
	 * Requests a Double which is the average length (that is - number of nodes) 
	 * of CandidatePrograms in that generation.
	 */
	public static final String GEN_LENGTH_AVE = "xge-gen-length-ave";
	
	/**
	 * Requests a Double which is the standard deviation of lengths (that is - 
	 * number of nodes) of CandidatePrograms in that generation.
	 */
	public static final String GEN_LENGTH_STDEV = "xge-gen-length-stdev";

	/**
	 * 
	 */
	public static final String GEN_DEPTHS = "xge-gen-depths";
	
	/**
	 * Requests a Double which is the average depth of CandidatePrograms in 
	 * that generation.
	 */
	public static final String GEN_DEPTH_AVE = "xge-gen-depth-ave";
	
	/**
	 * Requests a Double which is the standard deviation of depths of 
	 * CandidatePrograms in that generation.
	 */
	public static final String GEN_DEPTH_STDEV = "xge-gen-depth-stdev";
	
	/**
	 * Requests a Double which is maximum depth of all the CandidatePrograms in 
	 * that generation. Should always be a whole number.
	 */
	public static final String GEN_DEPTH_MAX = "xge-gen-depth-max";
	
	/**
	 * Requests a Double which is minimum depth of all the CandidatePrograms in 
	 * that generation. Should always be a whole number.
	 */
	public static final String GEN_DEPTH_MIN = "xge-gen-depth-min";

}
