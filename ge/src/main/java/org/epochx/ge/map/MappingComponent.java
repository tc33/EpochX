/*
 * Copyright 2007-2013
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

package org.epochx.ge.map;

import org.epochx.Config.ConfigKey;
import org.epochx.Population;
import org.epochx.ProxyComponent;

/**
 * Instances of <code>MappingComponent</code> are components that are responsible for 
 * assigning parse trees to individuals in a population. Typically this involves
 * converting a <code>GEIndividual</code>'s chromosome into a parse tree with reference
 * to grammar rules.
 * 
 * @since 2.0
 */
public class MappingComponent extends ProxyComponent<Mapper> {

	/**
	 * The key for setting and retrieving the <code>Mapper</code> used by this component
	 */
	public static final ConfigKey<Mapper> MAPPER = new ConfigKey<Mapper>();

	/**
	 * Constructs a <code>MappingComponent</code>.
	 */
	public MappingComponent() {
		super(MAPPER);
	}

	/**
	 * Delegates the mapping of the population to the <code>Mapper</code> object
	 * 
	 * @param population the population of individuals to process
	 */
	public Population process(Population population) {
		if (handler == null) {
			throw new IllegalStateException("The mapper has not been set.");
		}

		handler.map(population);
		return population;
	}

}