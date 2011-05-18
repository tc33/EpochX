package org.epochx.gr.model.epox;

import org.epochx.core.Evolver;
import org.epochx.gr.op.crossover.WhighamCrossover;
import org.epochx.gr.op.init.RampedHalfAndHalfInitialiser;
import org.epochx.gr.op.mutation.WhighamMutation;
import org.epochx.life.*;
import org.epochx.op.selection.FitnessProportionateSelector;
import org.epochx.stats.*;
import org.epochx.test.*;
import org.junit.*;

public class SymbolicRegressionTest extends ModelTest {

	private void setupModel(final Regression model) {
		Evolver evolver = getEvolver();
		
		model.setNoRuns(100);
		model.setPopulationSize(500);
		model.setNoGenerations(51);
		model.setCrossoverProbability(0.9);
		model.setMutationProbability(0.0);
		model.setReproductionProbability(0.1);

		model.setCrossover(new WhighamCrossover(evolver));
		model.setMutation(new WhighamMutation(evolver));

		model.setMaxDepth(16);
		model.setMaxInitialDepth(5);
		model.setInitialiser(new RampedHalfAndHalfInitialiser(evolver, 1, false));
		model.setPoolSelector(null);
		model.setProgramSelector(new FitnessProportionateSelector(evolver));
		model.setNoElites(0);

		model.setTerminationFitness(0.01);
		
		//setupRunPrinting(evolver.getStats(model));
		//setupGenPrinting(evolver.getStats(model));
	}

	/**
	 * Tests quartic symbolic regression with standard setup.
	 * 
	 * Expecting success rate between % and %.
	 */
	@Test
	public void testQuartic() {
		final int LOWER_SUCCESS = 45;
		final int UPPER_SUCCESS = 55;

		final QuarticRegression model = new QuarticRegression(getEvolver(), 20);
		setupModel(model);

		final int noSuccess = getNoSuccesses(model, false, false);
		assertBetween("Unexpected success rate for quartic symbolic regression", LOWER_SUCCESS, UPPER_SUCCESS, noSuccess);
	}
}
