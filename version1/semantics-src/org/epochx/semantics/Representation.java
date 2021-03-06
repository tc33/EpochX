/*  
 *  Copyright 2007-2010 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of Epoch X - (The Genetic Programming Analysis Software)
 *
 *  Epoch X is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Epoch X is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with Epoch X.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.epochx.semantics;

/**
 * The interface behaviour specifies two core methods required in all behaviours.
 */
public interface Representation {

	/**
	 * Tests whether behaviour is dependent on any of the terminals used in the GP program
	 * @return true if the behaviour is not dependent on any of the terminals
	 */
	public boolean isConstant();
	
	/**
	 * Compares two behaviours for equivalence
	 * @param anotherBehaviour The behaviour to compare this this one
	 * @return TRUE is the behaviours are equivalent
	 */
	public boolean equals(Representation anotherBehaviour);
	
}
