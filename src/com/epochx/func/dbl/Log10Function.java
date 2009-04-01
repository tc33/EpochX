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
package com.epochx.func.dbl;

import com.epochx.core.representation.*;

/**
 * Implements the base 10 logarithm.
 */
public class Log10Function extends FunctionNode<Double> {
	
	public Log10Function(Node<Double> child) {
		super(child);
	}

	@Override
	public Double evaluate() {
		// TODO Could this bit not be done in superclass somehow, with a call to super.evaluate() required here?
		double c = ((Double) getChild(0).evaluate()).doubleValue();
		
		// TODO Need to check that this and others don't throw up any nasty divide by 0 like issues to protect against.
		return Math.log10(c);
	}
	
	@Override
	public String getFunctionName() {
		return "LOG-10";
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Could this instanceof business not be done higher up too? i.e. are they of the same type?
		return super.equals(obj) && (obj instanceof Log10Function);
	}
}
