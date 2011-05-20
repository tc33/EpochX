package org.epochx;

import org.epochx.Config.*;
import org.epochx.event.*;
import org.epochx.event.GenerationEvent.*;

public class MaximumGenerations implements TerminationCriteria,
		Listener<EndGeneration> {

	public static final ConfigKey<Integer> MAXIMUM_GENERATIONS = new ConfigKey<Integer>();
	
	private int generation = 0;

	public boolean terminate() {
		return generation >= Config.getInstance().get(MAXIMUM_GENERATIONS);
	}

	public void onEvent(EndGeneration event) {
		generation = event.getGeneration();
	}

}
