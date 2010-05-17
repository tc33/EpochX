package org.epochx.gr.example.java;

import static org.epochx.gr.stats.GRStatField.*;

import org.epochx.gr.op.crossover.*;
import org.epochx.gr.op.init.*;
import org.epochx.gr.op.mutation.*;
import org.epochx.life.*;
import org.epochx.op.selection.*;

public class SantaFeTrail extends org.epochx.gr.model.java.SantaFeTrail {
	
	public SantaFeTrail() {
		
		setPopulationSize(500);
		setNoGenerations(100);
		setCrossoverProbability(0.9);
		setMutationProbability(0.1);
		setNoRuns(50);
		//setPoolSize(-1);
		setNoElites(10);
		setMaxInitialDepth(12);
		setMaxDepth(12);
		setPoolSelector(null);
		setProgramSelector(new TournamentSelector(this, 7));
		setCrossover(new WhighamCrossover(this));
		setMutation(new WhighamMutation(this));
		setInitialiser(new RampedHalfAndHalfInitialiser(this));

		/*getLifeCycleManager().addGenerationListener(new GenerationAdapter() {
			@Override
			public void onGenerationEnd() {
				getStatsManager().printGenerationStats(GEN_NUMBER, GEN_FITNESS_MIN, GEN_FITNESS_AVE, GEN_DEPTH_AVE, GEN_DEPTH_MAX, GEN_FITTEST_PROGRAM);
			}
		});*/
		
		getLifeCycleManager().addRunListener(new RunAdapter() {
			@Override
			public void onRunEnd() {
				getStatsManager().printRunStats(RUN_NUMBER, RUN_FITNESS_MIN, RUN_FITTEST_PROGRAM);
			}
		});
	}
	
	public static void main(String[] args) {
		new SantaFeTrail().run();
	}
}
