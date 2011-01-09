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
 * The latest version is available from: http://www.epochx.org
 */
package org.epochx.gr.model.epox;

import org.epochx.gr.op.crossover.WhighamCrossover;
import org.epochx.gr.op.init.RampedHalfAndHalfInitialiser;
import org.epochx.gr.op.mutation.WhighamMutation;
import org.epochx.life.*;
import org.epochx.op.selection.FitnessProportionateSelector;
import org.epochx.stats.*;
import org.epochx.test.*;
import org.junit.*;

/**
 * 
 */
public class AntTrailTest extends ModelTest {

	private RunListener runPrinter;
	private GenerationListener genPrinter;

	@Before
	public void setUp() {
		runPrinter = new RunAdapter() {

			@Override
			public void onRunEnd() {
				Stats.get().print(StatField.RUN_NUMBER, StatField.RUN_FITNESS_MIN);
			}
		};
		Life.get().addRunListener(runPrinter);

		genPrinter = new GenerationAdapter() {

			@Override
			public void onGenerationEnd() {
				Stats.get().print(StatField.RUN_NUMBER, StatField.GEN_NUMBER, StatField.GEN_FITNESS_MIN, StatField.GEN_FITNESS_AVE);
			}
		};
		Life.get().addGenerationListener(genPrinter);
	}

	@After
	public void tearDown() throws Exception {
		Life.get().removeRunListener(runPrinter);
		Life.get().removeGenerationListener(genPrinter);
	}

	private void setupModel(final AntTrail model) {
		model.setNoRuns(100);
		model.setPopulationSize(500);
		model.setNoGenerations(51);
		model.setCrossoverProbability(0.9);
		model.setMutationProbability(0.1);
		model.setReproductionProbability(0.0);

		model.setCrossover(new WhighamCrossover(model));
		model.setMutation(new WhighamMutation(model));

		model.setMaxDepth(8);
		model.setMaxInitialDepth(8);
		model.setInitialiser(new RampedHalfAndHalfInitialiser(model, 4, false));
		model.setPoolSelector(null);
		model.setProgramSelector(new FitnessProportionateSelector(model));
		model.setNoElites(0);

		model.setTerminationFitness(0.0);
	}

	/**
	 * Tests Santa Fe trail with standard setup.
	 * 
	 * Expecting success rate between % and %.
	 */
	@Test
	public void testSantaFeTrail() {
		final int LOWER_SUCCESS = 0;
		final int UPPER_SUCCESS = 0;

		final AntTrail model = new SantaFeTrail(600);
		setupModel(model);

		final SuccessCounter counter = new SuccessCounter();

		Life.get().addRunListener(counter);

		model.run();

		Life.get().removeRunListener(counter);

		final int noSuccess = counter.getNoSuccess();
		assertBetween("Unexpected success rate for Santa Fe trail", LOWER_SUCCESS, UPPER_SUCCESS, noSuccess);
	}

}
