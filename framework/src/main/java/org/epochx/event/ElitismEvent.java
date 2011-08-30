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
package org.epochx.event;

import org.epochx.*;


/**
 * 
 */
public abstract class ElitismEvent implements Event {

	private Population population;
	
	public ElitismEvent(Population population) {
		this.population = population;
	}

	public Population getPopulation() {
		return population;
	}
	
	public static class StartElitism extends ElitismEvent {

		public StartElitism(Population population) {
			super(population);
		}
	}

	public static class EndElitism extends ElitismEvent {

		private Individual[] elites;
		
		public EndElitism(Population population, Individual[] elites) {
			super(population);
			
			this.elites = elites;
		}
		
		public Individual[] getElites() {
			return elites;
		}
	}
	
}
