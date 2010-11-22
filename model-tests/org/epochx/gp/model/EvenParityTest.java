package org.epochx.gp.model;

import org.epochx.gp.op.crossover.*;
import org.epochx.gp.op.init.*;
import org.epochx.life.*;
import org.epochx.op.selection.*;
import org.epochx.test.*;

public class EvenParityTest extends ModelTest {
	
	/**
	 * Testing against Koza's setup and results.
	 * 
	 * Koza's success rate: 100% (p531).
	 * Expecting success rate between 98% and 100%.
	 */
	public void testEven3Parity() {
		final int LOWER_SUCCESS = 98;
		final int UPPER_SUCCESS = 100;
		
		EvenParity model = new EvenParity(3);
		int noRuns = 100;
		
		model.setNoRuns(noRuns);
		model.setPopulationSize(4000);
		model.setNoGenerations(51);
		model.setCrossoverProbability(0.9);
		model.setMutationProbability(0.0);
		model.setReproductionProbability(0.1);
		
		model.setCrossover(new KozaCrossover(model));
		
		model.setMaxDepth(17);
		model.setMaxInitialDepth(6);
		model.setInitialiser(new RampedHalfAndHalfInitialiser(model));
		model.setPoolSelector(null);
		model.setProgramSelector(new FitnessProportionateSelector(model));
		model.setNoElites(0);
		
		model.setTerminationFitness(0.0);
		
		SuccessCounter counter = new SuccessCounter();
		
		Life.get().addRunListener(counter);
		
		model.run();
		
		Life.get().removeRunListener(counter);
		
		int noSuccess = counter.getNoSuccess();
		assertBetween("Unexpected success rate for Even 3 Parity", LOWER_SUCCESS, UPPER_SUCCESS, noSuccess);
	}
}
