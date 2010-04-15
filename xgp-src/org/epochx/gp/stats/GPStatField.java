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
package org.epochx.gp.stats;

import org.epochx.stats.StatField;

/**
 * Provides constants to be used as keys to request statistics from the 
 * StatsManager specific to XGP.
 */
public class GPStatField extends StatField {

	/*
	 * No need to ever create an object of GPStatField.
	 */
	private GPStatField() {}
	
	/**
	 * Returns an <code>int[]</code> which contains the depths of all the 
	 * <code>CandidateProgram</code>s in the population at the end of the 
	 * previous generation.
	 */
	public static final String GEN_DEPTHS = "xgp-gen-depths";
	
	/**
	 * Returns a <code>Double</code> which is the average depth of all the
	 * <code>CandidateProgram</code>s in the population at the end of the
	 * previous generation.
	 */
	public static final String GEN_DEPTH_AVE = "xgp-gen-depth-ave";
	
	/**
	 * Returns a <code>Double</code> which is the standard deviation of the 
	 * depths of all the <code>CandidateProgram</code>s in the 
	 * population at the end of the previous generation.
	 */
	public static final String GEN_DEPTH_STDEV = "xgp-gen-depth-stdev";
	
	/**
	 * Returns an <code>Integer</code> which is the maximum program depth of 
	 * all the <code>CandidateProgram</code>s in the population at the end of
	 * the previous generation.
	 */
	public static final String GEN_DEPTH_MAX = "xgp-gen-depth-max";
	
	/**
	 * Returns an <code>Integer</code> which is the minimum program depth of all
	 * the <code>CandidateProgram</code>s in the population at the end of the
	 * previous generation.
	 */
	public static final String GEN_DEPTH_MIN = "xgp-gen-depth-min";
	
	/**
	 * Returns a <code>double[]</code> which contains the average number of 
	 * nodes at each depth of the <code>CandidateProgram</code>s in the 
	 * population at the end of the previous generation. The root node of a 
	 * <code>CandidateProgram</code> is depth 0.
	 */
	public static final String GEN_AVE_NODES_PER_DEPTH = "xgp-gen-ave-nodes-per-depth";
	
	/**
	 * Returns an <code>int[]</code> which contains the lengths of all the 
	 * <code>CandidateProgram</code>s in the population at the end of the 
	 * previous generation.
	 */
	public static final String GEN_LENGTHS = "xgp-gen-lengths";
	
	/**
	 * Returns a <code>Double</code> which is the average length of all the
	 * <code>CandidateProgram</code>s in the population at the end of the
	 * previous generation.
	 */
	public static final String GEN_LENGTH_AVE = "xgp-gen-length-ave";
	
	/**
	 * Returns a <code>Double</code> which is the standard deviation of the 
	 * lengths of all the <code>CandidateProgram</code>s in the 
	 * population at the end of the previous generation.
	 */
	public static final String GEN_LENGTH_STDEV = "xgp-gen-length-stdev";
	
	/**
	 * Returns an <code>Integer</code> which is the maximum program length of 
	 * all the <code>CandidateProgram</code>s in the population at the end of
	 * the previous generation.
	 */
	public static final String GEN_LENGTH_MAX = "xgp-gen-length-max";
	
	/**
	 * Returns an <code>Integer</code> which is the minimum program length of 
	 * all the <code>CandidateProgram</code>s in the population at the end of 
	 * the previous generation.
	 */
	public static final String GEN_LENGTH_MIN = "xgp-gen-length-min";
	
	/**
	 * Returns a <code>int[]</code> which contains the number of terminal nodes
	 * in each <code>CandidateProgram</code> in the population at the end of the
	 * previous generation.
	 */
	public static final String GEN_NO_TERMINALS = "xgp-gen-no-terminals";
	
	/**
	 * Returns a <code>Double</code> which is the average number of terminals
	 * in all the <code>CandidateProgram</code>s in the population at the end of
	 * the previous generation.
	 */
	public static final String GEN_NO_TERMINALS_AVE = "xgp-gen-no-terminals-ave";
	
	/**
	 * Returns a <code>Double</code> which is the standard deviation of the 
	 * number of terminals in all the <code>CandidateProgram</code>s in the 
	 * population at the end of the previous generation.
	 */
	public static final String GEN_NO_TERMINALS_STDEV = "xgp-gen-no-terminals-stdev";
	
	/**
	 * Returns an <code>Integer</code> which is the maximum number of terminals
	 * of all the <code>CandidateProgram</code>s in the population at the end of
	 * the previous generation.
	 */
	public static final String GEN_NO_TERMINALS_MAX = "xgp-gen-no-terminals-max";
	
	/**
	 * Returns an <code>Integer</code> which is the minimum number of terminals
	 * of all the <code>CandidateProgram</code>s in the population at the end of
	 * the previous generation.
	 */
	public static final String GEN_NO_TERMINALS_MIN = "xgp-gen-no-terminals-min";
	
	/**
	 * Returns a <code>int[]</code> which contains the number of unique terminal
	 * nodes in each <code>CandidateProgram</code> in the population at the end 
	 * of the previous generation.
	 */
	public static final String GEN_NO_DISTINCT_TERMINALS = "xgp-gen-no-distinct-terminals";
	
	/**
	 * Returns a <code>Double</code> which is the average number of unique 
	 * terminals in all the <code>CandidateProgram</code>s in the population at 
	 * the end of the previous generation.
	 */
	public static final String GEN_NO_DISTINCT_TERMINALS_AVE = "xgp-gen-no-distinct-terminals-ave";
	
	/**
	 * Returns a <code>Double</code> which is the standard deviation of the 
	 * number of unique terminals in all the <code>CandidateProgram</code>s in 
	 * the population at the end of the previous generation.
	 */
	public static final String GEN_NO_DISTINCT_TERMINALS_STDEV = "xgp-gen-no-distinct-terminals-stdev";
	
	/**
	 * Returns an <code>Integer</code> which is the maximum number of unique 
	 * terminals of all the <code>CandidateProgram</code>s in the population at 
	 * the end of the previous generation.
	 */
	public static final String GEN_NO_DISTINCT_TERMINALS_MAX = "xgp-gen-no-distinct-terminals-max";
	
	/**
	 * Returns an <code>Integer</code> which is the minimum number of unique 
	 * terminals of all the <code>CandidateProgram</code>s in the population at 
	 * the end of the previous generation.
	 */
	public static final String GEN_NO_DISTINCT_TERMINALS_MIN = "xgp-gen-no-distinct-terminals-min";
	
	/**
	 * Returns a <code>int[]</code> which contains the number of function nodes
	 * in each <code>CandidateProgram</code> in the population at the end of the
	 * previous generation.
	 */
	public static final String GEN_NO_FUNCTIONS = "xgp-gen-no-functions";	
	
	/**
	 * Returns a <code>Double</code> which is the average number of functions
	 * in all the <code>CandidateProgram</code>s in the population at the end of
	 * the previous generation.
	 */
	public static final String GEN_NO_FUNCTIONS_AVE = "xgp-gen-no-functions-ave";
	
	/**
	 * Returns a <code>Double</code> which is the standard deviation of the 
	 * number of functions in all the <code>CandidateProgram</code>s in the 
	 * population at the end of the previous generation.
	 */
	public static final String GEN_NO_FUNCTIONS_STDEV = "xgp-gen-no-functions-stdev";
	
	/**
	 * Returns an <code>Integer</code> which is the maximum number of functions
	 * of all the <code>CandidateProgram</code>s in the population at the end of
	 * the previous generation.
	 */
	public static final String GEN_NO_FUNCTIONS_MAX = "xgp-gen-no-functions-max";
	
	/**
	 * Returns an <code>Integer</code> which is the minimum number of functions
	 * of all the <code>CandidateProgram</code>s in the population at the end of
	 * the previous generation.
	 */
	public static final String GEN_NO_FUNCTIONS_MIN = "xgp-gen-no-functions-min";
	
	/**
	 * Returns a <code>int[]</code> which contains the number of unique function
	 * nodes in each <code>CandidateProgram</code> in the population at the end 
	 * of the previous generation.
	 */
	public static final String GEN_NO_DISTINCT_FUNCTIONS = "xgp-gen-no-distinct-functions";	
	
	/**
	 * Returns a <code>Double</code> which is the average number of unique 
	 * functions in all the <code>CandidateProgram</code>s in the population at 
	 * the end of the previous generation.
	 */
	public static final String GEN_NO_DISTINCT_FUNCTIONS_AVE = "xgp-gen-no-distinct-functions-ave";
	
	/**
	 * Returns a <code>Double</code> which is the standard deviation of the 
	 * number of unique functions in all the <code>CandidateProgram</code>s in 
	 * the population at the end of the previous generation.
	 */
	public static final String GEN_NO_DISTINCT_FUNCTIONS_STDEV = "xgp-gen-no-distinct-functions-stdev";
	
	/**
	 * Returns an <code>Integer</code> which is the maximum number of unique 
	 * functions of all the <code>CandidateProgram</code>s in the population at 
	 * the end of the previous generation.
	 */
	public static final String GEN_NO_DISTINCT_FUNCTIONS_MAX = "xgp-gen-no-distinct-functions-max";
	
	/**
	 * Returns an <code>Integer</code> which is the minimum number of unique 
	 * functions of all the <code>CandidateProgram</code>s in the population at 
	 * the end of the previous generation.
	 */
	public static final String GEN_NO_DISTINCT_FUNCTIONS_MIN = "xgp-gen-no-distinct-functions-min";
	
}
