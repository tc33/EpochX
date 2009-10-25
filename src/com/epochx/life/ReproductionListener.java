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

import com.epochx.representation.*;

/**
 *
 */
public interface ReproductionListener<TYPE> {
	
	/**
	 * Called after selection of an individual to be reproduced into the next 
	 * generation.
	 * 
	 * @param child the program that was selected to be reproduced.
	 * @return a CandidateProgram that should be used as the reproduced program
	 * and inserted into the next population.
	 */
	public CandidateProgram<TYPE> onReproduction(CandidateProgram<TYPE> child);
	
}
