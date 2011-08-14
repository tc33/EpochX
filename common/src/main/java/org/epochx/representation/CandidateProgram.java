/*
 * Copyright 2007-2011 Tom Castle & Lawrence Beadle
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
package org.epochx.representation;

/**
 * An instance of <code>CandidateProgram</code> represents an individual
 * candidate solution to a problem. Specific subclasses represent the programs
 * in different ways.
 */
public abstract class CandidateProgram implements Cloneable {
	
	/**
	 * Creates a copy of this candidate program. Subclass implementations should
	 * override this method to copy their internal program structure. It should
	 * always be true that the returned program is equal to this program
	 * according to the implementation of the <code>equals</code> method.
	 * 
	 * @return a new instance of <code>CandidateProgram</code> that is
	 *         equivalent to this <code>CandidateProgram</code>.
	 */
	@Override
	public CandidateProgram clone() {
		CandidateProgram clone = null;
		try {
			clone = (CandidateProgram) super.clone();
		} catch (final CloneNotSupportedException e) {
			assert false;
		}

		return clone;
	}
}
