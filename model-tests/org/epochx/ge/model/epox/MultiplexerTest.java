package org.epochx.ge.model.epox;

import org.epochx.core.Evolver;
import org.epochx.ge.op.crossover.FixedPointCrossover;
import org.epochx.ge.op.init.RampedHalfAndHalfInitialiser;
import org.epochx.ge.op.mutation.*;
import org.epochx.life.*;
import org.epochx.op.selection.FitnessProportionateSelector;
import org.epochx.stats.*;
import org.epochx.test.*;
import org.junit.*;

public class MultiplexerTest extends ModelTest {

	private void setupModel(final Multiplexer model) {
		Evolver evolver = getEvolver();
		
		model.setNoRuns(100);
		model.setPopulationSize(500);
		model.setNoGenerations(51);
		model.setCrossoverProbability(0.9);
		model.setMutationProbability(0.1);
		model.setReproductionProbability(0.0);

		model.setCrossover(new FixedPointCrossover(evolver));
		model.setMutation(new PointMutation(evolver));

		model.setMaxDepth(16);
		model.setMaxInitialDepth(5);
		model.setInitialiser(new RampedHalfAndHalfInitialiser(evolver, 1, false));
		model.setPoolSelector(null);
		model.setProgramSelector(new FitnessProportionateSelector(evolver));
		model.setNoElites(0);

		model.setTerminationFitness(0.0);
		
		//setupRunPrinting(evolver.getStats(model));
		//setupGenPrinting(evolver.getStats(model));
	}

	/**
	 * Tests 6-bit multiplexer with standard setup.
	 * 
	 * Expecting success rate between % and %.
	 */
	@Test
	public void testMultiplexer6Bit() {
		final int LOWER_SUCCESS = 37;
		final int UPPER_SUCCESS = 47;

		final Multiplexer model = new Multiplexer(getEvolver(), 6);
		setupModel(model);

		final int noSuccess = getNoSuccesses(model);
		assertBetween("Unexpected success rate for Multiplexer 6-bit", LOWER_SUCCESS, UPPER_SUCCESS, noSuccess);
	}

	/**
	 * Tests 11-bit multiplexer with standard setup except:
	 * - Population size of 4000
	 * - Program selector of FitnessProportionateSelector with over-selection
	 * 
	 * Expecting success rate between % and %.
	 */
	@Test
	public void testMultiplexer11Bit() {
		final int LOWER_SUCCESS = 35;
		final int UPPER_SUCCESS = 45;

		Evolver evolver = getEvolver();
		final Multiplexer model = new Multiplexer(evolver, 11);
		setupModel(model);
		model.setProgramSelector(new FitnessProportionateSelector(evolver));

		final int noSuccess = getNoSuccesses(model);
		assertBetween("Unexpected success rate for Multiplexer 11-bit", LOWER_SUCCESS, UPPER_SUCCESS, noSuccess);
	}
}
