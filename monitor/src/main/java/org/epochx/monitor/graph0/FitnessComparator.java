/* 
 * Copyright 2007-2012
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
package org.epochx.monitor.graph0;

import java.util.Comparator;


/**
 * A <code>FitnessComparator</code> compares two <code>GraphNode</code> regarding
 * their <code>Fitness</code>.
 */
public class FitnessComparator implements Comparator<GraphNode> {

	/**
	 * Compares its two arguments for order. Returns a negative integer, zero,
	 * or a positive integer as the first <code>GraphNode</code> is less than,
	 * equal to, or greater than the second regarding their <code>Fitness</code>.
	 * 
	 * @param o1 the first <code>GraphNode</code> to be compared.
	 * @param o2 the second <code>GraphNode</code> to be compared.
	 * @return a negative integer, zero, or a positive integer as the first
	 *         argument is less than, equal to, or greater than the second.
	 */
	public int compare(GraphNode o1, GraphNode o2) {
		
		return o1.getFitness().compareTo(o2.getFitness());
	}
	
	@Override
	public String toString() {
		return "fitness";
	}

}
