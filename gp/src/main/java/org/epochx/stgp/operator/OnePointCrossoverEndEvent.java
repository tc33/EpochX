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
package org.epochx.stgp.operator;

import org.epochx.Individual;
import org.epochx.epox.Node;
import org.epochx.event.OperatorEvent;

/**
 * An event fired at the end of a one-point crossover
 * 
 * @see OnePointCrossover
 */
public class OnePointCrossoverEndEvent extends OperatorEvent.EndOperator {

	private Node[] subtrees;
	private int[] points;

	/**
	 * Constructs a <tt>OnePointCrossoverEndEvent</tt> with the details of the
	 * event
	 * 
	 * @param operator the operator that performed the crossover
	 * @param parents an array of two individuals that the operator was
	 *        performed on
	 */
	public OnePointCrossoverEndEvent(OnePointCrossover operator, Individual[] parents) {
		super(operator, parents);
	}

	/**
	 * Returns an array of the two crossover points in the parent program trees
	 * 
	 * @return an array containing two indices which are the crossover points
	 */
	public int[] getCrossoverPoints() {
		return points;
	}
	
	/**
	 * Sets an array of the crossover points in the two parent programs
	 * 
	 * @param points an array of the two crossover points in the parents
	 */
	public void setCrossoverPoints(int[] points) {
		this.points = points;
	}

	/**
	 * Returns an array of nodes which are the root nodes of the subtrees that
	 * were exchanged. The nodes are given in the same order as the parents they
	 * were taken from
	 * 
	 * @return the subtrees
	 */
	public Node[] getSubtrees() {
		return subtrees;
	}

	/**
	 * Sets an array containing the subtrees that were exchanged
	 * 
	 * @param subtrees the two subtrees that were exchanged
	 */
	public void setSubtrees(Node[] subtrees) {
		this.subtrees = subtrees;
	}
}
