package org.epochx.core;

import java.util.List;

import org.epochx.life.LifeCycleManager;
import org.epochx.model.Model;
import org.epochx.representation.CandidateProgram;

public class InitialisationManager {
	
	// The controlling model.
	private Model model;
	
	// Manager of life cycle events.
	private LifeCycleManager lifeCycle;
	
	// The number of times the initialisation was rejected.
	private int reversions;
	
	/**
	 * Constructs an instance of GPInitialisation which will setup the 
	 * initialisation operation. Note that the actual initialisation operation 
	 * will be performed by the subclass of <code>GPInitialiser</code> returned 
	 * by the models <code>getInitialiser()</code> method.
	 * 
	 * @param model the GPModel which defines the GPInitialiser operator and 
	 * 				life cycle listener.
	 * @see GPInitialiser
	 */
	public InitialisationManager(Model model) {
		this.model = model;
		
		lifeCycle = Controller.getLifeCycleManager();
	}
	
	/**
	 * Initialises a new population of <code>CandidatePrograms</code> by 
	 * calling <code>getInitialPopulation()</code> on the initialiser provided 
	 * by the model.
	 * 
	 * <p>After an initial population is constructed, the model's life cycle 
	 * listener is given an opportunity to confirm or modify it before 
	 * proceeding. The listener's <code>onInitialisation()</code> method is 
	 * called, passing it the newly formed population. If this method returns
	 * null then the initialisation operation will be repeated, otherwise the 
	 * population returned by the life cycle listener will be used as the 
	 * initial population. The number of times the initial population is 
	 * rejected and thus regenerated is available with a call to 
	 * <code>getReversions()</code>.
	 * 
	 * @return a List of CandidatePrograms generated by the model's initialiser.
	 */
	public List<CandidateProgram> initialise() {
		List<CandidateProgram> pop = null;
		
		reversions = -1;
		do {
			// Perform initialisation.
			pop = (List<CandidateProgram>) model.getInitialiser().getInitialPopulation();
			
			// Allow life cycle manager to confirm or modify.
			pop = lifeCycle.onInitialisation(pop);
			
			// Increment reversions - starts at -1 to cover first increment.
			reversions++;
		} while(pop == null);
		
		return pop;
	}
	
	/**
	 * Number of times the initial population was rejected and regenerated.
	 * 
	 * <p>After an initial population is constructed, the model's life cycle 
	 * listener is given an opportunity to confirm or modify it before 
	 * proceeding. The listener's <code>onInitialisation()</code> method is 
	 * called, passing it the newly formed population. If this method returns
	 * null then the initialisation operation will be repeated, otherwise the 
	 * population returned by the life cycle listener will be used as the 
	 * initial population. The number of times the initial population is 
	 * rejected and thus regenerated is available with a call to this method.
	 * 
	 * @return the number of times the intialisation was rejected by the life 
	 * cycle listener.
	 */
	public int getReversions() {
		return reversions;
	}
	
}
