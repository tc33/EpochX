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
import org.epochx.grammar.Grammar;

/**
 * A chromosome is a sequential, iterable list of {@link Codon}s, which are used to represent
 * an individual solution in Grammatical Evolution. Each codon has a numeric value, but otherwise
 * may be represented in any format, for example, as an integer or a binary string. In the standard
 * Grammatical Evolution algorithm, an individual's chromosome is used by a {@link Mapper} to 
 * generate a parse tree with reference to a problem specific {@link Grammar}.
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
	 * extend themselves up to the maximum length setting if there are insufficient
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
	
	/**
	 * Returns the codon at the given index. If the index is beyond the length of the chromosome and 
	 * extending is enabled then the chromosome will be extended up to the index, or to the maximum
	 * length setting, whichever is smaller. If the index is still not within the length of the 
	 * chromosome and wrapping is enabled then the codons will be wrapped up to the maximum wraps 
	 * setting. If the index is not reachable even with wrapping and extending then null is returned.
	 * 
	 * @param index the codon to return
	 * @return the codon at the specified index
	 */
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

	/**
	 * Replaces the codon at the specified index. Wrapping and extending are not used, so the
	 * maximum index is the number of codons minus one.
	 * 
	 * @param index the index of the codon to replace
	 * @param codon the codon to set
	 */
	public void setCodon(int index, Codon codon) {
		codons.set(index, codon);
	}

	/**
	 * Returns the number of codons in the chromosome
	 * 
	 * @return the size of the chromosome
	 */
	public int length() {
		return codons.size();
	}

	/**
	 * Removes and returns the codon at the specified index. The index position will 
	 * not be wrapped and codon length will not be extended.
	 * 
	 * @param index the index of the codon to remove
	 * @return the codon that was removed
	 */
	public Codon removeCodon(int index) {
		return codons.remove(index);
	}

	/**
	 * Removes a range of codons between the given indices and returns a list of the
	 * codons that are removed. The index positions will not be wrapped and codon length 
	 * will not be extended.
	 * 
	 * @param from the starting index (inclusive)
	 * @param to the upper index (exclusive)
	 * @return a list of the codons that were removed
	 */
	public List<Codon> removeCodons(int from, int to) {
		List<Codon> removed = new ArrayList<Codon>();
		for (int i = from; i < to; i++) {
			// As we remove them the list will shrink so we use from not i.
			removed.add(codons.remove(from));
		}

		return removed;
	}

	/**
	 * Appends the given codon to the end of the chromosome
	 * 
	 * @param codon the codon to append
	 */
	public void appendCodon(Codon codon) {
		codons.add(codon);
	}
	
	/**
	 * Appends multiple codons to the end of the chromosome
	 * 
	 * @param codons a list of codons to append
	 */
	public void appendCodons(List<Codon> codons) {
		codons.addAll(codons);
	}

	/**
	 * Returns the maximum number of wraps allowed
	 * 
	 * @return the maximum wraps setting
	 */
	public int getMaxWraps() {
		return maxWraps;
	}

	/**
	 * Sets the maximum number of times the codons can be wrapped. If automatic configuration 
	 * is enabled then any value set here will be overwritten by the 
	 * {@link Chromosome#MAXIMUM_WRAPS} configuration setting on the next config event.
	 * 
	 * @param maxWraps the maximum number of times to allow the codons to wrap
	 */
	public void setMaxWraps(int maxWraps) {
		this.maxWraps = maxWraps;
	}

	/**
	 * Returns true if auto-extending is enabled and false otherwise
	 * 
	 * @return true if auto-extending is enabled and false otherwise
	 */
	public boolean isAutoExtending() {
		return extending;
	}

	/**
	 * Enables or disables auto-extending of the chromosome. If automatic configuration is 
	 * enabled then any value set here will be overwritten by the 
	 * {@link Chromosome#ALLOW_EXTENSION} configuration setting on the next config event.
	 * 
	 * @param extending true if auto-extending should be enabled or false to disable it
	 */
	public void setAutoExtending(boolean extending) {
		this.extending = extending;
	}
	
	/**
	 * @return the random
	 */
	public RandomSequence getRandom() {
		return random;
	}

	/**
	 * @param random the random to set
	 */
	public void setRandom(RandomSequence random) {
		this.random = random;
	}

	/**
	 * @return the minCodon
	 */
	public Long getMinCodon() {
		return minCodon;
	}

	/**
	 * @param minCodon the minCodon to set
	 */
	public void setMinCodon(Long minCodon) {
		this.minCodon = minCodon;
		
		codonRange = maxCodon - minCodon;
	}

	/**
	 * @return the maxCodon
	 */
	public Long getMaxCodon() {
		return maxCodon;
	}

	/**
	 * @param maxCodon the maxCodon to set
	 */
	public void setMaxCodon(Long maxCodon) {
		this.maxCodon = maxCodon;
		
		codonRange = maxCodon - minCodon;
	}

	/**
	 * @return the maxLength
	 */
	public Integer getMaxLength() {
		return maxLength;
	}

	/**
	 * @param maxLength the maxLength to set
	 */
	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * @return the codonFactory
	 */
	public CodonFactory getCodonFactory() {
		return codonFactory;
	}

	/**
	 * @param codonFactory the codonFactory to set
	 */
	public void setCodonFactory(CodonFactory codonFactory) {
		this.codonFactory = codonFactory;
	}


	/**
	 * Creates and returns a clone of this chromosome. The internal list of codons is cloned.
	 * 
	 * @return a copy of this chromosome
	 */
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

	/**
	 * Returns a string representation of this chromosome's list of codons
	 * 
	 * @return a string representation of the list of codons
	 */
	@Override
	public String toString() {
		return codons.toString();
	}

	/**
	 * Compares the given object to this instance for equality. Equivalence is
	 * defined as them both being instances of <code>Chromosome</code> and
	 * having equal lists of codons. The two chromosomes must also have the same
	 * settings for allowing extending, maximum wraps and maximum length.
	 * 
	 * @return true if the object is an equivalent chromosome, false otherwise
	 */
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
	
	/*
	 * An iterator for looping through a chromosome's codons
	 */
	private class ChromosomeIterator implements Iterator<Codon> {

		private Codon previous;
		private long nextPosition;
		
		/**
		 * Constructs a <code>ChromosomeIterator</code> with the current position set
		 * as the first codon in the chromosome
		 */
		public ChromosomeIterator() {
			nextPosition = 0;
			previous = null;
		}

		/**
		 * Returns true if the current position is not beyond the end of the chromosome. If extending 
		 * or wrapping are enabled then it will make use of them and therefore will be inconsistent 
		 * with the length of the underlying chromosome.
		 * 
		 * @return true if a codon is available at the current position and false otherwise
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

		/**
		 * Returns the next codon and increments the index of the current codon
		 * 
		 * @return the next codon if there is one
		 * @throws NoSuchElementException if there is not a next codon 
		 */
		@Override
		public Codon next() {
			if (hasNext()) {
				Codon previous = getCodon(nextPosition++);
				return previous;
			} else {
				throw new NoSuchElementException("There is no next codon");
			}
		}

		/**
		 * Removes the current codon from the chromosome
		 * 
		 * @throws NoSuchElementException if there is no codon to remove, such as if the 
		 * chromosome is empty or if the <code>next()</code> method has not been called
		 * yet
		 */
		@Override
		public void remove() {
			if (previous != null) {
				codons.remove(nextPosition-1);
				previous = null;
			} else {
				throw new NoSuchElementException("No current codon to remove");
			}
		}
	}
}
