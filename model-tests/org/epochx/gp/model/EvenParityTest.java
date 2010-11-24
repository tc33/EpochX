package org.epochx.gp.model;

import org.epochx.gp.op.crossover.*;
import org.epochx.gp.op.init.*;
import org.epochx.life.*;
import org.epochx.op.selection.*;
import org.epochx.stats.*;
import org.epochx.test.*;

public class EvenParityTest extends ModelTest {
	
	/**
	 * Testing against Koza's setup and results.
	 * 
	 * Koza's success rate: 100% (p531).
	 * Expecting success rate between 99% and 100%.
	 */
	public void testEven3Parity() {
		final int LOWER_SUCCESS = 99;
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
		
		model.setMaxDepth(16);
		model.setMaxInitialDepth(5);
		model.setInitialiser(new RampedHalfAndHalfInitialiser(model, 1, false));
		model.setPoolSelector(null);
		model.setProgramSelector(new FitnessProportionateSelector(model, true));
		model.setNoElites(0);
		
		model.setTerminationFitness(0.0);
		
		SuccessCounter counter = new SuccessCounter();
		
		Life.get().addRunListener(counter);
		
//		Life.get().addGenerationListener(new GenerationAdapter() {
//			@Override
//			public void onGenerationEnd() {
//				Stats.get().print(StatField.RUN_NUMBER, StatField.GEN_NUMBER, StatField.GEN_FITNESS_MIN, StatField.GEN_FITNESS_AVE);
//			}
//		});
//
		Life.get().addRunListener(new RunAdapter() {
			@Override
			public void onRunEnd() {
				Stats.get().print(StatField.RUN_NUMBER, StatField.RUN_FITNESS_MIN);
			}
		});
		
		model.run();
		
		Life.get().removeRunListener(counter);
		
		int noSuccess = counter.getNoSuccess();
		assertBetween("Unexpected success rate for Even 3 Parity", LOWER_SUCCESS, UPPER_SUCCESS, noSuccess);
	}
	
	/**
	 * Testing against Koza's setup and results.
	 * 
	 * Koza's success rate: 45% (p531).
	 * Expecting success rate between 40% and 50%.
	 */
	public void testEven4Parity() {
		final int LOWER_SUCCESS = 40;
		final int UPPER_SUCCESS = 50;
		
		EvenParity model = new EvenParity(4);
		int noRuns = 100;
		
		model.setNoRuns(noRuns);
		model.setPopulationSize(4000);
		model.setNoGenerations(51);
		model.setCrossoverProbability(0.9);
		model.setMutationProbability(0.0);
		model.setReproductionProbability(0.1);
		
		model.setCrossover(new KozaCrossover(model));
		
		model.setMaxDepth(16);
		model.setMaxInitialDepth(5);
		model.setInitialiser(new RampedHalfAndHalfInitialiser(model, 1, false));
		model.setPoolSelector(null);
		model.setProgramSelector(new FitnessProportionateSelector(model, true));
		model.setNoElites(0);
		
		model.setTerminationFitness(0.0);
		
		SuccessCounter counter = new SuccessCounter();
		
		Life.get().addRunListener(counter);
		
//		Life.get().addGenerationListener(new GenerationAdapter() {
//			@Override
//			public void onGenerationEnd() {
//				Stats.get().print(StatField.RUN_NUMBER, StatField.GEN_NUMBER, StatField.GEN_FITNESS_MIN, StatField.GEN_FITNESS_AVE);
//			}
//		});
//	
//		Life.get().addRunListener(new RunAdapter() {
//			@Override
//			public void onRunEnd() {
//				Stats.get().print(StatField.RUN_NUMBER, StatField.RUN_FITNESS_MIN);
//			}
//		});
		
		model.run();
		
		Life.get().removeRunListener(counter);
		
		int noSuccess = counter.getNoSuccess();
		assertBetween("Unexpected success rate for Even 4 Parity", LOWER_SUCCESS, UPPER_SUCCESS, noSuccess);
	}
	
	/**
	 * Testing against Koza's setup and results.
	 * 
	 * Koza's success rate: 0% (p533).
	 * Expecting success rate between 0% and 2%.
	 */
	public void testEven5Parity() {
		final int LOWER_SUCCESS = 0;
		final int UPPER_SUCCESS = 0;
		
		EvenParity model = new EvenParity(5);
		int noRuns = 100;
		
		model.setNoRuns(noRuns);
		model.setPopulationSize(4000);
		model.setNoGenerations(51);
		model.setCrossoverProbability(0.9);
		model.setMutationProbability(0.0);
		model.setReproductionProbability(0.1);
		
		model.setCrossover(new KozaCrossover(model));
		
		model.setMaxDepth(16);
		model.setMaxInitialDepth(5);
		model.setInitialiser(new RampedHalfAndHalfInitialiser(model, 1, false));
		model.setPoolSelector(null);
		model.setProgramSelector(new FitnessProportionateSelector(model));
		model.setNoElites(0);
		
		model.setTerminationFitness(0.0);
		
		SuccessCounter counter = new SuccessCounter();
		
		Life.get().addRunListener(counter);
		
//		Life.get().addGenerationListener(new GenerationAdapter() {
//			@Override
//			public void onGenerationEnd() {
//				Stats.get().print(StatField.RUN_NUMBER, StatField.GEN_NUMBER, StatField.GEN_FITNESS_MIN, StatField.GEN_FITNESS_AVE);
//			}
//		});
//	
//		Life.get().addRunListener(new RunAdapter() {
//			@Override
//			public void onRunEnd() {
//				Stats.get().print(StatField.RUN_NUMBER, StatField.RUN_FITNESS_MIN);
//			}
//		});
		
		model.run();
		
		Life.get().removeRunListener(counter);
		
		int noSuccess = counter.getNoSuccess();
		assertBetween("Unexpected success rate for Even 5 Parity", LOWER_SUCCESS, UPPER_SUCCESS, noSuccess);
	}
}
