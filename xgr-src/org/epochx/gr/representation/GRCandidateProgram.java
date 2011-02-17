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
package org.epochx.gr.representation;

import org.epochx.representation.*;
import org.epochx.tools.grammar.NonTerminalSymbol;

/**
 * <p>
 * Note: this class has a natural ordering that is inconsistent with equals.
 */
public class GRCandidateProgram extends CandidateProgram {

	// The phenotype.
	private NonTerminalSymbol parseTree;
	
	public GRCandidateProgram(NonTerminalSymbol parseTree) {
		this.parseTree = parseTree;
	}
	
	public void setParseTree(final NonTerminalSymbol parseTree) {
		this.parseTree = parseTree;
	}

	public String getSourceCode() {
		return parseTree.toString();
	}

	public NonTerminalSymbol getParseTree() {
		return parseTree;
	}

	public int getDepth() {
		return parseTree.getDepth();
	}

	/**
	 * Create a clone of this GECandidateProgram. The list of codons are copied
	 * as are all caches.
	 * 
	 * @return a copy of this GECandidateProgram instance.
	 */
	@Override
	public CandidateProgram clone() {
		final GRCandidateProgram clone = (GRCandidateProgram) super.clone();

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
		if (parseTree != null) {
			return parseTree.toString();
		} else {
			return null;
		}
	}

	/**
	 * Compares the given argument for equivalence to this GECandidateProgram.
	 * Two GR candidate programs are equal if they have equal syntax regardless
	 * of grammar rules used or if both have a null parse tree.
	 * 
	 * @return true if the object is an equivalent candidate program, false
	 *         otherwise.
	 */
	@Override
	public boolean equals(final Object o) {
		if (o instanceof GRCandidateProgram) {
			final GRCandidateProgram prog = (GRCandidateProgram) o;

			return prog.toString().equals(toString());
		} else {
			return false;
		}
	}
}
