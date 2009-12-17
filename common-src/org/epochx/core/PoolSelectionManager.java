package org.epochx.core;

import java.util.List;

import org.epochx.life.*;
import org.epochx.op.PoolSelector;
import org.epochx.representation.CandidateProgram;

public class PoolSelectionManager implements GenerationListener {
	
	// The controlling model.
	private Model model;
	
	// Manager of life cycle events.
	private LifeCycleManager lifeCycle;
	
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
		
		lifeCycle = Controller.getLifeCycleManager();
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
	public List<CandidateProgram> getPool(List<CandidateProgram> pop) {		
		List<CandidateProgram> pool = null;
		
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
