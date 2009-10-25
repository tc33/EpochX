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

import java.util.*;

import com.epochx.representation.*;

/**
 *
 */
public interface ElitismListener<TYPE> {

	/**
	 * Called after selection of elites. If the number of elites to use is set 
	 * by the model to <=0, then this method will still be called at the 
	 * appropriate time, but with an empty list.
	 * 
	 * @param elites the selection of chosen elites.
	 * @return a List of CandidatePrograms to use as the set of elites. Note 
	 * that it is not appropriate to return a value of null and this will cause 
	 * undefined behaviour.
	 */
	public List<CandidateProgram<TYPE>> onElitism(List<CandidateProgram<TYPE>> elites);
	
}
