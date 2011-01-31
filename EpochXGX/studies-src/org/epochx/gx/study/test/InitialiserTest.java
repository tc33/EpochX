package org.epochx.gx.study.test;

import org.epochx.core.*;
import org.epochx.gx.model.*;
import org.epochx.life.*;
import org.epochx.stats.*;

public class InitialiserTest {

	public static void main(String[] args) {

		Model model = new Fibonacci();
		model.setCrossoverProbability(0.0);
		model.setMutationProbability(1.0);
		model.setReproductionProbability(0.0);
		
		model.setPopulationSize(1000);
		model.setNoGenerations(1000);
		
		Life.get().addGenerationListener(new GenerationAdapter() {
			@Override
			public void onGenerationEnd() {
				Stats.get().print(StatField.GEN_FITNESS_MIN, StatField.GEN_FITNESS_AVE);
			}
		});
		
		model.run();
	}
}
