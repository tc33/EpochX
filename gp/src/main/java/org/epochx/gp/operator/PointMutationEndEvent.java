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
package org.epochx.gp.operator;

import java.util.List;

import org.epochx.Individual;
import org.epochx.event.OperatorEvent;
import org.epochx.gp.STGPIndividual;

/**
 * An event fired at the end of a point mutation
 * 
 * @see PointMutation
 */
public class PointMutationEndEvent extends OperatorEvent.EndOperator {

	private List<Integer> points;

	/**
	 * Constructs a <tt>SubtreeMutationEndEvent</tt> with the details of the
	 * event
	 * 
	 * @param operator the operator that performed the mutation
	 * @param parent the individual that the operator was performed on
	 * @param child the result of performing the mutation
	 * @param points a <tt>List</tt> of the mutation points
	 */
	public PointMutationEndEvent(PointMutation operator, STGPIndividual parent, STGPIndividual child,
			List<Integer> points) {
		super(operator, new Individual[]{parent}, new Individual[]{child});

		this.points = points;
	}

	/**
	 * Returns a list of the mutation points in the parent program tree
	 * 
	 * @return a list of indices of the mutation points
	 */
	public List<Integer> getMutationPoints() {
		return points;
	}

}
