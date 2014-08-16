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

package org.epochx.ge.map;

import org.epochx.event.Event;
import org.epochx.ge.GEIndividual;

/**
 * Base class for mapping related events.
 * 
 * @see Mapper
 */
public abstract class MappingEvent implements Event {

	private Mapper mapper;
	
	private GEIndividual individual;
	
	public MappingEvent(Mapper mapper, GEIndividual individual) {
		this.individual = individual;
		this.mapper = mapper;
	}
	
	public GEIndividual getIndividual() {
		return individual;
	}
	
	public Mapper getMapper() {
		return mapper;
	}
	
	/**
	 * An event that indicates the start of a mapping.
	 */
	public static class StartMapping extends MappingEvent {
		
		public StartMapping(Mapper mapper, GEIndividual individual) {
			super(mapper, individual);
		}
		
	}

	/**
	 * An event that indicates the end of a mapping.
	 */
	public static class EndMapping extends MappingEvent {
		
		private int noActiveCodons;
		
		public EndMapping(Mapper mapper, GEIndividual individual) {
			super(mapper, individual);
		}
		
		public void setNoActiveCodons(int noActiveCodons) {
			this.noActiveCodons = noActiveCodons;
		}
		
		/**
		 * This may not be supported by all mappers.
		 * 
		 * @return
		 */
		public int getNoActiveCodons() {
			return noActiveCodons;
		}
		
	}
	
}