package org.epochx.selection;

import static org.epochx.RandomSequence.RANDOM_SEQUENCE;

import org.epochx.*;

public class RandomSelector extends AbstractSelector {

	public Individual select() {
		return population.get(Config.getInstance().get(RANDOM_SEQUENCE).nextInt(population.size()));
	}

}
