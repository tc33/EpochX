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
package org.epochx;

import junit.framework.TestCase;

import org.epochx.Config.ConfigKey;
import org.epochx.selection.TournamentSelector;

/**
 * The <code>ConfigTest</code> class provides unit tests for methods of the
 * {@link Config} class.
 * 
 * @see Config
 */
public class ConfigTest extends TestCase {

	/**
	 * Test for the {@link Config#get(ConfigKey)} method.
	 */
	public void testGet() {
		ConfigKey<Double> key = new ConfigKey<Double>();
		assertNull(Config.getInstance().get(key));
		Config.getInstance().set(key, 1.0);
		assertNotNull(Config.getInstance().get(key));
	}

	/**
	 * Test for the {@link Config#set(ConfigKey, Object)} method.
	 */
	public void testSet() {
		ConfigKey<Double> key = new ConfigKey<Double>();
		Config.getInstance().set(key, 0.2);
		assertEquals(0.2, Config.getInstance().get(key));

		Config.getInstance().set(key, 0.4);
		assertEquals(0.4, Config.getInstance().get(key));
	}

	/**
	 * Test for the {@link Config#defaults()} method.
	 */
	public void testDefaults() {
		Config config = Config.getInstance();
		config.defaults();

		assertNotNull(config.get(Population.SIZE));
		assertNotNull(config.get(GenerationalStrategy.TERMINATION_CRITERIA));
		assertNotNull(config.get(TournamentSelector.TOURNAMENT_SIZE));
		assertNotNull(config.get(BranchedBreeder.SELECTOR));
		assertNotNull(config.get(Evolver.STRATEGY));
		assertNotNull(config.get(RandomSequence.RANDOM_SEQUENCE));
	}

}