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

import org.apache.commons.lang.ObjectUtils;
import org.epochx.*;
import org.epochx.Config.ConfigKey;
import org.epochx.grammar.*;

/**
 * A <code>GEIndividual</code> is a candidate solution which is represented by a
 * list of {@link Codon}s. 
 * 
 * <p>
 * Note: this class has a natural ordering that may be inconsistent with
 * <code>equals</code>.
 * 
 * @since 2.0
 */
public class GEIndividual extends AbstractIndividual {

	private static final long serialVersionUID = -6028225391868926598L;
	
	/**
	 * The key for setting and retrieving the maximum depth for parse trees
	 */
	public static final ConfigKey<Integer> MAXIMUM_DEPTH = new ConfigKey<Integer>();
	
	// The individual's genotype
	private Chromosome chromosome;
	
	// The individual's phenotype
	private NonTerminalSymbol parseTree;

	/**
	 * Constructs a GE individual with an initial chromosome of <code>null</code>
	 */
	public GEIndividual() {
		this(null);
	}
	
	/**
	 * Constructs a GE individual represented by the given chromosome.
	 * 
	 * @param chromosome the initial set of codons
	 */
	public GEIndividual(Chromosome chromosome) {
		this.chromosome = chromosome;
	}

	/**
	 * Returns the <code>Chromosome</code> that defines this individual
	 * 
	 * @return this individual's chromosome
	 */
	public Chromosome getChromosome() {
		return chromosome;
	}
	
	/**
	 * Returns the root <code>NonTerminalSymbol</code> of the parse tree, if it has 
	 * been set. Otherwise <code>null</code> is returned.
	 * 
	 * @return the root of the parse tree, or <code>null</code> if it has not been
	 * set
	 */
	public NonTerminalSymbol getParseTree() {
		return parseTree;
	}
	
	/**
	 * Sets the <code>NonTerminalSymbol</code> that is the root node of the parse
	 * tree that represents this individual. Typically this would be derived 
	 * from the chromosome.
	 * 
	 * @param parseTree the root of the parse tree that represents this 
	 * individual
	 */
	public void setParseTree(NonTerminalSymbol parseTree) {
		this.parseTree = parseTree;
	}

	/**
	 * Creates and returns a clone of this individual. The chromosome is copied as 
	 * is the parse tree and fitness.
	 * 
	 * @return a copy of this GEIndividual instance.
	 */
	@Override
	public GEIndividual clone() {
		GEIndividual clone = (GEIndividual) super.clone();
		clone.chromosome = chromosome.clone();

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
	 * Returns a string representation of this program. This will be a string
	 * representation of the parse tree if it has been set, otherwise it will be
	 * a string representation of the chromosome.
	 */
	@Override
	public String toString() {
		if (parseTree != null) {
			return parseTree.toString();
		}
		
		return chromosome.toString();
	}

	/**
	 * Compares the given object to this instance for equality. Equivalence is
	 * defined as them both being instances of <code>GEIndividual</code> and
	 * having equal parse trees, according to <code>getParseTree().equals(obj)</code>.
	 * If either individual's parse tree is <code>null</code>, then the chromosomes
	 * must be equal for the individuals to be considered equal.
	 * 
	 * @return true if the object is an equivalent candidate program, false
	 *         otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		GEIndividual prog = (GEIndividual) o;

		Symbol thisParseTree = parseTree;
		Symbol progParseTree = prog.parseTree;

		if ((thisParseTree == null) || (progParseTree == null)) {
			// Compare genotypes.
			return chromosome.equals(prog.chromosome);
		} else {
			// Compare phenotypes.
			return ObjectUtils.equals(thisParseTree, progParseTree);
		}
	}
	
	/**
	 * Compares this individual to another based on their fitness. It returns a
	 * negative integer, zero, or a positive integer as this instance represents
	 * the quality of an individual that is less fit, equally fit, or more fit
	 * than the specified object. The individuals do not need to be of the same
	 * object type, but must have non-null, comparable <code>Fitness</code> instances.
	 * 
	 * @param other an individual to compare against
	 * @return a negative integer, zero, or a positive integer as this object is
	 *         less fit than, equally fit as, or fitter than the specified
	 *         object
	 */
	@Override
	public int compareTo(Individual other) {
		return getFitness().compareTo(other.getFitness());
	}
}
