package org.epochx.gr.model.epox.example;

import org.epochx.gr.model.GRModel;
import org.epochx.gr.op.init.FullInitialiser;
import org.epochx.life.*;
import org.epochx.stats.StatField;

/**
 * 
 */
public class QuarticRegression extends
		org.epochx.gr.model.epox.QuarticRegression {

	public static void main(final String[] args) {
		final GRModel model = new QuarticRegression();
		model.setNoRuns(50);
		model.setNoGenerations(100);
		model.setMaxDepth(14);
		model.setMaxInitialDepth(14);
		model.setInitialiser(new FullInitialiser(model));
		model.setTerminationFitness(0.01);

		model.run();
	}

}
