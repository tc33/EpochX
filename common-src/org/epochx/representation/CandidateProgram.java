/* 
 * Copyright 2007-2010 Tom Castle & Lawrence Beadle
 * Licensed under GNU General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.representation;

public abstract class CandidateProgram implements Cloneable, Comparable<CandidateProgram> {

	public abstract double getFitness();
	
	@Override
	public CandidateProgram clone() {
		CandidateProgram clone = null;
		try {
			clone = (CandidateProgram) super.clone();
		} catch (CloneNotSupportedException e) {
			// This shouldn't ever happen - if it does then everything is 
			// going to blow up anyway.
		}
		
		return clone;
	}
	
	/**
	 * Compares this program to another based upon fitness. Returns a negative 
	 * integer if this program has a larger (worse) fitness value, zero if they 
	 * have equal fitnesses and a positive integer if this program has a 
	 * smaller (better) fitness value.
	 * 
	 * This is super expensive if using to sort a list. Might be possible to 
	 * improve performance if we can implement caching of fitness within a 
	 * GPCandidateProgram.
	 * 
	 * @param o the GPCandidateProgram to be compared.
	 * @return a negative integer, zero, or a positive integer if this program 
	 * has a worse, equal or better fitness respectively. 
	 */
	@Override
	public int compareTo(CandidateProgram o) {
		double thisFitness = this.getFitness();
		double objFitness = o.getFitness();
		
		if (thisFitness > objFitness) {
			return -1;
		} else if (thisFitness == objFitness) {
			return 0;
		} else {
			return 1;
		}
	}
}
