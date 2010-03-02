package org.epochx.core;

import java.util.*;

import org.epochx.life.LifeCycleManager;
import org.epochx.model.Model;
import org.epochx.representation.*;
import org.epochx.tools.random.RandomNumberGenerator;

import static org.epochx.stats.StatField.*;

public class GenerationManager {
	
	// The controlling model.
	private Model model;
	
	// Manager of life cycle events.
	private LifeCycleManager lifeCycle;
	
	// Core components.
	private ElitismManager elitism;
	private PoolSelectionManager poolSelection;
	private CrossoverManager crossover;
	private MutationManager mutation;
	private ReproductionManager reproduction;
	
	private RandomNumberGenerator rng;

	// Operator probabilities.
	private double mutationProbability;
	private double crossoverProbability;
	
	/**
	 * Constructs a generation component for performing each generation.
	 * 
	 * @param model 
	 */
	public GenerationManager(Model model) {
		this.model = model;
		
		// Get a reference to the life cycle manager.
		lifeCycle = Controller.getLifeCycleManager();
		
		// Setup core components.
		elitism = new ElitismManager(model);
		poolSelection = new PoolSelectionManager(model);
		crossover = new CrossoverManager(model);
		mutation = new MutationManager(model);
		reproduction = new ReproductionManager(model);
	}
	
	/*
	 * Reset the component with parameters from the model which might have 
	 * changed since the last generation.
	 */
	private void reset() {
		rng = model.getRNG();
		mutationProbability = model.getMutationProbability();
		crossoverProbability = model.getCrossoverProbability();
	}
	
	/**
	 * Performs one generation of a GP run. The method receives the previous 
	 * population and then performs one generation and returns the resultant
	 * population. 
	 * 
	 * <p>A generation consists of the following sequence of events:
	 * 
	 * <ol>
	 *   <li>Select the elites and put them into the next population.</li>
	 *   <li>Select a breeding pool of programs.</li>
	 *   <li>Randomly choose an operator based upon probablities from the model:
	 *   	<ul>
	 *   		<li>Crossover - pass control to crossover component.</li>
	 *   		<li>Mutation - pass control to mutation component.</li>
	 *   		<li>Reproduction - pass control to reproduction component.</li>
	 *   	</ul>
	 *   </li>
	 *   <li>Insert the result of the operator into the next population.</li>
	 *   <li>Start back at 3. until the next population is full.</li>
	 *   <li>Return the new population.</li>
	 * </ol>
	 * 
	 * <p>The necessary events trigger life cycle events.
	 * 
	 * @param previousPop
	 * @return
	 */
	public List<CandidateProgram> generation(int generationNumber, List<CandidateProgram> previousPop) {
		reset();
		
		Controller.getStatsManager().addGenerationData(GEN_NUMBER, generationNumber);
		
		// Tell life cycle manager we're starting a new generation.
		lifeCycle.onGenerationStart();
		
		// Create next population to fill.
		int popSize = model.getPopulationSize();
		List<CandidateProgram> pop = new ArrayList<CandidateProgram>(popSize);
		
		// Perform elitism.
		pop.addAll(elitism.getElites(previousPop));
		
		// Construct a breeding pool.
		List<CandidateProgram> pool = poolSelection.getPool(previousPop);
		
		// Tell the parent selector what selection pool to use.
		model.getProgramSelector().setSelectionPool(pool);
		
		// Fill the population by performing genetic operations.
		while(pop.size() < popSize) {
			// Pick a genetic operator using Pr, Pc and Pm.
			double random = rng.nextDouble();
			
			if (random < crossoverProbability) {
				// Perform crossover.
				CandidateProgram[] children = crossover.crossover();
				for (CandidateProgram c: children) {
					if (pop.size() < popSize)
						pop.add(c);
				}
			} else if (random < crossoverProbability+mutationProbability) {
				// Perform mutation.
				pop.add(mutation.mutate());
			} else {
				// Perform reproduction.
				pop.add(reproduction.reproduce());
			}
		}
		
		Controller.getStatsManager().addGenerationData(GEN_POPULATION, pop);

		return pop;
	}
	
}
