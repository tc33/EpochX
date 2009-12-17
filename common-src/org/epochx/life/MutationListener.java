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

/**
 *
 */
public interface MutationListener {
	
	//public GPCandidateProgram onMutationSelection(List<GPCandidateProgram> pop, GPCandidateProgram parent);
	
	/**
	 * Called after selection and mutation of an individual program.
	 * 
	 * @param parent the program that was selected to undergo mutation.
	 * @param child the resultant program from the parent undergoing mutation.
	 * @return a GPCandidateProgram that should be considered the result of a 
	 * mutation operation, or null if the mutation should be reverted.
	 */
	public CandidateProgram onMutation(CandidateProgram parent, CandidateProgram child);
	
	//public GPCandidateProgram onMutationAndSelection(GPCandidateProgram parent, GPCandidateProgram child);
	
}
