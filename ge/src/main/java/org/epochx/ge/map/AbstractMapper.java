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
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.ge.map;

import org.epochx.Individual;
import org.epochx.Population;
import org.epochx.event.EventManager;
import org.epochx.ge.GEIndividual;
import org.epochx.ge.map.MappingEvent.EndMapping;
import org.epochx.ge.map.MappingEvent.StartMapping;
import org.epochx.grammar.NonTerminalSymbol;

/**
 * This class provides an abstract implementation of a <tt>Mapper</tt>. Sub-classes 
 * must provide an implementation of the <tt>map(GEIndividual)</tt> method, which converts 
 * one individual into a parse tree. This method will be called for each individual in
 * the population being mapped, with the result set as the individual's parse tree.
 * 
 * @since 2.0
 */
public abstract class AbstractMapper implements Mapper {

	/**
	 * Maps all individuals in the population by calling <tt>map(GEIndividual)</tt>,
	 * then assigns the resultant parse tree to the individual
	 * 
	 * @param population the population of individuals to process 
	 */
	@Override
	public void map(Population population) {
		for (Individual individual: population) {
			// Fires the start event
			StartMapping start = getStartEvent((GEIndividual) individual);
			EventManager.getInstance().fire(start);

			EndMapping end = getEndEvent((GEIndividual) individual);
			
			NonTerminalSymbol parseTree = map(end, (GEIndividual) individual);
			
			// Fires the end event only if the operator was successful
			if (parseTree != null) {
				((GEIndividual) individual).setParseTree(parseTree);
				EventManager.getInstance().fire(end);
			}
		}
	}

	/**
	 * Maps the given <tt>GEIndividual</tt> to a parse tree
	 * 
	 * @param individual the individual to be converted to a parse tree
	 * @return a <tt>Symbol</tt> which is the root node of a valid parse tree,
	 *         or <tt>null</tt> if no valid parse tree could be created from the
	 *         individual
	 */
	public abstract NonTerminalSymbol map(EndMapping event, GEIndividual individual);

	/**
	 * Returns the mapper's start event. The default implementation returns
	 * a <tt>StartMapping</tt> instance.
	 * 
	 * @param individual the individual being mapped
	 * 
	 * @return the mapper's start event
	 */
	protected StartMapping getStartEvent(GEIndividual individual) {
		return new StartMapping(this, individual);
	}

	/**
	 * Returns the mapper's end event. The default implementation returns
	 * an <code>EndMapping</code> instance. The end event is passed to the
	 * {@link #map(Individual)} method to allow the mapper to add
	 * additional information.
	 * 
	 * @param individual the individual that was mapped
	 * 
	 * @return the mapper's end event
	 */
	protected EndMapping getEndEvent(GEIndividual individual) {
		return new EndMapping(this, individual);
	}	
}
