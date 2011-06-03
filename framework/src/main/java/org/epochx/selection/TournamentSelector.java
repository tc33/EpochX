/* 
 * Copyright 2007-2011
 * Lawrence Beadle, Tom Castle and Fernando Otero
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */
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
