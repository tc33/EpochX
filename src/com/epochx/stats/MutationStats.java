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
package com.epochx.stats;

import java.util.*;

import com.epochx.core.representation.*;

/**
 * 
 */
public class MutationStats<TYPE> {

	// The objects listening for mutation stats.
	public List<MutationStatListener> listeners;
	
	public MutationStats() {
		listeners = new ArrayList<MutationStatListener>();
	}
	
	public void addMutationStatListener(MutationStatListener listener) {
		listeners.add(listener);
	}
	
	public void removeMutationStatListener(MutationStatListener listener) {
		listeners.remove(listener);
	}
	
	public void addMutation(CandidateProgram<TYPE> parent, 
							CandidateProgram<TYPE> child, 
							long runtime) {
		
	}
	
	public enum MutationStatField {
		PROGRAM_BEFORE,
		PROGRAM_AFTER,
		RUN_TIME
	}
	
}
