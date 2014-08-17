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
 * The latest version is available from: http://www.epochx.org
 */
package org.epochx.stgp.operator;

import java.util.List;

import org.epochx.Individual;
import org.epochx.event.OperatorEvent;

/**
 * An event fired at the end of a point mutation
 * 
 * @see PointMutation
 * 
 * @since 2.0
 */
public class PointMutationEndEvent extends OperatorEvent.EndOperator {

	private List<Integer> points;

	/**
	 * Constructs a <code>SubtreeMutationEndEvent</code> with the details of the
	 * event
	 * 
	 * @param operator the operator that performed the mutation
	 * @param parent the individual that the operator was performed on
	 * 
	 */
	public PointMutationEndEvent(PointMutation operator, Individual ... parents) {
		super(operator, parents);
	}

	/**
	 * Returns a list of the mutation points in the parent program tree
	 * 
	 * @return a list of indices of the mutation points
	 */
	public List<Integer> getMutationPoints() {
		return points;
	}

	/**
	 * Sets the points that were mutated in the parent program tree
	 * 
	 * @param points a list of indices of the mutation points
	 */
	public void setMutationPoints(List<Integer> points) {
		this.points = points;
	}
}
