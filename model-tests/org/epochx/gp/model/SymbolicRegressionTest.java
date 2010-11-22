package org.epochx.gp.model;

import org.epochx.gp.op.crossover.*;
import org.epochx.gp.op.init.*;
import org.epochx.life.*;
import org.epochx.op.selection.*;
import org.epochx.stats.*;
import org.epochx.test.*;

public class SymbolicRegressionTest extends ModelTest {

	/**
	 * Testing against Koza's setup and results.
	 * 
	 * Koza's success rate: 23% (p586).
	 * Expecting success rate between 18% and 28%.
	 */
	public void testMultiplexer6Bit() {
		final int LOWER_SUCCESS = 58;
		final int UPPER_SUCCESS = 68;
		
		Multiplexer model = new Multiplexer(6);
		int noRuns = 100;
		
		model.setNoRuns(noRuns);
		model.setPopulationSize(500);
		model.setNoGenerations(51);
		model.setCrossoverProbability(0.9);
		model.setMutationProbability(0.0);
		model.setReproductionProbability(0.1);
		
		model.setCrossover(new KozaCrossover(model));
		
		model.setMaxDepth(17);
		model.setMaxInitialDepth(6);
		model.setInitialiser(new RampedHalfAndHalfInitialiser(model));
		model.setPoolSelector(null);
		model.setProgramSelector(new TournamentSelector(model, 7));
		model.setNoElites(0);
		
		model.setTerminationFitness(0.0);
		
		SuccessCounter counter = new SuccessCounter();
		
		Life.get().addRunListener(counter);
		
		model.run();
		
		Life.get().removeRunListener(counter);
		
		int noSuccess = counter.getNoSuccess();
		assertBetween("Unexpected success rate for Multiplexer 6-bit", LOWER_SUCCESS, UPPER_SUCCESS, noSuccess);
	}
	
}
