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

import java.util.*;

import com.epochx.representation.*;

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
public class GPPoolSelection<TYPE> {
	
	// The controlling model.
	private GPModel<TYPE> model;
	
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
	}
	
	/**
	 * 
	 * @param pop
	 * @return
	 */
	public List<CandidateProgram<TYPE>> getPool(List<CandidateProgram<TYPE>> pop) {
		int poolSize = model.getPoolSize();
		
		List<CandidateProgram<TYPE>> pool = null;
		
		reversions = -1;
		do {
			// Perform pool selection.
			pool = model.getPoolSelector().getPool(pop, poolSize);
			
			// Allow life cycle listener to confirm or modify.
			pool = model.getLifeCycleListener().onPoolSelection(pool);
			
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
}
