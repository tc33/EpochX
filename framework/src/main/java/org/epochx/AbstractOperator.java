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

import org.epochx.event.Event;
import org.epochx.event.EventManager;
import org.epochx.event.Listener;
import org.epochx.event.OperatorEvent.EndOperator;
import org.epochx.event.OperatorEvent.StartOperator;

/**
 * A skeletal implementation of the {@link Operator} interface than fires
 * events at the start (before) and end (after) the operator is performed.
 * 
 * @see Event
 * @see EventManager
 * @see Listener
 */
public abstract class AbstractOperator implements Operator {

	public final Individual[] apply(Individual ... individuals) {
		// fires the start event
		StartOperator start = getStartEvent(individuals);
		EventManager.getInstance().fire(start.getClass(), start);

		EndOperator end = getEndEvent(individuals);
		Individual[] offspring = perform(end, individuals);

		// fires the end event
		end.setOffspring(clone(offspring));
		EventManager.getInstance().fire(end.getClass(), end);

		return offspring;
	}

	/**
	 * Performs the operator on the specified individiduals.
	 * 
	 * @param event the end event object to be fired.
	 * @param individuals the individuals undergoing the operator.
	 * 
	 * @return the indivuals produced by this operator.
	 */
	public abstract Individual[] perform(EndOperator event, Individual ... individuals);

	/**
	 * Returns the operator's start event. The default implementation returns
	 * a <code>StartOperator</code> instance.
	 * 
	 * @param parents the individuals undergoing the operator.
	 * 
	 * @return the operator's start event.
	 */
	protected StartOperator getStartEvent(Individual ... parents) {
		return new StartOperator(this, parents);
	}

	/**
	 * Returns the operator's end event. The default implementation returns
	 * a <code>EndOperator</code> instance. The end event is passed to the
	 * {@link #perform(Individual...)} method to allow the operator to add
	 * addition information.
	 * 
	 * @param parents the individuals undergoing the operator.
	 * 
	 * @return the operator's end event.
	 */
	protected EndOperator getEndEvent(Individual ... parents) {
		return new EndOperator(this, parents);
	}

	/**
	 * Returns a (deep) clone copy of the specified array of individuals.
	 * 
	 * @param individuals the array of individuals to be cloned.
	 * 
	 * @return a (deep) clone copy of the specified array of individuals.
	 */
	private Individual[] clone(Individual[] individuals) {
		Individual[] clone = new Individual[individuals.length];

		for (int i = 0; i < clone.length; i++) {
			clone[i] = individuals[i].clone();
		}

		return clone;
	}

}