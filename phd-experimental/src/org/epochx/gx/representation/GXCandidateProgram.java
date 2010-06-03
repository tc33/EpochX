/* 
 * Copyright 2007-2010 Tom Castle & Lawrence Beadle
 * Licensed under GNU General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.gx.representation;

import org.epochx.gx.model.*;
import org.epochx.representation.*;

public class GXCandidateProgram extends CandidateProgram {

	private GXModel model;
	
	// The abstract syntax tree.
	private Program program;
	
	// The fitness of the phenotype.
	private double fitness;
	
	// A stash of the source for testing if fitness cache is up to date.
	private String sourceCache;
	
	public GXCandidateProgram(GXModel model) {
		this(null, model);
	}
	
	public GXCandidateProgram(Program program, GXModel model) {
		this.model = model;
		this.program = program;
		
		sourceCache = null;
		
		// Initialise the fitness to -1 until we are asked to calculate it.
		fitness = -1;
	}
	
	public void setParseTree(Program program) {
		this.program = program;
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
	
	@Override
	public boolean isValid() {
		boolean valid = true;
		
		/*int maxProgramDepth = model.getMaxDepth();
		
		if ((maxProgramDepth != -1)
				&& (getDepth() > maxProgramDepth)) {
			valid = false;
		}*/
		
		return valid;
	}

	public String getSourceCode() {
		return program.toString();
	}
	
	public Program getAST() {
		return program;
	}
	
	/*public int getDepth() {
		return parseTree.getDepth();
	}*/
	
	/**
	 * Create a clone of this GXCandidateProgram.
	 * 
	 * @return a copy of this GXCandidateProgram instance.
	 */
	@Override
	public CandidateProgram clone() {
		//TODO This needs writing properly.
		GXCandidateProgram clone = (GXCandidateProgram) super.clone();
				
		// If codons are the same then the source and fitness should be the same.
		clone.program = (Program) this.program.clone();
		
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
		if (program != null) {
			return program.toString();
		} else {
			return null;
		}
	}
	
	/**
	 * Compares the given argument for equivalence to this GXCandidateProgram.
	 * Two GX candidate programs are equal if they have equal syntax.
	 * 
	 * @return true if the object is an equivalent candidate program, false 
	 * otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		
		if (o instanceof GXCandidateProgram) {
			GXCandidateProgram prog = (GXCandidateProgram) o;
			
			return prog.toString().equals(this.toString());
		} else {
			return false;
		}
	}
}
