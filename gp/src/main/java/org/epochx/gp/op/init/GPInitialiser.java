/*
 * Copyright 2007-2011 Tom Castle & Lawrence Beadle
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
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
package org.epochx.gp.op.init;

import org.epochx.Initialiser;
import org.epochx.gp.representation.GPIndividual;


/**
 * Implementations of this interface should be capable of generating an initial
 * population of <code>GPCandidatePrograms</code>. The getInitialPopulation()
 * method is called towards the start of execution of a run to get the first
 * population which will then be evolved.
 */
public interface GPInitialiser extends Initialiser {

	/**
	 * Constructs and returns an initial population of
	 * <code>GPIndividual</code> instances.
	 * 
	 * @return A List of newly generated GPCandidatePrograms.
	 */
	@Override
	public GPIndividual create();

}
