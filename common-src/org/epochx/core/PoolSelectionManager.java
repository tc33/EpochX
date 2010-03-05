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

import java.util.List;

import org.epochx.life.*;
import org.epochx.model.Model;
import org.epochx.op.PoolSelector;
import org.epochx.representation.CandidateProgram;

public class PoolSelectionManager {
	
	// The controlling model.
	private Model model;
	
	// The pool selector to use to generate the breeding pool.
	private PoolSelector poolSelector;
	
	// The number of programs that make up a breeding pool.
	private int poolSize;
	
	// The number of times the pool selection was rejected.
	//TODO Perhaps should use a long here, just incase.
	private int reversions;
	
	/**
	 * Constructs an instance of GPPoolSelection which will setup the breeding 
	 * pool selection operation. Note that the actual construction of the pool  
	 * will be performed by the subclass of <code>PoolSelector</code> returned 
	 * by the models <code>getPoolSelector()</code> method.
	 * 
	 * @param model the GPModel which defines the PoolSelector operator and 
	 * 				life cycle listener.
	 * @see PoolSelector
	 */
	public PoolSelectionManager(Model model) {
		this.model = model;
		
		// Initialise parameters.
		initialise();
		
		Controller.getLifeCycleManager().addGenerationListener(new GenerationAdapter() {
			@Override
			public void onGenerationStart() {
				initialise();
			}
		});
	}
	
	/*
	 * Initialises GPPoolSelection, in particular all parameters from the model should
	 * be refreshed incase they've changed since the last call.
	 */
	private void initialise() {
		poolSize = model.getPoolSize();
		poolSelector = model.getPoolSelector();
	}
	
	/**
	 * 
	 * @param pop
	 * @return
	 */
	public List<CandidateProgram> getPool(List<CandidateProgram> pop) {	
		Controller.getLifeCycleManager().onPoolSelectionStart();
		
		List<CandidateProgram> pool = null;
		
		reversions = -1;
		do {
			// Perform pool selection.
			pool = poolSelector.getPool(pop, poolSize);
			
			// Allow life cycle listener to confirm or modify.
			pool = Controller.getLifeCycleManager().onPoolSelection(pool);
			
			// Increment reversions - starts at -1 to cover first increment.
			reversions++;
		} while(pool == null);
		
		Controller.getLifeCycleManager().onPoolSelectionEnd();
		
		return pool;
	}
	
	/**
	 * Number of times the selected pool was rejected and re-selected.
	 * 
	 * <p>After a breeding pool is selected, the controlling model's life cycle 
	 * listener is requested to confirm the pool by a call to 
	 * <code>onPoolSelection()</code>. This gives the model total control over 
	 * the breeding pool allowing manipulation of it's contents before 
	 * proceeding. If <code>onPoolSelection()</code> returns <code>null</code> 
	 * then the pool of programs is discarded and a new pool is formed. The 
	 * number of times the pool is reverted before being accepted is available 
	 * through a call to this method.
	 * 
	 * @return the number of times the selected breeding pool was rejected by 
	 * the model.
	 */
	public int getReversions() {
		return reversions;
	}
}
