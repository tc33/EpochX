package org.epochx.gr.representation;

import org.epochx.gr.core.GRModel;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.grammar.Symbol;

public class GRCandidateProgram extends CandidateProgram {

	private GRModel model;
	
	// The phenotype.
	private Symbol parseTree;
	
	public GRCandidateProgram(GRModel model) {
		this(null, model);
	}
	
	public GRCandidateProgram(Symbol parseTree, GRModel model) {
		this.model = model;
		this.parseTree = parseTree;
	}
	
	@Override
	public double getFitness() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getSourceCode() {
		return parseTree.toString();
	}
	
	public Symbol getParseTree() {
		return parseTree;
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
			clone.parseTree = (Symbol) this.parseTree.clone();
		}
		
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
