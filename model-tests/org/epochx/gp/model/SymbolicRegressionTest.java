package org.epochx.gp.model;

import org.epochx.core.Evolver;
import org.epochx.gp.op.crossover.KozaCrossover;
import org.epochx.gp.op.init.RampedHalfAndHalfInitialiser;
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

		model.setCrossover(new KozaCrossover(evolver));

		model.setMaxDepth(16);
		model.setMaxInitialDepth(5);
		// TODO Start depth below should be 1 not 2, but insufficient programs.
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
	 * Koza's success rate: 23% (p586).
	 * OR 35% (p203).
	 * 
	 * Expecting success rate between 45% and 55%.
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

	/**
	 * Tests symbolic regression on function 2.718x^2 +3.1416x with standard
	 * setup.
	 * 
	 * Koza's success rate: 31% (p245).
	 * 
	 * Expecting success rate between % and %.
	 */
	// @Test
	// public void testEphemeral() {
	// final int LOWER_SUCCESS = 45;
	// final int UPPER_SUCCESS = 55;
	//
	// Regression model = new Regression(20) {
	// @Override
	// public double getCorrectResult(double x) {
	// return (2.718 * Math.pow(x, 2)) + (3.1416 * x);
	// }
	// };
	// setupModel(model);
	// SuccessCounter counter = new SuccessCounter();
	//
	// evolver.getLife().addRunListener(counter);
	//
	// model.run();
	//
	// evolver.getLife().removeRunListener(counter);
	//
	// int noSuccess = counter.getNoSuccess();
	// assertBetween("Unexpected success rate for quartic symbolic regression",
	// LOWER_SUCCESS, UPPER_SUCCESS, noSuccess);
	// }
}
