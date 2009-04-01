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
package com.epochx.core;

import java.util.*;

import org.apache.log4j.*;

import com.epochx.core.representation.*;

/**
 * This class has the task of retrieving and maintaining elites within a 
 * population.
 */
public class GPElitism {

	static Logger logger = Logger.getLogger(GPElitism.class);
	
	/**
	 * Gets the best CandidatePrograms from the given population and returns 
	 * them. The number of programs returned will be either noElites or pop.size()
	 * if the size of the given population is smaller than noElites. Elites in 
	 * are defined as the very best programs in a population.
	 * 
	 * @param <TYPE> 	the return type of the CandidatePrograms being used.
	 * @param pop	 	the population from which elites need to be retrieved.
	 * @param noElites	the number of elites to obtain.
	 * @return a list containing the noElites best CandidatePrograms 
	 * 		   determined by fitness. If noElites is less than the population 
	 * 		   size then the returned list will contain all CandidatePrograms 
	 * 		   from the population.
	 */
	public static <TYPE> List<CandidateProgram<TYPE>> getElites(List<CandidateProgram<TYPE>> pop, int noElites) {
		List<CandidateProgram<TYPE>> elites = new ArrayList<CandidateProgram<TYPE>>(noElites);
		
		// Sort the population and scoop off the best noElites.
		if (noElites > 0) {
			//Collections.sort(pop, Collections.reverseOrder());
			Collections.sort(pop);
			elites.addAll(pop.subList(pop.size()-noElites,pop.size()));
		}
		
		return elites;
	}
	
}
