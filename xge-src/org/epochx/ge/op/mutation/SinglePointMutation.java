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
 *//* 
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
package org.epochx.ge.op.mutation;

import org.epochx.ge.codon.CodonGenerator;
import org.epochx.ge.model.GEModel;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.life.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.random.RandomNumberGenerator;

/**
 * This class performs a single point mutation on a <code>GECandidateProgram</code>.
 * 
 * <p>Whereas a standard PointMutation will consider each codon in the selected 
 * program for mutation, and could potentially mutate any number of codons from 
 * 0 to the number of codons, SinglePointMutation will always mutate exactly 
 * one codon in a program that it is asked to mutate. If the codon does undergo 
 * mutation then a replacement codon is generated using the CodonGenerator 
 * specified in the model.
 */
public class SinglePointMutation implements GEMutation {

	// The current controlling model.
	private GEModel model;
	
	// Operator statistics store.
	private int mutationPoint;
	
	private RandomNumberGenerator rng;
	private CodonGenerator codonGenerator;
	
	/**
	 * Construct a single point mutation.
	 * 
	 * @param model The current controlling model. Parameters such as the codon
	 * 				generator to use will come from here.
	 */
	public SinglePointMutation(GEModel model) {
		this.model = model;
		
		initialise();
		
		LifeCycleManager.getLifeCycleManager().addGenerationListener(new GenerationAdapter() {
			@Override
			public void onGenerationStart() {
				initialise();
			}
		});
	}
	
	/**
	 * Perform point mutation on the given GECandidateProgram. A codon position 
	 * will be randomly chosen from the length of codons in the program, and 
	 * will undergo mutation.
	 * 
	 * @param program The GECandidateProgram selected to undergo this mutation 
	 * 				  operation.
	 * @return A GECandidateProgram that was the result of a single point mutation 
	 * on the provided GECandidateProgram.
	 */
	@Override
	public GECandidateProgram mutate(CandidateProgram p) {
		GECandidateProgram program = (GECandidateProgram) p;
		
		mutationPoint = rng.nextInt(program.getNoCodons());
		int codon = codonGenerator.getCodon();
		program.setCodon(mutationPoint, codon);

		return program;
	}

	public void initialise() {
		rng = model.getRNG();
		codonGenerator = model.getCodonGenerator();
	}

}
