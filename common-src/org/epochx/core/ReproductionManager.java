/*  
 *  Copyright 2007-2010 Tom Castle & Lawrence Beadle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming software for research
 *
 *  EpochX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EpochX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 *  The latest version is available from: http:/www.epochx.org
 */
package org.epochx.core;

import org.epochx.life.*;
import org.epochx.model.Model;
import org.epochx.op.ProgramSelector;
import org.epochx.representation.CandidateProgram;

public class ReproductionManager {
	
	// The controlling model.
	private Model model;
	
	// Manager of life cycle events.
	private LifeCycleManager lifeCycle;
	
	// The selector to use to choose the program to reproduce.
	private ProgramSelector programSelector;
	
	// The number of times the selected reproduction was rejected.
	private int reversions;
	
	/**
	 * Constructs an instance of GPReproduction which will setup the 
	 * reproduction operation. Since the actual reproduction operation requires 
	 * little more than selection of a program, the actual operation is also 
	 * performed here.
	 * 
	 * @param model the GPModel which defines the ProgramSelector to use to 
	 * 				select the program to be reproduced.
	 */
	public ReproductionManager(Model model) {
		this.model = model;
		
		// Initialise parameters.
		initialise();
		
		lifeCycle = Controller.getLifeCycleManager();
		lifeCycle.addGenerationListener(new GenerationAdapter() {
			@Override
			public void onGenerationStart() {
				// Initialise parameters.
				initialise();
			}
		});
		
		// Initialise parameters.
		initialise();
	}
	
	/*
	 * Initialises GPReproduction, in particular all parameters from the model should
	 * be refreshed incase they've changed since the last call.
	 */
	private void initialise() {
		programSelector = model.getProgramSelector();
	}
	
	/**
	 * Selects a <code>GPCandidateProgram</code> from the population using the
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
	 * @return a GPCandidateProgram selected for reproduction.
	 */
	public CandidateProgram reproduce() {
		Controller.getLifeCycleManager().onReproductionStart();
		
		CandidateProgram parent = null;
		
		reversions = -1;
		
		do {
			// Choose a parent.
			parent = programSelector.getProgram();
			
			// Allow the life cycle listener to confirm or modify.
			parent = lifeCycle.onReproduction(parent);
			reversions++;
		} while(parent == null);
		
		Controller.getLifeCycleManager().onReproductionEnd();
		
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
