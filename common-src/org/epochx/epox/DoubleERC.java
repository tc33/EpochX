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
package org.epochx.epox;

import java.math.*;

import org.epochx.tools.random.*;


/**
 * 
 */
public class DoubleERC extends Literal {

	private RandomNumberGenerator rng;
	
	private double range;
	private double lower;
	
	private int precision;
	
	/**
	 * Constructs a <code>DoubleERC</code>.
	 * 
	 * @param value
	 */
	public DoubleERC(RandomNumberGenerator rng, double lower, double upper, int precision) {
		super(null);
		
		this.rng = rng;
		this.range = upper - lower;
		this.lower = lower;
	}

	@Override
	public Literal newInstance() {
		DoubleERC erc = (DoubleERC) super.newInstance();
		
		erc.setValue(generateValue());
		
		return erc;
	}
	
	protected double generateValue() {
		// Position random within range.
		double d = (rng.nextDouble() * range) + lower;

		// Round to the correct precision.
		BigDecimal big = new BigDecimal(d);
		big.round(new MathContext(precision));
		
		return big.doubleValue();
	}
}
