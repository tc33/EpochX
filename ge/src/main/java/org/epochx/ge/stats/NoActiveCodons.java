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

package org.epochx.ge.stats;

import org.epochx.event.stat.AbstractStat;
import org.epochx.ge.map.MappingEvent.EndMapping;

/**
 * A stat that returns the number of codons that were used in the previous 
 * GE mapping 
 */
public class NoActiveCodons extends AbstractStat<EndMapping> {

	private int noActiveCodons;
	
	/**
	 * Constructs a <code>NoActiveCodons</code> stat and registers its dependencies
	 */
	public NoActiveCodons() {
		super(NO_DEPENDENCIES);
	}

	/**
	 * Triggers the generation of an updated value for this stat. Once this stat
	 * has been registered, this method will be called on each
	 * <code>EndMapping</code> event.
	 * 
	 * @param event an object that encapsulates information about the event that
	 *        occurred
	 */
	@Override
	public void refresh(EndMapping event) {
		noActiveCodons = event.getNoActiveCodons();
	}
	
	/**
	 * Returns the number of codons that were used in the mapping
	 * 
	 * @return the depths of each program tree in the previous generation
	 */
	public int getNoActiveCodons() {
		return noActiveCodons;
	}

	/**
	 * Returns a string representation of the value of this stat
	 * 
	 * @return a <code>String</code> that represents the value of this stat
	 */
	@Override
	public String toString() {
		return Integer.toString(noActiveCodons);
	}
}