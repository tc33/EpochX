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
 * Defines the mutation events which may be raised.
 */
public interface MutationListener {
	
	/**
	 * Event raised before the mutation operation starts.
	 */
	public void onMutationStart();
	
	/**
	 * Event raised after the selection and mutation operation has occurred. 
	 * The child may be modified and returned. This event is revertable by 
	 * returning null which will trigger the discarding of the parent and 
	 * mutant child, the reselection of a new parent, a new mutation attempt 
	 * and this event being raised again. If the mutation should be accepted 
	 * then the child should be returned as it is.
	 * 
	 * @param parent the program that was selected to undergo mutation.
	 * @param child the resultant program from the parent undergoing mutation.
	 * @return a GPCandidateProgram that should be considered the result of a 
	 * mutation operation, or null if the mutation should be reverted.
	 */
	public CandidateProgram onMutation(CandidateProgram parent, CandidateProgram child);
	
	/**
	 * Event raised after the mutation operation has ended and been accepted.
	 */
	public void onMutationEnd();
}
