/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
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
package com.epochx.semantics;

/**
 * @author Lawrence Beadle & Tom Castle
 */
public class RegressionRepresentation implements Representation {
	
	private double[][] regressionRepresentation;
	
	/**
	 * Constructor for regression representation object
	 * @param regressionRepresentation list of the coefficients
	 */
	public RegressionRepresentation(double[][] regressionRepresentation) {
		this.regressionRepresentation = regressionRepresentation;
	}
	
	/**
	 * Returns the regression representation (the formula coefficients)
	 * @return A list of the formula coefficients
	 */
	public double[][] getRegressionRepresentation() {
		return regressionRepresentation;
	}

	/* (non-Javadoc)
	 * @see com.epochx.semantics.Representation#equals(com.epochx.semantics.Representation)
	 */
	@Override
	public boolean equals(Representation anotherBehaviour) {
		if(anotherBehaviour instanceof RegressionRepresentation) {
			RegressionRepresentation regRep = (RegressionRepresentation) anotherBehaviour;
			if(regRep.getRegressionRepresentation()==this.regressionRepresentation) {
				return true;
			}			
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.epochx.semantics.Representation#isConstant()
	 */
	@Override
	public boolean isConstant() {
		// if length = 1 there is only a constant in the x side of f(x)
		if(regressionRepresentation.length==1) {
			return true;
		}
		return false;
	}

}
