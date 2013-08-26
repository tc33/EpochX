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
package org.epochx.monitor.graph;

import java.util.Comparator;


/**
 * A <code>FitnessComparator</code> compares two <code>GraphVertex</code> regarding
 * their <code>Fitness</code>.
 */
public class FitnessComparator implements Comparator<GraphVertex> {

	/**
	 * Compares its two arguments for order. Returns a negative integer, zero,
	 * or a positive integer as the first <code>GraphVertex</code> is less than,
	 * equal to, or greater than the second regarding their <code>Fitness</code>.
	 * 
	 * @param o1 the first <code>GraphVertex</code> to be compared.
	 * @param o2 the second <code>GraphVertex</code> to be compared.
	 * @return a negative integer, zero, or a positive integer as the first
	 *         argument is less than, equal to, or greater than the second.
	 */
	public int compare(GraphVertex o1, GraphVertex o2) {
		
		return o2.getFitness().compareTo(o1.getFitness());
	}
	
	@Override
	public String toString() {
		return "fitness";
	}

}
