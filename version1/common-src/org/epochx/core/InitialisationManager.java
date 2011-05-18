/*
 * Copyright 2007-2011 Tom Castle & Lawrence Beadle
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http://www.epochx.org
 */
package org.epochx.core;

import static org.epochx.stats.StatField.*;

import java.util.List;

import org.epochx.life.*;
import org.epochx.op.Initialiser;
import org.epochx.representation.CandidateProgram;
import org.epochx.stats.Stats;

/**
 * This component manages the initialisation step of an evolutionary run and
 * fires initialisation events. The actual act of program construction and
 * population initialisation is performed by the implementation of Initialiser
 * that is obtained from the {@link Model} provided to the constructor.
 * 
 * <p>
 * With regards to life cycle, initialisation as performed by this class is
 * considered to be generation zero. As such, both a generation event and an
 * initialisation event will be generated, for both the start and end.
 * Immediately after an initial population is generated, the life cycle
 * listeners are given an opportunity to revert or modify it. This occurs
 * through a call to each listener's <code>onInitialisation</code> method. The
 * first listener is passed the new population, and must return either a list of
 * programs to take the place as the new population or null to request
 * reversion. The result of this first listener is passed into the second
 * listener, and so on with each listener in turn. The value returned from the
 * final listener in this pipeline will either be the population that will
 * become the initial population or null, and initialisation will be reverted.
 * In the case of reversion the newly initialised population will be discarded
 * and a new one generated. A reversion counter will be incremented, the value
 * of which is retrievable from the <code>Stats</code>.
 * 
 * <p>
 * Use of the initialisation operation will generate the following events:
 * 
 * <table border="1">
 * <tr>
 * <th>Event</th>
 * <th>Revert</th>
 * <th>Modify</th>
 * <th>Raised when?</th>
 * </tr>
 * <tr>
 * <td>onConfigure</td>
 * <td>no</td>
 * <td>no</td>
 * <td>Immediately before the onGenerationStart event is fired.</td>
 * </tr>
 * <tr>
 * <td>onGenerationStart</td>
 * <td>no</td>
 * <td>no</td>
 * <td>Immediately before the onInitialisationStart event is fired.</td>
 * </tr>
 * <tr>
 * <td>onInitialisationStart</td>
 * <td>no</td>
 * <td>no</td>
 * <td>Before the initialisation operation is carried out.</td>
 * </tr>
 * <tr>
 * <td>onGeneration</td>
 * <td><strong>yes</strong></td>
 * <td><strong>yes</strong></td>
 * <td>Immediately after a population has been generated, giving the listener
 * the opportunity to request a revert by returning null. If null is returned
 * then the following onInitialisation event will still be fired.</td>
 * </tr>
 * <tr>
 * <td>onInitialisation</td>
 * <td><strong>yes</strong></td>
 * <td><strong>yes</strong></td>
 * <td>Immediately after a population has been generated, giving the listener
 * the opportunity to request a revert which will cause the re-generation of an
 * initial population and cause this event to be raised again. The population
 * that is passed will be the result of the onGeneration event so may be null.</td>
 * </tr>
 * <tr>
 * <td>onInitialisationEnd</td>
 * <td>no</td>
 * <td>no</td>
 * <td>After the initialisation operation has been completed.</td>
 * </tr>
 * <tr>
 * <td>onGenerationEnd</td>
 * <td>no</td>
 * <td>no</td>
 * <td>Immediately after the initialisation end event is fired.</td>
 * </tr>
 * </table>
 * 
 * @see Initialiser
 */
public class InitialisationManager implements ConfigListener {

	// The controlling model.
	private final Evolver evolver;
	
	private Stats stats;

	// The initialisation operator.
	private Initialiser initialiser;

	// The number of times the initialisation was rejected.
	private int reversions;

	/**
	 * Constructs an instance of <code>InitialisationManager</code> which will
	 * setup the initialisation operation.
	 * 
	 * @param model the model which defines the Initialiser operator and any
	 *        other control parameters.
	 * @see Initialiser
	 */
	public InitialisationManager(final Evolver evolver) {
		this.evolver = evolver;

		// Configure parameters from the model.
		evolver.getLife().addConfigListener(this, false);
	}

	/*
	 * Configure component with parameters from the model.
	 */
	@Override
	public void configure(Model model) {
		stats = evolver.getStats(model);
		initialiser = model.getInitialiser();
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
		if (initialiser == null) {
			throw new IllegalStateException("no initialiser set");
		}

		evolver.getLife().fireGenerationStartEvent();
		evolver.getLife().fireInitialisationStartEvent();

		// Record the start time.
		final long startTime = System.nanoTime();

		// Record the generation number as zero in the stats data.
		stats.addData(GEN_NUMBER, 0);

		// Reset the number of reversions.
		reversions = 0;

		List<CandidateProgram> pop = null;
		do {
			// Perform initialisation.
			pop = initialiser.getInitialPopulation();

			// Allow life cycle manager to confirm or modify. (init has final
			// say).
			pop = evolver.getLife().runGenerationHooks(pop);
			pop = evolver.getLife().runInitialisationHooks(pop);

			// If reverted then increment reversion count.
			if (pop == null) {
				reversions++;
			}
		} while (pop == null);

		// Store the stats data from the initialisation.
		stats.addData(INIT_REVERSIONS, reversions);
		stats.addData(GEN_POP, pop);
		stats.addData(GEN_TIME, (System.nanoTime() - startTime));

		// Trigger life cycle events for end of initialisation and generation 0.
		evolver.getLife().fireInitialisationEndEvent();
		evolver.getLife().fireGenerationEndEvent();

		return pop;
	}

}
