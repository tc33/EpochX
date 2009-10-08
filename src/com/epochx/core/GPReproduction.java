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

import com.epochx.representation.*;

/**
 * This class controls the reproduction operation.
 * 
 * @see ProgramSelector
 */
public class GPReproduction<TYPE> {

	// The controlling model.
	private GPModel<TYPE> model;
	
	// The number of times the selected reproduction was rejected.
	private int reversions;
	
	/**
	 * Constructs an instance of GEReproduction which will setup the 
	 * reproduction operation. Since the actual reproduction operation requires 
	 * little more than selection of a program, the actual operation is also 
	 * performed here.
	 * 
	 * @param model the GEModel which defines the ProgramSelector to use to 
	 * 				select the program to be reproduced.
	 */
	public GPReproduction(GPModel<TYPE> model) {
		this.model = model;
	}
	
	/**
	 * Selects a <code>CandidateProgram</code> from the population using the
	 * <code>ProgramSelector</code> returned by a call to 
	 * <code>getProgramSelector()</code> on the model given at construction. 
	 * 
	 * <p>After a program is selected for reproduction, the model's life cycle 
	 * listener is requested to confirm or modify the selection by a call to 
	 * <code>onReproduction()</code>. This gives over total control to decide 
	 * whether a reproduction is allowed to proceed, and gives an opportunity 
	 * for manipulation of the reproduced child program. If 
	 * <code>onReproduction()</code> returns <code>null</code> then the child 
	 * is discarded and a new parent is selected and reproduced. The number of 
	 * times the reproduction was reverted before being accepted is available 
	 * through a call to <code>getReversions()</code>.
	 * 
	 * @return a CandidateProgram selected for reproduction.
	 */
	public CandidateProgram<TYPE> reproduce() {
		CandidateProgram<TYPE> parent = null;
		
		reversions = -1;
		
		do {
			// Choose a parent.
			parent = model.getProgramSelector().getProgram();
			
			// Allow the life cycle listener to confirm or modify.
			parent = model.getLifeCycleListener().onReproduction(parent);
			reversions++;
		} while(parent == null);
		
		return parent;
	}
	
	/**
	 * Number of times the reproduction was rejected and re-attempted.
	 * 
	 * <p>After a program is selected for reproduction, the model's life cycle 
	 * listener is requested to confirm or modify the selection by a call to 
	 * <code>onReproduction()</code>. This gives over total control to decide 
	 * whether a reproduction is allowed to proceed, and gives an opportunity 
	 * for manipulation of the reproduced child program. If 
	 * <code>onReproduction()</code> returns <code>null</code> then the child 
	 * is discarded and a new parent is selected and reproduced. The number of 
	 * times the reproduction was reverted before being accepted is available 
	 * through a call to this method.
	 * 
	 * @return the number of times the reproduction was rejected by the model.
	 */
	public int getReversions() {
		return reversions;
	}
}