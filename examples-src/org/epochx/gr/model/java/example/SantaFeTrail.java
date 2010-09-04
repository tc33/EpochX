package org.epochx.gr.model.java.example;

import static org.epochx.stats.StatField.*;

import org.epochx.gr.op.crossover.WhighamCrossover;
import org.epochx.gr.op.init.RampedHalfAndHalfInitialiser;
import org.epochx.gr.op.mutation.WhighamMutation;
import org.epochx.life.RunAdapter;
import org.epochx.op.selection.TournamentSelector;

public class SantaFeTrail extends org.epochx.gr.model.java.SantaFeTrail {

	public SantaFeTrail() {

		setPopulationSize(500);
		setNoGenerations(100);
		setCrossoverProbability(0.9);
		setMutationProbability(0.1);
		setNoRuns(50);
		// setPoolSize(-1);
		setNoElites(10);
		setMaxInitialDepth(12);
		setMaxDepth(12);
		setPoolSelector(null);
		setProgramSelector(new TournamentSelector(this, 7));
		setCrossover(new WhighamCrossover(this));
		setMutation(new WhighamMutation(this));
		setInitialiser(new RampedHalfAndHalfInitialiser(this));

		/*
		 * getLifeCycleManager().addGenerationListener(new GenerationAdapter() {
		 * 
		 * @Override
		 * public void onGenerationEnd() {
		 * getStatsManager().printStats(GEN_NUMBER, GEN_FITNESS_MIN,
		 * GEN_FITNESS_AVE, GEN_DEPTH_AVE, GEN_DEPTH_MAX, GEN_FITTEST_PROGRAM);
		 * }
		 * });
		 */

		getLifeCycleManager().addRunListener(new RunAdapter() {

			@Override
			public void onRunEnd() {
				getStatsManager().printStats(RUN_NUMBER, RUN_FITNESS_MIN,
						RUN_FITTEST_PROGRAM);
			}
		});
	}

	public static void main(final String[] args) {
		new SantaFeTrail().run();
	}
}
