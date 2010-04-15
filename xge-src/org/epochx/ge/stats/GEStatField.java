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

import org.epochx.stats.StatField;

/**
 * Provides constants to be used as keys to request statistics from the 
 * StatsManager specific to XGE.
 */
public final class GEStatField extends StatField {

	/*
	 * No need to ever create an object of GEStatField.
	 */
	private GEStatField() {}
	
	/**
	 * Returns an <code>int[]</code> which contains the depths of all the 
	 * <code>CandidateProgram</code>s in the population at the end of the 
	 * previous generation.
	 */
	public static final String GEN_DEPTHS = "xge-gen-depths";
	
	/**
	 * Returns a <code>Double</code> which is the average depth of all the
	 * <code>CandidateProgram</code>s in the population at the end of the
	 * previous generation.
	 */
	public static final String GEN_DEPTH_AVE = "xge-gen-depth-ave";
	
	/**
	 * Returns a <code>Double</code> which is the standard deviation of the 
	 * depths of all the <code>CandidateProgram</code>s in the 
	 * population at the end of the previous generation.
	 */
	public static final String GEN_DEPTH_STDEV = "xge-gen-depth-stdev";
	
	/**
	 * Returns an <code>Integer</code> which is the maximum program depth of 
	 * all the <code>CandidateProgram</code>s in the population at the end of
	 * the previous generation.
	 */
	public static final String GEN_DEPTH_MAX = "xge-gen-depth-max";
	
	/**
	 * Returns an <code>Integer</code> which is the minimum program depth of all
	 * the <code>CandidateProgram</code>s in the population at the end of the
	 * previous generation.
	 */
	public static final String GEN_DEPTH_MIN = "xge-gen-depth-min";
	
	/**
	 * Returns an <code>int[]</code> which contains the lengths of all the 
	 * <code>CandidateProgram</code>s in the population at the end of the 
	 * previous generation.
	 */
	public static final String GEN_LENGTHS = "xge-gen-lengths";
	
	/**
	 * Returns a <code>Double</code> which is the average length of all the
	 * <code>CandidateProgram</code>s in the population at the end of the
	 * previous generation.
	 */
	public static final String GEN_LENGTH_AVE = "xge-gen-length-ave";
	
	/**
	 * Returns a <code>Double</code> which is the standard deviation of the 
	 * lengths of all the <code>CandidateProgram</code>s in the 
	 * population at the end of the previous generation.
	 */
	public static final String GEN_LENGTH_STDEV = "xge-gen-length-stdev";
	
	/**
	 * Returns an <code>Integer</code> which is the maximum program length of 
	 * all the <code>CandidateProgram</code>s in the population at the end of
	 * the previous generation.
	 */
	public static final String GEN_LENGTH_MAX = "xge-gen-length-max";
	
	/**
	 * Returns an <code>Integer</code> which is the minimum program length of 
	 * all the <code>CandidateProgram</code>s in the population at the end of 
	 * the previous generation.
	 */
	public static final String GEN_LENGTH_MIN = "xge-gen-length-min";

}
