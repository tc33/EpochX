package org.epochx;

import org.epochx.Config.ConfigKey;
import org.epochx.event.EventManager;
import org.epochx.event.GenerationEvent.EndGeneration;
import org.epochx.event.Listener;

public class MaximumGenerations implements TerminationCriteria,
		Listener<EndGeneration> {

	public static final ConfigKey<Integer> MAXIMUM_GENERATIONS = new ConfigKey<Integer>();

	private int generation = 0;

	public MaximumGenerations() {
		EventManager.getInstance().add(EndGeneration.class, this);
	}

	public boolean terminate() {
		return generation >= Config.getInstance().get(MAXIMUM_GENERATIONS);
	}

	public void onEvent(EndGeneration event) {
		generation = event.getGeneration();
	}

}
