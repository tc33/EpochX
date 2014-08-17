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
import org.epochx.ge.Codon;

/**
 * An event fired at the end of a one-point crossover
 * 
 * @see OnePointCrossover
 * 
 * @since 2.0
 */
public class OnePointCrossoverEndEvent extends OperatorEvent.EndOperator {

	private List<Codon> exchangedCodons1;
	private List<Codon> exchangedCodons2;
	private int point1;
	private int point2;

	/**
	 * Constructs a <code>OnePointCrossoverEndEvent</code> with the details of the
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
	 * Returns an integer which is the position within the first parent's codons that the 
	 * crossover was performed
	 * 
	 * @return an integer which is the index of the crossover point in the first parent
	 */
	public int getCrossoverPoint1() {
		return point1;
	}
	
	/**
	 * Sets the crossover point within the first parent
	 * 
	 * @param point index used as the crossover point in the first parent
	 */
	public void setCrossoverPoint1(int point1) {
		this.point1 = point1;
	}
	
	/**
	 * Returns an integer which is the position within the second parent's codons that the 
	 * crossover was performed
	 * 
	 * @return an integer which is the index of the crossover point in the second parent
	 */
	public int getCrossoverPoint2() {
		return point2;
	}
	
	/**
	 * Sets the crossover point within the second parent
	 * 
	 * @param point index used as the crossover point in the second parent
	 */
	public void setCrossoverPoint2(int point2) {
		this.point2 = point2;
	}

	/**
	 * Returns a list of the codons from the first parent that were exchanged
	 * with codons from the second parent (as returned from getExchangedCodons2).
	 * The codons returned will be from the tail of the individual's chromosome,
	 * from the crossover point to the end of the chromosome.
	 * 
	 * @return a list of the codons exchanged from parent 1
	 */
	public List<Codon> getExchangedCodons1() {
		return exchangedCodons1;
	}

	/**
	 * Sets a list containing the codons from the first parent that were exchanged
	 * with codons from the second parent.
	 * 
	 * @param exchangedCodons1 the codons that were exchanged from parent 1
	 */
	public void setExchangedCodons1(List<Codon> exchangedCodons1) {
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
	public List<Codon> getExchangedCodons2() {
		return exchangedCodons2;
	}

	/**
	 * Sets a list containing the codons from the second parent that were exchanged
	 * with codons from the first parent.
	 * 
	 * @param exchangedCodons2 the codons that were exchanged from parent 2
	 */
	public void setExchangedCodons2(List<Codon> exchangedCodons2) {
		this.exchangedCodons2 = exchangedCodons2;
	}
}
