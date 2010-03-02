package org.epochx.core;

import org.epochx.life.*;
import org.epochx.model.*;
import org.epochx.op.*;
import org.epochx.representation.*;

public class MutationManager implements GenerationListener {
	
	// The controlling model.
	private Model model;
	
	// Manager of life cycle events.
	private LifeCycleManager lifeCycle;
	
	// The selector for choosing the individual to mutate.
	private ProgramSelector programSelector;
	
	// The mutation operator that will perform the actual operation.
	private Mutation mutator;
	
	// The number of times the mutation was rejected by the model.
	private int reversions;
	
	/**
	 * Constructs an instance of GPMutation which will setup the mutation 
	 * operation. Note that the actual mutation operation will be performed by 
	 * the subclass of <code>Mutation</code> returned by the models 
	 * <code>getMutation()</code> method.
	 * 
	 * @param model the GPModel which defines the Mutation operator and 
	 * 				ProgramSelector to use to perform one act of mutation on 
	 * 				an individual in the population.
	 * @see Mutation
	 */
	public MutationManager(Model model) {
		this.model = model;

		// Register interest in generation events so we can reset.
		lifeCycle = Controller.getLifeCycleManager();
		lifeCycle.addGenerationListener(this);

		initialise();
	}
	
	/*
	 * Initialises GPMutation, in particular all parameters from the model should
	 * be refreshed incase they've changed since the last call.
	 */
	private void initialise() {
		programSelector = model.getProgramSelector();
		mutator = model.getMutation();
	}
	
	/**
	 * Selects a <code>GPCandidateProgram</code> from the population using the
	 * <code>ProgramSelector</code> returned by a call to 
	 * <code>getProgramSelector()</code> on the model given at construction and 
	 * submits it to the <code>Mutation</code> operator which is obtained by 
	 * calling <code>getMutation()</code> on the model. 
	 * 
	 * <p>After a mutation is made, the controlling model is requested to 
	 * confirm the mutation by a call to <code>acceptMutation()</code>. This 
	 * gives the model total control over whether a mutation is allowed to 
	 * proceed. If <code>acceptMutation()</code> returns <code>false</code> 
	 * then the new <code>GPCandidateProgram</code> is discarded and a new 
	 * individual is selected and attempted for mutation. The number of times 
	 * the mutation was reverted before being accepted is available through 
	 * a call to <code>getRevertedCount()</code>.
	 * 
	 * <p>Even after a mutation has been accepted by the model, it may still 
	 * be prevented from proceeding if the program depth of the new program 
	 * exceeds the max depth that the model defines. In the case that it does 
	 * exceed the limit then the original program is returned as the result. 
	 * This does not count towards the number of reversions.
	 * 
	 * @return a GPCandidateProgram generated through mutation by the Mutation
	 *         operator in use, or if the max depth limit was exceeded then 
	 *         the original selected program before mutation will be returned.
	 */
	public CandidateProgram mutate() {
		long crossoverStartTime = System.nanoTime();
		
		CandidateProgram parent = null;
		CandidateProgram child = null;

		reversions = -1;
		do {
			// Attempt mutation.
			parent = programSelector.getProgram();
			
			// Create child as a clone of parent.
			child = (CandidateProgram) parent.clone();
			
			// Mutate the child.
			child = mutator.mutate(child);

			// Allow the life cycle listener to confirm or modify.
			child = lifeCycle.onMutation(parent, child);
			reversions++;
		} while(child == null);
		
		//child = (CandidateProgram) parent.clone();
		
		long runtime = System.nanoTime() - crossoverStartTime;
		//mutationStats.addMutation(parent, child, runtime, reversions);
		
		return child;
	}
	
	/**
	 * <p>After a mutation is made, the controlling model is requested to 
	 * confirm the mutation by a call to <code>acceptMutation()</code>. This 
	 * gives the model total control over whether a mutation is allowed to 
	 * proceed. If <code>acceptMutation()</code> returns <code>false</code> 
	 * then the mutated program is discarded and a new 
	 * <code>GPCandidateProgram</code> selected to undergo mutation. The number 
	 * of times the mutation was reverted before being accepted is available 
	 * through a call to this method.
	 * 
	 * @return the number of times the mutation was rejected by the model.
	 */
	public int getRevertedCount() {
		return reversions;
	}
	
	/**
	 * Called after each generation. For each generation we should reset all 
	 * parameters taken from the model incase they've changed. The generation
	 * event is then CONFIRMed.
	 */
	@Override
	public void onGenerationStart() {
		// Reset GPMutation.
		initialise();
	}
}
