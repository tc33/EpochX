/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming for research
 *
 *  EpochX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EpochX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EpochX.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.epochx.semantics;

import net.sf.javabdd.*;

/**
 * Boolean Representation holds the behavioural representation of 
 * problems in the Boolean domain
 */
public class BooleanRepresentation implements Representation {
	
	private BDD bdd;
	
	/**
	 * Constructor for Boolean Representation
	 * @param bdd The ROBDD representing the representation
	 */
	public BooleanRepresentation(BDD bdd) {
		this.bdd = bdd;
	}
	
	/**
	 * Returns the ROBDD associated with this representation
	 * @return The ROBDD
	 */
	public BDD getBDD() {
		return this.bdd;
	}

	/* (non-Javadoc)
	 * @see org.epochx.semantics.Behaviour#isTautology()
	 */
	@Override
	public boolean isConstant() {
		if(this.bdd.satCount()==0 || this.bdd.satCount()==1) {
			return true;
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.epochx.semantics.Behaviour#equal(org.epochx.semantics.Behaviour)
	 */
	@Override
	public boolean equals(Representation anotherBehaviour) {
		if(anotherBehaviour instanceof BooleanRepresentation) {
			BooleanRepresentation boolRep = (BooleanRepresentation) anotherBehaviour;
			if(this.bdd.equals(boolRep.getBDD())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}		
	}
}
