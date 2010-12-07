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

import java.util.*;
import org.epochx.epox.*;
import org.epochx.epox.dbl.CoefficientPowerFunction;

/**
 * Regression representation is for canonically modelling the behaviour of 
 * symbolic regression domains.
 */
public class RegressionRepresentation implements Representation, Cloneable {
	
	private ArrayList<CoefficientPowerFunction> regressionRepresentation;
	
	/**
	 * Constructor for regression representation object
	 * @param regressionRepresentation list of the coefficients
	 */
	public RegressionRepresentation(ArrayList<CoefficientPowerFunction> regressionRepresentation) {
		this.regressionRepresentation = regressionRepresentation;
	}
	
	/**
	 * Constructor for repression representation object - will create blank representation
	 */
	public RegressionRepresentation() {
		this.regressionRepresentation = new ArrayList<CoefficientPowerFunction>();
	}
	
	/**
	 * Returns the regression representation (the formula coefficients)
	 * @return A list of the formula coefficients
	 */
	public ArrayList<CoefficientPowerFunction> getRegressionRepresentation() {
		return regressionRepresentation;
	}
	
	/**
	 * Simplifies any CVPs with same variable and power
	 */
	public void simplify() {
		outer: for (int i=0; i<regressionRepresentation.size(); i++) {
			CoefficientPowerFunction cvp1 = regressionRepresentation.get(i);
			DoubleNode coefficient1 = (DoubleNode) cvp1.getChild(0);
			DoubleNode term1 = (DoubleNode) cvp1.getChild(1);
			DoubleNode exponent1 = (DoubleNode) cvp1.getChild(2);

			// Compare against every one AFTER it in the list.
			for (int j=i+1; j<regressionRepresentation.size(); j++) {
				CoefficientPowerFunction cvp2 = regressionRepresentation.get(j);
				DoubleNode coefficient2 = (DoubleNode) cvp2.getChild(0);
				DoubleNode term2 = (DoubleNode) cvp2.getChild(1);
				DoubleNode exponent2 = (DoubleNode) cvp2.getChild(2);
				
				if (term1.equals(term2) && exponent1.equals(exponent2)) {
					double newCoefficient = coefficient1.evaluate() + coefficient2.evaluate();
					
					// Update the second element with the new coefficient.
					cvp2.setChild(0, new DoubleLiteral(newCoefficient));
					
					// Nullify the current one and then we'll skip to the next...
					regressionRepresentation.set(i, null);
					
					// Once we've done one merge go onto the next element - others will be caught later.
					continue outer;
				}
			}
		}
	
		// kill any 0 or -0 coefficients
		for(int i = 0; i<regressionRepresentation.size(); i++) {
			if(regressionRepresentation.get(i) instanceof CoefficientPowerFunction) {
				double coefficient = (Double) regressionRepresentation.get(i).getChild(0).evaluate();
				if(coefficient==0 || coefficient==-0) {
					regressionRepresentation.set(i, null);
				}
			}
		}
		
		// Add non-nulls to this new list.
		List<CoefficientPowerFunction> combinedCVPs = new ArrayList<CoefficientPowerFunction>();
		for (CoefficientPowerFunction cvp: regressionRepresentation) {
			if (cvp != null) {
				combinedCVPs.add(cvp);
			}
		}
		
		// Clear the old list.
		regressionRepresentation.clear();
		
		// Then throw the new ones back in to the old list.
		regressionRepresentation.addAll(combinedCVPs);
		
		// if representation is zero
		if(regressionRepresentation.size()==0) {
			CoefficientPowerFunction cvp = new CoefficientPowerFunction(new DoubleLiteral(0d), new DoubleVariable("X"), new DoubleLiteral(0d));
			regressionRepresentation.add(cvp);
		}
	}
	
	/**
	 * Orders the CVP clauses - starting with the lowest power.
	 */
	public void order() {
		Collections.sort(regressionRepresentation, new Comparator<CoefficientPowerFunction>(){
			@Override
			public int compare(CoefficientPowerFunction cvp1,
							   CoefficientPowerFunction cvp2) {
				double power1 = (Double) ((Node) cvp1.getChild(2)).evaluate();
				double power2 = (Double) ((Node) cvp2.getChild(2)).evaluate();
				
				return Double.compare(power1, power2);
			}
		});
	}
	
	@Override
	public String toString() {
		String output = "";
		for(CoefficientPowerFunction c: regressionRepresentation) {
			output = output + c.toString() + " ";
		}
		return output;
	}

	/* (non-Javadoc)
	 * @see com.epochx.semantics.Representation#equals(com.epochx.semantics.Representation)
	 */
	@Override
	public boolean equals(Representation anotherBehaviour) {
		boolean marker = false;
		if(anotherBehaviour instanceof RegressionRepresentation) {
			RegressionRepresentation regRep = (RegressionRepresentation) anotherBehaviour;
			if(regRep.getRegressionRepresentation().equals(this.regressionRepresentation)) {
				marker = true;
			}			
		}
		return marker;
	}

	/* (non-Javadoc)
	 * @see com.epochx.semantics.Representation#isConstant()
	 */
	@Override
	public boolean isConstant() {
		// if length = 1 there is only a constant in the x side of f(x)
		if(regressionRepresentation.size()==1) {
			if(regressionRepresentation.get(0).getChild(2).equals(new DoubleLiteral(0d))) {
				return true;
			}
			if(regressionRepresentation.get(0).getChild(2).equals(new DoubleLiteral(-0d))) {
				return true;
			}			
		} else if(regressionRepresentation.size()==0) {
			return true;			
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object clone() throws CloneNotSupportedException {
		RegressionRepresentation newRep = (RegressionRepresentation) super.clone();
		newRep.regressionRepresentation = (ArrayList<CoefficientPowerFunction>) this.regressionRepresentation.clone();
		
		// Clone each cvp element.
		for (int i=0; i<newRep.regressionRepresentation.size(); i++) {
			CoefficientPowerFunction cvp = newRep.regressionRepresentation.get(i);
			newRep.regressionRepresentation.set(i, (CoefficientPowerFunction) cvp.clone());
		}
		return newRep;
	}

}
