/* 
 * Copyright 2007-2013
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX
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
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.ge;

import static org.epochx.Config.Template.TEMPLATE;
import static org.epochx.RandomSequence.RANDOM_SEQUENCE;
import static org.epochx.ge.BinaryCodon.NO_BITS;

import java.util.ArrayList;
import java.util.List;

import org.epochx.Config;
import org.epochx.RandomSequence;
import org.epochx.event.ConfigEvent;
import org.epochx.event.EventManager;
import org.epochx.event.Listener;

/**
 * 
 */
public class BinaryCodonFactory implements CodonFactory, Listener<ConfigEvent> {
	
	private RandomSequence random;
	private int noBits;
	
	public BinaryCodonFactory() {
		this(true);
	}
	
	public BinaryCodonFactory(boolean autoConfig) {
		setup();

		if (autoConfig) {
			EventManager.getInstance().add(ConfigEvent.class, this);
		}
	}
	
	/**
	 * Sets up this operator with the appropriate configuration settings.
	 * This method is called whenever a <code>ConfigEvent</code> occurs for a
	 * change in any of the following configuration parameters:
	 * <ul>
	 * <li>{@link RandomSequence#RANDOM_SEQUENCE}
	 * </ul>
	 */
	protected void setup() {
		random = Config.getInstance().get(RANDOM_SEQUENCE);
		noBits = Config.getInstance().get(NO_BITS);
	}

	/**
	 * Receives configuration events and triggers this operator to configure its
	 * parameters if the <code>ConfigEvent</code> is for one of its required
	 * parameters.
	 * 
	 * @param event {@inheritDoc}
	 */
	@Override
	public void onEvent(ConfigEvent event) {
		if (event.isKindOf(TEMPLATE, RANDOM_SEQUENCE, NO_BITS)) {
			setup();
		}
	}
	
	
	/**
	 * Generates a random BinaryCodon
	 * 
	 * @return a new random binary codon
	 */
	@Override
	public Codon codon() {
		boolean[] bits = new boolean[noBits];
		
		for (int i=0; i<noBits; i++) {
			bits[i] = random.nextBoolean();
		}
		
		return new BinaryCodon(bits);
	}
	
	/**
	 * Generates a new BinaryCodon with the given value
	 * 
	 * @param value the value to assign to the new codon
	 * @return a new codon instance
	 */
	@Override
	public Codon codon(long value) {
		boolean[] bits = new boolean[noBits];
		
	    for (int i = (bits.length-1); i >= 0; i--) {
	        bits[i] = (value & (1 << i)) != 0;
	    }
	    
	    return new BinaryCodon(bits);
	}
	
	/**
	 * Converts the given chromosome to a boolean array of bits, where each codon in the 
	 * chromosome is represented by NO_BITS. This method only works on chromosomes which 
	 * contain only <code>BinaryCodon</code>s.
	 * 
	 * @param chromosome the chromosome to convert to bits
	 * @return a boolean array representing the bits of the chromosome
	 * @throws IllegalArgumentException if any of the codons in the chromosome are not an
	 * instance of <code>BinaryBinary</code>
	 */
	public static boolean[] chromosomeToBits(Chromosome chromosome) {
		List<Boolean> bits = new ArrayList<Boolean>();
		List<Codon> codons = chromosome.getCodons();
		
		for (Codon codon: codons) {
			if (! (codon instanceof BinaryCodon)) {
				throw new IllegalArgumentException("Chromosome must contain only binary codons, found " + codon.getClass().getName());
			}
			
			BinaryCodon binaryCodon = (BinaryCodon) codon;
			boolean[] codonBits = binaryCodon.getBits();
			for (boolean bit: codonBits) {
				bits.add(bit);
			}
		}
		
		return toPrimitiveArray(bits);
	}

	/*
	 * Converts the given list of Booleans to an array of primitive booleans
	 */
	private static boolean[] toPrimitiveArray(List<Boolean> booleanList) {
	    boolean[] primitives = new boolean[booleanList.size()];
	    
	    int index = 0;
	    for (Boolean object : booleanList) {
	        primitives[index++] = object;
	    }
	    
	    return primitives;
	}

}
