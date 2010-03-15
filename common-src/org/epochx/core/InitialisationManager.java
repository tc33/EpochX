/* 
 * Copyright 2007-2010 Tom Castle & Lawrence Beadle
 * Licensed under GNU General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.core;

import static org.epochx.stats.StatField.*;

import java.util.List;

import org.epochx.life.GenerationAdapter;
import org.epochx.life.LifeCycleManager;
import org.epochx.model.Model;
import org.epochx.op.Initialiser;
import org.epochx.representation.CandidateProgram;
import org.epochx.stats.StatsManager;

/**
 * This component manages the initialisation step of an evolutionary run and
 * raises initialisation events. The actual act of program generation and 
 * population initialisation is performed by the implementation of Initialiser 
 * that is obtained from the {@link Model} provided to the constructor.
 * 
 * <p>
 * With regards to life cycle, initialisation as performed by this class is  
 * considered to be generation zero. As such both a generation event and an 
 * initialisation event will be generated, for both the start and end. 
 * Immediately after an initial population is generated, the life cycle 
 * listeners are given an opportunity to revert or modify it. This occurs 
 * through a call to each listener's <code>onInitialisation</code> method. The 
 * first listener is passed the new population, and must return either a list 
 * of programs to take the place as the new population or null to request 
 * reversion. The result of this first listener is passed into the second 
 * listener, and so on with each listener in turn. The value returned from the 
 * final listener in this pipeline will either be the population that will 
 * become the initial population or null, and initialisation will be reverted. 
 * In the case of reversion the newly initialised population will be discarded 
 * and a new one generated. A reversion counter will be incremented, the value 
 * of which is retrievable from the <code>StatsManager</code>.
 * 
 * <p>
 * Use of the initialisation operation will generate the following events:
 * 
 * <table border="1">
 *     <tr>
 *         <th>Event</th>
 *         <th>Revert</th>
 *         <th>Modify</th>
 *         <th>Raised when?</th>
 *     </tr>
 *     <tr>
 *         <td>onInitialisationStart</td>
 *         <td>no</td>
 *         <td>no</td>
 *         <td>Before the initialisation operation is carried out.
 *         </td>
 *     </tr>
 *     <tr>
 *         <td>onInitialisation</td>
 *         <td><strong>yes</strong></td>
 *         <td><strong>yes</strong></td>
 *         <td>Immediately after a population has been generated, giving
 *         the listener the opportunity to request a revert which will cause 
 *         the re-generation of an initial population and cause this event to 
 *         be raised again.
 *         </td>
 *     </tr>
 *     <tr>
 *         <td>onInitialisationEnd</td>
 *         <td>no</td>
 *         <td>no</td>
 *         <td>After the initialisation operation has been completed.
 *         </td>
 *     </tr>
 * </table>
 * 
 * @see Initialiser
 */
public class InitialisationManager {
	
	// Initialiser operator.
	private Initialiser initialiser;
	
	// The number of times the initialisation was rejected.
	private int reversions;
	
	/**
	 * Constructs an instance of <code>InitialisationManager</code> which will 
	 * setup the initialisation operation.
	 * 
	 * @param model the model which defines the Initialiser operator and any 
	 * 				other control parameters.
	 * @see Initialiser
	 */
	public InitialisationManager() {
		// Initialise parameters on each generation.
		LifeCycleManager.getLifeCycleManager().addGenerationListener(new GenerationAdapter() {
			@Override
			public void onGenerationStart() {
				updateModel();
			}
		});
	}
	
	/*
	 * 
	 */
	private void updateModel() {
		initialiser = Controller.getModel().getInitialiser();
	}
	
	/**
	 * Initialises a new population of <code>CandidatePrograms</code> by 
	 * calling <code>getInitialPopulation()</code> on the Initialiser retrieved 
	 * from the model given at construction. 
	 * 
	 * <p>
	 * Note that the actual initialisation operation will be performed by the 
	 * implementation of <code>Initialiser</code> returned by the given model's
	 * <code>getInitialiser()</code> method.
	 * 
	 * @return a List of CandidatePrograms generated by the model's initialiser.
	 */
	public List<CandidateProgram> initialise() {
		// Trigger life cycle events for both generation and initialisation.
		LifeCycleManager.getLifeCycleManager().onGenerationStart();
		LifeCycleManager.getLifeCycleManager().onInitialisationStart();
		
		// Record the start time.
		final long startTime = System.nanoTime();
		
		// Record the generation number as zero in the stats data.
		StatsManager.getStatsManager().addGenerationData(GEN_NUMBER, 0);

		// Reset the number of reversions.
		reversions = 0;
		
		List<CandidateProgram> pop = null;
		do {
			// Perform initialisation.
			pop = (List<CandidateProgram>) initialiser.getInitialPopulation();
			
			// Allow life cycle manager to confirm or modify. (init has final say).
			pop = LifeCycleManager.getLifeCycleManager().onGeneration(pop);
			pop = LifeCycleManager.getLifeCycleManager().onInitialisation(pop);
			
			// If reverted then increment reversion count.
			if (pop == null) {
				reversions++;
			}
		} while(pop == null);
		
		// Store the stats data from the initialisation.
		StatsManager.getStatsManager().addGenerationData(INIT_REVERSIONS, reversions);
		StatsManager.getStatsManager().addGenerationData(GEN_POPULATION, pop);
		StatsManager.getStatsManager().addGenerationData(GEN_TIME, (System.nanoTime() - startTime));
		
		// Trigger life cycle events for end of initialisation and generation 0.
		LifeCycleManager.getLifeCycleManager().onInitialisationEnd();
		LifeCycleManager.getLifeCycleManager().onGenerationEnd();
		
		return pop;
	}
	
}
