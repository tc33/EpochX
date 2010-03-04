package org.epochx.core;

import static org.epochx.stats.StatField.*;

import org.epochx.life.*;
import org.epochx.model.*;
import org.epochx.op.*;
import org.epochx.representation.*;


public class CrossoverManager {

	// The controlling model.
	private Model model;
	
	// Manager of life cycle events.
	private LifeCycleManager lifeCycle;
	
	// The selector for choosing parents.
	private ProgramSelector programSelector;
	
	// The crossover operator that will perform the actual operation.
	private Crossover crossover;
	
	// The number of times the crossover was rejected by the model.
	private int reversions;
	
	/**
	 * Constructs an instance of GPCrossover which will setup the crossover 
	 * operation. Note that the actual crossover operation will be performed by 
	 * the subclass of <code>Crossover</code> returned by the models 
	 * getCrossover() method.
	 * 
	 * @param model the GPModel which defines the Crossover operator and 
	 * 				ProgramSelector to use to perform one act of crossover on 
	 * 				a population.
	 * @see Crossover
	 */
	public CrossoverManager(Model model) {
		this.model = model;
		
		// Initialise parameters.
		initialise();
		
		// Get a reference to the life cycle manager for convenience.
		lifeCycle = Controller.getLifeCycleManager();
		
		lifeCycle.addGenerationListener(new GenerationAdapter() {
			@Override
			public void onGenerationStart() {
				initialise();
			}
		});
	}
	
	/*
	 * Initialises GPCrossover, in particular all parameters from the model should
	 * be refreshed incase they've changed since the last call.
	 */
	private void initialise() {
		programSelector = model.getProgramSelector();
		crossover = model.getCrossover();
	}

	/**
	 * Selects two parents by calling <code>getProgramSelector()</code> on the 
	 * instance of <code>GPModel</code> given at construction and submits them 
	 * to the <code>Crossover</code> operator which is obtained by calling 
	 * <code>getCrossover()</code> on the model. 
	 * 
	 * <p>After a crossover is made, the controlling model is requested to 
	 * confirm the crossover by a call to <code>acceptCrossover()</code>. This 
	 * gives the model total control over whether a crossover is allowed to 
	 * proceed. If <code>acceptCrossover()</code> returns <code>false</code> 
	 * then the children are discarded and two new parents are selected and 
	 * attempted for crossover. The number of times the crossover was reverted 
	 * before being accepted is available through a call to 
	 * <code>getRevertedCount()</code>.
	 * 
	 * <p>Even after a crossover has been accepted by the model, it may still 
	 * be prevented from proceeding if the program depth of either of the 
	 * children exceeds the max depth that the model defines. In the case that 
	 * the children do exceed the limit then the parents are returned as the 
	 * result. This does not count towards the number of reversions.
	 * 
	 * @return an array of CandidatePrograms generated through crossover. This 
	 * 		   is typically 2 child programs, but could in theory be any number 
	 * 	 	   as returned by the Crossover operator in use.
	 */
	public CandidateProgram[] crossover() {
		long crossoverStartTime = System.nanoTime();

		CandidateProgram parent1;
		CandidateProgram parent2;

		CandidateProgram clone1;
		CandidateProgram clone2;

		CandidateProgram[] parents = null;
		CandidateProgram[] children = null;
		
		reversions = -1;
		do {
			// Select the parents for crossover.
			parent1 = programSelector.getProgram();
			parent2 = programSelector.getProgram();

			clone1 = (CandidateProgram) parent1.clone();
			clone2 = (CandidateProgram) parent2.clone();
			parents = new CandidateProgram[]{parent1, parent2};
			
			// Attempt crossover.
			children = crossover.crossover(clone1, clone2);
			
			// Ask life cycle listener to confirm the crossover.
			children = lifeCycle.onCrossover(parents, children);
			reversions++;
		} while(children == null);
		
		//TODO This is actually horrible - it's so unflexible, theres no way 
		//for a user to control whether/how this happens. Also, it doesn't even
		//seem like the right thing to be doing. Why use the parents?!
		/*int replacement = 0;
		for (int i=0; i<children.length; i++) {
			if (replacement >= parents.length) {
				replacement = 0;
			}
			children[i] = (CandidateProgram) parents[replacement].clone();
			replacement++;
		}*/
		
		long runtime = System.nanoTime() - crossoverStartTime;

		Controller.getStatsManager().addMutationData(CROSSOVER_PARENTS, parents);
		Controller.getStatsManager().addMutationData(CROSSOVER_CHILDREN, children);
		Controller.getStatsManager().addMutationData(CROSSOVER_REVERTED, reversions);
		Controller.getStatsManager().addMutationData(CROSSOVER_TIME, runtime);
		
		return children;
	}
	
	/**
	 * <p>After a crossover is made, the controlling model is requested to 
	 * confirm the crossover by a call to <code>acceptCrossover()</code>. This 
	 * gives the model total control over whether a crossover is allowed to 
	 * proceed. If <code>acceptCrossover()</code> returns <code>false</code> 
	 * then the children are discarded and two new parents are selected and 
	 * attempted for crossover. The number of times the crossover was reverted 
	 * before being accepted is available through a call to this method.
	 * 
	 * @return the number of times the crossover was rejected by the model.
	 */
	public int getRevertedCount() {
		return reversions;
	}
	
}
