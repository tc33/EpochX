package org.epochx.core;

import org.epochx.life.*;
import org.epochx.op.ProgramSelector;
import org.epochx.representation.CandidateProgram;

public class ReproductionManager implements GenerationListener {
	
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
		
		lifeCycle = Controller.getLifeCycleManager();
		lifeCycle.addGenerationListener(this);
		
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
		CandidateProgram parent = null;
		
		reversions = -1;
		
		do {
			// Choose a parent.
			parent = programSelector.getProgram();
			
			// Allow the life cycle listener to confirm or modify.
			parent = lifeCycle.onReproduction(parent);
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
