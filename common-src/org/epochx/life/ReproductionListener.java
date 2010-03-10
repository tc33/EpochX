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

import org.epochx.representation.CandidateProgram;

/**
 * Defines the reproduction events which may be raised.
 */
public interface ReproductionListener {
	
	/**
	 * Event raised before the reproduction operation starts.
	 */
	public void onReproductionStart();
	
	/**
	 * Event raised after the selection of a program to be reproduced. 
	 * The selected program may be modified and returned. This event is 
	 * revertable by returning null which will trigger the discarding of the 
	 * program and the reselection of a new one. This event will then be raised
	 * again. If the reproduction should be accepted then the program should be 
	 * returned as it is.
	 * 
	 * @param child the program that was selected to be reproduced.
	 * @return a CandidateProgram that should be used as the reproduced program
	 * and inserted into the next population.
	 */
	public CandidateProgram onReproduction(CandidateProgram program);
	
	/**
	 * Event raised after the reproduction operation has ended and been accepted.
	 */
	public void onReproductionEnd();
}
