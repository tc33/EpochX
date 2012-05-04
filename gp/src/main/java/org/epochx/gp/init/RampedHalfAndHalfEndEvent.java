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
package org.epochx.gp.init;

import org.epochx.Population;
import org.epochx.event.InitialisationEvent;
import org.epochx.gp.init.RampedHalfAndHalfInitialisation.Method;

/**
 * An event fired at the end of a ramped half-and-half population initialisation
 * 
 * @see RampedHalfAndHalfInitialisation
 */
public class RampedHalfAndHalfEndEvent extends InitialisationEvent.EndInitialisation {

	private Method[] method;

	/**
	 * Constructs an event with the population that was constructed by the 
	 * ramped half-and-half initialisation procedure and a listing of the 
	 * initialisation method used for each individual in that population
	 * 
	 * @param population the population of individuals created by the
	 *        initialisation procedure
	 * @param method an array listing the initialisation methods used
	 */
	public RampedHalfAndHalfEndEvent(Population population, Method[] method) {
		super(population);

		this.method = method;
	}

	/**
	 * Returns an array listing the initialisation method used for each
	 * individual in the population
	 * 
	 * @return an array listing the initialisation methods used
	 */
	public Method[] getMethod() {
		return method;
	}

}
