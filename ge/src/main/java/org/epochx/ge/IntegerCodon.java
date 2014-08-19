/*
 * Copyright 2007-2013
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
package org.epochx.ge;


/**
 * A codon which stores its value as an integer
 * 
 * @see IntegerCodonFactory
 * 
 * @since 2.0
 */
public class IntegerCodon implements Codon {
	
	private long value;
	
	/**
	 * Constructs an <code>IntegerCodon</code> with the given value
	 * 
	 * @param value the value to assign to this codon
	 */
	public IntegerCodon(long value) {
		this.value = value;
	}
	
	/**
	 * Returns the value of this codon
	 * 
	 * @return the value of this codon
	 */
	@Override
	public long value() {
		return value;
	}
	
	/**
	 * Returns a string representation of this codon
	 * 
	 * @return a string representation of this codon
	 */
	@Override
	public String toString() {
		return Long.toString(value);
	}
}
