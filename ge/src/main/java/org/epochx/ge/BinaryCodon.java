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

import java.util.Arrays;

import org.epochx.Config.ConfigKey;


/**
 * A codon which stores its value as a sequence of bits
 * 
 * @see BinaryCodonFactory
 * 
 * @since 2.0
 */
public class BinaryCodon implements Codon {

	/**
	 * The key for setting and retrieving the number of binary bits to use for
	 * one binary codon
	 */
	public static final ConfigKey<Integer> NO_BITS = new ConfigKey<Integer>();
	
	private boolean[] bits;
	
	/**
	 * Constructs a <code>BinaryCodon</code> with the given bits used as the value
	 * 
	 * @param bits the bit sequence to assign to this codon
	 */
	public BinaryCodon(boolean[] bits) {
		this.bits = bits;
	}
	
	/**
	 * Returns the value of this codon
	 * 
	 * @return the value of this codon
	 */
	@Override
	public long value() {
		return toLong(bits);
	}
	
	/*
	 * Converts the given sequence of bits to a long
	 */
	private long toLong(boolean[] a) {
	    long result = 0;
	    for (int i = 0; i < a.length; i++) {
	    	long value = (a[i] ? 1 : 0) << i;
	    	result = result | value;
	    }

	    return result;
	}
	
	/**
	 * Returns the bit string representation of this codon's value
	 * 
	 * @return this codon's bit string
	 */
	public boolean[] getBits() {
		return bits;
	}
	
	/**
	 * Returns a string representation of this codon
	 * 
	 * @return a string representation of this codon
	 */
	@Override
	public String toString() {
		return Arrays.toString(bits);
	}
	
}
