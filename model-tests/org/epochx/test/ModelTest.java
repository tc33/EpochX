package org.epochx.test;

import static org.junit.Assert.fail;

import org.epochx.core.*;
import org.epochx.life.*;
import org.epochx.stats.*;
import org.junit.*;

public class ModelTest {
	
	private Evolver evolver;
	
	private RunListener runPrinter;
	private GenerationListener genPrinter;
	
	@Before
	public void setUp() {
		evolver = new Evolver();
	}
	
	protected void setupRunPrinting(final Stats stats) {
		runPrinter = new RunAdapter() {
			@Override
			public void onRunEnd() {
				stats.print(StatField.RUN_NUMBER, StatField.RUN_FITNESS_MIN);
			}
		};
		evolver.getLife().addRunListener(runPrinter);
	}
	
	protected void setupGenPrinting(final Stats stats) {
		genPrinter = new GenerationAdapter() {
			@Override
			public void onGenerationEnd() {
				stats.print(StatField.RUN_NUMBER, StatField.GEN_NUMBER, StatField.GEN_FITNESS_MIN, StatField.GEN_FITNESS_AVE);
			}
		};
		evolver.getLife().addGenerationListener(genPrinter);
	}
	
	protected Evolver getEvolver() {
		return evolver;
	}
	
	protected int getNoSuccesses(Model model) {
		final SuccessCounter counter = new SuccessCounter();

		evolver.getLife().addRunListener(counter);

		evolver.run();

		evolver.getLife().removeRunListener(counter);

		return counter.getNoSuccess();
	}

	@After
	public void tearDown() {
		evolver.getLife().removeRunListener(runPrinter);
		evolver.getLife().removeGenerationListener(genPrinter);
	}

	public void assertBetween(String msg, final int lower, final int upper, final int actual) {
		if (msg == null) {
			msg = "";
		}

		if ((actual < lower) || (actual > upper)) {
			fail(msg + " lower=" + lower + " upper=" + upper + " actual=" + actual);
		}
	}

	public void assertBetween(final int lower, final int upper, final int actual) {
		assertBetween("", lower, upper, actual);
	}

	public void assertBetween(String msg, final double lower, final double upper, final double actual) {
		if (msg == null) {
			msg = "";
		}

		if ((actual < lower) || (actual > upper)) {
			fail(msg + " lower=" + lower + " upper=" + upper + " actual=" + actual);
		}
	}

	public void assertBetween(final double lower, final double upper, final double actual) {
		assertBetween("", lower, upper, actual);
	}
}
