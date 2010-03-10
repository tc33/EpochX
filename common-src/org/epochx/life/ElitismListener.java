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
package org.epochx.life;

import java.util.List;

import org.epochx.representation.CandidateProgram;


/**
 * Defines the elitism events which may be raised.
 */
public interface ElitismListener {

	/**
	 * Event raised before the elitism operation starts.
	 */
	public void onElitismStart();
	
	/**
	 * Event raised after the elitism operation has been carried out. 
	 * The elites may be modified and returned to be used, but it is not 
	 * possible to (not does it make sense to) revert elitism.
	 * 
	 * @param elites the selection of chosen elites.
	 * @return a list of <code>CandidatePrograms</code> to use as the set of 
	 * elites. Note that it is not appropriate to return a value of null and 
	 * this will cause undefined behaviour.
	 */
	public List<CandidateProgram> onElitism(List<CandidateProgram> elites);
	
	/**
	 * Event raised after the crossover operation has ended and been accepted.
	 */
	public void onElitismEnd();
}
