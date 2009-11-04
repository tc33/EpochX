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
package org.epochx.core;

import java.util.*;

import org.epochx.life.*;
import org.epochx.op.selection.PoolSelector;
import org.epochx.representation.*;


/**
 * This class is responsible for controlling the selection of a breeding pool. 
 * Note that as with the other core classes, no actual (selection) operation is 
 * performed by this class. Rather, it provides the infrastructure around the 
 * operation, and calls the implementation of <code>PoolSelector</code> which 
 * will perform the actual operation.
 * 
 * @see PoolSelector
 * @see TournamentSelector
 */
public class GPPoolSelection<TYPE> implements GenerationListener {
	
	// The controlling model.
	private GPModel<TYPE> model;
	
	// Manager of life cycle events.
	private LifeCycleManager<TYPE> lifeCycle;
	
	// The pool selector to use to generate the breeding pool.
	private PoolSelector<TYPE> poolSelector;
	
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
	public GPPoolSelection(GPModel<TYPE> model) {
		this.model = model;
		
		lifeCycle = GPController.getLifeCycleManager();
		lifeCycle.addGenerationListener(this);
		
		initialise();
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
	public List<CandidateProgram<TYPE>> getPool(List<CandidateProgram<TYPE>> pop) {		
		List<CandidateProgram<TYPE>> pool = null;
		
		reversions = -1;
		do {
			// Perform pool selection.
			pool = poolSelector.getPool(pop, poolSize);
			
			// Allow life cycle listener to confirm or modify.
			pool = lifeCycle.onPoolSelection(pool);
			
			// Increment reversions - starts at -1 to cover first increment.
			reversions++;
		} while(pool == null);
		
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
	
	/**
	 * Called after each generation. For each generation we should reset all 
	 * parameters taken from the model incase they've changed. The generation
	 * event is then CONFIRMed.
	 */
	@Override
	public void onGenerationStart() {
		// Reset.
		initialise();
	}
}
