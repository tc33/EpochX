package org.epochx.gr.representation;

import org.epochx.gr.model.GRModel;
import org.epochx.representation.*;
import org.epochx.tools.grammar.*;

public class GRCandidateProgram extends CandidateProgram {

	private GRModel model;
	
	// The phenotype.
	private NonTerminalSymbol parseTree;
	
	// The fitness of the phenotype.
	private double fitness;
	
	// A stash of the source for testing if fitness cache is up to date.
	private String sourceCache;
	
	public GRCandidateProgram(GRModel model) {
		this(null, model);
	}
	
	public GRCandidateProgram(NonTerminalSymbol parseTree, GRModel model) {
		this.model = model;
		this.parseTree = parseTree;
		
		sourceCache = null;
		
		// Initialise the fitness to -1 until we are asked to calculate it.
		fitness = -1;
	}
	
	@Override
	public double getFitness() {
		// Only get the source code if caching to avoid overhead otherwise.
		String source = null;
		if (model.cacheFitness()) {
			source = getSourceCode();
		}
		
		// If we're not caching or the cache is out of date.
		if (!model.cacheFitness() || !source.equals(sourceCache)) {
			fitness = model.getFitness(this);
			sourceCache = source;
		}
		
		return fitness;
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
		//TODO This needs writing properly.
		GRCandidateProgram clone = (GRCandidateProgram) super.clone();
				
		// If codons are the same then the source and fitness should be the same.
		if (this.parseTree == null) {
			clone.parseTree = null;
		} else {
			clone.parseTree = (NonTerminalSymbol) this.parseTree.clone();
		}
		
		// Copy the caches.
		clone.sourceCache = this.sourceCache;
		clone.fitness = this.fitness;
		
		// Shallow copy the model.
		clone.model = this.model;
		
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
	 * Two candidate programs are equal if they have equal genotypes, or if 
	 * they have equal (but non-null) phenotypes.
	 * 
	 * @return true if the object is an equivalent candidate program, false 
	 * otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		//TODO Should really be checking the datatype of o first.
		GRCandidateProgram prog = (GRCandidateProgram) o;
		
		Symbol thisParseTree = this.parseTree;
		Symbol progParseTree = prog.parseTree;
		
		if (thisParseTree == null && progParseTree == null) {
			return true;
		} else {
			return thisParseTree.equals(progParseTree);
		}
	}
}
