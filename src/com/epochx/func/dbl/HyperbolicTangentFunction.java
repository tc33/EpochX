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
 * 
 */
public class HyperbolicTangentFunction extends FunctionNode<Double> {

	public HyperbolicTangentFunction() {
		this(null);
	}
	
	public HyperbolicTangentFunction(Node<Double> child) {
		super(child);
	}

	@Override
	public Double evaluate() {
		double c = ((Double) getChild(0).evaluate()).doubleValue();
		
		return Math.tanh(c);
	}
	
	@Override
	public String getFunctionName() {
		return "TANH";
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj) && (obj instanceof HyperbolicTangentFunction);
	}
}
