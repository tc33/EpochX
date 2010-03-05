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
