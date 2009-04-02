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

import java.util.ArrayList;

import com.epochx.core.representation.Node;
import com.epochx.core.representation.TerminalNode;
import com.epochx.func.dbl.CoefficientExponentFunction;

/**
 * @author Lawrence Beadle & Tom Castle
 */
public class RegressionRepresentation implements Representation {
	
	private ArrayList<CoefficientExponentFunction> regressionRepresentation;
	
	/**
	 * Constructor for regression representation object
	 * @param regressionRepresentation list of the coefficients
	 */
	public RegressionRepresentation(ArrayList<CoefficientExponentFunction> regressionRepresentation) {
		this.regressionRepresentation = regressionRepresentation;
	}
	
	/**
	 * Constructor for repression representation object - will create blank representation
	 */
	public RegressionRepresentation() {
		this.regressionRepresentation = new ArrayList<CoefficientExponentFunction>();
	}
	
	/**
	 * Returns the regression representation (the formula coefficients)
	 * @return A list of the formula coefficients
	 */
	public ArrayList<CoefficientExponentFunction> getRegressionRepresentation() {
		return regressionRepresentation;
	}
	
	/**
	 * Simplifies any CVPs with same variable and power
	 */
	public void simplify() {
		for(int i = 0; i<regressionRepresentation.size()-1; i++) {
			CoefficientExponentFunction a = regressionRepresentation.get(i);
			for(int j = i+1; j<regressionRepresentation.size(); j++) {
				CoefficientExponentFunction b = regressionRepresentation.get(j);
				if(a.getChild(1).equals(b.getChild(1)) && a.getChild(2).equals(b.getChild(2))) {
					double coefficient1 = (Double) a.getChild(0).evaluate();
					double coefficient2 = (Double) b.getChild(0).evaluate();
					double newCoefficient = coefficient1 + coefficient2;
					a = new CoefficientExponentFunction(new TerminalNode<Double>(newCoefficient),(Node<Double>) a.getChild(1),(Node<Double>) a.getChild(2));
					regressionRepresentation.remove(j);
				}
			}
		}
	}
	
	public void order() {
		ArrayList<CoefficientExponentFunction> ordered = new ArrayList<CoefficientExponentFunction>();
		// find lowest power
		int size = regressionRepresentation.size();
		double lowestPower = new Double(Double.POSITIVE_INFINITY);
		for(CoefficientExponentFunction c: regressionRepresentation) {
			double power = (Double) c.getChild(2).evaluate();
			if(power<lowestPower) {
				lowestPower = power;
			}
		}
		// re-jig into ordered array
		while(ordered.size()<size) {
			for(CoefficientExponentFunction c: regressionRepresentation) {
				double power = (Double) c.getChild(2).evaluate();
				if(power==lowestPower) {
					ordered.add(c);
					break;
				}
			}
			lowestPower++;
		}
		// set rep to ordered
		regressionRepresentation = ordered;
	}
	
	public String toString() {
		String output = "";
		for(CoefficientExponentFunction c: regressionRepresentation) {
			output = output + c.toString() + " ";
		}
		return output;
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
		if(regressionRepresentation.size()==1) {
			if(regressionRepresentation.get(0).getChild(2).equals(new TerminalNode<Double>(0d))) {
				return true;
			}
			if(regressionRepresentation.get(0).getChild(2).equals(new TerminalNode<Double>(-0d))) {
				return true;
			}
			
		}
		return false;
	}

}
