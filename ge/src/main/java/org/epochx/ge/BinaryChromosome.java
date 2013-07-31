/*
 * Copyright 2007-2011 Tom Castle & Lawrence Beadle
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

import java.util.*;

import org.epochx.RandomSequence;
import org.epochx.Config.ConfigKey;

/**
 * 
 */
public class BinaryChromosome extends Chromosome<BinaryCodon> {

	/**
	 * The key for setting and retrieving the number of bits that constitute a
	 * codon
	 */
	public static final ConfigKey<Boolean> NO_CODON_BITS = new ConfigKey<Boolean>();
	
	private RandomSequence random;
	
	private int noBits;
	
	public BinaryChromosome() {
		super();
	}
	
	public BinaryChromosome(List<BinaryCodon> codons) {
		super(codons);
	}
	
	@Override
	public BinaryCodon generateCodon(long value) {
		boolean[] bits = new boolean[noBits];
		
		for (int i=0; i<noBits; i++) {
			bits[i] = random.nextBoolean();
		}
		
		return new BinaryCodon(bits);
	}
	
	public boolean[] bits() {
		List<Boolean> bits = new ArrayList<Boolean>(noBits * length());
		List<BinaryCodon> codons = getCodons();
		
		for (BinaryCodon codon: codons) {
			boolean[] codonBits = codon.getBits();
			for (boolean bit: codonBits) {
				bits.add(bit);
			}
		}
		
		return toPrimitiveArray(bits);
	}

	private boolean[] toPrimitiveArray(List<Boolean> booleanList) {
	    boolean[] primitives = new boolean[booleanList.size()];
	    
	    int index = 0;
	    for (Boolean object : booleanList) {
	        primitives[index++] = object;
	    }
	    
	    return primitives;
	}
}
