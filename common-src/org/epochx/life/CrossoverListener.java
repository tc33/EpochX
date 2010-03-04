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

import org.epochx.representation.*;

public interface CrossoverListener {

	public void onCrossoverStart();
	
	public void onCrossoverEnd();
	
	/**
	 * Called after selection and crossover of 2 individuals.
	 * 
	 * @param parents the programs that were selected to undergo crossover.
	 * @param children the programs that were generated as a result of the 
	 * crossover operation.
	 * @return an array of CandidatePrograms to be used as the children of the 
	 * crossover operation, or null if the crossover should be reverted.
	 */
	public CandidateProgram[] onCrossover(CandidateProgram[] parents, CandidateProgram[] children);
	
}
