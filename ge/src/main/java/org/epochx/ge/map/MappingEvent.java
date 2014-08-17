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
 * The root class for mapping events.
 * 
 * @see Mapper
 * 
 * @since 2.0
 */
public abstract class MappingEvent implements Event {

	// The Mapper that fired the event
	private Mapper mapper;
	
	// The individual undergoing mapping
	private GEIndividual individual;
	
	/**
	 * Constructs a <code>MappingEvent</code>.
	 * 
	 * @param mapper the mapper that fired the event
	 * @param individual the individual undergoing mapping
	 */
	public MappingEvent(Mapper mapper, GEIndividual individual) {
		this.individual = individual;
		this.mapper = mapper;
	}
	
	/**
	 * Returns the individual undergoing mapping
	 * 
	 * @return the individual undergoing mapping
	 */
	public GEIndividual getIndividual() {
		return individual;
	}
	
	/**
	 * Returns the mapper that fired the event
	 * 
	 * @return the mapper that fired the event
	 */
	public Mapper getMapper() {
		return mapper;
	}
	
	/**
	 * Default event that indicates the start of a mapping
	 */
	public static class StartMapping extends MappingEvent {
		
		/**
		 * Constructs a <code>StartMapping</code> mapping event
		 * 
		 * @param mapper the mapper that fired the event
		 * @param individual the individual undergoing mapping
		 */
		public StartMapping(Mapper mapper, GEIndividual individual) {
			super(mapper, individual);
		}
		
	}

	/**
	 * Default event that indicates the end of a mapping
	 */
	public static class EndMapping extends MappingEvent {
		
		// The number of codons that were used in the mapping
		private int noActiveCodons;
		
		/**
		 * Constructs an <code>EndMapping</code> mapping event
		 * 
		 * @param mapper the mapper that fired the event
		 * @param individual the individual undergoing mapping
		 */
		public EndMapping(Mapper mapper, GEIndividual individual) {
			super(mapper, individual);
		}
		
		/**
		 * Sets the number of codons that were used in the mapping
		 * 
		 * @param noActiveCodons the number of codons used in the mapping
		 */
		public void setNoActiveCodons(int noActiveCodons) {
			this.noActiveCodons = noActiveCodons;
		}
		
		/**
		 * Returns the number of codons that were used in the mapping. This information 
		 * may not be provided by all mappers.
		 * 
		 * @return the number of codons used in the mapping
		 */
		public int getNoActiveCodons() {
			return noActiveCodons;
		}
		
	}
	
}