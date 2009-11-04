/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming for research
 *
 *  EpochX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EpochX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EpochX.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.epochx.life;


/**
 *
 */
public interface GenerationListener {

	/**
	 * Called at the start of each generation.
	 * 
	 * Note that this method is called <b>before</b> the generation statistics
	 * are generated so any generation statistics will be created based upon 
	 * the population returned here.
	 * 
	 * @param pop the population as it stands after the previous generation.
	 * @return a List of CandidatePrograms that should be used as the 
	 * population resulting from this generation.
	 */
	public void onGenerationStart();
	//TODO This method should allow null to revert a generation.
	
}