package org.epochx.ge.model.epox;

import org.epochx.ge.op.crossover.FixedPointCrossover;
import org.epochx.ge.op.init.RampedHalfAndHalfInitialiser;
import org.epochx.ge.op.mutation.PointMutation;
import org.epochx.life.*;
import org.epochx.op.selection.FitnessProportionateSelector;
import org.epochx.stats.*;
import org.epochx.test.*;
import org.junit.*;

public class MultiplexerTest extends ModelTest {
	
	private RunListener runPrinter;
	private GenerationListener genPrinter;
	
	@Before
	public void setUp() throws Exception {
		runPrinter = new RunAdapter() {
			@Override
			public void onRunEnd() {
				Stats.get().print(StatField.RUN_NUMBER, StatField.RUN_FITNESS_MIN);
			}
		};
		Life.get().addRunListener(runPrinter);
		
		genPrinter = new GenerationAdapter() {
			@Override
			public void onGenerationEnd() {
				Stats.get().print(StatField.RUN_NUMBER, StatField.GEN_NUMBER, StatField.GEN_FITNESS_MIN, StatField.GEN_FITNESS_AVE);
			}
		};
		//Life.get().addGenerationListener(genListener);
	}
	
	@After
	public void tearDown() throws Exception {
		Life.get().removeRunListener(runPrinter);
		Life.get().removeGenerationListener(genPrinter);
	}
	
	private void setupModel(Multiplexer model) {
		model.setNoRuns(100);
		model.setPopulationSize(500);
		model.setNoGenerations(51);
		model.setCrossoverProbability(0.9);
		model.setMutationProbability(0.1);
		model.setReproductionProbability(0.0);
		
		model.setCrossover(new FixedPointCrossover(model));
		model.setMutation(new PointMutation(model));
		
		model.setMaxDepth(16);
		model.setMaxInitialDepth(5);
		model.setInitialiser(new RampedHalfAndHalfInitialiser(model, 1, false));
		model.setPoolSelector(null);
		model.setProgramSelector(new FitnessProportionateSelector(model));
		model.setNoElites(0);
		
		model.setTerminationFitness(0.0);
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
		
		Multiplexer model = new Multiplexer(6);
		setupModel(model);
		
		SuccessCounter counter = new SuccessCounter();
		
		Life.get().addRunListener(counter);
		
		model.run();
		
		Life.get().removeRunListener(counter);
		
		int noSuccess = counter.getNoSuccess();
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
		
		Multiplexer model = new Multiplexer(11);
		setupModel(model);
		model.setPopulationSize(4000);
		model.setProgramSelector(new FitnessProportionateSelector(model, true));
		
		SuccessCounter counter = new SuccessCounter();
		
		Life.get().addRunListener(counter);
		
		model.run();
		
		Life.get().removeRunListener(counter);
		
		int noSuccess = counter.getNoSuccess();
		assertBetween("Unexpected success rate for Multiplexer 11-bit", LOWER_SUCCESS, UPPER_SUCCESS, noSuccess);
	}
}
