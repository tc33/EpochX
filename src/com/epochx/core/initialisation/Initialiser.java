/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of Epoch X - (The Genetic Programming Analysis Software)
 *
 *  Epoch X is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Epoch X is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with Epoch X.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.epochx.core.initialisation;

import java.util.*;

import com.epochx.core.representation.*;

/**
 * Implementations of this interface should be capable of generating an initial 
 * population of <code>CandidatePrograms</code>. The getInitialPopulation() 
 * method is called towards the start of execution of a run to get the first 
 * population which will then be evolved.
 */
public interface Initialiser<TYPE> {

	/**
	 * Construct and return an initial population of CandidatePrograms.
	 * Implementations will typically wish to return a population with a size 
	 * as given by calling getPopulationSize() on the controlling model.
	 * 
	 * @return A List of newly generated CandidatePrograms which will form the 
	 * initial population for a GP run.
	 */
	public List<CandidateProgram<TYPE>> getInitialPopulation();
	
}