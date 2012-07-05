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
import static org.epochx.ge.Codon.*;

import java.util.*;

import org.epochx.Config.ConfigKey;
import org.epochx.*;
import org.epochx.event.*;

/**
 * 
 */
public abstract class Chromosome implements Iterable<Codon>, Cloneable, Listener<ConfigEvent> {

	/**
	 * The key for setting and retrieving the maximum length setting for 
	 * chromosomes
	 */
	public static final ConfigKey<Integer> MAXIMUM_LENGTH = new ConfigKey<Integer>();
	
	/**
	 * The key for setting and retrieving the maximum number of times the 
	 * codons may be wrapped
	 */
	public static final ConfigKey<Integer> MAXIMUM_WRAPS = new ConfigKey<Integer>();
	
	private List<Codon> codons;

	// These are mutually exclusive - one or the other or neither.
	private boolean wrapping;
	private boolean extending;
	
	private RandomSequence random;
	
	private long minCodon;
	private long maxCodon;
	private long codonRange;

	public Chromosome() {
		this(new ArrayList<Codon>());
	}
	
	public Chromosome(List<Codon> codons) {
		this.codons = codons;

		wrapping = false;
		extending = true;
		
		setup();

		EventManager.getInstance().add(ConfigEvent.class, this);
	}
	
	/**
	 * Sets up this operator with the appropriate configuration settings.
	 * This method is called whenever a <tt>ConfigEvent</tt> occurs for a
	 * change in any of the following configuration parameters:
	 * <ul>
	 * <li>{@link RandomSequence#RANDOM_SEQUENCE}
	 * <li>{@link IntegerCodon#MAXIMUM_VALUE} (default: <tt>Long.MAX_VALUE</tt>)
	 * <li>{@link IntegerCodon#MINIMUM_VALUE} (default: <tt>0L</tt>)
	 * </ul>
	 */
	protected void setup() {
		random = Config.getInstance().get(RANDOM_SEQUENCE);
		maxCodon = Config.getInstance().get(MAXIMUM_VALUE, Long.MAX_VALUE);
		minCodon = Config.getInstance().get(MINIMUM_VALUE, 0L);
		
		codonRange = maxCodon - minCodon;
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
		if (event.isKindOf(RANDOM_SEQUENCE, MAXIMUM_VALUE, MINIMUM_VALUE)) {
			setup();
		}
	}
	
	public abstract Codon generateCodon(long value);
	
	public void extend() {
		long value = minCodon + random.nextLong(codonRange);
		
		appendCodon(generateCodon(value));
	}
	
	public Codon getCodon(int index) {
		if (wrapping) {
			index = index % codons.size();
		} else if (index >= codons.size()) {
			return null;
		}

		return codons.get(index);
	}

	public void setCodon(int index, Codon codon) {
		codons.set(index, codon);
	}

	public int length() {
		return codons.size();
	}

	public Codon removeCodon(int index) {
		return codons.remove(index);
	}

	public List<Codon> removeCodons(int from, int to) {
		List<Codon> removed = new ArrayList<Codon>();
		for (int i = from; i < to; i++) {
			// As we remove them the list will shrink so we use from not i.
			removed.add(codons.remove(from));
		}

		return removed;
	}

	public void appendCodon(Codon codon) {
		codons.add(codon);
	}

	public boolean isWrapping() {
		return wrapping;
	}

	public void setWrapping(boolean wrap) {
		wrapping = wrap;

		if (wrapping) {
			extending = false;
		}
	}

	public boolean isAutoExtending() {
		return extending;
	}

	public void setAutoExtending(boolean extending) {
		this.extending = extending;

		if (extending) {
			wrapping = false;
		}
	}

	@Override
	public Chromosome clone() {
		Chromosome clone = null;
		try {
			clone = (Chromosome) super.clone();
			// This assumes codons are immutable
			clone.codons = new ArrayList<Codon>(codons);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		return clone;
	}

	@Override
	public String toString() {
		return codons.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Chromosome) {
			Chromosome c = (Chromosome) obj;

			if ((c.extending == extending) && (c.wrapping == wrapping)) {
				return codons.equals(c.codons);
			}
		}

		return false;
	}
	
	/**
	 * Returns an iterator over the codons in this chromosome
	 * 
	 * @return an iterator over the codons in this chromosome
	 */
	@Override
	public Iterator<Codon> iterator() {
		//TODO This isn't right - it should wrap if wrapping is enabled
		return codons.iterator();
	}
}
