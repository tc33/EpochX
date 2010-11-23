package org.epochx.gp.model;

import org.epochx.gp.op.crossover.KozaCrossover;
import org.epochx.gp.op.init.RampedHalfAndHalfInitialiser;
import org.epochx.life.*;
import org.epochx.op.selection.FitnessProportionateSelector;
import org.epochx.stats.*;
import org.epochx.test.*;

public class MultiplexerTest extends ModelTest {
	
	/**
	 * Not testing against Koza's setup and results, because have been unable
	 * to reproduce them. His performance is also contradicted:
	 * 
	 * Koza's success rate: 63% (p572).
	 * OR: 28% (p195).
	 * 
	 * Expecting success rate between 35% and 45%.
	 */
	public void testMultiplexer6Bit() {
		final int LOWER_SUCCESS = 35;
		final int UPPER_SUCCESS = 45;
		
		Multiplexer model = new Multiplexer(6);
		int noRuns = 100;
		
		model.setNoRuns(noRuns);
		model.setPopulationSize(500);
		model.setNoGenerations(51);
		model.setCrossoverProbability(0.9);
		model.setMutationProbability(0.0);
		model.setReproductionProbability(0.1);
		
		model.setCrossover(new KozaCrossover(model));
		
		model.setMaxDepth(16);
		model.setMaxInitialDepth(5);
		RampedHalfAndHalfInitialiser initialiser = new RampedHalfAndHalfInitialiser(model);
		initialiser.setDuplicatesEnabled(false);
		model.setInitialiser(initialiser);
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
		
//		Life.get().addRunListener(new RunAdapter() {
//			@Override
//			public void onRunEnd() {
//				Stats.get().print(StatField.RUN_NUMBER, StatField.RUN_FITNESS_MIN);
//			}
//		});
		
		model.run();
		
		Life.get().removeRunListener(counter);
		
		int noSuccess = counter.getNoSuccess();
		assertBetween("Unexpected success rate for Multiplexer 6-bit", LOWER_SUCCESS, UPPER_SUCCESS, noSuccess);
	}
	
	/**
	 * Not testing against Koza's setup and results, because none seem to be 
	 * listed.
	 * 
	 * Expecting success rate between 35% and 45%.
	 */
	public void testMultiplexer11Bit() {
		final int LOWER_SUCCESS = 35;
		final int UPPER_SUCCESS = 45;
		
		Multiplexer model = new Multiplexer(11);
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
		RampedHalfAndHalfInitialiser initialiser = new RampedHalfAndHalfInitialiser(model);
		initialiser.setDuplicatesEnabled(false);
		model.setInitialiser(initialiser);
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
		assertBetween("Unexpected success rate for Multiplexer 11-bit", LOWER_SUCCESS, UPPER_SUCCESS, noSuccess);
	}
}
