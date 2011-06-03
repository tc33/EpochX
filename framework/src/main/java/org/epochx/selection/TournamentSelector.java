package org.epochx.selection;

import org.epochx.*;
import org.epochx.Config.ConfigKey;

public class TournamentSelector implements IndividualSelector {

	public static final ConfigKey<Integer> TOURNAMENT_SIZE = new ConfigKey<Integer>();

	private final RandomSelector randomSelector;
	private int size;

	public TournamentSelector() {
		randomSelector = new RandomSelector();
	}

	public void setup(Population population) {
		randomSelector.setup(population);
		size = Config.getInstance().get(TOURNAMENT_SIZE);
	}

	public Individual select() {
		Individual best = null;

		// Choose and compare randomly selected programs.
		for (int i = 0; i < size; i++) {
			Individual individual = randomSelector.select();
			if ((best == null) || (individual.getFitness().compareTo(best.getFitness()) > 0)) {
				best = individual;
			}
		}

		return best;
	}

}
