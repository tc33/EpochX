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

import static org.epochx.Config.Template.TEMPLATE;
import static org.epochx.RandomSequence.RANDOM_SEQUENCE;
import static org.epochx.ge.Codon.*;
import static org.epochx.ge.CodonFactory.CODON_FACTORY;

import java.util.*;

import org.epochx.Config.ConfigKey;
import org.epochx.*;
import org.epochx.event.*;

/**
 * A chromosome is a sequential, iterable list of Codons. 
 */
public class Chromosome implements Iterable<Codon>, Cloneable, Listener<ConfigEvent> {

	/**
	 * The key for setting and retrieving the maximum length setting for chromosomes
	 */
	public static final ConfigKey<Integer> MAXIMUM_LENGTH = new ConfigKey<Integer>();
	
	/**
	 * The key for setting and retrieving the maximum number of times the codons may be wrapped
	 */
	public static final ConfigKey<Integer> MAXIMUM_WRAPS = new ConfigKey<Integer>();
	
	/**
	 * The key for setting and retrieving whether chromosomes should be allowed to 
	 * themselves extend up to the maximum length setting if there are insufficient
	 * codons
	 */
	public static final ConfigKey<Boolean> ALLOW_EXTENSION = new ConfigKey<Boolean>();
	
	// The chromosome's codons
	private List<Codon> codons;
	
	// Configuration settings
	private RandomSequence random;
	private Long minCodon;
	private Long maxCodon;
	private Long codonRange;
	private Integer maxWraps;
	private Integer maxLength;
	private Boolean extending;
	private CodonFactory codonFactory;

	/**
	 * Constructs a <code>Chromosome</code> with an empty list of codons and control parameters
	 * automatically loaded from the config
	 */
	public Chromosome() {
		this(new ArrayList<Codon>());
	}
	
	/**
	 * Constructs a <code>Chromosome</code> with the given list of codons and control parameters
	 * automatically loaded from the config
	 * 
	 * @param codons the initial list of codons to represent this chromosome
	 */
	public Chromosome(List<Codon> codons) {
		this(codons, true);
	}
	
	/**
	 * Constructs a <code>Chromosome</code> with the given list of codons and control parameters 
	 * initially loaded from the config. If the <code>autoConfig</code> argument is set to
	 * <code>true</code> then the configuration will be automatically updated when the config is 
	 * modified.
	 * 
	 * @param codons the initial list of codons to represent this chromosome
	 * @param autoConfig whether this operator should automatically update its
	 *        configuration settings from the config
	 */
	public Chromosome(List<Codon> codons, boolean autoConfig) {
		this.codons = codons;
		
		// Default config values
		maxCodon = Long.MAX_VALUE;
		minCodon = 0L;
		maxWraps = Integer.MAX_VALUE;
		maxLength = Integer.MAX_VALUE;
		extending = Boolean.FALSE;
		
		setup();

		EventManager.getInstance().add(ConfigEvent.class, this);
	}
	
	/**
	 * Sets up this operator with the appropriate configuration settings.
	 * This method is called whenever a <code>ConfigEvent</code> occurs for a
	 * change in any of the following configuration parameters:
	 * <ul>
	 * <li>{@link RandomSequence#RANDOM_SEQUENCE}
	 * <li>{@link Codon#MAXIMUM_VALUE} (default: <code>Long.MAX_VALUE</code>)
	 * <li>{@link Codon#MINIMUM_VALUE} (default: <code>0L</code>)
	 * <li>{@link Chromosome#MAXIMUM_WRAPS} (default: <code>Integer.MAX_VALUE</code>)
	 * <li>{@link Chromosome#MAXIMUM_LENGTH} (default: <code>Integer.MAX_VALUE</code>)
	 * <li>{@link Chromosome#ALLOW_EXTENSION} (default: <code>False</code>)
	 * <li>{@link CodonFactory#CODON_FACTORY}
	 * </ul>
	 */
	protected void setup() {
		random = Config.getInstance().get(RANDOM_SEQUENCE);
		maxCodon = Config.getInstance().get(MAXIMUM_VALUE, maxCodon);
		minCodon = Config.getInstance().get(MINIMUM_VALUE, minCodon);
		maxWraps = Config.getInstance().get(MAXIMUM_WRAPS, maxWraps);
		maxLength = Config.getInstance().get(MAXIMUM_LENGTH, maxLength);
		extending = Config.getInstance().get(ALLOW_EXTENSION, extending);
		codonFactory = Config.getInstance().get(CODON_FACTORY);
		
		codonRange = maxCodon - minCodon;
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
		if (event.isKindOf(TEMPLATE, RANDOM_SEQUENCE, MAXIMUM_VALUE, MINIMUM_VALUE, MAXIMUM_WRAPS, MAXIMUM_LENGTH, ALLOW_EXTENSION, CODON_FACTORY)) {
			setup();
		}
	}
	
	/**
	 * Appends a codon with a random value to the chromosome. The codon is generated using the
	 * currently set codon factory.
	 */
	public void extend() {
		long value = minCodon + random.nextLong(codonRange);
		
		appendCodon(codonFactory.codon(value));
	}
	
	/**
	 * Returns a list of the chromosome's codons. Modifying the returned list will not effect the chromosome's internal
	 * list, although modifications to the codons themselves may be reflected in the chromosome's codons.
	 * 
	 * @return a list of this chromosome's codons
	 */
	protected List<Codon> getCodons() {
		//TODO Would it be safe to just give access to the underlying list? Does it matter that it's not a deep clone?
		return new ArrayList<Codon>(codons);
	}
	
	
	public Codon getCodon(long index) {
		// If within chromosome size just return the codon
		if (index < codons.size()) {
			return codons.get((int) index);		
		}
		
		// Otherwise need to do some combination of wrapping and extending
		if (extending) {
			while (codons.size() <= index && codons.size() < maxLength) {
				extend();
			}
		} else if (codons.size() == 0) {
			throw new IndexOutOfBoundsException("Index " + index + " outside bounds of chromosome of length " + codons.size());
		}
		
		int wraps = (int) Math.floor(index / codons.size());
		if (wraps > maxWraps) {
			return null;
		}
		
		index = index % codons.size();
		if (index >= codons.size()) {
			return null;
		}

		return codons.get((int) index);
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
	
	public void appendCodons(List<Codon> codons) {
		codons.addAll(codons);
	}


	public int getMaxWraps() {
		return maxWraps;
	}

	public void setMaxWraps(int maxWraps) {
		this.maxWraps = maxWraps;
	}

	public boolean isAutoExtending() {
		return extending;
	}

	public void setAutoExtending(boolean extending) {
		this.extending = extending;
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

			if ((c.extending == extending) && (c.maxWraps == maxWraps) && (c.maxLength == maxLength)) {
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
		//return codons.iterator();
		return new ChromosomeIterator();
	}
	
	private class ChromosomeIterator implements Iterator<Codon> {

		private Codon previous;
		private long nextPosition;
		
		public ChromosomeIterator() {
			nextPosition = 0;
			previous = null;
		}

		/**
		 * Note that if extending and wrapping are used, hasNext will be inconsistent
		 * with the length of the underlying chromosome
		 */
		@Override
		public boolean hasNext() {			
			long lastPosition = codons.size()-1;
			
			if (extending) {
				lastPosition = maxLength-1;
			}
			
			if (maxWraps > 0) {
				lastPosition++;
				lastPosition *= (maxWraps+1);
				lastPosition--;
			}
			
			return (nextPosition <= lastPosition);
		}

		@Override
		public Codon next() {
			if (hasNext()) {
				Codon previous = getCodon(nextPosition++);
				return previous;
			} else {
				throw new NoSuchElementException("There is no next codon");
			}
		}

		@Override
		public void remove() {
			if (previous == null) {
				codons.remove(nextPosition-1);
				previous = null;
			} else {
				throw new IllegalStateException("No previous codon to remove");
			}
		}
	}
}
