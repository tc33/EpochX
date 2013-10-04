/*
 * Copyright 2007-2013
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
package org.epochx.ge.stats;

import org.epochx.event.stat.AbstractStat;
import org.epochx.ge.operator.SinglePointMutation;
import org.epochx.ge.operator.SinglePointMutationEndEvent;

/**
 * A stat that returns the mutation point from a single-point mutation between two 
 * <tt>GEIndividuals</tt>.
 * 
 * @see SinglePointMutation
 * @see SinglePointMutationEndEvent
 */
public class SinglePointMutationPoint extends AbstractStat<SinglePointMutationEndEvent> {

	private int mutationPoint;

	/**
	 * Constructs a <tt>SinglePointCrossoverPoint</tt> stat and registers its
	 * dependencies
	 */
	public SinglePointMutationPoint() {
		super(NO_DEPENDENCIES);
	}

	/**
	 * Triggers the generation of an updated value for this stat. Once this stat
	 * has been registered, this method will be called on each
	 * <tt>SinglePointMutationEndEvent</tt> event.
	 * 
	 * @param event an object that encapsulates information about the event that
	 *        occurred
	 */
	@Override
	public void refresh(SinglePointMutationEndEvent event) {
		mutationPoint = event.getMutationPoint();
	}

	/**
	 * Returns the mutation point from the previous single-point mutation that occurred.
	 * 
	 * @return an integer which was the mutation point in the individual
	 */
	public int getMutationPoint() {
		return mutationPoint;
	}

	/**
	 * Returns a string representation of the value of this stat
	 * 
	 * @return a <tt>String</tt> that represents the value of this stat
	 */
	@Override
	public String toString() {
		return Integer.toString(mutationPoint);
	}
}