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

import static org.epochx.RandomSequence.RANDOM_SEQUENCE;
import static org.epochx.ge.BinaryCodon.BITS;

import org.epochx.*;
import org.epochx.event.*;

/**
 * 
 */
public class BinaryChromosome extends Chromosome implements Listener<ConfigEvent> {

	private RandomSequence random;
	
	private int noBits;
	
	public BinaryChromosome() {
		setup();

		EventManager.getInstance().add(ConfigEvent.class, this);
	}
	
	/**
	 * Sets up this operator with the appropriate configuration settings.
	 * This method is called whenever a <tt>ConfigEvent</tt> occurs for a
	 * change in any of the following configuration parameters:
	 * <ul>
	 * <li>{@link RandomSequence#RANDOM_SEQUENCE}
	 * <li>{@link BinaryCodon#BITS} (default: <tt>8</tt>)
	 * </ul>
	 */
	protected void setup() {
		random = Config.getInstance().get(RANDOM_SEQUENCE);
		noBits = Config.getInstance().get(BITS, 8);
	}
	
	/**
	 * Receives configuration events and triggers this operator to configure its
	 * parameters if the <tt>ConfigEvent</tt> is for one of its required
	 * parameters.
	 * 
	 * @param event {@inheritDoc}
	 */
	@Override
	public void onEvent(ConfigEvent event) {
		if (event.isKindOf(RANDOM_SEQUENCE, BITS)) {
			setup();
		}
	}
	
	@Override
	protected Codon generateCodon() {
		boolean[] bits = new boolean[noBits];
		
		for (int i=0; i<noBits; i++) {
			bits[i] = random.nextBoolean();
		}
		
		return new BinaryCodon(bits);
	}

}
