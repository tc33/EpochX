package org.epochx.event.stat;

import org.epochx.event.GenerationEvent.*;

public class GenerationAverageFitness extends AbstractStat<EndGeneration> {

	public GenerationAverageFitness() {
		super(NO_DEPENDENCIES);
	}

	public void onEvent(EndGeneration event) {
		
	}
}