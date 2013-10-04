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
package org.epochx.ge.operator;

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

	private List<Integer> mutationPoints;

	/**
	 * Constructs a <tt>PointMutationEndEvent</tt> with the details of the
	 * event
	 * 
	 * @param operator the operator that performed the mutation
	 * @param parents an array of two individuals that the operator was
	 *        performed on
	 */
	public PointMutationEndEvent(PointMutation operator, Individual[] parents) {
		super(operator, parents);
	}

	/**
	 * Returns a list of the indexes which were mutated in the individual's 
	 * chromosome
	 * 
	 * @return a list of the mutation points
	 */
	public List<Integer> getMutationPoints() {
		return mutationPoints;
	}
	
	/**
	 * Sets a list of the mutation points
	 * 
	 * @param mutationPoints a list of the mutation points
	 */
	public void setMutationPoints(List<Integer> mutationPoints) {
		this.mutationPoints = mutationPoints;
	}
}
