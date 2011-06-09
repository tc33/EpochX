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
 * The latest version is available from: http://www.epochx.org
 */

package org.epochx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.epochx.event.ConfigEvent;
import org.epochx.event.EventManager;
import org.epochx.random.MersenneTwisterFast;
import org.epochx.selection.TournamentSelector;

public class Config {

	private static final Config singleton = new Config();

	private final HashMap<ConfigKey<?>, Object> mapping = new HashMap<ConfigKey<?>, Object>();

	private Config() {
	}

	public static Config getInstance() {
		return singleton;
	}

	public void defaults() {
		singleton.set(Population.SIZE, 500);
		singleton.set(MaximumGenerations.MAXIMUM_GENERATIONS, 50);

		List<TerminationCriteria> criteria = new ArrayList<TerminationCriteria>();
		criteria.add(new MaximumGenerations());
		singleton.set(GenerationalStrategy.TERMINATION_CRITERIA, criteria);

		singleton.set(TournamentSelector.TOURNAMENT_SIZE, 5);
		singleton.set(BranchedBreeder.SELECTOR, new TournamentSelector());
		singleton.set(Evolver.STRATEGY, new GenerationalStrategy(new BranchedBreeder()));
		singleton.set(RandomSequence.RANDOM_SEQUENCE, new MersenneTwisterFast());
	}

	public <T> void set(ConfigKey<T> key, T value) {
		mapping.put(key, value);
		EventManager.getInstance().fire(ConfigEvent.class, new ConfigEvent(key));
	}

	@SuppressWarnings("unchecked")
	public <T> T get(ConfigKey<T> key) {
		return (T) mapping.get(key);
	}

	public static class ConfigKey<T> {}
}