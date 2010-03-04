/*  
 *  Copyright 2009 Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of XGE: grammatical evolution for research
 *
 *  XGE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  XGE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with XGE.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.epochx.ge.op.mutation;

import org.epochx.core.Controller;
import org.epochx.ge.codon.CodonGenerator;
import org.epochx.ge.model.GEModel;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.life.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.random.RandomNumberGenerator;

/**
 * This class performs a simple point mutation on a <code>GECandidateProgram</code>.
 * 
 * <p>Each codon in the program's chromosome is considered for mutation, with the 
 * probability of that codon being mutated given as an argument to the 
 * PointMutation constructor. If the codon does undergo mutation then a 
 * replacement codon is generated using the CodonGenerator specified in the model.
 */
public class PointMutation implements GEMutation {

	// The current controlling model.
	private GEModel model;
	
	// The probability each codon has of being mutated in a selected program.
	private double pointProbability;
	
	private RandomNumberGenerator rng;
	private CodonGenerator codonGenerator;
	
	/**
	 * Construct a point mutation with a default point probability of 0.01. It is
	 * generally recommended that the PointMutation(GEModel, double) constructor 
	 * is used instead.
	 * 
	 * @param model The current controlling model. Parameters such as the codon
	 * 				generator to use will come from here.
	 */
	public PointMutation(GEModel model) {
		this(model, 0.01);
	}
	
	/**
	 * Construct a point mutation with user specified point probability.
	 * 
	 * @param model The current controlling model. Parameters such as the codon
	 * 				generator to use will come from here.
	 * @param pointProbability The probability each node in a selected program 
	 * 				has of undergoing a mutation. 1.0 would result in all nodes 
	 * 				being changed, and 0.0 would mean no nodes were changed. A 
	 * 				typical value would be 0.01.
	 */
	public PointMutation(GEModel model, double pointProbability) {
		this.model = model;
		this.pointProbability = pointProbability;
		
		Controller.getLifeCycleManager().addGenerationListener(new GenerationAdapter() {
			@Override
			public void onGenerationStart() {
				initialise();
			}
		});
	}
		
	/**
	 * Perform point mutation on the given GECandidateProgram. Each codon in the 
	 * program's chromosome is considered in turn, with each having the given 
	 * probability of actually being exchanged. Given that a codon is chosen 
	 * then a new codon is generated using the model's CodonGenerator to 
	 * replace it.
	 * 
	 * @param program The GECandidateProgram selected to undergo this mutation 
	 * 				  operation.
	 * @return A GECandidateProgram that was the result of a point mutation on 
	 * the provided GECandidateProgram.
	 */
	@Override
	public GECandidateProgram mutate(CandidateProgram p) {
		GECandidateProgram program = (GECandidateProgram) p;
		
		int noCodons = program.getNoCodons();
		
		for (int i=0; i<noCodons; i++) {
			// Perform a point mutation at the ith node, pointProbability% of time.
			if (rng.nextDouble() < pointProbability) {
				int c = codonGenerator.getCodon();
				program.setCodon(i, c);
			}
		}

		return program;
	}

	public void initialise() {
		rng = model.getRNG();
		codonGenerator = model.getCodonGenerator();
	}

}
