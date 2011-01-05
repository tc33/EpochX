package org.epochx.gp.model;

import org.epochx.gp.op.crossover.KozaCrossover;
import org.epochx.gp.op.init.RampedHalfAndHalfInitialiser;
import org.epochx.life.*;
import org.epochx.op.selection.FitnessProportionateSelector;
import org.epochx.stats.*;
import org.epochx.test.*;
import org.junit.*;

public class SymbolicRegressionTest extends ModelTest {

	private RunListener runPrinter;
	private GenerationListener genPrinter;
	
	@Before
	public void setUp() throws Exception {
		runPrinter = new RunAdapter() {
			@Override
			public void onRunEnd() {
				Stats.get().print(StatField.RUN_NUMBER, StatField.RUN_FITNESS_MIN, StatField.RUN_FITTEST_PROGRAM);
			}
		};
		Life.get().addRunListener(runPrinter);
		
		genPrinter = new GenerationAdapter() {
			@Override
			public void onGenerationEnd() {
				Stats.get().print(StatField.RUN_NUMBER, StatField.GEN_NUMBER, StatField.GEN_FITNESS_MIN, StatField.GEN_FITNESS_AVE, StatField.GEN_FITTEST_PROGRAM);
			}
		};
		//Life.get().addGenerationListener(genPrinter);
	}
	
	@After
	public void tearDown() throws Exception {
		Life.get().removeRunListener(runPrinter);
		Life.get().removeGenerationListener(genPrinter);
	}
	
	private void setupModel(Regression model) {
		model.setNoRuns(100);
		model.setPopulationSize(500);
		model.setNoGenerations(51);
		model.setCrossoverProbability(0.9);
		model.setMutationProbability(0.0);
		model.setReproductionProbability(0.1);
		
		model.setCrossover(new KozaCrossover(model));
		
		model.setMaxDepth(16);
		model.setMaxInitialDepth(5);
		//TODO Start depth below should be 1 not 2, but insufficient programs.
		model.setInitialiser(new RampedHalfAndHalfInitialiser(model, 1, false));
		model.setPoolSelector(null);
		model.setProgramSelector(new FitnessProportionateSelector(model));
		model.setNoElites(0);
		
		model.setTerminationFitness(0.01);
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
		
		QuarticRegression model = new QuarticRegression(20);
		setupModel(model);
		SuccessCounter counter = new SuccessCounter();
		
		Life.get().addRunListener(counter);
		
		model.run();
		
		Life.get().removeRunListener(counter);
		
		int noSuccess = counter.getNoSuccess();
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
//	@Test
//	public void testEphemeral() {
//		final int LOWER_SUCCESS = 45;
//		final int UPPER_SUCCESS = 55;
//		
//		Regression model = new Regression(20) {
//			@Override
//			public double getCorrectResult(double x) {
//				return (2.718 * Math.pow(x, 2)) + (3.1416 * x);
//			}
//		};
//		setupModel(model);
//		SuccessCounter counter = new SuccessCounter();
//		
//		Life.get().addRunListener(counter);
//		
//		model.run();
//		
//		Life.get().removeRunListener(counter);
//		
//		int noSuccess = counter.getNoSuccess();
//		assertBetween("Unexpected success rate for quartic symbolic regression", LOWER_SUCCESS, UPPER_SUCCESS, noSuccess);
//	}
}
