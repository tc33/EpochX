package org.epochx.gr.example.epox;

import org.epochx.gr.model.GRModel;
import org.epochx.gr.op.init.FullInitialiser;
import org.epochx.gr.stats.GRStatField;
import org.epochx.life.RunAdapter;


/**
 * 
 */
public class QuarticRegression extends org.epochx.gr.model.epox.QuarticRegression {
	
    public static void main(String[] args) {
		final GRModel model = new QuarticRegression();
		model.setNoRuns(50);
		model.setNoGenerations(100);
		model.setMaxDepth(14);
		model.setMaxInitialDepth(14);
		model.setInitialiser(new FullInitialiser(model));
		model.setTerminationFitness(0.01);
		/*model.getLifeCycleManager().addGenerationListener(new GenerationAdapter() {			
			@Override
			public void onGenerationEnd() {
				model.getStatsManager().printGenerationStats(GRStatField.GEN_NUMBER, GRStatField.GEN_FITNESS_MIN, GRStatField.GEN_FITNESS_AVE, GRStatField.GEN_DEPTH_AVE, GRStatField.GEN_DEPTH_MAX, GRStatField.GEN_FITTEST_PROGRAM);
			}
		});*/
		
		model.getLifeCycleManager().addRunListener(new RunAdapter() {			
			@Override
			public void onRunEnd() {
				model.getStatsManager().printRunStats(GRStatField.RUN_NUMBER, GRStatField.RUN_FITNESS_MIN, GRStatField.RUN_FITTEST_PROGRAM);
			}
		});
		model.run();
	}
	
}
