package org.epochx.gp.stats;

public final class GPStatField {

	/*
	 * No need to ever create an object of GPStatField.
	 */
	private GPStatField() {}

	/**
	 * 
	 */
	public static final String GEN_DEPTHS = "xgp-gen-depths";
	
	/**
	 * Requests a Double which is the average depth of CandidatePrograms in 
	 * that generation.
	 */
	public static final String GEN_DEPTH_AVE = "xgp-gen-depth-ave";
	
	/**
	 * Requests a Double which is the standard deviation of depths of 
	 * CandidatePrograms in that generation.
	 */
	public static final String GEN_DEPTH_STDEV = "xgp-gen-depth-stdev";
	
	/**
	 * Requests a Double which is maximum depth of all the CandidatePrograms in 
	 * that generation. Should always be a whole number.
	 */
	public static final String GEN_DEPTH_MAX = "xgp-gen-depth-max";
	
	/**
	 * Requests a Double which is minimum depth of all the CandidatePrograms in 
	 * that generation. Should always be a whole number.
	 */
	public static final String GEN_DEPTH_MIN = "xgp-gen-depth-min";
	
	/**
	 * Requests a Double[] which is of length DEPTH_MAX. Each element is the 
	 * average number of nodes of all CandidatePrograms in the population at 
	 * that depth.
	 */
	public static final String GEN_AVE_NODES_PER_DEPTH = "xgp-gen-ave-nodes-per-depth";
	
	/**
	 * 
	 */
	public static final String GEN_LENGTHS = "xgp-gen-lengths";
	
	/**
	 * Requests a Double which is the average length (that is - number of nodes) 
	 * of CandidatePrograms in that generation.
	 */
	public static final String GEN_LENGTH_AVE = "xgp-gen-length-ave";
	
	/**
	 * Requests a Double which is the standard deviation of lengths (that is - 
	 * number of nodes) of CandidatePrograms in that generation.
	 */
	public static final String GEN_LENGTH_STDEV = "xgp-gen-length-stdev";
	
	/**
	 * Requests a Double which is maximum length (that is - number of nodes) 
	 * of all the CandidatePrograms in that generation. Should always be a 
	 * whole number.
	 */
	public static final String GEN_LENGTH_MAX = "xgp-gen-length-max";
	
	/**
	 * Requests a Double which is minimum length (that is - number of nodes) 
	 * of all the CandidatePrograms in that generation. Should always be a 
	 * whole number.
	 */
	public static final String GEN_LENGTH_MIN = "xgp-gen-length-min";
	
	/**
	 * Requests a Double which is the average number of terminal nodes in all 
	 * the CandidatePrograms of that generation.
	 */
	public static final String GEN_NO_TERMINALS = "xgp-gen-no-terminals";
	
	/**
	 * Requests a Double which is the average number of terminal nodes in all 
	 * the CandidatePrograms of that generation.
	 */
	public static final String GEN_NO_TERMINALS_AVE = "xgp-gen-no-terminals-ave";
	
	/**
	 * Requests a Double which is the standard deviation of the number of 
	 * terminal nodes in each GPCandidateProgram in that generation.
	 */
	public static final String GEN_NO_TERMINALS_STDEV = "xgp-gen-no-terminals-stdev";
	
	/**
	 * Requests a Double which is the maximum number of terminal nodes in a 
	 * GPCandidateProgram in that generation.
	 */
	public static final String GEN_NO_TERMINALS_MAX = "xgp-gen-no-terminals-max";
	
	/**
	 * Requests a Double which is the minimum number of terminal nodes in a 
	 * GPCandidateProgram in that generation.
	 */
	public static final String GEN_NO_TERMINALS_MIN = "xgp-gen-no-terminals-min";
	
	/**
	 * 
	 */
	public static final String GEN_NO_DISTINCT_TERMINALS = "xgp-gen-no-distinct-terminals";
	
	/**
	 * Requests a Double which is the average number of unique terminal nodes
	 * in a GPCandidateProgram in that generation.
	 */
	public static final String GEN_NO_DISTINCT_TERMINALS_AVE = "xgp-gen-no-distinct-terminals-ave";
	
	/**
	 * Requests a Double which is the standard deviation of the number of 
	 * unique terminal nodes in each GPCandidateProgram in that generation.
	 */
	public static final String GEN_NO_DISTINCT_TERMINALS_STDEV = "xgp-gen-no-distinct-terminals-stdev";
	
	/**
	 * Requests a Double which is the maximum number of unique terminal nodes
	 * in a GPCandidateProgram in that generation.
	 */
	public static final String GEN_NO_DISTINCT_TERMINALS_MAX = "xgp-gen-no-distinct-terminals-max";
	
	/**
	 * Requests a Double which is the minimum number of unique terminal nodes
	 * in a GPCandidateProgram in that generation.
	 */
	public static final String GEN_NO_DISTINCT_TERMINALS_MIN = "xgp-gen-no-distinct-terminals-min";
	
	/**
	 * 
	 */
	public static final String GEN_NO_FUNCTIONS = "xgp-gen-no-functions";	
	
	/**
	 * Requests a Double which is the average number of function nodes in all 
	 * the CandidatePrograms of that generation.
	 */
	public static final String GEN_NO_FUNCTIONS_AVE = "xgp-gen-no-functions-ave";
	
	/**
	 * Requests a Double which is the standard deviation of the number of 
	 * function nodes in each GPCandidateProgram in that generation.
	 */
	public static final String GEN_NO_FUNCTIONS_STDEV = "xgp-gen-no-functions-stdev";
	
	/**
	 * Requests a Double which is the maximum number of function nodes in a 
	 * GPCandidateProgram in that generation.
	 */
	public static final String GEN_NO_FUNCTIONS_MAX = "xgp-gen-no-functions-max";
	
	/**
	 * Requests a Double which is the minimum number of function nodes in a 
	 * GPCandidateProgram in that generation.
	 */
	public static final String GEN_NO_FUNCTIONS_MIN = "xgp-gen-no-functions-min";
	
	/**
	 * 
	 */
	public static final String GEN_NO_DISTINCT_FUNCTIONS = "xgp-gen-no-distinct-functions";	
	
	/**
	 * Requests a Double which is the average number of unique function nodes
	 * in a GPCandidateProgram in that generation.
	 */
	public static final String GEN_NO_DISTINCT_FUNCTIONS_AVE = "xgp-gen-no-distinct-functions-ave";
	
	/**
	 * Requests a Double which is the standard deviation of the number of 
	 * unique function nodes in each GPCandidateProgram in that generation.
	 */
	public static final String GEN_NO_DISTINCT_FUNCTIONS_STDEV = "xgp-gen-no-distinct-functions-stdev";
	
	/**
	 * Requests a Double which is the maximum number of unique function nodes
	 * in a GPCandidateProgram in that generation.
	 */
	public static final String GEN_NO_DISTINCT_FUNCTIONS_MAX = "xgp-gen-no-distinct-functions-max";
	
	/**
	 * Requests a Double which is the minimum number of unique function nodes
	 * in a GPCandidateProgram in that generation.
	 */
	public static final String GEN_NO_DISTINCT_FUNCTIONS_MIN = "xgp-gen-no-distinct-functions-min";
	
}
