package org.epochx.ge.model.epox;

import org.epochx.core.Evolver;
import org.epochx.ge.op.crossover.*;
import org.epochx.ge.op.init.RampedHalfAndHalfInitialiser;
import org.epochx.ge.op.mutation.*;
import org.epochx.gp.op.crossover.OnePointCrossover;
import org.epochx.life.*;
import org.epochx.op.selection.*;
import org.epochx.stats.*;
import org.epochx.test.*;
import org.junit.*;

public class EvenParityTest extends ModelTest {

	private void setupModel(final EvenParity model) {
		Evolver evolver = getEvolver();
		
		model.setNoRuns(100);
		model.setPopulationSize(4000);
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
		model.setProgramSelector(new FitnessProportionateSelector(evolver, true));
		model.setNoElites(0);

		model.setTerminationFitness(0.0);
		
		//setupRunPrinting(evolver.getStats(model));
		//setupGenPrinting(evolver.getStats(model));
	}

	/**
	 * Tests even 3 parity with standard setup.
	 * 
	 * Expecting success rate between % and %.
	 */
	@Test
	public void testEven3Parity() {
		final int LOWER_SUCCESS = 99;
		final int UPPER_SUCCESS = 100;

		final EvenParity model = new EvenParity(getEvolver(), 3);
		setupModel(model);

		final int noSuccess = getNoSuccesses(model, false, false);
		assertBetween("Unexpected success rate for Even 3 Parity", LOWER_SUCCESS, UPPER_SUCCESS, noSuccess);
	}

	/**
	 * Tests even 4 parity with standard setup.
	 * 
	 * Expecting success rate between % and %.
	 */
	@Test
	public void testEven4Parity() {
		final int LOWER_SUCCESS = 40;
		final int UPPER_SUCCESS = 50;

		final EvenParity model = new EvenParity(getEvolver(), 4);
		setupModel(model);

		final int noSuccess = getNoSuccesses(model, false, false);
		assertBetween("Unexpected success rate for Even 4 Parity", LOWER_SUCCESS, UPPER_SUCCESS, noSuccess);
	}

	/**
	 * Tests even 5 parity with standard setup.
	 * 
	 * Expecting success rate between % and %.
	 */
	@Test
	public void testEven5Parity() {
		final int LOWER_SUCCESS = 0;
		final int UPPER_SUCCESS = 0;

		final EvenParity model = new EvenParity(getEvolver(), 5);
		setupModel(model);

		final int noSuccess = getNoSuccesses(model, false, false);
		assertBetween("Unexpected success rate for Even 5 Parity", LOWER_SUCCESS, UPPER_SUCCESS, noSuccess);
	}

	/**
	 * Tests standard setup except:
	 * - Mutation operator of SinglePointMutation
	 * 
	 * Expecting success rate between % and %.
	 */
	@Test
	public void testEven4ParitySinglePointMutation() {
		final int LOWER_SUCCESS = 50;
		final int UPPER_SUCCESS = 50;

		Evolver evolver = getEvolver();
		final EvenParity model = new EvenParity(evolver, 4);
		setupModel(model);
		model.setMutation(new SinglePointMutation(evolver));

		final int noSuccess = getNoSuccesses(model, false, false);
		assertBetween("Unexpected success rate for Even 4 Parity with single point mutation", LOWER_SUCCESS, UPPER_SUCCESS, noSuccess);
	}

	/**
	 * Tests standard setup except:
	 * - Crossover operator of OnePointCrossover
	 * 
	 * Expecting success rate between 40% and 50%.
	 */
	@Test
	public void testEven4ParityOnePointCrossover() {
		final int LOWER_SUCCESS = 50;
		final int UPPER_SUCCESS = 50;

		Evolver evolver = getEvolver();
		final EvenParity model = new EvenParity(evolver, 4);
		setupModel(model);
		model.setCrossover(new OnePointCrossover(evolver));

		final int noSuccess = getNoSuccesses(model, false, false);
		assertBetween("Unexpected success rate for Even 4 Parity with one point crossover", LOWER_SUCCESS, UPPER_SUCCESS, noSuccess);
	}

	/**
	 * Tests standard setup except:
	 * - Program selector of LinearRankSelector
	 * 
	 * Expecting success rate between 40% and 50%.
	 */
	@Test
	public void testEven4ParityLinearRankSelection() {
		final int LOWER_SUCCESS = 50;
		final int UPPER_SUCCESS = 50;

		Evolver evolver = getEvolver();
		final EvenParity model = new EvenParity(evolver, 4);
		setupModel(model);
		model.setProgramSelector(new LinearRankSelector(evolver));

		final int noSuccess = getNoSuccesses(model, false, false);
		assertBetween("Unexpected success rate for Even 4 Parity with linear rank selector", LOWER_SUCCESS, UPPER_SUCCESS, noSuccess);
	}

	/**
	 * Tests standard setup except:
	 * - Program selector of TournamentSelector (size 7)
	 * 
	 * Expecting success rate between 40% and 50%.
	 */
	@Test
	public void testEven4ParityTournament7Selection() {
		final int LOWER_SUCCESS = 50;
		final int UPPER_SUCCESS = 50;

		Evolver evolver = getEvolver();
		final EvenParity model = new EvenParity(evolver, 4);
		setupModel(model);
		model.setProgramSelector(new TournamentSelector(evolver, 7));

		final int noSuccess = getNoSuccesses(model, false, false);
		assertBetween("Unexpected success rate for Even 4 Parity with tournament selector", LOWER_SUCCESS, UPPER_SUCCESS, noSuccess);
	}
}
