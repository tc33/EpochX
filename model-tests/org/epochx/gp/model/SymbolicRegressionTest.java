package org.epochx.gp.model;

import org.epochx.gp.op.crossover.KozaCrossover;
import org.epochx.gp.op.init.RampedHalfAndHalfInitialiser;
import org.epochx.life.Life;
import org.epochx.op.selection.FitnessProportionateSelector;
import org.epochx.test.*;

public class SymbolicRegressionTest extends ModelTest {

	/**
	 * Not testing against Koza's setup and results, because have been unable
	 * to reproduce them - mainly because our setup is so different to his. 
	 * His performance is also contradicted:
	 * 
	 * Koza's success rate: 23% (p586).
	 * OR 35% (p203).
	 * Expecting success rate between 45% and 55%.
	 */
	public void testQuartic() {
		final int LOWER_SUCCESS = 45;
		final int UPPER_SUCCESS = 55;
		
		QuarticRegression model = new QuarticRegression(20);
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
		model.setPoolSelector(null);
		model.setProgramSelector(new FitnessProportionateSelector(model));
		model.setNoElites(0);
		
		model.setTerminationFitness(0.01);
		
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
//				Stats.get().print(StatField.RUN_NUMBER, StatField.RUN_FITNESS_MIN, StatField.RUN_FITTEST_PROGRAM);
//			}
//		});
		
		model.run();
		
		Life.get().removeRunListener(counter);
		
		int noSuccess = counter.getNoSuccess();
		assertBetween("Unexpected success rate for quartic symbolic regression", LOWER_SUCCESS, UPPER_SUCCESS, noSuccess);
	}	
}
