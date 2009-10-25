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
public interface MutationListener<TYPE> {
	
	//public CandidateProgram onMutationSelection(List<CandidateProgram> pop, CandidateProgram parent);
	
	/**
	 * Called after selection and mutation of an individual program.
	 * 
	 * @param parent the program that was selected to undergo mutation.
	 * @param child the resultant program from the parent undergoing mutation.
	 * @return a CandidateProgram that should be considered the result of a 
	 * mutation operation, or null if the mutation should be reverted.
	 */
	public CandidateProgram<TYPE> onMutation(CandidateProgram<TYPE> parent, CandidateProgram<TYPE> child);
	
	//public CandidateProgram onMutationAndSelection(CandidateProgram parent, CandidateProgram child);
	
}
