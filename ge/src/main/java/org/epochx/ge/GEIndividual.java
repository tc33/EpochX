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

import org.apache.commons.lang.ObjectUtils;
import org.epochx.*;
import org.epochx.Config.ConfigKey;
import org.epochx.grammar.*;

/**
 * A <tt>GEIndividual</tt> encapsulates an individual program within a
 * generation of a GE run.
 * 
 * <p>
 * The main components of a GEIndividual are a series of codons, which are
 * the individual's genotype; a source string in someway derived from the codons
 * based upon a grammar, which is the individuals phenotype; and a fitness score
 * calculated by the model that is controlling execution.
 * 
 * <p>
 * To ensure caching can provide maximum performance, the source code and
 * fitness of a program should always be obtained directly from the program
 * instance itself, with a call to <tt>getSourceCode()</tt> or
 * <tt>getFitness()</tt>, rather than forcing the model or otherwise to
 * calculate them on each call.
 * 
 * <p>
 * Note: this class has a natural ordering that is inconsistent with equals.
 */
public class GEIndividual implements Individual {

	private static final long serialVersionUID = -6028225391868926598L;
	
	/**
	 * The key for setting and retrieving the maximum depth for parse trees
	 */
	public static final ConfigKey<Integer> MAXIMUM_DEPTH = new ConfigKey<Integer>();
	
	private Fitness fitness;
	
	private Chromosome chromosome;

	// The phenotype cache.
	private NonTerminalSymbol parseTree;

	// How many of the codons are actually used during mapping.
	private int noActiveCodons;

	public GEIndividual() {
		this(null);
	}

	public GEIndividual(Chromosome chromosome) {
		this.chromosome = chromosome;
	}

	public Chromosome getChromosome() {
		return chromosome;
	}

	public int getParseTreeDepth() {
		int depth = 0;

		if (parseTree != null) {
			depth = parseTree.getDepth();
		}

		return depth;
	}
	
	public NonTerminalSymbol getParseTree() {
		return parseTree;
	}
	
	public void setParseTree(NonTerminalSymbol parseTree) {
		this.parseTree = parseTree;
	}

	/**
	 * Returns a source code string that is the phenotype to this candidate
	 * program's genotypic codon string. This method should always be called
	 * over calling the mapper directly as it may be able to perform caching.
	 * If caching is in use then the source code will never be generated twice
	 * for the same codon string.
	 * 
	 * <p>
	 * The source code is generated by performing mapping using the mapper
	 * specified in the controlling model.
	 * 
	 * @return the source code of this candidate program.
	 */
//	public String getSourceCode() {
//		// If we have not already done the mapping then do it now and stash it.
//		if (!model.cacheSource() || !codons.equals(sourceCacheCodons)) {
//			// May be null still after if the map was invalid.
//			parseTree = model.getMapper().map(this);
//			// Update the number of codons that were used in mapping.
//			noActiveCodons = model.getMapper().getNoMappedCodons();
//		}
//
//		return (parseTree == null) ? null : parseTree.toString();
//	}

	/**
	 * Returns the number of codons that are actually active. That is, the
	 * number of codons that are actually used in the mapping process to
	 * convert from chromosome to source code.
	 * 
	 * @return the number of active codons this program has.
	 */
	public int getNoActiveCodons() {
		return noActiveCodons;
	}

	/**
	 * Create a clone of this GEIndividual. The list of codons are copied
	 * as are all caches.
	 * 
	 * @return a copy of this GEIndividual instance.
	 */
	@Override
	public GEIndividual clone() {
		GEIndividual clone = null;
		try {
			clone = (GEIndividual) super.clone();
			clone.chromosome = chromosome.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		// If codons are the same then the source and fitness should be the
		// same.
		if (parseTree == null) {
			clone.parseTree = null;
		} else {
			clone.parseTree = parseTree.clone();
		}

		return clone;
	}

	/**
	 * Returns a string representation of this program. This will be either the
	 * genotype or the phenotype, depending on whether the program has
	 * undergone mapping or not.
	 */
	@Override
	public String toString() {
		return chromosome.toString();
	}

	/**
	 * Compares the given argument for equivalence to this GEIndividual.
	 * Two candidate programs are equal if they have equal genotypes, or if
	 * they have equal (but non-null) phenotypes.
	 * 
	 * @return true if the object is an equivalent candidate program, false
	 *         otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		GEIndividual prog = (GEIndividual) o;

		Symbol thisParseTree = parseTree;
		Symbol progParseTree = prog.parseTree;

		if ((thisParseTree == null) && (progParseTree == null)) {
			// Compare genotypes.
			return chromosome.equals(prog.chromosome);
		} else {
			// Compare phenotypes.
			return ObjectUtils.equals(thisParseTree, progParseTree);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Fitness getFitness() {
		return fitness;
	}

	/**
	 * Sets the fitness of this individual. The fitness is used as the basis of
	 * comparison between individuals.
	 * 
	 * @param fitness the fitness value to set
	 * @see #compareTo(Individual)
	 */
	public void setFitness(Fitness fitness) {
		this.fitness = fitness;
	}
	
	/**
	 * Compares this individual to another based on their fitness. It returns a
	 * negative integer, zero, or a positive integer as this instance represents
	 * the quality of an individual that is less fit, equally fit, or more fit
	 * than the specified object. The individuals do not need to be of the same
	 * object type, but must have comparable <tt>Fitness</tt> instances.
	 * 
	 * @param other an individual to compare against
	 * @return a negative integer, zero, or a positive integer as this object is
	 *         less fit than, equally fit as, or fitter than the specified
	 *         object
	 */
	@Override
	public int compareTo(Individual other) {
		return fitness.compareTo(other.getFitness());
	}
}