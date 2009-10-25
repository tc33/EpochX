/*  
 *  Copyright 2009 Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of XGE: grammatical evolution for research
 *
 *  XGE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  XGE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with XGE.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.epochx.life;

/**
 *
 */
public interface TerminationListener {
	
	/**
	 * Called on termination of the GE run where execution ended because of the 
	 * successful identification of a CandidateProgram with a fitness equal to 
	 * or lower than the models termination fitness parameter.
	 */
	public void onFitnessTermination();
	
	/**
	 * Called on termination of the GE run where execution ended because the 
	 * requested number of generations was completed without identification of 
	 * a CandidateProgram with a fitness equal to or lower than the models 
	 * termination fitness parameter.
	 */
	public void onGenerationTermination();
	
}
