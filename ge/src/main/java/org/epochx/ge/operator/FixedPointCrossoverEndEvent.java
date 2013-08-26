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
 * An event fired at the end of a fixed-point crossover
 * 
 * @see FixedPointCrossover
 */
public class FixedPointCrossoverEndEvent extends OperatorEvent.EndOperator {

	private List<Integer> exchangedCodons1;
	private List<Integer> exchangedCodons2;
	private int point;

	/**
	 * Constructs a <tt>FixedPointCrossoverEndEvent</tt> with the details of the
	 * event
	 * 
	 * @param operator the operator that performed the crossover
	 * @param parents an array of two individuals that the operator was
	 *        performed on
	 */
	public FixedPointCrossoverEndEvent(FixedPointCrossover operator, Individual[] parents) {
		super(operator, parents);
	}

	/**
	 * Returns an integer which is the position within the codons that the crossover
	 * was performed
	 * 
	 * @return an integer which is the index of the crossover point
	 */
	public int getCrossoverPoint() {
		return point;
	}
	
	/**
	 * Sets the crossover point
	 * 
	 * @param point index used as the crossover point in both individuals
	 */
	public void setCrossoverPoint(int point) {
		this.point = point;
	}

	/**
	 * Returns a list of the codons from the first parent that were exchanged
	 * with codons from the second parent (as returned from getExchangedCodons2).
	 * The codons returned will be from the tail of the individual's chromosome,
	 * from the crossover point to the end of the chromosome.
	 * 
	 * @return a list of the codons exchanged from parent 1
	 */
	public List<Integer> getExchangedCodons1() {
		return exchangedCodons1;
	}

	/**
	 * Sets a list containing the codons from the first parent that were exchanged
	 * with codons from the second parent.
	 * 
	 * @param exchangedCodons1 the codons that were exchanged from parent 1
	 */
	public void setExchangedCodons1(List<Integer> exchangedCodons1) {
		this.exchangedCodons1 = exchangedCodons1;
	}
	
	/**
	 * Returns a list of the codons from the second parent that were exchanged
	 * with codons from the first parent (as returned from getExchangedCodons1).
	 * The codons returned will be from the tail of the individual's chromosome,
	 * from the crossover point to the end of the chromosome.
	 * 
	 * @return a list of the codons exchanged from parent 2
	 */
	public List<Integer> getExchangedCodons2() {
		return exchangedCodons2;
	}

	/**
	 * Sets a list containing the codons from the second parent that were exchanged
	 * with codons from the first parent.
	 * 
	 * @param exchangedCodons2 the codons that were exchanged from parent 2
	 */
	public void setExchangedCodons2(List<Integer> exchangedCodons2) {
		this.exchangedCodons2 = exchangedCodons2;
	}
}
